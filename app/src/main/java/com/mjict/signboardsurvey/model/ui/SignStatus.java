package com.mjict.signboardsurvey.model.ui;

/**
 * Created by Junseo on 2016-12-26.
 */
public enum  SignStatus {
    SHUT_DOWN,      // 폐업
    DEMOLISHED,     // 철거 됨
    TO_BE_DEMOLISH, // 철거 예정
    NORMAL;

    @Override
    public String toString() {
        String str = "";
        switch (this) {
            case SHUT_DOWN:
                str = "폐업";
                break;
            case DEMOLISHED:
                str = "철거";
                break;
            case TO_BE_DEMOLISH:
                str = "철거 예정";
                break;
            case NORMAL:
                str = "정상";
                break;
        }

        return str;
    }
}
