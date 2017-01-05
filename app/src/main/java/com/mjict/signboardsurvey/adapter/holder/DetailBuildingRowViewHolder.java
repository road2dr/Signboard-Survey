package com.mjict.signboardsurvey.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2017-01-05.
 */
public class DetailBuildingRowViewHolder {
//    View view = inflater.inflate(R.layout.list_row_search_result_building, null);
//    ImageView imageView = (ImageView) view.findViewById(R.id.image_view);
//    TextView nameTextView = (TextView)view.findViewById(R.id.name_text_view);
//    TextView addressTextView = (TextView)view.findViewById(R.id.address_text_view);
//    TextView shopCountTextView = (TextView)view.findViewById(R.id.shop_count_text_view);
//    TextView signCountTextView = (TextView)view.findViewById(R.id.sign_count_text_view);

    private View parent;
    private ImageView imageView;
    private TextView nameTextView;
    private TextView addressTextView;
    private TextView shopCountTextView;
    private TextView signCountTextView;

    public DetailBuildingRowViewHolder(View view) {
        parent = view;
    }

    public ImageView getImageView() {
        if(imageView == null)
            imageView = (ImageView) parent.findViewById(R.id.image_view);

        return imageView;
    }

    public TextView getNameTextView() {
        if(nameTextView == null)
            nameTextView = (TextView)parent.findViewById(R.id.name_text_view);

        return nameTextView;
    }

    public TextView getAddressTextView() {
        if(addressTextView == null)
            addressTextView = (TextView)parent.findViewById(R.id.address_text_view);

        return addressTextView;
    }

    public TextView getShopCountTextView() {
        if(shopCountTextView == null)
            shopCountTextView = (TextView)parent.findViewById(R.id.shop_count_text_view);

        return shopCountTextView;
    }

    public TextView getSignCountTextView() {
        if(signCountTextView == null)
            signCountTextView = (TextView)parent.findViewById(R.id.sign_count_text_view);

        return signCountTextView;
    }
}
