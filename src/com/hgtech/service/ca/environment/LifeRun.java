package com.hgtech.service.ca.environment;

import com.hgtech.service.ca.cell.Cell;
import com.hgtech.service.ca.cell.CellStatusEnum;
import com.hgtech.service.ca.environment.rater.DefaultRaterImpl;
import com.hgtech.service.ca.environment.rater.Rater;
import com.hgtech.service.ca.rule.Rule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yue.ge
 * @version 1.0 2022/11/26 07:37
 **/
public class LifeRun {

    /**
     * 元胞数量
     */
    private int cellNum;

    /**
     * 每侧能感知的元胞数量
     */
    private int neighbourNumPerSide;

    /**
     * 基于某个规则运行一次元胞计算，并给出该规则的评分
     * @param rule 规则
     * @param runTime 运行次数
     * @param countPerRun 每次运行的迭代次数
     * @param useShadowStatus 是否使用影子状态
     * @return 评分, 0 ~ 100
     */
    public int runCalculate(Rule rule, int runTime, int countPerRun, boolean useShadowStatus) {
        return runCalculate(rule, runTime, countPerRun, useShadowStatus, new DefaultRaterImpl());
    }

    /**
     * 基于某个规则运行一次元胞计算，并给出该规则的评分
     * @param rule 规则
     * @param runTime 运行次数
     * @param countPerRun 每次运行的迭代次数
     * @param useShadowStatus 是否使用影子状态
     * @param rater 评分者
     * @return 评分, 0 ~ 100
     */
    public int runCalculate(Rule rule, int runTime, int countPerRun, boolean useShadowStatus, Rater rater) {

        int score = 0;
        for (int i = 0; i < runTime; i++) {
            // 初始化元胞
            List<Cell> cellList = initCell(rule);
            // 初始状态
            List<CellStatusEnum> initStatusList = cellList.stream().map(Cell::getStatus).collect(Collectors.toList());
            // 运算
            calculate(countPerRun, cellList, useShadowStatus);
            // 运算完的状态
            List<CellStatusEnum> endStatusList = cellList.stream().map(Cell::getStatus).collect(Collectors.toList());
            // 评分
            score += rater.getScore(initStatusList, endStatusList);
        }
        return score / runTime;
    }

    /**
     * 基于某个规则运行一次，并返回时空图
     * @param rule 规则
     * @param count 迭代次数
     * @return 时空图
     */
    public List<List<Integer>> runOnceWithTimelyGraph(Rule rule, int count) {
        List<List<Integer>> res = new ArrayList<>();
        List<Cell> cells = initCell(rule);
        res.add(cells.stream().map(Cell::getStatus).map(CellStatusEnum::getStatus).collect(Collectors.toList()));
        long countOn = cells.stream().filter(cell -> CellStatusEnum.ON.equals(cell.getStatus())).count();
        long countOff = cells.size() - countOn;
        System.out.println("on: " + countOn + " off:" + countOff );
        for (int i = 0; i < count; i++) {
            calculate(1, cells, false);
            res.add(cells.stream().map(Cell::getStatus).map(CellStatusEnum::getStatus).collect(Collectors.toList()));
        }
        return res;
    }

    /**
     * 初始化元胞
     * @param rule 规则
     */
    private List<Cell> initCell(Rule rule) {

        // 初始化随机状态的元胞
        List<Cell> cellList = new ArrayList<>(cellNum);
        for (int i = 0; i < cellNum; i++) {
            cellList.add(Cell.getCellWithRandomStatus(rule));
        }

        // 设置每个元胞的相邻元胞
        int neighbourSize = neighbourNumPerSide * 2 + 1;
        for (int i = 0; i < cellNum; i++) {
            List<Cell> neighbourList = new ArrayList<>(neighbourSize);
            for (int j = -neighbourNumPerSide; j <= neighbourNumPerSide; j++) {
                int index = i + j;
                if (index < 0) {
                    index += cellNum;
                } else if (index >= cellNum) {
                    index -= cellNum;
                }
                neighbourList.add(cellList.get(index));
            }
            cellList.get(i).setNeighbours(neighbourList);
        }

        return cellList;
    }

    /**
     * 计算
     * @param count 迭代次数
     * @param cellList 元胞
     * @param useShadowStatus 是否使用影子状态
     */
    private void calculate(int count, List<Cell> cellList, boolean useShadowStatus) {
        List<String> prevStatus = Arrays.asList("", "", "");
        Integer stableCount = 0;
        for (int i = 0; i < count; i++) {
            // 如果使用影子状态，先更新影子状态，待所有元胞的影子状态更新完后，再同意
            if (useShadowStatus) {
                for (Cell cell : cellList) {
                    cell.updateStatusShadow();
                }
                for (Cell cell : cellList) {
                    cell.freshStatus();
                }
            } else {
                for (Cell cell : cellList) {
                    cell.updateStatus();
                }
            }

            // 检测是否已达稳态
            if (reachStable(cellList, prevStatus, stableCount)) {
                return;
            }
        }
    }

    /**
     * 判断是否已达稳态
     * @param cellList 元胞
     * @param prevStatus 用于存储之前状态的临时变量
     * @param stableCount 与之前状态相同的计数
     * @return 是否已达稳态
     */
    private boolean reachStable(List<Cell> cellList, List<String> prevStatus, Integer stableCount) {

        StringBuilder status = new StringBuilder();
        for (Cell cell : cellList) {
            status.append(cell.getStatus().getStatus());
        }
        // 如果已到达稳态，提前退出
        if (prevStatus.get(0).equals(status.toString())
                || prevStatus.get(1).equals(status.toString())
                || prevStatus.get(2).equals(status.toString())) {
            stableCount++;
        } else {
            stableCount = 0;
        }
        if (stableCount > 5) {
            return true;
        }
        prevStatus.set(2, prevStatus.get(1));
        prevStatus.set(1, prevStatus.get(0));
        prevStatus.set(0, status.toString());

        return false;
    }

    public LifeRun(int cellNum, int neighbourNumPerSide) {
        this.cellNum = cellNum;
        this.neighbourNumPerSide = neighbourNumPerSide;
    }

    public int getCellNum() {
        return cellNum;
    }

    public void setCellNum(int cellNum) {
        this.cellNum = cellNum;
    }

    public int getNeighbourNumPerSide() {
        return neighbourNumPerSide;
    }

    public void setNeighbourNumPerSide(int neighbourNumPerSide) {
        this.neighbourNumPerSide = neighbourNumPerSide;
    }
}
