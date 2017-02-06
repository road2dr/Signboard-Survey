package com.mjict.signboardsurvey.autojudgement;

/**
 * Created by Junseo on 2017-01-17.
 */
public class EquationCondition extends Condition {
    private boolean equation;
    private String left;

    public EquationCondition() {

    }

    public EquationCondition(boolean equation, String left) {
        this.equation = equation;
        this.left = left;
    }

    public boolean isEquation() {
        return equation;
    }

    public void setEquation(boolean equation) {
        this.equation = equation;
    }

    public String getLeft() {
        return left;
    }

    public void setLeft(String value) {
        this.left = value;
    }


    @Override
    public boolean check() {
        if(inputValue == -1)    // 값이 정의 되지 않음
            return false;

        String valuePrefix = "@value/";
        int value = -1;
        if(left.startsWith(valuePrefix)) {
            String valueName = left.substring(valuePrefix.length());
            Item valueItem = AutoJudgementRuleManager.findValue(valueName);
            if(valueItem == null) {     // @value/left 값에 해당하는 값이 없다
                return false;
            }
            value = Integer.parseInt(valueItem.getCode());
        } else {
            value = Integer.parseInt(left);
        }

        boolean answer = false;
        if(equation)
            answer = (inputValue == value);
        else
            answer = (inputValue != value);

        return answer;
    }

    @Override
    public String toXml() {
        String operationStatement = "";
        switch (getOperation()) {
            case AND:
                operationStatement = "\n" + "operation=\"AND\"";
                break;
            case OR:
                operationStatement = "\n" + "operation=\"OR\"";
                break;
            default:
                break;
        }

        String format = "<%s" + "\n" +
                "item=\"%s\"" + "\n" +
                "equation=\"%s\"" + "\n" +
                "left=\"%s\"" +
                "%s />";

//        int value = -1;
//        try {
//            value = Integer.parseInt(inputValue);
//        }catch (Exception e) {
//            e.printStackTrace();
//        }

        String statement = String.format(format, Constants.EQUATION_CONDITION, valueType.toString(),
                String.valueOf(equation), left, operationStatement);

        return statement;
    }
}
