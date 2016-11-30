package com.mjict.signboardsurvey.task;

import android.os.AsyncTask;

/**
 * Created by Junseo on 2016-07-29.
 */
public abstract class DefaultAsyncTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result> {
    private AsyncTaskListener<Progress, Result> defaultAsyncTaskListener;
    private SimpleAsyncTaskListener<Result> simpleAsyncTaskListener;

    public DefaultAsyncTask() {
    }


    public void setSimpleAsyncTaskListener(SimpleAsyncTaskListener<Result> listener) {
        this.simpleAsyncTaskListener = listener;
    }

    public void setDefaultAsyncTaskListener(AsyncTaskListener<Progress, Result> listener) {
        defaultAsyncTaskListener = listener;
    }


    @Override
    protected void onPreExecute() {
        if(defaultAsyncTaskListener != null)
            defaultAsyncTaskListener.onTaskStart();

        if(simpleAsyncTaskListener != null)
            simpleAsyncTaskListener.onTaskStart();

        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(Result result) {
        if(defaultAsyncTaskListener != null)
            defaultAsyncTaskListener.onTaskFinished(result);

        if(simpleAsyncTaskListener != null)
            simpleAsyncTaskListener.onTaskFinished(result);

        super.onPostExecute(result);
    }

    @Override
    protected void onProgressUpdate(Progress... values) {
        if(defaultAsyncTaskListener != null)
            defaultAsyncTaskListener.onTaskProgressUpdate(values);

        super.onProgressUpdate(values);
    }
}