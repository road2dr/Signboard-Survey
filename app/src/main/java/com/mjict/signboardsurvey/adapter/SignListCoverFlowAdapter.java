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
public class SignListCoverFlowAdapter extends BaseAdapter {

    private ArrayList<SignInfo> signs;

    private LayoutInflater inflater = null;
    private Context context;

    public SignListCoverFlowAdapter(Context context) {
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

        View view = inflater.inflate(R.layout.list_row_sign_recent_style, null, false);

        ImageView imageView = (ImageView)view.findViewById(R.id.image_view);
        TextView contentTextView = (TextView)view.findViewById(R.id.content_text_view);
        TextView sizeTextView = (TextView)view.findViewById(R.id.size_text_view);
        TextView resultTextView = (TextView)view.findViewById(R.id.result_text_view);

//        imageView.setImageResource(s.image);
        contentTextView.setText(s.content);
        sizeTextView.setText(s.size);
        resultTextView.setText(s.result);

        final int[] colors = {0xffFFFFA5, 0xffE7FFC0, 0xffFFD2FF};
        int color = colors[position%3];
        view.setBackgroundColor(color);

//        ImageView iv = new ImageView(context);
//        iv.setImageResource(s.image);
//        iv.setBackgroundColor(0xffffffff);
//        return iv;

        return view;
    }
}
