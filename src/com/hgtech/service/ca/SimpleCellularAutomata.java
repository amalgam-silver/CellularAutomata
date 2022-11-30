package com.hgtech.service.ca;

import com.hgtech.service.ca.config.Configuration;
import com.hgtech.service.ca.environment.Evolution;
import com.hgtech.service.ca.environment.evolve.DefaultEvolve;
import com.hgtech.service.ca.environment.evolve.Evolve;
import com.hgtech.service.ca.rule.MapRule;
import com.hgtech.service.ca.rule.Rule;
import com.hgtech.service.ca.util.Utils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

/**
 * @author yue.ge
 * @version 1.0 2022/11/24 21:02
 **/
public class SimpleCellularAutomata {

    private static final int ruleNum = 50;

    private static final int evolutionTime = 800;

    private static final String filename = "config.dat";

    private static final Evolve evolve = new DefaultEvolve(5, 0, 10);

    public static void main(String[] args) {

        // 如果有规则文件，优先从文件中读取规则
        Configuration config = (Configuration) Utils.readObjectFromFile(filename);

        // 初始化一批规则
        if (Objects.isNull(config) || Objects.isNull(config.getRuleList())) {
            config = new Configuration();
            config.setGen(0);
            ArrayList<Rule> ruleList = new ArrayList<>(ruleNum);
            for (int i = 0; i < ruleNum; i++) {
                MapRule rule = MapRule.getRandomRule(128);
                ruleList.add(rule);
            }
            config.setRuleList(ruleList);
        }

        // 进化
        Evolution evolution = new Evolution(config.getGen(), 10, 200, false);
        for (int i = 0; i < evolutionTime; i++) {
            Map<String, Integer> res = evolution.evolution(config.getRuleList(), true, evolve);
            System.out.println(i + " - " + Utils.mapToString(res));
        }
        config.setGen(config.getGen() + evolutionTime);

        // 保存规则
        Utils.writeObjectToFile(config, filename);
    }

}
