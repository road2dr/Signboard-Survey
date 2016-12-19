package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.model.SignInfo;
import com.mjict.signboardsurvey.widget.RecentsAdapter;

import java.util.ArrayList;

/**
 * Created by Junseo on 2016-11-15.
 */
public class SignListRecentStyleAdapter implements RecentsAdapter {
    private ArrayList<SignInfo> signs;

    private LayoutInflater inflater = null;
    private Context context;

    public SignListRecentStyleAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
        signs = new ArrayList<>();
    }

    public void add(SignInfo sign) {
        signs.add(sign);
    }

    @Override
    public String getTitle(int position) {
        return signs.get(position).content;
    }

    @Override
    public View getView(int position) {
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

//        ImageView iv = new ImageView(context);
//        iv.setImageResource(s.image);
//        iv.setBackgroundColor(0xffffffff);
//        return iv;

        return view;
    }

    @Override
    public Drawable getIcon(int position) {
        return null;
    }

    @Override
    public int getHeaderColor(int position) {
        Log.d("junseo", "??: "+position);

        final int[] colors = {0xffFFFFA5, 0xffE7FFC0, 0xffFFD2FF};
        return colors[position%3];
    }

    @Override
    public int getCount() {
        return signs.size();
    }
}
