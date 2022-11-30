package com.hgtech.service.ca.environment;

import com.hgtech.service.ca.environment.evolve.Evolve;
import com.hgtech.service.ca.rule.Rule;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author yue.ge
 * @version 1.0 2022/11/26 07:58
 **/
public class Evolution {

    /**
     * 代数
     */
    private int gen;

    /**
     * 每次进化计算多少次
     */
    private int runTimePerEvolution;

    /**
     * 每次运算迭代多少次
     */
    private int countPerRun;

    /**
     * 是否使用影子状态
     */
    private boolean useShadowStatus;

    private final ExecutorService threadPool = Executors.newFixedThreadPool(10);

    private final LifeRun lifeRun = new LifeRun(99, 3);

    /**
     * 运行一次规则进化
     * @param ruleList 规则
     * @param parallelCal 是否使用并行计算
     * @param evolve 演化规则
     */
    public Map<String, Integer> evolution(List<Rule> ruleList, boolean parallelCal, Evolve evolve) {

        // 对每个规则评分
        if (parallelCal) {
            runParallel(ruleList);
        } else {
            for (Rule rule : ruleList) {
                int score = lifeRun.runCalculate(rule, runTimePerEvolution, countPerRun, useShadowStatus);
                rule.setScore(score);
            }
        }

        // 优胜劣汰
        evolve.evolve(ruleList, gen);
        gen++;

        // 统计数据
        return statistics(ruleList);
    }

    /**
     * 统计数据
     * @param ruleList 规则列表
     * @return 统计数据
     */
    private Map<String, Integer> statistics(List<Rule> ruleList) {
        int ruleNum = ruleList.size();
        // 按评分排序(低 -> 高)
        ruleList.sort(Comparator.comparing(Rule::getScore));
        // 平均分
        int averageScore = 0;
        for (int i = 10; i < ruleList.size(); i++) {
            Rule rule = ruleList.get(i);
            averageScore += rule.getScore();
        }
        averageScore /= ruleList.size() - 10;
        // 最高分
        Rule firstRule = ruleList.get(ruleNum - 1);
        int maxScore = firstRule.getScore();
        // 最小代数
        int minGen = ruleList.stream().min(Comparator.comparing(Rule::getGeneration)).map(Rule::getGeneration).orElse(-1);
        // 最大代数
        int maxGen = ruleList.stream().max(Comparator.comparing(Rule::getGeneration)).map(Rule::getGeneration).orElse(-1);
        // 最好代数
        int bestGen = firstRule.getGeneration();

        Map<String, Integer> res = new HashMap<>();
        res.put("max", maxScore);
        res.put("avg", averageScore);
        res.put("minGen", minGen);
        res.put("maxGen", maxGen);
        res.put("bestGen", bestGen);

        return res;
    }

    /**
     * 并行计算
     * @param ruleList 规则
     */
    private void runParallel(List<Rule> ruleList) {
        List<Future<?>> futureList = new ArrayList<>();
        // 将任务提交线程池
        for (Rule rule : ruleList) {
            Future<?> future = threadPool.submit(() -> {
                int score = lifeRun.runCalculate(rule, runTimePerEvolution, countPerRun, useShadowStatus);
                rule.setScore(score);
                return score;
            });
            futureList.add(future);
        }
        // 等待所有任务执行完成
        futureList.forEach(future -> {
            try {
                future.get();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }

    public Evolution(int gen, int runTimePerEvolution, int countPerRun, boolean useShadowStatus) {
        this.gen = gen;
        this.runTimePerEvolution = runTimePerEvolution;
        this.countPerRun = countPerRun;
        this.useShadowStatus = useShadowStatus;
    }

    public int getGen() {
        return gen;
    }

    public void setGen(int gen) {
        this.gen = gen;
    }

    public int getRunTimePerEvolution() {
        return runTimePerEvolution;
    }

    public void setRunTimePerEvolution(int runTimePerEvolution) {
        this.runTimePerEvolution = runTimePerEvolution;
    }

    public int getCountPerRun() {
        return countPerRun;
    }

    public void setCountPerRun(int countPerRun) {
        this.countPerRun = countPerRun;
    }

    public boolean isUseShadowStatus() {
        return useShadowStatus;
    }

    public void setUseShadowStatus(boolean useShadowStatus) {
        this.useShadowStatus = useShadowStatus;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public LifeRun getLifeRun() {
        return lifeRun;
    }
}
