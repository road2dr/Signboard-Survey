package com.mjict.signboardsurvey.task;

public interface SimpleAsyncTaskListener<Result> {
	public void onTaskStart();
	public void onTaskFinished(Result result);
}