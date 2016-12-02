package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.holder.KeywordRowViewHolder;

/**
 * Created by Junseo on 2016-12-02.
 */
public class KeywordListAdapter extends ArrayAdapter<String>  {

    private LayoutInflater inflater = null;
    private Context context;

    public KeywordListAdapter(Context context) {
        super(context, R.layout.list_row_keyword);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        KeywordRowViewHolder holder = null;
        if(view == null) {
            view = inflater.inflate(R.layout.list_row_keyword, parent, false);
            holder = new KeywordRowViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (KeywordRowViewHolder)view.getTag();
        }

        String keyword = getItem(position);
        holder.getKeywordTextView().setText(keyword);


        return view;
    }
}