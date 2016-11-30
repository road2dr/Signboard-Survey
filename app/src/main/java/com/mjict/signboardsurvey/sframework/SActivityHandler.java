package com.mjict.signboardsurvey.sframework;

import android.app.Activity;

/**
 * �⺻ �����ڰ� �� �־�� �Ǵ°� �ƴ����� �ִ°� ���ڳ�
 * @author Junseo
 *
 */
public abstract class SActivityHandler implements SActivityHandleable {
	public static String HANDLER_CLASS="handler_class";

	// TODO ���߿� WeakReference ��..
	private Activity activity;
	
	public SActivityHandler() {
	}
	
	public SActivityHandler(Activity act) {

		activity = act;
	}
	
	public void setActivity(Activity act) {
		activity = act;
	}
	
	public Activity getActivity() {
		return activity;
	}
	

}
