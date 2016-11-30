package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.model.ShopInfo;
import com.mjict.signboardsurvey.model.ThreeShopInfo;

/**
 * Created by Junseo on 2016-11-15.
 */
public class ShopTableListAdapter extends ArrayAdapter<ThreeShopInfo> {

    private LayoutInflater inflater = null;
    private Context context;

    public ShopTableListAdapter(Context context) {
        super(context, R.layout.list_row_three_shop);
        this.context = context;
        inflater = LayoutInflater.from(context);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if(view == null) {
            view = inflater.inflate(R.layout.list_row_three_shop, parent, false);
        }

        ThreeShopInfo ss = getItem(position);
        ShopInfo first = ss.first;
        ShopInfo second = ss.second;
        ShopInfo third = ss.third;

        View firstCell = (View)view.findViewById(R.id.first_cell);
        View secondCell = (View)view.findViewById(R.id.second_cell);
        View thirdCell = (View)view.findViewById(R.id.third_cell);

        if(first == null)
            firstCell.setVisibility(View.INVISIBLE);
        else {
            firstCell.setVisibility(View.VISIBLE);
            TextView nameTextView = (TextView)view.findViewById(R.id.first_name_text_view);
            TextView categoryTextView = (TextView)view.findViewById(R.id.first_category_text_view);
            TextView phoneTextView = (TextView)view.findViewById(R.id.first_phone_text_view);

            nameTextView.setText(first.name);
            categoryTextView.setText(first.category);
            phoneTextView.setText(first.phone);
        }

        if(second == null)
            secondCell.setVisibility(View.INVISIBLE);
        else {
            secondCell.setVisibility(View.VISIBLE);
            TextView nameTextView = (TextView)view.findViewById(R.id.second_name_text_view);
            TextView categoryTextView = (TextView)view.findViewById(R.id.second_category_text_view);
            TextView phoneTextView = (TextView)view.findViewById(R.id.second_phone_text_view);

            nameTextView.setText(second.name);
            categoryTextView.setText(second.category);
            phoneTextView.setText(second.phone);
        }

        if(third == null)
            thirdCell.setVisibility(View.INVISIBLE);
        else {
            thirdCell.setVisibility(View.VISIBLE);
            TextView nameTextView = (TextView)view.findViewById(R.id.third_name_text_view);
            TextView categoryTextView = (TextView)view.findViewById(R.id.third_category_text_view);
            TextView phoneTextView = (TextView)view.findViewById(R.id.third_phone_text_view);

            nameTextView.setText(third.name);
            categoryTextView.setText(third.category);
            phoneTextView.setText(third.phone);
        }
        return view;
    }
}
