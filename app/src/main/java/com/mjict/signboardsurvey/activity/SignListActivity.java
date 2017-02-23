package com.mjict.signboardsurvey.activity;

import android.graphics.Bitmap;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.SignListAdapter;
import com.mjict.signboardsurvey.model.ui.SignInfo;
import com.mjict.signboardsurvey.widget.SignOptionDialog;

/**
 * Created by Junseo on 2016-11-16.
 */
public class SignListActivity extends SABaseActivity {

    private TextView shopNameTextView;
    private TextView shopCategoryTextView;
    private ListView signListView;
    private SignListAdapter adapter;
    private SignOptionDialog signOptionDialog;

    private OnOptionMenuItemClickListener optionMenuItemClickListener;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_list;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.signboard_list);
        this.inflateOptionMenu(R.menu.option_menu_sign_list);
        this.showOptionButton();

        this.setOnOptionMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.add_sign:
                        if(optionMenuItemClickListener != null)
                            optionMenuItemClickListener.onAddNewSignClicked();
                        break;
                }
                return true;
            }
        });

        shopNameTextView = (TextView)this.findViewById(R.id.shop_name_text_view);
        shopCategoryTextView = (TextView)this.findViewById(R.id.shop_category_text_view);
        signListView = (ListView)this.findViewById(R.id.sign_list_view);
        adapter = new SignListAdapter(this);
        signListView.setAdapter(adapter);

        signOptionDialog = new SignOptionDialog(this);
        signOptionDialog.show();
        signOptionDialog.hide();
//        signOptionDialog.create();
    }

    @Override
    protected void onDestroy() {
        if(signOptionDialog != null)
            signOptionDialog.dismiss();

        super.onDestroy();
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

    public void setSignListOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        signListView.setOnItemLongClickListener(listener);
    }

    public void removeFromList(int index) {
        SignInfo item = adapter.getItem(index);
        adapter.remove(item);
    }

    public void addToList(SignInfo sign) {
        adapter.add(sign);
    }

    public void setSignInfo(int position, SignInfo sign) {
        adapter.replaceItem(position, sign);
    }

    public void setSignImage(int index, Bitmap image) {
        adapter.setImage(index, image);
    }

    public void setOptionMenuItemClickListener(OnOptionMenuItemClickListener listener) {
        optionMenuItemClickListener = listener;
    }

    public void setSignDeleteDialogButtonVisible(boolean visible) {
        signOptionDialog.setDeleteButtonVisible(visible);
    }

    public void showSignOptionDialog(SignOptionDialog.SignOptionDialogOnClickListener listener) {
        signOptionDialog.setSignOptionDailogOnClickListener(listener);
        signOptionDialog.show();
    }

    public void hideSignOptionDialog() {
        signOptionDialog.hide();
    }

    public static interface OnOptionMenuItemClickListener {
        public void onAddNewSignClicked();
    }
}
