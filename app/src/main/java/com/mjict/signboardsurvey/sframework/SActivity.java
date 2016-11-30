package com.mjict.signboardsurvey.sframework;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * - setContentView �� init() ���� ȣ�� ���� ���� ��� exception �߻��ϰ� ����.
 * @author Junseo
 *
 */
public abstract class SActivity extends Activity {

	private SActivityHandler handler = null;
//	private boolean handlerUsable = false;
	
	/**
	 * view �� �������̽� �ʱ�ȭ <br> setContentView �� �ݵ�� ���⼭ ȣ�� �Ǿ�� �Ѵ�.
	 */
	abstract protected void init();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Intent intent = getIntent();
		if(intent == null) {
		}
		
		Class<?> handlerClass = null;
		Bundle bundle = intent.getExtras();
		if(bundle == null)
			return;
		
		if(intent instanceof SIntent)
			handlerClass = ((SIntent)intent).getHandlerClass();
		else		
			handlerClass = (Class<?>) bundle.get(SActivityHandler.HANDLER_CLASS);	
		
		// TODO �Ʒ� ���� üũ�� Exception ���� ������ ����ҵ�.
		// handler Ŭ������ �������� �ʰų� ���ܰ� �߻��� ��� �Ϲ� ��Ƽ��Ƽ ó�� �۵�.
		boolean status = false;
		if(handlerClass == null) {
		}
		
		if(handlerClass != SActivityHandler.class) {			
		}

		if(handlerClass != null) {

			try {
				handler = (SActivityHandler) handlerClass.newInstance();
				handler.setActivity(this);
				status = true;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} finally {
				// TODO
			}
			
			init();
			
			if(status == true)
				handler.onActivityCreate(savedInstanceState);
			
		}

//		handlerUsable = status;
	}
	
	@Override
	protected void onStart() {
		if(handler != null)
			handler.onActivityStart();
		
		super.onStart();
	}
	
	@Override
	protected void onPause() {
		if(handler != null)
			handler.onActivityPause();
		
		super.onPause();
	}
	
	@Override
	protected void onResume() {		
		if(handler != null)
			handler.onActivityResume();
		
		super.onResume();
	}
	
	@Override
	protected void onStop() {
		if(handler != null)
			handler.onActivityStop();
		
		super.onStop();
	}
	
	@Override
	protected void onRestart() {
		if(handler != null)
			handler.onActivityRestart();
	
		super.onRestart();
	}
	@Override
	protected void onDestroy() {
		if(handler != null)
			handler.onActivityDestroy();
		
		super.onDestroy();
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if(handler != null)
			handler.onSaveInstanceState(outState);
		
		super.onSaveInstanceState(outState);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(handler != null)
			handler.onActivityResult(requestCode, resultCode, data);
		
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		if(handler != null)
			handler.onConfigurationChanged(newConfig);
		
		super.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void onBackPressed() {
		if(handler != null)
			handler.onBackPressed();
		
		super.onBackPressed();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
		if(handler != null)
			handler.onSaveInstanceState(outState);
			
		super.onSaveInstanceState(outState, outPersistentState);
	}
	

}
