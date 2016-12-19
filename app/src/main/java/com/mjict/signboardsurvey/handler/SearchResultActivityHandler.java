package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.os.Bundle;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.BuildingSearchActivity;
import com.mjict.signboardsurvey.activity.SearchResultActivity;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.StreetAddress;
import com.mjict.signboardsurvey.model.UnifiedSearchResult;
import com.mjict.signboardsurvey.model.ui.BuildingResult;
import com.mjict.signboardsurvey.sframework.DefaultSActivityHandler;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.task.UnifiedSearchTask;
import com.mjict.signboardsurvey.util.SyncConfiguration;

import java.util.List;

/**
 * Created by Junseo on 2016-11-11.
 */
public class SearchResultActivityHandler extends DefaultSActivityHandler {
    public static final int MAX_ADDRESS_RESULT = 7;
    public static final int MAX_BUILDING_RESULT = 5;
    public static final int MAX_SHOP_RESULT = 10;

    private List<StreetAddress> addressResults;
    private List<BuildingResult> buildingResults;
    private List<Shop> shopResults;

    private SearchResultActivity activity;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (SearchResultActivity)getActivity();

        // register listener
        activity.setSearchResultItemOnClickListener(new SearchResultActivity.SearchResultItemOnClickListener() {
            @Override
            public void onAddressItemClicked(int index) {
                addressItemClicked(index);
            }

            @Override
            public void onBuildingItemClicked(int index) {
                buildingItemClicked(index);
            }

            @Override
            public void onShopItemClicked(int index) {
                shopItemClicked(index);
            }
        });

        // init
        String keyword = activity.getIntent().getStringExtra(MJConstants.KEYWORD);
        if(keyword == null) {
            // TODO 메세지 추가
            activity.finish();
            return;
        }

        // do first job
        startToFindResult(keyword);
    }

    private void startToFindResult(final String keyword) {
        UnifiedSearchTask task = new UnifiedSearchTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<UnifiedSearchResult>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.searching);
            }
            @Override
            public void onTaskFinished(UnifiedSearchResult unifiedSearchResult) {
                activity.hideWaitingDialog();
                addressResults = unifiedSearchResult.addresses;
                buildingResults = unifiedSearchResult.buildings;
                shopResults = unifiedSearchResult.shops;

                if(unifiedSearchResult.addresses != null) {
                    int size = unifiedSearchResult.addresses.size();
                    boolean visible = (size > 0);
                    activity.setAddressResultVisible(visible);
                    for(int i=0; i<size; i++) {
                        StreetAddress a = unifiedSearchResult.addresses.get(i);
                        String text = a.getTown()+" "+a.getStreet();
                        int index = text.indexOf(keyword);  // TODO StreetAddress 에서 DB 구까지 내용이 있어야 할 듯.
                        activity.addAddressResult(text, index, keyword.length());
                    }
                }

                if(unifiedSearchResult.buildings != null) {
                    int size = unifiedSearchResult.buildings.size();
                    boolean visible = (size > 0);
                    activity.setBuildingResultVisible(visible);
                    for(int i=0; i<size; i++)
                        activity.addBuildingResult(unifiedSearchResult.buildings.get(i));
                }

                if(unifiedSearchResult.shops != null) {
                    int size = unifiedSearchResult.shops.size();
                    boolean visible = (size > 0);
                    activity.setShopResultVisible(visible);
                    for(int i=0; i<size; i++) {
                        Shop shop = unifiedSearchResult.shops.get(i);
                        String text = shop.getName()+" "+shop.getPhoneNumber();
                        int index = text.indexOf(keyword);
                        activity.addShopResult(text, index, keyword.length());
                    }
                }
            }
        });
        task.setMaxAddressResultCount(MAX_ADDRESS_RESULT);
        task.setMaxBuildingsResultCount(MAX_BUILDING_RESULT);
        task.setMaxShopCount(MAX_SHOP_RESULT);
        task.execute(keyword);
    }

    private void addressItemClicked(int index) {
        StreetAddress address = addressResults.get(index);

        Intent intent = new Intent(activity, BuildingSearchActivity.class);
        intent.putExtra(HANDLER_CLASS, BuildingSearchActivityHandler.class);
        intent.putExtra(MJConstants.COUNTY, SyncConfiguration.getCountyForSync());  // TODO StreetAddress 에서 가져 오는게 바람직 하겠지
        intent.putExtra(MJConstants.TOWN, address.getTown());
        intent.putExtra(MJConstants.STREET, address.getStreet());

        activity.startActivity(intent);
    }

    private void buildingItemClicked(int index) {

    }

    private void shopItemClicked(int index) {

    }
}
