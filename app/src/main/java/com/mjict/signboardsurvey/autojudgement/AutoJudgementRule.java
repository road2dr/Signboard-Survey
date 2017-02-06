package com.mjict.signboardsurvey.autojudgement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2017-01-17.
 */
public class AutoJudgementRule implements Encodable {

    private String signType;
    private String defaultResult;
    private List<Rule> rules;

    public AutoJudgementRule() {
        rules = new ArrayList<>();
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String type) {
        this.signType = type;
    }

    public String  getDefaultResult() {
        return defaultResult;
    }

    public void setDefaultResult(String defaultResult) {
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

    @Override
    public String toXml() {
        String format = "<%s" + "\n" +
                "sign_type=\"%s\"" + "\n" +
                "default_result=\"%s\">" + "\n" +
                "%s"+
                "</%s>";

        String rulesXml = "";
        for(int i=0; i<rules.size(); i++){
            Rule r = rules.get(i);
            rulesXml = rulesXml + r.toXml();
        }

        String statement = String.format(format, Constants.INSPECTION_RULE, signType, defaultResult, rulesXml, Constants.INSPECTION_RULE);

        return statement;
    }
}
