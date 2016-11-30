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

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = inflater.inflate(R.layout.list_row_building, parent, false);
        }

        BuildingResult b = getItem(position);

        ImageView buildingImageView = (ImageView)view.findViewById(R.id.building_image_view);
        TextView titleTextView = (TextView)view.findViewById(R.id.title_text_view);
        TextView streetAddressTextView = (TextView)view.findViewById(R.id.street_address_text_view);
        TextView houseAddressTextView = (TextView)view.findViewById(R.id.house_address_text_view);

        String title = (b.buildingName == null) ? b.buildingNumber : b.buildingName;
        titleTextView.setText(title);
        buildingImageView.setImageResource(b.image);
        streetAddressTextView.setText(b.streetAddress);
        houseAddressTextView.setText(b.houseAddress);

        return view;
    }
}
