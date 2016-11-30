package com.mjict.signboardsurvey.activity;

import android.graphics.Color;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mjict.signboardsurvey.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Junseo on 2016-11-24.
 */
public class LocalStatisticsActivity extends SABaseActivity {

    private PieChart reviewSignChart;
    private BarChart signCountChart;

    private float[] yData = { 784, 1466, 8655 };
    private String[] xData = { "재조사 완료", "재조사 대상", "해당 사항 없음" };

    @Override
    protected int getContentLayout() {
        return R.layout.activity_local_statistics;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle("지역 통계");

        reviewSignChart = (PieChart)this.findViewById(R.id.review_sign_chart);
        signCountChart = (BarChart)this.findViewById(R.id.sign_count_chart);

        // configure pie chart
//        reviewSignChart.setUsePercentValues(true);    // 값을 퍼센트로 표시 할지 여부
//        reviewSignChart.setDescription("Smartphones Market Share");
        // enable hole and configure
        reviewSignChart.setDrawHoleEnabled(true);
//        reviewSignChart.setHoleColorTransparent(true);
        reviewSignChart.setHoleRadius(5);   // 가운데 구멍 크기 인듯
        reviewSignChart.setTransparentCircleRadius(10);
        reviewSignChart.setCenterTextColor(Color.BLACK);    // 가운데 구멍에 들어가는 글자색
        reviewSignChart.setEntryLabelColor(Color.BLACK);    // 각 항목 글자색 숫자 밑에 위치함
        // enable rotation of the chart by touch
        reviewSignChart.setRotationAngle(0);
        reviewSignChart.setRotationEnabled(true);
        reviewSignChart.setDescription(null);       // 오른쪽 아래 작게 나오는 설명문 없앰

        // set a chart value selected listener
        reviewSignChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                // display msg when value selected
                if (e == null)
                    return;

                String msg = "x: "+e.getX()+" y: "+e.getY()+" data: "+e.getData();
                Toast.makeText(LocalStatisticsActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected() {
            }
        });


        signCountChart.setDescription(null);
        signCountChart.setDrawValueAboveBar(false);
        signCountChart.setDrawGridBackground(false);


        reviewSignChart.animateY(2000);
        signCountChart.animateY(2000);


        // test
        // add data
        addTestReviewData();
        addTestCountData();

        // customize legends
        Legend l = reviewSignChart.getLegend();     //
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7);
        l.setYEntrySpace(5);

    }

    private void addTestReviewData() {
        ArrayList<PieEntry> yVals1 = new ArrayList<PieEntry>();

        for (int i = 0; i < yData.length; i++)
            yVals1.add(new PieEntry(yData[i], xData[i], i));

        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < xData.length; i++)
            xVals.add(xData[i]);

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
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

        // instantiate pie data object now
//        PieData data = new PieData(xVals, dataSet);

        PieData data = new PieData(dataSet);
        data.setValueFormatter(new CountFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.BLACK);

        reviewSignChart.setData(data);

        // undo all highlights
        reviewSignChart.highlightValues(null);

        // update pie chart
        reviewSignChart.invalidate();
    }

    private void addTestCountData() {
        float[] yData = { 3140, 1782, 5678, 2827, 7433 };
        String[] xData = { "역삼동", "봉곡동", "북삼동", "구월동", "산성동" };

        ArrayList<BarEntry> values = new ArrayList<BarEntry>();

        for (int i = 0; i < yData.length; i++)
            values.add(new BarEntry(i, yData[i], xData[i]));

        // create pie data set
        BarDataSet dataSet = new BarDataSet(values, "");

        // add many colors
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
        dataSet.setLabel("????");

//        // customize legends
//        List<LegendEntry> legendEntries = new ArrayList();
//        for(int i=0; i<xData.length; i++) {
//            String label = xData[i];
//            Legend.LegendForm form = null;
//            float formSize = 3;
//            float formLineWidth = 5;
//            DashPathEffect formLineDashEffect = null;
//            int formColor = colors.get(i);
//            legendEntries.add(new LegendEntry(label, form, formSize, formLineWidth, formLineDashEffect, formColor));
//        }
//        Legend scl = reviewSignChart.getLegend();     //
//        scl.setDirection(Legend.LegendDirection.LEFT_TO_RIGHT);
//        scl.setCustom(legendEntries);


        BarData data = new BarData(dataSet);
        signCountChart.setData(data);
    }


    public class CountFormatter implements IValueFormatter, IAxisValueFormatter  {

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
