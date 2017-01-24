package com.mjict.signboardsurvey.autojudgement;

/**
 * Created by Junseo on 2017-01-17.
 */
public class EquationCondition extends Condition {
//    private Type itemType;
    private boolean equation;
    private int value;
//    private int inputValue = -1;

    public EquationCondition() {

    }

    public EquationCondition(boolean equation, int value) {
//        this.itemType = itemType;
        this.equation = equation;
        this.value = value;
    }

//    public Type getItemType() {
//        return itemType;
//    }
//
//    public void setItemType(Type itemType) {
//        this.itemType = itemType;
//    }

    public boolean isEquation() {
        return equation;
    }

    public void setEquation(boolean equation) {
        this.equation = equation;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

//    public void setInputValue(int value) {
//        inputValue = value;
//    }

    @Override
    public boolean check() {
        if(inputValue == -1)    // 값이 정의 되지 않음
            return false;

        boolean answer = false;
        if(equation)
            answer = (value == inputValue);
        else
            answer = (value != inputValue);

        return answer;
    }
}
