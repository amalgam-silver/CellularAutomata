package com.hgtech.service.ca.rule;

import com.hgtech.service.ca.cell.Cell;
import com.hgtech.service.ca.cell.CellStatusEnum;

import java.util.List;

/**
 * @author yue.ge
 * @version 1.0 2022/11/26 07:18
 **/
public interface Rule {

    /**
     * 根据相邻的元胞更新状态
     * @param neighbours 相邻的元胞
     * @return 下一次更新的状态
     */
    CellStatusEnum getNewStatus(List<Cell> neighbours);

    /**
     * 设置规则的评分
     * @param score 评分
     */
    void setScore(int score);

    /**
     * 获取规则的评分
     * @return 评分
     */
    int getScore();

    /**
     * 设置代数
     * @param gen 代数
     */
    void setGeneration(int gen);

    /**
     * 获取代数
     * @return 代数
     */
    int getGeneration();
}
