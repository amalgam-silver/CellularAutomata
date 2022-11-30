package com.hgtech.service.ca.environment.rater;

import com.hgtech.service.ca.cell.CellStatusEnum;

import java.util.List;

/**
 * @author yue.ge
 * @version 1.0 2022/11/29 10:05
 **/
public class DefaultRaterImpl implements Rater {

    @Override
    public int getScore(List<CellStatusEnum> initStatus, List<CellStatusEnum> endStatus) {
        long initOnCount = initStatus.stream().filter(CellStatusEnum.ON::equals).count();
        long initOffCount = initStatus.size() - initOnCount;

        long endOnCount = endStatus.stream().filter(CellStatusEnum.ON::equals).count();
        long endOffCount = endStatus.size() - endOnCount;

        if (initOnCount > initOffCount) {
            return (int) (endOnCount * 100 / initStatus.size());
        } else {
            return (int) (endOffCount * 100 / initStatus.size());
        }
    }
}
