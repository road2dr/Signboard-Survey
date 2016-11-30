package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.model.SignInfo;

/**
 * Created by Junseo on 2016-11-16.
 */
public class SignListAdapter extends ArrayAdapter<SignInfo> {
    private LayoutInflater inflater = null;
    private Context context;

    public SignListAdapter(Context context) {
        super(context, R.layout.list_row_sign);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = inflater.inflate(R.layout.list_row_sign, parent, false);
        }

        SignInfo s = getItem(position);

        ImageView imageView = (ImageView)view.findViewById(R.id.image_view);
        TextView nameTextView = (TextView)view.findViewById(R.id.name_text_view);
        TextView sizeTextView = (TextView)view.findViewById(R.id.size_text_view);
        TextView resultTextView = (TextView)view.findViewById(R.id.result_text_view);

        imageView.setImageResource(s.image);
        nameTextView.setText(s.content);
        sizeTextView.setText(s.size);
        resultTextView.setText(s.result);

        final int[] colors = {0xffFFFFA5, 0xffE7FFC0, 0xffFFD2FF};
        int color = colors[position%3];
        view.setBackgroundColor(color);

        return view;

    }
}
