package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.holder.RecentBuildingViewHolder;
import com.mjict.signboardsurvey.model.BitmapBuilding;
import com.mjict.signboardsurvey.model.ui.RecentBuilding;


/**
 * Created by Junseo on 2016-11-30.
 */
public class RecentBuildingListAdapter extends ArrayAdapter<RecentBuilding> {

    private LayoutInflater inflater = null;
    private Context context;

    public RecentBuildingListAdapter(Context context) {
        super(context, R.layout.list_col_recent_building);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setImage(int position, Bitmap img) {
        if(position <0 || position >= getCount())
            return;

        BitmapBuilding b = getItem(position);
        b.image = img;

        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        RecentBuildingViewHolder holder = null;
        if(view == null) {
            view = inflater.inflate(R.layout.list_col_recent_building, parent, false);
            holder = new RecentBuildingViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (RecentBuildingViewHolder)view.getTag();
        }

        RecentBuilding b = getItem(position);

        if(b.image != null)
            holder.getImageView().setImageBitmap(b.image);
        else
            holder.getImageView().setImageResource(R.drawable.ic_no_image);
        holder.getNameTextView().setText(b.name);
        holder.getAddressTextView().setText(b.address);


        return view;
    }
}
