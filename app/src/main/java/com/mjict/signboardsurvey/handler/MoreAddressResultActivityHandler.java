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
import com.mjict.signboardsurvey.activity.BuildingSearchActivity;
import com.mjict.signboardsurvey.activity.TextListActivity;
import com.mjict.signboardsurvey.model.StreetAddress;
import com.mjict.signboardsurvey.task.SearchAddressByKeywordTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.SyncConfiguration;

import java.util.List;

/**
 * Created by Junseo on 2017-01-05.
 */
public class MoreAddressResultActivityHandler extends SABaseActivityHandler {

    private TextListActivity activity;
    private String keyword;
    private List<StreetAddress> resultAddresses;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (TextListActivity)getActivity();
        activity.setTitle(R.string.more_address_search_result);

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
                StreetAddress address = resultAddresses.get(position);
                goToBuldingSearch(address);
            }
        });

        // do first job
        startToLoadAddress();
    }

    private void startToLoadAddress() {
        SearchAddressByKeywordTask task = new SearchAddressByKeywordTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<StreetAddress>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.searching);
            }

            @Override
            public void onTaskFinished(List<StreetAddress> streetAddresses) {
                activity.hideWaitingDialog();

                resultAddresses = streetAddresses;
                if(streetAddresses == null) {
                    // TODO something is wrong
                    return;
                }
                int n = resultAddresses.size();
                for(int i=0; i<n; i++) {
                    StreetAddress a = resultAddresses.get(i);
                    String text = a.getTown()+" "+a.getStreet();
                    int index = text.indexOf(keyword);  // TODO StreetAddress 에서 DB 구까지 내용이 있어야 할 듯.
                    SpannableStringBuilder ssb = new SpannableStringBuilder(text);
                    ssb.setSpan(new AbsoluteSizeSpan(40), index, index+keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                    activity.addToList(ssb);
                }
            }
        });
        task.execute(keyword);
    }

    private void goToBuldingSearch(StreetAddress address) {
        Intent intent = new Intent(activity, BuildingSearchActivity.class);
        intent.putExtra(HANDLER_CLASS, BuildingSearchActivityHandler.class);
        intent.putExtra(MJConstants.COUNTY, SyncConfiguration.getCountyForSync());  // TODO StreetAddress 에서 가져 오는게 바람직 하겠지
        intent.putExtra(MJConstants.TOWN, address.getTown());
        intent.putExtra(MJConstants.STREET, address.getStreet());

        activity.startActivity(intent);
    }
}
