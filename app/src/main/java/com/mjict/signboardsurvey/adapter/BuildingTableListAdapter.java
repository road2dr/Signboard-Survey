package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.holder.TwoBuildingRowViewHolder;
import com.mjict.signboardsurvey.model.ui.TwoBuildingResult;
import com.mjict.signboardsurvey.model.ui.BuildingResult;

/**
 * Created by Junseo on 2016-11-14.
 */
public class BuildingTableListAdapter extends ArrayAdapter<TwoBuildingResult> {

    private LayoutInflater inflater = null;
    private Context context;

    private OnColumnClickListener columnClickListener;

    public BuildingTableListAdapter(Context context) {
        super(context, R.layout.list_row_two_building);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void add(BuildingResult building) {
        int count = getCount();

        if(count == 0) {
            TwoBuildingResult newData = new TwoBuildingResult(building, null);
            add(newData);
            notifyDataSetChanged();
            return;
        }

        TwoBuildingResult last = getItem(count-1);
        if(last.second == null)
            last.second = building;
        else {
            TwoBuildingResult newData = new TwoBuildingResult(building, null);
            add(newData);
        }

        notifyDataSetChanged();
    }

    public void setImage(int position, Bitmap image) {
        BuildingResult item = getColumnItem(position);
        if(item != null)
            item.image = image;

        notifyDataSetChanged();
    }

    public BuildingResult getColumnItem(int index) {
        if(index < 0 || index >= getCount()*2)
            return null;

        TwoBuildingResult row = getItem(index/2);
        BuildingResult item = (index % 2 == 0) ? row.first : row.second;

        return item;
    }

    public void setOnColumnClickListener(OnColumnClickListener listener) {
        columnClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TwoBuildingRowViewHolder holder = null;
        if(view == null) {
            view = inflater.inflate(R.layout.list_row_two_building, parent, false);
            holder = new TwoBuildingRowViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (TwoBuildingRowViewHolder)view.getTag();
        }

        TwoBuildingResult tb = getItem(position);
        BuildingResult first = tb.first;
        BuildingResult second = tb.second;
        int lIndex = position*2;
        int rIndex = lIndex+1;

        holder.getFirstLayout().setTag(lIndex);
        holder.getFirstLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(columnClickListener != null) {
                    int index = (Integer)v.getTag();
                    columnClickListener.onClick(index);
                }
            }
        });

        holder.getFirstImageView().setImageBitmap(first.image);
        holder.getFirstTitleTextView().setText(first.name);
        holder.getFirstStreetAddressTextView().setText(first.streetAddress);
        holder.getFirstHouseAddressTextView().setText(first.houseAddress);

        if(second == null) {
            holder.getSecondLayout().setVisibility(View.INVISIBLE);
        } else {
            holder.getSecondLayout().setVisibility(View.VISIBLE);

            holder.getSecondLayout().setTag(rIndex);
            holder.getSecondLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(columnClickListener != null) {
                        int index = (Integer)v.getTag();
                        columnClickListener.onClick(index);
                    }
                }
            });

            holder.getSecondImageView().setImageBitmap(second.image);
            holder.getSecondTitleTextView().setText(second.name);
            holder.getSecondStreetAddressTextView().setText(second.streetAddress);
            holder.getSecondHouseAddressTextView().setText(second.houseAddress);

        }

        return view;
    }

    public static interface OnColumnClickListener {
        public void onClick(int index);
    }
}
