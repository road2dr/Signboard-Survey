package com.mjict.signboardsurvey.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mjict.signboardsurvey.R;


public class JExpandableLinearLayout extends LinearLayout {

	private ExpandAnimationListener animationListener;
	private int expandHeight;
	private int initialHeight;
	
	private boolean isAnimating = false;
	private boolean isExpanded = false;
	
	public JExpandableLinearLayout(Context context) {
		super(context);
	}
	
	public JExpandableLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.JExpandableLinearLayout);
        expandHeight = typedArray.getDimensionPixelSize(R.styleable.JExpandableLinearLayout_expandHeight, -1);
        initialHeight = typedArray.getDimensionPixelOffset(R.styleable.JExpandableLinearLayout_android_layout_height, -1);
        
        if(expandHeight == -1)
        	expandHeight = initialHeight;        
        
        typedArray.recycle();
	}
	
//	private init(Contex)
	
	public void setExpandHeight(int height) {
		expandHeight = height;
	}
	
//	public void setExpandAnimationListener(ExpandAnimationListener listener) {
//		animationListener = listener;
//	}
//	
//	public void expand() {
//		if()
//	}
	
	public boolean isExpanded() {
		return isExpanded;
	}

	public void expandWithAnimation(final ExpandAnimationListener listener) {
		startAnimation(listener, expandHeight);
	}
	
	public void foldWithAnimation(ExpandAnimationListener listener) {
		startAnimation(listener, initialHeight);
	}
	
	private void startAnimation(final ExpandAnimationListener listener, int height) {
		if(isAnimating)
			return;
		
		final ViewGroup.LayoutParams params = this.getLayoutParams();
		
		ValueAnimator animator = ValueAnimator.ofInt(params.height, height);
		animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
		    @Override
		    public void onAnimationUpdate(ValueAnimator valueAnimator) {
		        params.height = (Integer) valueAnimator.getAnimatedValue();
		        requestLayout();
		    }
		});
		
		animator.addListener(new AnimatorListener() {					
			@Override
			public void onAnimationStart(Animator animation) {
				isAnimating = true;
			}
			@Override
			public void onAnimationRepeat(Animator animation) {						
			}
			@Override
			public void onAnimationEnd(Animator animation) {
				isAnimating = false;
				isExpanded = !isExpanded;
				
				if(listener != null)
					listener.onExpandFinished();
			}
			@Override
			public void onAnimationCancel(Animator animation) {
			}
		});
		animator.setDuration(500);
		animator.start();
	}
	
	public interface ExpandAnimationListener {
		public void onExpandFinished();
	}
}
