package com.mjict.signboardsurvey.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2017-03-07.
 */
public class SimpleBuildingRowViewHolder {

    private View parent;
    private TextView addressTextView;

    public SimpleBuildingRowViewHolder(View view) {
        parent = view;
    }

    public TextView getAddressTextView() {
        if(addressTextView == null) {
            addressTextView = (TextView) parent.findViewById(R.id.address_text_view);
        }

        return addressTextView;
    }
}
