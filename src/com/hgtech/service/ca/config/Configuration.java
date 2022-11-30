package com.hgtech.service.ca.config;

import com.hgtech.service.ca.rule.Rule;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * @author yue.ge
 * @version 1.0 2022/11/29 09:46
 **/
public class Configuration implements Serializable {

    private static final long serialVersionUID = 8021757631502887368L;

    /**
     * 规则列表
     */
    private List<Rule> ruleList;

    /**
     * 代数
     */
    private Integer gen;

    public List<Rule> getRuleList() {
        return ruleList;
    }

    public void setRuleList(List<Rule> ruleList) {
        this.ruleList = ruleList;
    }

    public Integer getGen() {
        return Objects.isNull(gen) ? 0 : gen;
    }

    public void setGen(Integer gen) {
        this.gen = gen;
    }
}
