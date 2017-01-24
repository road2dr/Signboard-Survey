package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;


/**
 * Created by Junseo on 2016-10-18.
 */
public class SummaryStatisticsViewPagerAdapter extends PagerAdapter {
    public static final int MAX_PAGE = 2;

    private Context context;
    private LayoutInflater inflater;
    private PageOnClickListener pageOnClickListener;

    private SparseArray<PageView> pageViews = new SparseArray<PageView>();

    private String allBuildingCountText;    // first
    private String allShopCountText;        // second
    private String allSignCountText;        // third
    private String reviewSignCountText;     // fourth
    private String demolishSignCountText;   // fifth

    private String userAllSignCountText;    // first
    private String userTodaySignCountText;  // second
    private String userReviewSignCountText; // third
    private String userShopCountText;       // fourth
    private String userDemolishSignCountText;   // fifth

    public SummaryStatisticsViewPagerAdapter(Context c){
        super();
        context = c;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {

        return MAX_PAGE;
    }

    public void setFirstPageText(String... text) {
        if(text != null && text.length == 5) {
            allBuildingCountText = text[0];
            allShopCountText = text[1];
            allSignCountText = text[2];
            reviewSignCountText = text[3];
            demolishSignCountText = text[4];

            updateUI(0);
        }
    }

    public void setSecondPageText(String... text) {
        if(text != null && text.length == 5) {
            userAllSignCountText = text[0];
            userTodaySignCountText = text[1];
            userReviewSignCountText = text[2];
            userShopCountText = text[3];
            userDemolishSignCountText = text[4];

            updateUI(1);
        }
    }

    public void updateUI(int position) {
        PageView pageView = pageViews.get(position);
        if(pageView == null)
            return;

        String firstText = (position == 0) ? allBuildingCountText : userAllSignCountText;
        String secondText = (position == 0) ? allShopCountText : userTodaySignCountText;
        String thirdText = (position == 0) ? allSignCountText : userReviewSignCountText;
        String fourthText = (position == 0) ? reviewSignCountText : userShopCountText;
        String fifthText = (position == 0) ? demolishSignCountText : userDemolishSignCountText;

        pageView.setFirstText(firstText);
        pageView.setSecondTextView(secondText);
        pageView.setThirdText(thirdText);
        pageView.setFourthText(fourthText);
        pageView.setFifthText(fifthText);
    }


    public void setPageOnClickListener(PageOnClickListener listener) {
        pageOnClickListener = listener;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        Log.d("junseo", "instantiateItem: "+position);
        if(position < 0 || position >= MAX_PAGE)
            return null;

        View view = null;

        int resId = position == 0 ? R.layout.view_page_local_statistics : R.layout.view_page_user_statistics;
        view = inflater.inflate(resId, null);
        container.addView(view);

        PageView pageView = new PageView(view, position);
        pageViews.put(position, pageView);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pageOnClickListener != null)
                    pageOnClickListener.onPageClicked(position);
            }
        });

        return view;
    }

    @Override
    public void destroyItem(View pager, int position, Object o) {
        View view = (View) o;

        switch (position){
            case 0:
                break;
            case 1:
                break;
            default:
                // nothing to do
        }

        ((ViewPager)pager).removeView(view);
        pageViews.remove(position);
    }

    @Override
    public boolean isViewFromObject(View pager, Object obj) {
        return pager == obj;
    }

    @Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
    @Override public Parcelable saveState() { return null; }
    @Override public void startUpdate(View arg0) {}
    @Override public void finishUpdate(View arg0) {}

    public static interface PageOnClickListener {
        public void onPageClicked(int position);
    }

    private class PageView {
        private View parent;
        private int pageIndex;

        TextView firstTextView;
        TextView secondTextView;
        TextView thirdTextView;
        TextView fourthTextView;
        TextView fifthTextView;

        public PageView(View view, final int position) {
            parent = view;
            pageIndex = position;

            init();
        }

        private void init() {
            firstTextView = (TextView)parent.findViewById(R.id.first_text_view);
            secondTextView = (TextView)parent.findViewById(R.id.second_text_view);
            thirdTextView = (TextView)parent.findViewById(R.id.third_text_view);
            fourthTextView = (TextView)parent.findViewById(R.id.fourth_text_view);
            fifthTextView = (TextView)parent.findViewById(R.id.fifth_text_view);
        }

        public void setFirstText(String text) {
            firstTextView.setText(text);
        }

        public void setSecondTextView(String text) {
            secondTextView.setText(text);
        }

        public void setThirdText(String text) {
            thirdTextView.setText(text);
        }

        public void setFourthText(String text) {
            fourthTextView.setText(text);
        }

        public void setFifthText(String text) {
            fifthTextView.setText(text);
        }

    }
}
