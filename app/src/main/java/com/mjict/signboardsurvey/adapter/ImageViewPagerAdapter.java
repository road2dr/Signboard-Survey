package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Junseo on 2016-11-15.
 */
public class ImageViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;

    private ArrayList<Integer> images;

    public ImageViewPagerAdapter(Context c){
        super();
        context = c;
        inflater = LayoutInflater.from(c);
        images = new ArrayList<>();
    }

    public void addImage(int resId) {
        images.add(resId);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return images.size();
    }


    @Override
    public Object instantiateItem(final ViewGroup container, int position) {

        ImageView imageView = new ImageView(context);
        imageView.setImageResource(images.get(position));
        container.addView(imageView);

        return imageView;
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
}
