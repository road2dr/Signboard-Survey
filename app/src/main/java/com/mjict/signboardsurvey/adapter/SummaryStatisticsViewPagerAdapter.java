package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mjict.signboardsurvey.R;


/**
 * Created by Junseo on 2016-10-18.
 */
public class SummaryStatisticsViewPagerAdapter extends PagerAdapter {
    public static final int MAX_PAGE = 2;

    private Context context;
    private LayoutInflater inflater;
    private PageOnClickListener pageOnClickListener;

    public SummaryStatisticsViewPagerAdapter(Context c){
        super();
        context = c;
        inflater = LayoutInflater.from(c);
    }

    @Override
    public int getCount() {

        return MAX_PAGE;
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
}
