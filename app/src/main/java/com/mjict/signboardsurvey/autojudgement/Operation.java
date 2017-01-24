package com.mjict.signboardsurvey.autojudgement;

/**
 * Created by Junseo on 2017-01-19.
 */
public enum Operation {
    END,
    AND,
    OR;

            public static Operation toOperation(String value) {
                Operation operation = Operation.END;
            if(value.equalsIgnoreCase("and"))
                operation = Operation.AND;
            else if(value.equalsIgnoreCase("or"))
                operation = Operation.OR;
            else
                operation = Operation.END;

            return operation;
        }
}
