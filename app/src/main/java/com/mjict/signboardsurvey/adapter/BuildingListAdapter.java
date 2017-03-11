package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.holder.BuildingRowViewHolder;
import com.mjict.signboardsurvey.model.ui.BuildingResult;

/**
 * Created by Junseo on 2016-11-14.
 */
public class BuildingListAdapter extends ArrayAdapter<BuildingResult> {

    private LayoutInflater inflater = null;
    private Context context;

    public BuildingListAdapter(Context context) {
        super(context, R.layout.list_row_building);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setImage(int position, Bitmap image) {
        if(position < 0 || position >= getCount())
            return;

        BuildingResult br = getItem(position);
        br.image = image;

        notifyDataSetChanged();
    }

    public void replaceItem(int position, BuildingResult data) {
        if(position < 0 || position >= getCount())
            return;

        BuildingResult br = getItem(position);
        remove(br);
        insert(data, position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        BuildingRowViewHolder holder = null;
        if(view == null) {
            view = inflater.inflate(R.layout.list_row_building, parent, false);
            holder = new BuildingRowViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (BuildingRowViewHolder)view.getTag();
        }

        BuildingResult br = getItem(position);

        holder.getTitleTextView().setText(br.name);
        if(br.image != null)
            holder.getImageView().setImageBitmap(br.image);
        else
            holder.getImageView().setImageResource(R.drawable.ic_building);
        holder.getHouseAddressTextView().setText(br.houseAddress);
        holder.getStreetAddressTextView().setText(br.streetAddress);

        return view;
    }
}
