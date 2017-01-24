package com.mjict.signboardsurvey.autojudgement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2017-01-17.
 */
public class AutoJudgementRule {

    private int type;
    private int defaultResult;
    private List<Rule> rules;

    public AutoJudgementRule() {
        rules = new ArrayList<>();
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getDefaultResult() {
        return defaultResult;
    }

    public void setDefaultResult(int defaultResult) {
        this.defaultResult = defaultResult;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public int sizeOfRules() {
        return rules.size();
    }

    public Rule getRule(int index) {
        return rules.get(index);
    }

    public List<Rule> getRules() {
        return rules;
    }


//    public int get
}
