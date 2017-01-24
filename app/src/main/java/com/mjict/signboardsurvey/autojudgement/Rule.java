package com.mjict.signboardsurvey.autojudgement;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Junseo on 2017-01-17.
 */
public class Rule {

    private int result;
    private int order;
    private List<Condition> conditions;

    public Rule() {
        result = -1;
        conditions = new ArrayList<>();
    }

    public void setResult(int result) {
        this.result = result;
    }

    public int getResult() {
        return result;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getOrder() {
        return order;
    }

    public void addCondition(Condition condition) {
        conditions.add(condition);
    }

    public int sizeOfConditions() {
        return conditions.size();
    }

    public Condition getCondition(int index) {
        return conditions.get(index);
    }

    public boolean checkConditions() {
        boolean answer = false;
        if(conditions.size() <= 0)
            return answer;

        Collections.sort(conditions, new ConditionComparator());

        Condition last = null;
        int n = conditions.size();
        Log.d("junseo", "conditions size: "+conditions.size());
        for (int i = 0; i < n; i++) {
            Condition lcond = (last == null) ? conditions.get(i) : last;
            if(lcond == null)
                Log.d("junseo", "lcond is null");
            Condition rcond = null;
            if(i+1 < conditions.size())
                rcond = conditions.get(i+1);

            if(rcond == null) {
                Log.d("junseo", "rcond is null");
                last = lcond;
                break;
            }

            if (lcond.getOperation() == Operation.AND)
                last = lcond.and(rcond);
            else if (lcond.getOperation() == Operation.OR)
                last = lcond.or(rcond);
            else
                last = lcond;

            last.setOperation(rcond.getOperation());
        }

        return last.check();
    }

    private class ConditionComparator implements Comparator<Condition> {

        // OR -> AND -> END 순서로 정렬
        @Override
        public int compare(Condition lhs, Condition rhs) {
//            1이면 앞의 인자가 큰값, 0이면 같은 값, -1이면 뒤의 인자가 큰값
            int value = 0;
            if(lhs.getOperation() == Operation.END) {
                if(rhs.getOperation() == Operation.END)
                    value = 0;
                else
                    value = -1;
            } else if(lhs.getOperation() == Operation.OR) {
                if(rhs.getOperation() == Operation.OR)
                    value = 0;
                else
                    value = 1;
            } else if(lhs.getOperation() == Operation.AND) {
                if(rhs.getOperation() == Operation.OR)
                    value = -1;
                else if(rhs.getOperation() == Operation.AND)
                    value = 0;
                else
                    value = 1;
            } else {
                value = -1;
            }

            return value;
        }
    }
}
