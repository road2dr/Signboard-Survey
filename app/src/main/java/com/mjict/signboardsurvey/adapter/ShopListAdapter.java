package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.model.ShopInfo;

/**
 * Created by Junseo on 2016-11-14.
 */
public class ShopListAdapter extends ArrayAdapter<ShopInfo> {

    private LayoutInflater inflater = null;
    private Context context;

    public ShopListAdapter(Context context) {
        super(context, R.layout.list_row_shop);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = inflater.inflate(R.layout.list_row_shop, parent, false);
        }

        ShopInfo s = getItem(position);

        TextView nameTextView = (TextView)view.findViewById(R.id.shop_name_text_view);
        TextView phoneTextView = (TextView)view.findViewById(R.id.phone_number_text_view);
        TextView categoryTextView = (TextView)view.findViewById(R.id.shop_category_text_view);

        nameTextView.setText(s.name);
        phoneTextView.setText(s.phone);
        categoryTextView.setText(s.category);

        return view;

    }
}
