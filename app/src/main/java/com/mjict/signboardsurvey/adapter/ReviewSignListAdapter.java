package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.holder.ReviewSignListRowHolder;
import com.mjict.signboardsurvey.model.ui.ReviewSign;


/**
 * Created by Junseo on 2016-07-24.
 */
public class ReviewSignListAdapter extends ArrayAdapter<ReviewSign> {


    private LayoutInflater inflater = null;
    private Context context;

    public ReviewSignListAdapter(Context context) {
        super(context, R.layout.list_row_review_sign);

        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ReviewSignListRowHolder holder;

        Log.d("junseo", "getView: "+position);

        if(view == null) {
            view =  inflater.inflate(R.layout.list_row_review_sign, parent, false);
            holder = new ReviewSignListRowHolder(view);
            view.setTag(holder);
        } else {
            holder = (ReviewSignListRowHolder)view.getTag();
        }

        ReviewSign data = getItem(position);

        int visibility = data.permitted ? View.VISIBLE : View.GONE;

        holder.getSignImageView().setLabelVisual(false);    // 이미지 하단부로 옮김
        holder.getReviewTextView().setText(data.status);
        holder.getContentTextView().setText(data.content);
        holder.getPermitView().setVisibility(visibility);
        holder.getAddressTextView().setText(data.address);
        holder.getResultTextView().setText(data.result);
        holder.getSizeTextView().setText(data.size);
        holder.getSignImageView().setImageBitmap(data.signImage);

        return view;
    }

    public void setSignImage(int position, Bitmap image) {
        if(position < 0 || position >= getCount())
            return;

        ReviewSign data = getItem(position);
        data.signImage = image;

        notifyDataSetChanged();
    }
}
