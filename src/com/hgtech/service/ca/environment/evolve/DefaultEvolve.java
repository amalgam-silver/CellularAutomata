package com.hgtech.service.ca.environment.evolve;

import com.hgtech.service.ca.rule.MapRule;
import com.hgtech.service.ca.rule.Rule;

import java.util.Comparator;
import java.util.List;

/**
 * @author yue.ge
 * @version 1.0 2022/11/29 11:39
 **/
public class DefaultEvolve implements Evolve {

    /**
     * 通过交配新生成的后代
     */
    private int bornNum;

    /**
     * 随机生成的后代
     */
    private int randomNum;

    /**
     * 随机变异的比例
     */
    private int randomChangeRatio;

    @Override
    public void evolve(List<Rule> ruleList, int gen) {
        int ruleNum = ruleList.size();
        ruleList.sort(Comparator.comparing(Rule::getScore));
        Rule firstRule = ruleList.get(ruleNum - 1);
        Rule secondRule = ruleList.get(ruleNum - 2);

        // 将评分最高的两个规则交配生成新规则，淘汰评分最低的若干个规则
        for (int i = 0; i < bornNum; i++) {
            MapRule newRule = MapRule.bornNewRule((MapRule) firstRule, (MapRule) secondRule, randomChangeRatio, gen);
            ruleList.set(i, newRule);
        }
        // 随机生成若干个规则
        for (int i = 0; i < randomNum; i++) {
            MapRule rule = MapRule.getRandomRule(128);
            ruleList.set(bornNum + i, rule);
        }
    }

    public DefaultEvolve(int bornNum, int randomNum, int randomChangeRatio) {
        this.bornNum = bornNum;
        this.randomNum = randomNum;
        this.randomChangeRatio = randomChangeRatio;
    }
}
