package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2017-01-05.
 */
public class TextViewAdapter extends ArrayAdapter<CharSequence> {
    private LayoutInflater inflater = null;
    private Context context;

    public TextViewAdapter(Context context) {
        super(context, R.layout.tv_search_result_address);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null)
            view = inflater.inflate(R.layout.tv_search_result_address, parent, false);

        TextView tv = (TextView)view;
        CharSequence text = getItem(position);
        tv.setText(text);


        return view;
    }
}
