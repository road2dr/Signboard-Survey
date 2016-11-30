package com.mjict.signboardsurvey.task;

/**
 * Created by Junseo on 2016-07-28.
 */
public interface AsyncTaskListener<Progress, Result> {
    public void onTaskStart();

    public void onTaskProgressUpdate(Progress... values);

    public void onTaskFinished(Result result);
}

//public interface SimpleAsyncTaskListener<T> {
//
//}
