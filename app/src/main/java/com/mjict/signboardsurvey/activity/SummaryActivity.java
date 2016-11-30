package com.mjict.signboardsurvey.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.RecentBuildingListAdapter;
import com.mjict.signboardsurvey.adapter.RecentSignListAdapter;
import com.mjict.signboardsurvey.adapter.SummaryStatisticsViewPagerAdapter;
import com.mjict.signboardsurvey.handler.LocalStatisticsActivityHandler;
import com.mjict.signboardsurvey.handler.SummaryActivityHandler;
import com.mjict.signboardsurvey.model.BitmapBuilding;
import com.mjict.signboardsurvey.model.BitmapSign;
import com.mjict.signboardsurvey.model.RecentSign;
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


        // test
        recentSignAdapter.add(new RecentSign(R.drawable.test_sign1, "GS25", "가로형", "무허가 요건구비"));
        recentSignAdapter.add(new RecentSign(R.drawable.test_sign2, "할매국밥", "가로형", "요건불비(수량)"));
        recentSignAdapter.add(new RecentSign(R.drawable.test_sign3, "명인 순두부", "세로형", "무허가 요건구비"));
        recentSignAdapter.add(new RecentSign(R.drawable.test_sign4, "야호 노래방", "지주이용", "무허가 요건구비"));
        recentSignAdapter.add(new RecentSign(R.drawable.test_sign5, "삼성 본사", "가로형", "요건불비(규격)"));
        recentSignAdapter.add(new RecentSign(R.drawable.test_sign6, "가나 목욕탕", "세로형", "무신고 요건구비"));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SummaryActivity.this, SearchResultActivity.class);
                intent.putExtra(SActivityHandler.HANDLER_CLASS, SummaryActivityHandler.class);
                startActivity(intent);
            }
        });

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

    public void addToRecentBuilding(BitmapBuilding building) {
        recentBuildingAdapter.add(building);
    }

    public void setRecentBuildingImage(int pos, Bitmap image) {
        recentBuildingAdapter.setImage(pos, image);
    }

    public void setRecentSignListOnItemClickListener(AdapterView.OnItemClickListener listener) {
        recentSignListView.setOnItemClickListener(listener);
    }

    public void addToRecentSign(BitmapSign sign) {

    }

    public void setRecentSignImage(int pos, Bitmap img) {

    }

}
