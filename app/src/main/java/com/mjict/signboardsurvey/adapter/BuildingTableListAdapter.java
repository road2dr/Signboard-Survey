package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.model.BuildingResult;
import com.mjict.signboardsurvey.model.TwoBuildingResult;

/**
 * Created by Junseo on 2016-11-14.
 */
public class BuildingTableListAdapter extends ArrayAdapter<TwoBuildingResult> {

    private LayoutInflater inflater = null;
    private Context context;

    public BuildingTableListAdapter(Context context) {
        super(context, R.layout.list_row_two_building);
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = inflater.inflate(R.layout.list_row_two_building, parent, false);
        }

        TwoBuildingResult tb = getItem(position);
        BuildingResult first = tb.first;
        BuildingResult second = tb.second;

        ImageView firstImageView = (ImageView)view.findViewById(R.id.first_image_view);
        TextView firstTitleTextView = (TextView)view.findViewById(R.id.first_title_text_view);
        TextView firstAddressTextView = (TextView)view.findViewById(R.id.first_house_address_text_view);

        ImageView secondImageView = (ImageView)view.findViewById(R.id.second_image_view);
        TextView secondTitleTextView = (TextView)view.findViewById(R.id.second_title_text_view);
        TextView secondAddressTextView = (TextView)view.findViewById(R.id.second_house_address_text_view);

        if(first != null) {
            String title = (first.buildingName == null) ? first.buildingNumber : first.buildingName;
            firstTitleTextView.setText(title);
            firstImageView.setImageResource(first.image);
            firstAddressTextView.setText(first.houseAddress);
        }

        if(second != null) {
            String title = (second.buildingName == null) ? second.buildingNumber : second.buildingName;
            secondTitleTextView.setText(title);
            secondImageView.setImageResource(second.image);
            secondAddressTextView.setText(second.houseAddress);
        }

        return view;
    }
}
