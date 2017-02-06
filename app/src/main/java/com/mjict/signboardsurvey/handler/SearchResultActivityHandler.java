package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.BuildingListActivity;
import com.mjict.signboardsurvey.activity.BuildingSearchActivity;
import com.mjict.signboardsurvey.activity.SearchResultActivity;
import com.mjict.signboardsurvey.activity.ShopListActivity;
import com.mjict.signboardsurvey.activity.SignListActivity;
import com.mjict.signboardsurvey.activity.TextListActivity;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.DetailBuildingBitmap;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.StreetAddress;
import com.mjict.signboardsurvey.model.UnifiedSearchResult;
import com.mjict.signboardsurvey.model.ui.BuildingResult;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.task.UnifiedSearchTask;
import com.mjict.signboardsurvey.util.SyncConfiguration;

import java.util.List;

/**
 * Created by Junseo on 2016-11-11.
 */
public class SearchResultActivityHandler extends SABaseActivityHandler {
    public static final int MAX_ADDRESS_RESULT = 7;
    public static final int MAX_BUILDING_RESULT = 5;
    public static final int MAX_SHOP_RESULT = 10;

    private List<StreetAddress> addressResults;
    private List<DetailBuildingBitmap> buildingResults;
    private List<Shop> shopResults;

    private String keyword;

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

        activity.setBuildingResultMoreButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMoreBuildingResult();
            }
        });

        activity.setShopResultMoreButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMoreShopResult();
            }
        });

        activity.setAddressResultMoreButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToMoreAddressResult();
            }
        });

        // init
        keyword = activity.getIntent().getStringExtra(MJConstants.KEYWORD);
        if(keyword == null) {
            // TODO 메세지 추가
            activity.finish();
            return;
        }

        // do first job
        startToFindResult(keyword);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        activity.clearAddressResult();
        activity.clearBuildingResult();
        activity.clearShopResult();
        activity.setAddressResultVisible(false);
        activity.setBuildingResultVisible(false);
        activity.setShopResultVisible(false);

        startToFindResult(keyword);

        super.onActivityResult(requestCode, resultCode, data);
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

                if(buildingResults != null) {
                    int size = buildingResults.size();
                    boolean visible = (size > 0);
                    activity.setBuildingResultVisible(visible);
                    for(int i=0; i<size; i++) {
                        DetailBuildingBitmap dbp = buildingResults.get(i);
                        BuildingResult br = detailBuildingToBuildingResult(dbp);
                        activity.addBuildingResult(br);
                    }
                }

                if(shopResults != null) {
                    int size = shopResults.size();
                    boolean visible = (size > 0);
                    activity.setShopResultVisible(visible);
                    for(int i=0; i<size; i++) {
                        Shop shop = shopResults.get(i);
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
        Building building = buildingResults.get(index).building;

        goToShopList(building);
    }

    private void shopItemClicked(int index) {
        Shop shop = shopResults.get(index);

        goToSignList(shop);
    }

    private void goToSignList(Shop shop) {
        Intent intent = new Intent(activity, SignListActivity.class);
        intent.putExtra(HANDLER_CLASS, SignListActivityHandler.class);
        intent.putExtra(MJConstants.SHOP, shop);
        activity.startActivityForResult(intent, MJConstants.REQUEST_SHOP_INFORMATION);
    }

    private void goToShopList(Building building) {
        Intent intent = new Intent(activity, ShopListActivity.class);
        intent.putExtra(HANDLER_CLASS, ShopListActivityHandler.class);
        intent.putExtra(MJConstants.BUILDING, building);
        activity.startActivityForResult(intent, MJConstants.REQUEST_BUILDING_INFORMATION);
    }

    private void goToMoreAddressResult() {
        Intent intent = new Intent(activity, TextListActivity.class);
        intent.putExtra(HANDLER_CLASS, MoreAddressResultActivityHandler.class);
        intent.putExtra(MJConstants.KEYWORD, keyword);
        activity.startActivity(intent);
    }

    private void goToMoreShopResult() {
        Intent intent = new Intent(activity, TextListActivity.class);
        intent.putExtra(HANDLER_CLASS, MoreShopResultActivityHandler.class);
        intent.putExtra(MJConstants.KEYWORD, keyword);
        activity.startActivity(intent);
    }

    private void goToMoreBuildingResult() {
        Intent intent = new Intent(activity, BuildingListActivity.class);
        intent.putExtra(HANDLER_CLASS, MoreBuildingResultActivityHandler.class);
        intent.putExtra(MJConstants.KEYWORD, keyword);
        activity.startActivity(intent);
    }

    private BuildingResult detailBuildingToBuildingResult(DetailBuildingBitmap detailBuilding) {
        Building building = detailBuilding.building;

        String buildingNumber = building.getFirstBuildingNumber();
        if(building.getSecondBuildingNumber().equals("") == false)
            buildingNumber = buildingNumber + "_" +building.getSecondBuildingNumber();
        String baseAddress = building.getProvince()+" "+building.getCounty()+" "+building.getTown();
        String streetAddress = baseAddress + building.getStreetName() + " "+buildingNumber;
        String houseAddress = baseAddress + " "+building.getVillage() + building.getHouseNumber();
        String title = building.getName().equals("") ? buildingNumber : building.getName();

        Bitmap image = detailBuilding.image;
        String name = title;

        int shopCount = detailBuilding.shops.size();
        int signCount = detailBuilding.signs.size();
        String shopCountText = activity.getString(R.string.number_of_case, shopCount);
        String signCountText = activity.getString(R.string.number_of_case, signCount);


        BuildingResult br = new BuildingResult(image, name, streetAddress, houseAddress, shopCountText, signCountText);
        return br;
    }
}
