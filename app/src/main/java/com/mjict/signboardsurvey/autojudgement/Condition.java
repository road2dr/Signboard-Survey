package com.mjict.signboardsurvey.autojudgement;

/**
 * Created by Junseo on 2017-01-17.
 */
public abstract class Condition implements Checkable, Encodable {
    protected Type valueType;
    protected int inputValue = -1;
    private Operation operation = Operation.END;

    public void setInputValue(int value) {
        inputValue = value;
    }

    public Type getValueType() {
        return valueType;
    }

    public void setValueType(Type valueType) {
        this.valueType = valueType;
    }

    public void setOperation(Operation operation) {
        this.operation = operation;
    }

    public Operation getOperation() {
        return operation;
    }

    public Condition or(final Checkable target) {
        final boolean check = this.check();
        Condition cond = new Condition() {
            @Override
            public String toXml() {
                return null;
            }
            @Override
            public boolean check() {
                return (check || target.check());
            }
        };

        return cond;
    }

    public Condition and(final Checkable target) {
        final boolean check = this.check();
        Condition cond = new Condition() {
            @Override
            public String toXml() {
                return null;
            }
            @Override
            public boolean check() {
                return (check && target.check());
            }
        };
        return cond;
    }


}
