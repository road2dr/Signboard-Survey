package com.mjict.signboardsurvey.sframework;

import android.content.Context;
import android.content.Intent;

public class SIntent extends Intent {

	private Class<?> handlerClass;
	
	public SIntent(Context packageContext, Class<?> cls, Class<?> hndClass) {
		super(packageContext, cls);
		handlerClass = hndClass;
	}
	
	public Class<?> getHandlerClass() {
		return handlerClass;
	}
}
