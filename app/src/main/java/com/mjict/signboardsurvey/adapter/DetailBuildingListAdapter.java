package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.holder.DetailBuildingRowViewHolder;
import com.mjict.signboardsurvey.model.ui.BuildingResult;

/**
 * Created by Junseo on 2017-01-05.
 */
public class DetailBuildingListAdapter extends ArrayAdapter<BuildingResult> {

    private LayoutInflater inflater = null;
    private Context context;

    public DetailBuildingListAdapter(Context context) {
        super(context, R.layout.list_row_search_result_building);
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
        DetailBuildingRowViewHolder holder = null;
        if(view == null) {
            view = inflater.inflate(R.layout.list_row_search_result_building, parent, false);
            holder = new DetailBuildingRowViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (DetailBuildingRowViewHolder)view.getTag();
        }

        BuildingResult br = getItem(position);

        holder.getImageView().setImageBitmap(br.image);
        holder.getNameTextView().setText(br.name);
        holder.getAddressTextView().setText(br.streetAddress);
        holder.getShopCountTextView().setText(br.shopCountText);
        holder.getSignCountTextView().setText(br.signCountText);

        return view;
    }
}
