package com.hgtech.service.ca.environment.rater;

import com.hgtech.service.ca.cell.CellStatusEnum;

import java.util.List;

/**
 * @author yue.ge
 * @version 1.0 2022/11/29 10:00
 **/
public interface Rater {

    int getScore(List<CellStatusEnum> initStatus, List<CellStatusEnum> endStatus);
}
