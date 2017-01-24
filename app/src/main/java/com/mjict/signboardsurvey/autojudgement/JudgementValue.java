package com.mjict.signboardsurvey.autojudgement;

import java.io.Serializable;

/**
 * Created by Junseo on 2017-01-21.
 */
public class JudgementValue implements Serializable {
    public InputType type;
    public int value;

    public JudgementValue(InputType type, int value) {
        this.type = type;
        this.value = value;
    }
}
