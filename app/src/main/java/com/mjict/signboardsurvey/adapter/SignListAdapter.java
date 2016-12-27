package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.holder.SignInfoRowViewHolder;
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

    public void replaceItem(int position, SignInfo data) {
        if(position < 0 || position >= getCount())
            return;

        SignInfo sd = getItem(position);
        remove(sd);
        insert(data, position);
    }

    public void setImage(int position, Bitmap image) {
        if(position < 0 || position >= getCount())
            return;

        SignInfo s = getItem(position);
        s.image = image;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        SignInfoRowViewHolder holder = null;
        if(view == null) {
            view = inflater.inflate(R.layout.list_row_sign, parent, false);
            holder = new SignInfoRowViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (SignInfoRowViewHolder)view.getTag();
        }

        SignInfo s = getItem(position);

        holder.getImageView().setImageBitmap(s.image);
        holder.getNameTextView().setText(s.content);
        holder.getResultTextView().setText(s.result);
        holder.getSizeTextView().setText(s.size);

        final int[] colors = {0xffFFFFA5, 0xffE7FFC0, 0xffFFD2FF};
        int color = colors[position%3];
        view.setBackgroundColor(color);

        return view;
    }
}
