package com.hgtech.service.ca.cell;

import com.hgtech.service.ca.rule.Rule;

import java.util.List;

/**
 * @author yue.ge
 * @version 1.0 2022/11/24 21:05
 **/
public class Cell {

    /**
     * 状态
     */
    private CellStatusEnum status;

    /**
     * 影子状态
     */
    private CellStatusEnum statusShadow;

    /**
     * 相邻的cell
     */
    private List<Cell> neighbours;

    /**
     * 规则
     */
    private Rule rule;

    /**
     * 更新状态
     */
    public void updateStatus() {
        status = rule.getNewStatus(neighbours);
    }

    /**
     * 更新影子状态
     */
    public void updateStatusShadow() {
        statusShadow = rule.getNewStatus(neighbours);
    }

    /**
     * 将影子状态更新到实际状态
     */
    public void freshStatus() {
        status = statusShadow;
    }

    public static Cell getCellWithRandomStatus(Rule rule) {
        CellStatusEnum status = Math.random() < 0.5 ? CellStatusEnum.ON : CellStatusEnum.OFF;
        return new Cell(status, rule);
    }

    public Cell(CellStatusEnum status, Rule rule) {
        this.status = status;
        this.statusShadow = status;
        this.rule = rule;
    }

    public Cell() {
    }

    public CellStatusEnum getStatus() {
        return status;
    }

    public void setStatus(CellStatusEnum status) {
        this.status = status;
    }

    public List<Cell> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Cell> neighbours) {
        this.neighbours = neighbours;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

}
