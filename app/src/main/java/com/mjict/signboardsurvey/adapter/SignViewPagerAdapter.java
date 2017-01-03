package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.model.ui.SignInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2016-11-16.
 */
public class SignViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;

    private List<SignInfo> signs;

    public SignViewPagerAdapter(Context c){
        super();
        context = c;
        inflater = LayoutInflater.from(c);
        signs = new ArrayList<>();
    }

    public void add(SignInfo s) {
        signs.add(s);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {

        return signs.size();
    }


    @Override
    public Object instantiateItem(final ViewGroup container, int position) {
        View view = null;

        SignInfo s = signs.get(position);

        view = inflater.inflate(R.layout.view_page_sign, null);
        container.addView(view);

        ImageView imageView = (ImageView)view.findViewById(R.id.image_view);
        TextView contentTextView = (TextView)view.findViewById(R.id.content_text_view);
        TextView sizeTextView = (TextView)view.findViewById(R.id.size_text_view);
        TextView typeTextView = (TextView)view.findViewById(R.id.type_text_view);
        TextView resultTextView = (TextView)view.findViewById(R.id.result_text_view);

//        imageView.setImageResource(s.image);
        contentTextView.setText(s.content);
        sizeTextView.setText(s.size);
//        typeTextView.setText(s.);
        resultTextView.setText(s.result);

        final int[] colors = {0xffFFFFA5, 0xffE7FFC0, 0xffFFD2FF};
        int color = colors[position%3];
        view.setBackgroundColor(color);

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

}
