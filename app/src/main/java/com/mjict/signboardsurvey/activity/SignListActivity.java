package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.SignListAdapter;
import com.mjict.signboardsurvey.model.SignInfo;

/**
 * Created by Junseo on 2016-11-16.
 */
public class SignListActivity extends SABaseActivity {

    private TextView shopNameTextView;
    private TextView shopCategoryTextView;
    private ListView signListView;
    private SignListAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_list;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.signboard_list);

        shopNameTextView = (TextView)this.findViewById(R.id.shop_name_text_view);
        shopCategoryTextView = (TextView)this.findViewById(R.id.shop_category_text_view);
        signListView = (ListView)this.findViewById(R.id.sign_list_view);
        adapter = new SignListAdapter(this);
        signListView.setAdapter(adapter);

    }

    public void setShopNameText(String name) {
        shopNameTextView.setText(name);
    }

    public void setShopCategoryText(String category) {
        shopCategoryTextView.setText(category);
    }

    public void setSignListOnItemClickListener(AdapterView.OnItemClickListener listener) {
        signListView.setOnItemClickListener(listener);
    }

    public void addToList(SignInfo sign) {
        adapter.add(sign);
    }

    public void setSignImage(int index, Bitmap image) {
        adapter.setImage(index, image);
    }
}
