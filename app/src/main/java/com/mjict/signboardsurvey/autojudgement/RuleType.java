package com.mjict.signboardsurvey.autojudgement;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2017-01-17.
 */
public class RuleType {

    private List<Type> scopeTypes;
    private List<Type> multiEquationTypes;
    private List<Type> singleEquationTypes;

    public RuleType() {
        scopeTypes = new ArrayList<>();
        multiEquationTypes = new ArrayList<>();
        singleEquationTypes = new ArrayList<>();
    }

    public void addScopeType(Type type) {
        scopeTypes.add(type);
    }

    public int sizeOfScopeTypes() {
        return scopeTypes.size();
    }

    public Type getScopeType(int index) {
        return scopeTypes.get(index);
    }

    public void addMultiEquationType(Type type) {
        multiEquationTypes.add(type);
    }

    public int sizeOfMultiEquationTypes() {
        return multiEquationTypes.size();
    }

    public Type getMultiEquationType(int index) {
        return multiEquationTypes.get(index);
    }

    public void addSingleEquationType(Type type) {
        singleEquationTypes.add(type);
    }

    public int sizeOfSingleEquationTypes() {
        return singleEquationTypes.size();
    }

    public Type getSingleEquationType(int index) {
        return singleEquationTypes.get(index);
    }





}
