package com.mjict.signboardsurvey.autojudgement;

/**
 * Created by Junseo on 2017-01-17.
 */
public class ScopeCondition extends Condition {
//    private Type type;
    private int left;
    private boolean leftEquation;
//    private int inputValue;
    private boolean rightEquation;
    private int right;

    public ScopeCondition(int left, boolean leftEquation, boolean rightEquation, int right) {
//        this.type = type;
        this.left = left;
        this.leftEquation = leftEquation;
//        this.inputValue = value;
        this.rightEquation = rightEquation;
        this.right = right;
    }

//    public Type getType() {
//        return type;
//    }
//
//    public void setType(Type type) {
//        this.type = type;
//    }

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

        boolean answer = false;
        if(leftEquation == true && rightEquation == true) {
            answer = (left <= inputValue && right >= inputValue);
        } else if(leftEquation == true && rightEquation == false) {
            answer = (left <= inputValue && right > inputValue);
        } else if(leftEquation == false && rightEquation == true) {
            answer = (left < inputValue && right >= inputValue);
        } else {
            answer = (left < inputValue && right > inputValue);
        }

        return answer;
    }
}
