package com.mjict.signboardsurvey.activity;

import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.RecentBuildingListAdapter;
import com.mjict.signboardsurvey.adapter.RecentSignListAdapter;
import com.mjict.signboardsurvey.adapter.SummaryStatisticsViewPagerAdapter;
import com.mjict.signboardsurvey.model.ui.RecentBuilding;
import com.mjict.signboardsurvey.model.ui.RecentSign;
import com.mjict.signboardsurvey.widget.CircleIndicator;
import com.mjict.signboardsurvey.widget.HorizontalListView;

/**
 * Created by Junseo on 2016-11-09.
 */
public class SummaryActivity extends SABaseActivity {

    private View searchEditText;
    private ImageButton searchButton;

    private ImageButton summaryMenuButton;
    private DrawerArrowDrawable drawerArrowDrawable;
    private ViewPager statisticsViewPager;
    private SummaryStatisticsViewPagerAdapter statisticsViewPagerAdapter;
    private CircleIndicator circleIndicator;

    private HorizontalListView recentBuildingListView;
    private RecentBuildingListAdapter recentBuildingAdapter;
    private HorizontalListView recentSignListView;
    private RecentSignListAdapter recentSignAdapter;

    @Override
    protected void init() {
        super.init();
        this.hideToolBar();
//        this.disableNavigation();

        statisticsViewPager = (ViewPager)this.findViewById(R.id.statistics_view_pager);
        statisticsViewPagerAdapter = new SummaryStatisticsViewPagerAdapter(this);
        statisticsViewPager.setAdapter(statisticsViewPagerAdapter);

        circleIndicator = (CircleIndicator)this.findViewById(R.id.circle_indicator);
        circleIndicator.setViewPager(statisticsViewPager);
        statisticsViewPagerAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        recentBuildingListView = (HorizontalListView)this.findViewById(R.id.recent_building_list_view);
        recentBuildingAdapter = new RecentBuildingListAdapter(this);
        recentBuildingListView.setAdapter(recentBuildingAdapter);

        recentSignListView = (HorizontalListView)this.findViewById(R.id.recent_sign_list_view);
        recentSignAdapter = new RecentSignListAdapter(this);
        recentSignListView.setAdapter(recentSignAdapter);

        searchEditText = (View)this.findViewById(R.id.search_edit_text);
        searchButton = (ImageButton)this.findViewById(R.id.search_button);

        summaryMenuButton = (ImageButton)this.findViewById(R.id.summary_menu_button);
        drawerArrowDrawable = new DrawerArrowDrawable(this);
        summaryMenuButton.setImageDrawable(drawerArrowDrawable);
        summaryMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(drawer.getDrawerLockMode(GravityCompat.START) == DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
                    return;

                if(drawer.isDrawerOpen(GravityCompat.START) == false)
                    drawer.openDrawer(GravityCompat.START);
            }
        });

        this.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                drawerArrowDrawable.setProgress(slideOffset);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                drawerArrowDrawable.setProgress(1.0f);
            }
            @Override
            public void onDrawerClosed(View drawerView) {
                drawerArrowDrawable.setProgress(0f);
            }
            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });







    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_summary;
    }

    public void setRecentBuildingListOnItemClickListener(AdapterView.OnItemClickListener listener) {
        recentBuildingListView.setOnItemClickListener(listener);
    }

    public void addToRecentBuilding(RecentBuilding building) {
        recentBuildingAdapter.add(building);
    }

    public void clearRecentBuildingList() {
        recentBuildingAdapter.clear();
    }

    public void setRecentSignListOnItemClickListener(AdapterView.OnItemClickListener listener) {
        recentSignListView.setOnItemClickListener(listener);
    }

    public void addToRecentSign(RecentSign sign) {
        recentSignAdapter.add(sign);
    }

    public void clearRecentSignList() {
        recentSignAdapter.clear();
    }

    public void setSearchViewOnclickListener(View.OnClickListener listener) {
        searchEditText.setOnClickListener(listener);
    }

    public void setStatisticsViewPagerOnClickListener(SummaryStatisticsViewPagerAdapter.PageOnClickListener listener) {
        statisticsViewPagerAdapter.setPageOnClickListener(listener);
    }
//
//    public void setAddressSearchSummaryButtonOnClickListener(View.OnClickListener listener) {
//        addressSearchButton.setOnClickListener(listener);
//    }
//
//    public void setMapSearchSummaryButtonOnClickListener(View.OnClickListener listener) {
//        mapSearchButton.setOnClickListener(listener);
//    }
}
