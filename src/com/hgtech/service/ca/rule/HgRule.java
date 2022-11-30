package com.hgtech.service.ca.rule;

import com.hgtech.service.ca.cell.Cell;
import com.hgtech.service.ca.cell.CellStatusEnum;

import java.util.List;

/**
 * @author yue.ge
 * @version 1.0 2022/11/26 07:21
 **/
public class HgRule implements Rule {

    @Override
    public CellStatusEnum getNewStatus(List<Cell> neighbours) {

        long onNum = neighbours.stream().filter(cell -> CellStatusEnum.ON.equals(cell.getStatus())).count();
        long offNum = neighbours.size() - onNum;
        return onNum > offNum ? CellStatusEnum.ON : CellStatusEnum.OFF;
    }

    @Override
    public void setScore(int score) {

    }

    @Override
    public int getScore() {
        return 0;
    }

    @Override
    public void setGeneration(int gen) {

    }

    @Override
    public int getGeneration() {
        return 0;
    }
}
