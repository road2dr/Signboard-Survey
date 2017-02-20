package com.mjict.signboardsurvey.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatSpinner;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.mjict.signboardsurvey.R;


/**
 * Created by Junseo on 2016-07-29.
 */
public class SimpleSpinner extends AppCompatSpinner implements AdapterView.OnItemSelectedListener {

    private ArrayAdapter<SpinnerItem> spinnerAdapter;
    private OnItemSelectionChangedListener listener;
    private OnItemSelectionChangedByTouchListener byTouchListener;

    public SimpleSpinner(Context context) {
        super(context);
        init(context, null);
    }

    public SimpleSpinner(Context context, AttributeSet attr) {
        super(context, attr);
        init(context, attr);
    }

    private void init(Context context, AttributeSet attrs) {
        int dropdownTextView = -1;
        int topTextView = -1;
        if(attrs != null) {
            final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.SimpleSpinner);
            dropdownTextView = typedArray.getResourceId(R.styleable.SimpleSpinner_dropdownTextView, R.layout.tv_simple_spinner_dropdown);
            topTextView = typedArray.getResourceId(R.styleable.SimpleSpinner_topTextView, R.layout.tv_simple_spinner_top);
        }

        spinnerAdapter = new ArrayAdapter<SpinnerItem>(context, topTextView);
        spinnerAdapter.setDropDownViewResource(dropdownTextView);


        this.setAdapter(spinnerAdapter);
        this.setOnItemSelectedListener(this);
    }

    public void setOnItemSelectionChangedListener(OnItemSelectionChangedListener listener) {
        this.listener = listener;
    }

    public void setOnItemSelectionChangedByTouchListener(OnItemSelectionChangedByTouchListener listener) {
        byTouchListener = listener;
    }

//    /**
//     * 기존 데이터를 지우고 새로 넣지만 onItemSelected 이벤트가 발생하지 않는다. <br>
//     * onItemSelected 이벤트를 발생 시키고 데이터를 넣고 싶으면 clearSpinner()를 호출한뒤에 이 함수를 호출 하면 된다.
//     * @param data
//     */
//    public void setSpinnerData(String[] data) {
//        spinnerAdapter.clear();
//
//        if(data == null)
//            return;
//
//        for(int i=0; i<data.length; i++)
//            spinnerAdapter.add(new SpinnerData(i, data[i]));
//    }

    public void addSpinnerData(Object data) {
        int i = spinnerAdapter.getCount();
        spinnerAdapter.add(new SpinnerItem(i, data));
    }

    public void addSpinnerData(Object id, Object data) {
        spinnerAdapter.add(new SpinnerItem(id, data));
    }

    public void addSpinnerData(SpinnerItem item) {
        spinnerAdapter.add(item);
    }

    public void clearSpinner() {
        spinnerAdapter.clear();
    }

    public Object getSelectedDataId() {
        if(spinnerAdapter == null)
            return -1;

        int idx = getSelectedItemPosition();
        SpinnerItem item = (idx == AdapterView.INVALID_POSITION) ? null : spinnerAdapter.getItem(idx);
        if(item == null)
            return -1;

        return item.id;
    }

    public Object getSelectedData() {
        if(spinnerAdapter == null)
            return null;

        int idx = getSelectedItemPosition();
        SpinnerItem item = (idx == AdapterView.INVALID_POSITION) ? null : spinnerAdapter.getItem(idx);
        if(item == null)
            return null;

        return item.data;
    }

//    public Object getSelectedSpinnerData() {
//        if(spinnerAdapter == null)
//            return null;
//
//        int idx = getSelectedItemPosition();
//        SpinnerItem item = (idx == AdapterView.INVALID_POSITION) ? null : spinnerAdapter.getItem(idx);
//        return item.data;
//    }

    public void setSpinnerSelection(Object id) {
        int n = spinnerAdapter.getCount();
        for(int i=0; i<n ;i++) {
            SpinnerItem sd = spinnerAdapter.getItem(i);
            if(sd.id.equals(id)) {
                this.setSelection(i, false);
                return;
            }
        }
        this.setSelection(0, false);
    }

    private boolean spinnerTouched = false;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        spinnerTouched = true;
        return super.onTouchEvent(event);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == AdapterView.INVALID_POSITION || spinnerAdapter.getCount() <= 0)
            return;

        SpinnerItem item = spinnerAdapter.getItem(position);

        if(listener != null)
            listener.onItemSelectionChanged(position, item.id, item.data);

        if(spinnerTouched && byTouchListener != null)
            byTouchListener.onItemSelectionChanged(position, item.data);

        spinnerTouched = false;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(listener != null)
            listener.onItemSelectionChanged(-1, -1, null);

        spinnerTouched = false;
    }

//    public void setOnDataSelectedListener(OnDataSelectedListener listener) {
//        this.listener = listener;
//    }
//
//    public interface OnDataSelectedListener {
//        void onItemSelected(int position, SpinnerData data);
//    }

    public static class SpinnerItem {
        public Object id;
        public Object data;

        public SpinnerItem(Object i, Object d) {
            id = i;
            data = d;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    public static interface OnItemSelectionChangedListener {
        void onItemSelectionChanged(int position, Object id, Object data);
    }

    public static interface OnItemSelectionChangedByTouchListener {
        void onItemSelectionChanged(int position, Object data);
    }
}
