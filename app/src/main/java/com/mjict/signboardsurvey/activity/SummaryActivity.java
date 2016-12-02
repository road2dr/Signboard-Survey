package com.mjict.signboardsurvey.activity;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.RecentBuildingListAdapter;
import com.mjict.signboardsurvey.adapter.RecentSignListAdapter;
import com.mjict.signboardsurvey.adapter.SummaryStatisticsViewPagerAdapter;
import com.mjict.signboardsurvey.handler.LocalStatisticsActivityHandler;
import com.mjict.signboardsurvey.model.ui.RecentBuilding;
import com.mjict.signboardsurvey.model.ui.RecentSign;
import com.mjict.signboardsurvey.sframework.SActivityHandler;
import com.mjict.signboardsurvey.widget.CircleIndicator;
import com.mjict.signboardsurvey.widget.HorizontalListView;

/**
 * Created by Junseo on 2016-11-09.
 */
public class SummaryActivity extends SABaseActivity {

    private View searchEditText;
    private ImageButton searchButton;

    private ViewPager statisticsViewPager;
    private SummaryStatisticsViewPagerAdapter vpAdapter;
    private CircleIndicator circleIndicator;

    private HorizontalListView recentBuildingListView;
    private RecentBuildingListAdapter recentBuildingAdapter;
    private HorizontalListView recentSignListView;
    private RecentSignListAdapter recentSignAdapter;

    private View statisticsView;

    @Override
    protected void init() {
        super.init();
        this.hideToolBar();
        this.disableNavigation();

        statisticsViewPager = (ViewPager)this.findViewById(R.id.statistics_view_pager);
        vpAdapter = new SummaryStatisticsViewPagerAdapter(this);
        statisticsViewPager.setAdapter(vpAdapter);

        circleIndicator = (CircleIndicator)this.findViewById(R.id.circle_indicator);
        circleIndicator.setViewPager(statisticsViewPager);
        vpAdapter.registerDataSetObserver(circleIndicator.getDataSetObserver());

        recentBuildingListView = (HorizontalListView)this.findViewById(R.id.recent_building_list_view);
        recentBuildingAdapter = new RecentBuildingListAdapter(this);
        recentBuildingListView.setAdapter(recentBuildingAdapter);

        recentSignListView = (HorizontalListView)this.findViewById(R.id.recent_sign_list_view);
        recentSignAdapter = new RecentSignListAdapter(this);
        recentSignListView.setAdapter(recentSignAdapter);

        searchEditText = (View)this.findViewById(R.id.search_edit_text);
        searchButton = (ImageButton)this.findViewById(R.id.search_button);

        statisticsView = this.findViewById(R.id.statistics_view);



//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SummaryActivity.this, SearchResultActivity.class);
//                intent.putExtra(SActivityHandler.HANDLER_CLASS, SummaryActivityHandler.class);
//                startActivity(intent);
//            }
//        });

        statisticsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int page = statisticsViewPager.getCurrentItem();
                switch (page) {
                    case 0:
                        Intent intent = new Intent(SummaryActivity.this, LocalStatisticsActivity.class);
                        intent.putExtra(SActivityHandler.HANDLER_CLASS, LocalStatisticsActivityHandler.class);
                        startActivity(intent);
                        break;

                    case 1:

                        break;
                }
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
}
