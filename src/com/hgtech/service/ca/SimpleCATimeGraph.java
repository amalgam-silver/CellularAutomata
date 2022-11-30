package com.hgtech.service.ca;

import com.hgtech.service.ca.config.Configuration;
import com.hgtech.service.ca.environment.LifeRun;
import com.hgtech.service.ca.rule.HgRule;
import com.hgtech.service.ca.rule.Rule;
import com.hgtech.service.ca.util.Utils;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @author yue.ge
 * @version 1.0 2022/11/26 07:17
 **/
public class SimpleCATimeGraph extends Frame {

    private static final long serialVersionUID = 6049928521930765593L;
    private final static LifeRun lifeRun = new LifeRun(99, 3);

    private static List<List<Integer>> lists;

    private static final String filename = "config.dat";

    public SimpleCATimeGraph() throws HeadlessException {
        this.setBounds(500, 500, 800, 800);
        this.setBackground(Color.BLUE);
        this.setVisible(true);
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                System.exit(0);
            }
        });

        Rule hgRule = new HgRule();
        Configuration config = (Configuration) Utils.readObjectFromFile(filename);
        List<Rule> ruleList = config.getRuleList();
        ruleList.sort(Comparator.comparing(Rule::getScore));
        hgRule = ruleList.get(ruleList.size() - 1);
        int score = lifeRun.runCalculate(hgRule, 50, 500, false);
        System.out.println(score);

        lists = lifeRun.runOnceWithTimelyGraph(hgRule, 100);
//        printLists(lists);

        new Timer().schedule(new UpdateTask(), 1000, 10000);
    }

    public static void main(String[] args) {

        new SimpleCATimeGraph();
    }

    private static void printLists(List<List<Integer>> lists) {
        for (List list : lists) {
            System.out.println(list);
        }
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        if (Objects.nonNull(lists)) {
            for (int i = 0; i < lists.size(); i++) {
                List<Integer> list = lists.get(i);
                for (int j = 0; j < list.size(); j++) {
                    Color color = list.get(j) == 0 ? Color.BLACK : Color.white;
                    g.setColor(color);
                    g.fillOval(j*7 + 50, i*7 + 50, 7, 7);
                }
            }
        }
    }

    private class UpdateTask extends TimerTask {

        @Override
        public void run() {
            if (Objects.nonNull(lists)) {
                repaint();
            }
        }
    }
}
