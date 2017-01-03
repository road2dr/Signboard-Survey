package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.model.ui.SignInfo;

import java.util.ArrayList;

/**
 * Created by Junseo on 2016-11-16.
 */
public class SignSimpleCoverFlowAdapter extends BaseAdapter {

    private ArrayList<SignInfo> signs;

    private LayoutInflater inflater = null;
    private Context context;

    public SignSimpleCoverFlowAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        signs = new ArrayList<>();
    }

    public void add(SignInfo s) {
        signs.add(s);
    }

    @Override
    public int getCount() {
        return signs.size();
    }

    @Override
    public Object getItem(int position) {
        return signs.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SignInfo s = signs.get(position);

        View view = inflater.inflate(R.layout.cover_sign_simple, null, false);

        ImageView imageView = (ImageView)view.findViewById(R.id.image_view);
        TextView sizeTextView = (TextView)view.findViewById(R.id.size_text_view);
        TextView resultTextView = (TextView)view.findViewById(R.id.result_text_view);

//        imageView.setImageResource(s.image);
        sizeTextView.setText(s.size);
        resultTextView.setText(s.result);

        return view;
    }
}

