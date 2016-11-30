package com.mjict.signboardsurvey.model;

/**
 * Created by Junseo on 2016-07-28.
 */
public class TaskResult {

    public boolean success;
    public Exception exception;
    public int errorMessage;

    public TaskResult() {

    }

    public TaskResult(boolean s, Exception e, int msg) {
        success = s;
        exception = e;
        errorMessage = msg;
    }
}

