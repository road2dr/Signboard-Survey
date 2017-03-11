package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.ArrayAdapter;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.holder.DemolishedSignListRowHolder;
import com.mjict.signboardsurvey.model.ui.DemolishedSign;
import com.mjict.signboardsurvey.widget.SwipeMenuLayout;


/**
 * Created by Junseo on 2016-07-25.
 */
public class DemolitionSignListAdapter extends ArrayAdapter<DemolishedSign> {

    private LayoutInflater inflater = null;
    private Context context;

    public DemolitionSignListAdapter(Context context) {
        super(context, R.layout.list_row_review_sign);

        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        SwipeMenuLayout layout = null;
        DemolishedSignListRowHolder holder = null;
        if(view == null) {
            View contentView = inflater.inflate(R.layout.list_row_review_sign, parent, false);
            View swipeView = inflater.inflate(R.layout.list_row_demolition_sign_swipe, parent, false);
            layout = new SwipeMenuLayout(contentView, swipeView, new BounceInterpolator(), new BounceInterpolator());

            holder = new DemolishedSignListRowHolder(layout);
            layout.setTag(holder);
        } else {
            layout = (SwipeMenuLayout)view;
            layout.closeMenu();
            holder = (DemolishedSignListRowHolder) view.getTag();
        }

        DemolishedSign data = getItem(position);

        boolean labelVisible = false;
        int labelColor = -1;
        String labelText = null;

        holder.getSignImageView().setLabelVisual(labelVisible);
        if(data.labelVisible) {
            holder.getSignImageView().setLabelText(data.labelText);
            holder.getSignImageView().setLabelBackgroundColor(data.labelColor);
            layout.setSwipEnable(true);
        }

//        holder.getContentTextView().setText(data.content);
//        holder.getTypeTextView().setText(data.type);
//        holder.getAddressTextView().setText(data.light);    // TODO
//        holder.getDemolitionDateTextView().setText(data.date);
//        holder.getLocationTextView().setText(data.location);
//        holder.getResultTextView().setText(data.result);
//        holder.getSizeTextView().setText(data.size);

        int visibility = data.permitted ? View.VISIBLE : View.GONE;

        holder.getSignImageView().setLabelVisual(false);    // 이미지 하단부로 옮김
        holder.getReviewTextView().setText(data.status.toString());
        holder.getContentTextView().setText(data.content);
        holder.getPermitView().setVisibility(visibility);
        holder.getDemolitionDateTextView().setText(data.date);
        holder.getAddressTextView().setText(data.address);
        holder.getResultTextView().setText(data.result);
        holder.getSizeTextView().setText(data.size);
        holder.getSignImageView().setImageBitmap(data.signImage);

        holder.getSignImageView().setImageBitmap(data.signImage);
        holder.getDemolitionImageView().setImageBitmap(data.demolishedImage);

        layout.setPosition(position);

        return layout;
    }

    public void setSignImage(int position, Bitmap image) {
        if(position < 0 || position >= getCount())
            return;

        DemolishedSign data = getItem(position);
        data.signImage = image;

        notifyDataSetChanged();
    }

    public void setDemolitionImage(int position, Bitmap image) {
        if(position < 0 || position >= getCount())
            return;

        DemolishedSign data = getItem(position);
        data.demolishedImage = image;

        notifyDataSetChanged();
    }

//    public void replace(int position, DemolitionSign data) {
//        if(position < 0 || position >= getCount())
//            return;
//
//
//    }
}
