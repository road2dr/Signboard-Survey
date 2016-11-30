package com.mjict.signboardsurvey.sframework;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;

/**
 * 
 * @author Junseo
 *
 */
public interface SActivityHandleable {

	public void onActivityCreate(Bundle savedInstanceState);
	
	public void onActivityStart();

	public void onActivityPause();

	public void onActivityResume();

	public void onActivityStop();

	public void onActivityRestart();

	public void onActivityDestroy();
	
	public void onSaveInstanceState(Bundle outState);
	
	public void onActivityResult(int requestCode, int resultCode, Intent data);
	
	public void onConfigurationChanged(Configuration newConfig);
	
	public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState);

	public void onBackPressed();
}
