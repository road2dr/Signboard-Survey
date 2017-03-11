package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.holder.SimpleBuildingRowViewHolder;
import com.mjict.signboardsurvey.model.ui.SimpleBuilding;

/**
 * Created by Junseo on 2017-03-07.
 */
public class SimpleBuildingListAdapter extends ArrayAdapter<SimpleBuilding> {

    private LayoutInflater inflater = null;
    private Context context;

    public SimpleBuildingListAdapter(Context context) {
        super(context, R.layout.list_row_simple_building);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        SimpleBuildingRowViewHolder holder = null;
        if(view == null) {
            view = inflater.inflate(R.layout.list_row_simple_building, parent, false);
            holder = new SimpleBuildingRowViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (SimpleBuildingRowViewHolder)view.getTag();
        }

        SimpleBuilding b = getItem(position);

        holder.getAddressTextView().setText(b.address);

        return view;
    }
}
