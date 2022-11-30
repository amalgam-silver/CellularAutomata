package com.hgtech.service.ca.environment.evolve;

import com.hgtech.service.ca.rule.Rule;

import java.util.List;

/**
 * @author yue.ge
 * @version 1.0 2022/11/29 11:36
 **/
public interface Evolve {

    /**
     * 对规则进行优胜劣汰
     * @param ruleList 规则列表
     */
    void evolve(List<Rule> ruleList, int gen);
}
