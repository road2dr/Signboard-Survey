package com.mjict.signboardsurvey.activity;

import android.graphics.Color;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.RecentBuildingListAdapter;
import com.mjict.signboardsurvey.adapter.RecentSignListAdapter;
import com.mjict.signboardsurvey.adapter.SummaryStatisticsViewPagerAdapter;
import com.mjict.signboardsurvey.model.ui.RecentBuilding;
import com.mjict.signboardsurvey.model.ui.RecentSign;
import com.mjict.signboardsurvey.widget.CircleIndicator;
import com.mjict.signboardsurvey.widget.HorizontalListView;

import java.text.DecimalFormat;
import java.util.ArrayList;

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

    private Button refreshStatisticsButton;
    private Button refreshPieChartButton;

//    sign_status_pie_chart
//    sign_status_loading_view
    private PieChart signStatusPieChart;
    private PieDataSet signStatusDataSet = null;
    private ArrayList<PieEntry> signStatusEntries;

    private TextView signStatusLoadingView;

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

        signStatusPieChart = (PieChart)this.findViewById(R.id.sign_status_pie_chart);
        signStatusLoadingView = (TextView)this.findViewById(R.id.sign_status_loading_view);

        recentBuildingListView = (HorizontalListView)this.findViewById(R.id.recent_building_list_view);
        recentBuildingAdapter = new RecentBuildingListAdapter(this);
        recentBuildingListView.setAdapter(recentBuildingAdapter);

        recentSignListView = (HorizontalListView)this.findViewById(R.id.recent_sign_list_view);
        recentSignAdapter = new RecentSignListAdapter(this);
        recentSignListView.setAdapter(recentSignAdapter);

        searchEditText = (View)this.findViewById(R.id.search_edit_text);
        searchButton = (ImageButton)this.findViewById(R.id.search_button);

        refreshStatisticsButton = (Button)this.findViewById(R.id.refresh_statistics_button);
        refreshPieChartButton = (Button)this.findViewById(R.id.refresh_pie_chart_button);

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


        initPieChart();
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

    public void setSignStatusPieChartVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        signStatusPieChart.setVisibility(visibility);
        signStatusPieChart.animateY(1500);
    }

    public void setSignStatusLoadingViewVisible(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.GONE;
        signStatusLoadingView.setVisibility(visibility);
    }

    public void addToSignResultStatusPieChart(String itemName, long itemCount) {
        int i = signStatusEntries.size();
        signStatusEntries.add(new PieEntry(itemCount, itemName, i));
        signStatusPieChart.invalidate();
    }

    public void clearSignResultStatusPieChart() {
        signStatusEntries.clear();
        signStatusPieChart.invalidate();
    }

    public void setFirstAllSignPageText(String... text) {
        statisticsViewPagerAdapter.setFirstPageText(text);
    }

    public void setSecondAllSignPageText(String... text) {
        statisticsViewPagerAdapter.setSecondPageText(text);
    }

    public void setRefreshStatisticsButtonOnClickListener(View.OnClickListener listener) {
        refreshStatisticsButton.setOnClickListener(listener);
    }

    public void setRefreshPieChartButtonOnClickListener(View.OnClickListener listener) {
        refreshPieChartButton.setOnClickListener(listener);
    }

    private void initPieChart() {
        signStatusEntries = new ArrayList<PieEntry>();
        PieDataSet dataSet = new PieDataSet(signStatusEntries, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        ArrayList<Integer> colors = new ArrayList<Integer>();
        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);
        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);
        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new CountFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        signStatusPieChart.setHoleRadius(5);   // 가운데 구멍 크기 인듯
        signStatusPieChart.setTransparentCircleRadius(10);
        signStatusPieChart.setCenterTextColor(Color.BLACK);    // 가운데 구멍에 들어가는 글자색
        signStatusPieChart.setEntryLabelColor(Color.BLACK);    // 각 항목 글자색 숫자 밑에 위치함
        // enable rotation of the chart by touch
        signStatusPieChart.setRotationAngle(0);
        signStatusPieChart.setRotationEnabled(true);
        signStatusPieChart.setDescription(null);       // 오른쪽 아래 작게 나오는 설명문 없앰

        signStatusPieChart.setData(data);
        signStatusPieChart.highlightValues(null);        // undo all highlights

        Legend l = signStatusPieChart.getLegend();     //
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(20);
        l.setYEntrySpace(5);
    }

    private class CountFormatter implements IValueFormatter, IAxisValueFormatter {

        protected DecimalFormat mFormat;

        public CountFormatter() {
            mFormat = new DecimalFormat("###,###,###");
        }

        /**
         * Allow a custom decimalformat
         *
         * @param format
         */
        public CountFormatter(DecimalFormat format) {
            this.mFormat = format;
        }

        // IValueFormatter
        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return mFormat.format(value) + " 건";
        }

        // IAxisValueFormatter
        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            return mFormat.format(value) + " 건";
        }

        public int getDecimalDigits() {
            return 1;
        }
    }
}
