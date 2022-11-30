package com.hgtech.service.ca.rule;

import com.hgtech.service.ca.cell.Cell;
import com.hgtech.service.ca.cell.CellStatusEnum;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过状态映射关系维护的元胞更新规则
 * @author yue.ge
 * @version 1.0 2022/11/25 09:09
 **/
public class MapRule implements Rule, Serializable {

    private static final long serialVersionUID = -8153206414157323228L;
    /**
     * 规则列表
     */
    private List<CellStatusEnum> ruleList;

    /**
     * 评分
     */
    private int score;

    /**
     * 第几代
     */
    private int generation;

    /**
     * 根据相邻cell获取更新的状态
     * @param neighbours 相邻cell
     * @return 更新的状态
     */
    @Override
    public CellStatusEnum getNewStatus(List<Cell> neighbours) {

        // 编码
        Integer neighboursStatus = 0;
        for (Cell cell : neighbours) {
            neighboursStatus *= 2;
            neighboursStatus += cell.getStatus().getStatus();
        }

        return ruleList.get(neighboursStatus);
    }

    /**
     * 随机生成一组规则
     * @param ruleNum 规则数量
     * @return 规则
     */
    public static MapRule getRandomRule(int ruleNum) {
        List<CellStatusEnum> ruleList = new ArrayList<>(ruleNum);
        for (int i = 0; i < ruleNum; i++) {
            double random = Math.random();
            ruleList.add(random < 0.5 ? CellStatusEnum.ON : CellStatusEnum.OFF);
        }
        return new MapRule(ruleList, 1, -1);
    }

    /**
     * 诞生新规则
     * @param father 父规则
     * @param mother 母规则
     * @param randomChangeRatio 随机变异比例(0~100)
     * @return 新规则
     */
    public static MapRule bornNewRule(MapRule father, MapRule mother, int randomChangeRatio, int generation) {
        List<CellStatusEnum> fatherRuleList = father.getRuleList();
        List<CellStatusEnum> motherRuleList = mother.getRuleList();

        if (fatherRuleList.size() != motherRuleList.size()) {
            throw new RuntimeException("rule num not equal!");
        }

        int size = fatherRuleList.size();

        // 从父母各继承一半的规则
        List<CellStatusEnum> childRuleList = new ArrayList<>();
        childRuleList.addAll(fatherRuleList.subList(0, size / 2));
        childRuleList.addAll(fatherRuleList.subList(size/2, size));

        // 随机变异
        int randomNum = randomChangeRatio * size / 100;
        for (int i = 0; i < randomNum; i++) {
            int randomIndex = (int) (Math.random() * size);
            childRuleList.set(randomIndex, Math.random() < 0.5 ? CellStatusEnum.ON : CellStatusEnum.OFF);
        }

        return new MapRule(childRuleList, generation, -1);
    }

    public MapRule(List<CellStatusEnum> ruleList, int generation, int score) {
        this.ruleList = ruleList;
        this.generation = generation;
        this.score = score;
    }

    public List<CellStatusEnum> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<CellStatusEnum> ruleList) {
        this.ruleList = ruleList;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getGeneration() {
        return generation;
    }

    public void setGeneration(int generation) {
        this.generation = generation;
    }
}
