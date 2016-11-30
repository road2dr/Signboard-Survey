package com.mjict.signboardsurvey.widget;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;


/**
 * @author Junseo
 *
 */
public class WaitingDialog extends Dialog {

//	private ProgressBar barProgressBar;
	private ProgressBar waitingView;
	private TextView statusTextView;
	
	public WaitingDialog(Context context) {
		super(context , android.R.style.Theme_Translucent_NoTitleBar);		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();    
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);
		setCanceledOnTouchOutside(false);
         
        setContentView(R.layout.dialog_waiting);
        
//        barProgressBar = (ProgressBar)this.findViewById(R.id.bar_progress_bar);

		waitingView = (ProgressBar)this.findViewById(R.id.circle_progress_bar);
//		waitingView.
        
        statusTextView = (TextView)this.findViewById(R.id.status_text_view);
	}
	
	public void setProgress(int value) {
//		barProgressBar.setProgress(value);
	}
	
	public void setStatusText(String text) {
		statusTextView.setText(text);
	}
	
	public void setStatusText(int resid) {
		statusTextView.setText(resid);
	}

    @Override
    public void onBackPressed() {
//        super.onBackPressed();    백 버튼에 의해 내려가는 거 막기
    }

//	@Override
//	public void show() {
//		Drawable drawable = waitingView.getDrawable();
//		if(drawable instanceof AnimationDrawable )
//			((AnimationDrawable) drawable).start();
//
//		super.show();
//	}
//
//	@Override
//	public void dismiss() {
//		Drawable drawable = waitingView.getDrawable();
//		if(drawable instanceof AnimationDrawable )
//			((AnimationDrawable) drawable).stop();
//
//		super.dismiss();
//	}
}
