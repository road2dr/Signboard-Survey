package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.holder.RecentSignViewHolder;
import com.mjict.signboardsurvey.model.BitmapSign;
import com.mjict.signboardsurvey.model.ui.RecentSign;

/**
 * Created by Junseo on 2016-11-10.
 */
public class RecentSignListAdapter extends ArrayAdapter<RecentSign> {

    private LayoutInflater inflater = null;
    private Context context;

    public RecentSignListAdapter(Context context) {
        super(context, R.layout.list_col_recent_sign);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setImage(int position, Bitmap image) {
        if(position <0 || position >= getCount())
            return;

        BitmapSign sign = getItem(position);
        sign.image = image;

        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        RecentSignViewHolder holder = null;
        if(view == null) {
            view = inflater.inflate(R.layout.list_col_recent_sign, parent, false);
            holder = new RecentSignViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (RecentSignViewHolder)view.getTag();
        }

        RecentSign s = getItem(position);
        if(s.image != null)
            holder.getImageView().setImageBitmap(s.image);
        else
            holder.getImageView().setImageResource(R.drawable.ic_no_image);

        holder.getContentTextView().setText(s.content);
        holder.getTypeTextView().setText(s.type);
        holder.getResultTextView().setText(s.result);

        return view;
    }
}
