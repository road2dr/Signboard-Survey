package com.mjict.signboardsurvey.widget;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.AutoCompleteTextView;

/**
 * Created by Junseo on 2016-12-02.
 */
public class JAutoCompleteTextView extends AutoCompleteTextView {

    private int myThreshold;

    public JAutoCompleteTextView(Context context) {
        super(context);
    }

    public JAutoCompleteTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public JAutoCompleteTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    //set threshold 0.
    public void setThreshold(int threshold) {
        if (threshold < 0) {
            threshold = 0;
        }
        myThreshold = threshold;
    }
    //if threshold   is 0 than return true
    public boolean enoughToFilter() {
        return true;
    }
    //invoke on focus
    protected void onFocusChanged(boolean focused, int direction,
                                  Rect previouslyFocusedRect) {
        //skip space and backspace
        super.performFiltering("", 67);
        // TODO Auto-generated method stub
        super.onFocusChanged(focused, direction, previouslyFocusedRect);

    }

    protected void performFiltering(CharSequence text, int keyCode) {
        // TODO Auto-generated method stub
        super.performFiltering(text, keyCode);
    }

    public int getThreshold() {
        return myThreshold;
    }
}