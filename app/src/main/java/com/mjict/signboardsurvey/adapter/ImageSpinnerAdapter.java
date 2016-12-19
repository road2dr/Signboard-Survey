package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.model.IconItem;

/**
 * Created by Junseo on 2016-11-17.
 */
public class ImageSpinnerAdapter extends ArrayAdapter<IconItem> {

    private LayoutInflater inflater = null;
    private Context context;

    /*************  CustomAdapter Constructor *****************/
    public ImageSpinnerAdapter(Context context) {
        super(context, R.layout.spinner_light_type);

        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getDropDownView(int position, View convertView,ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    // This funtion called for each row ( Called data.size() times )
    public View getCustomView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null)
            view = inflater.inflate(R.layout.spinner_light_type, parent, false);


        ImageView imageView = (ImageView)view.findViewById(R.id.image_view);
        TextView textView = (TextView)view.findViewById(R.id.text_view);

        IconItem item = getItem(position);
        if(item.image != -1)
            imageView.setImageResource(item.image);
        textView.setText(item.obj.toString());

//        if(position==0){
//
//            // Default selected Spinner item
//            label.setText("Please select company");
//            sub.setText("");
//        }
//        else
//        {
//            // Set values for spinner each row
//            label.setText(tempValues.getCompanyName());
//            sub.setText(tempValues.getUrl());
//            companyLogo.setImageResource(res.getIdentifier
//                    ("com.androidexample.customspinner:drawable/"
//                            + tempValues.getImage(),null,null));
//
//        }

        return view;
    }
}
