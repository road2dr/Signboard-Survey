package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.AdapterView;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.SignListActivity;
import com.mjict.signboardsurvey.activity.TextListActivity;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.task.SearchShopByKeywordTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;

import java.util.List;

/**
 * Created by Junseo on 2017-01-05.
 */
public class MoreShopResultActivityHandler extends SABaseActivityHandler {

    private TextListActivity activity;
    private String keyword;
    private List<Shop> resultShops;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (TextListActivity)getActivity();
        activity.setTitle(R.string.more_shop_search_result);

        // init
        Intent intent = activity.getIntent();
        keyword = intent.getStringExtra(MJConstants.KEYWORD);
        if(keyword == null) {
            // TODO something is wrong
            activity.finish();
            return;
        }

        // registerListener
        activity.setListOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Shop shop= resultShops.get(position);
                goToSignList(shop);
            }
        });

        // do first job
        startToSearchShop();
    }

    private void startToSearchShop() {
        SearchShopByKeywordTask task = new SearchShopByKeywordTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<Shop>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.searching);
            }

            @Override
            public void onTaskFinished(List<Shop> shops) {
                activity.hideWaitingDialog();

                resultShops = shops;
                if(resultShops == null) {
                    // TODO something is wrong
                    return;
                }
                int n = resultShops.size();
                for(int i=0; i<n; i++) {
                    Shop shop = resultShops.get(i);
                    String text = shop.getName()+" "+shop.getPhoneNumber();
                    int index = text.indexOf(keyword);  // TODO StreetAddress 에서 DB 구까지 내용이 있어야 할 듯.
                    SpannableStringBuilder ssb = new SpannableStringBuilder(text);
                    ssb.setSpan(new AbsoluteSizeSpan(40), index, index+keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    activity.addToList(ssb);
                }
            }
        });
        task.execute(keyword);
    }

    private void goToSignList(Shop shop) {
        Intent intent = new Intent(activity, SignListActivity.class);
        intent.putExtra(HANDLER_CLASS, SignListActivityHandler.class);
        intent.putExtra(MJConstants.SHOP, shop);
        activity.startActivity(intent);
    }
}
