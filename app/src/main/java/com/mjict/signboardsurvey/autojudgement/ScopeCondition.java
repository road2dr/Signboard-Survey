package com.mjict.signboardsurvey.autojudgement;

/**
 * Created by Junseo on 2017-01-17.
 */
public class ScopeCondition extends Condition implements Encodable {

    private int left;
    private boolean leftEquation;
    private boolean rightEquation;
    private int right;

    public ScopeCondition(int left, boolean leftEquation, boolean rightEquation, int right) {
        this.left = left;
        this.leftEquation = leftEquation;
        this.rightEquation = rightEquation;
        this.right = right;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public boolean isLeftEquation() {
        return leftEquation;
    }

    public void setLeftEquation(boolean leftEquation) {
        this.leftEquation = leftEquation;
    }

    public boolean isRightEquation() {
        return rightEquation;
    }

    public void setRightEquation(boolean rightEquation) {
        this.rightEquation = rightEquation;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    @Override
    public boolean check() {
        if(inputValue == -1) {
            return false;   // value 의 값이 정의 되지 않음
        }

        int value = inputValue;
//        try {
//            value = Integer.parseInt(inputValue);
//        }catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }

        boolean answer = false;
        if(leftEquation == true && rightEquation == true) {
            answer = (left <= value && right >= value);
        } else if(leftEquation == true && rightEquation == false) {
            answer = (left <= value && right > value);
        } else if(leftEquation == false && rightEquation == true) {
            answer = (left < value && right >= value);
        } else {
            answer = (left < value && right > value);
        }

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
                "left=\"%d\"" + "\n" +
                "left_equation=\"%s\"" + "\n" +
                "right_equation=\"%s\"" + "\n" +
                "right=\"%d\"" +
                "%s />";

        String statement = String.format(format, Constants.SCOPE_CONDITION, valueType.toString(),
                left, String.valueOf(leftEquation), String.valueOf(rightEquation), right, operationStatement);

        return statement;
    }
}
