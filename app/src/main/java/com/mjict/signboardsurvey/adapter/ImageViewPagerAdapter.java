package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-11-15.
 */
public class ImageViewPagerAdapter extends PagerAdapter {
    private Context context;
    private LayoutInflater inflater;

    private SparseArray<PageView> pageViews = new SparseArray<PageView>();

    private int count;
    private Bitmap image;
    private boolean picVisible = false;

    private OnPagerItemClickListener itemClickListener;

    public ImageViewPagerAdapter(Context c){
        super();
        context = c;
        inflater = LayoutInflater.from(c);
    }

    public void showImage(int current, Bitmap img) {
        image = img;
        picVisible = true;

        notifyDataSetChanged(current);
    }

    public void showLoading(int current) {
        picVisible = false;

        notifyDataSetChanged(current);
    }

    public void setCount(int count) {
        this.count = count;

        notifyDataSetChanged();
    }

    private void notifyDataSetChanged(int page) {
        PageView pageView = pageViews.get(page);
        if(pageView == null)
            return;

        updateUI(pageView);
    }

    private void updateUI(PageView pageView) {
        if(picVisible) {
            pageView.setImageVisible(View.VISIBLE);
            pageView.setLoadingVisible(View.GONE);
        } else {
            pageView.setImageVisible(View.GONE);
            pageView.setLoadingVisible(View.VISIBLE);
        }
        Log.d("junseo", "here??? 1");
        if(image != null && image.isRecycled() == false) {
            pageView.setImage(image);
        } else {
            pageView.setImage(R.drawable.ic_add_camera_n);   // TODO
        }
    }

    public void setOnItemClickListener(OnPagerItemClickListener listener) {
        itemClickListener = listener;
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        View view = null;
        view = inflater.inflate(R.layout.view_page_picture, null);

        PageView pageView = new PageView(view, position);
        pageViews.put(position, pageView);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(itemClickListener != null)
                    itemClickListener.onItemClick(position);
            }
        });

        container.addView(view);

        updateUI(pageView);

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

    private class PageView {
        private View parent;
        private ImageView imageView;
        private ProgressBar loadingView;
        private int pageIndex;

        public PageView(View view, final int position) {
            parent = view;
            pageIndex = position;

            init();
        }

        private void init() {
            imageView = (ImageView) parent.findViewById(R.id.image_view);
            loadingView = (ProgressBar) parent.findViewById(R.id.loading_view);
        }

        public void setImageVisible(int visible) {
            imageView.setVisibility(visible);
        }

        public void setLoadingVisible(int visible) {
            loadingView.setVisibility(visible);
        }

        public void setImage(Bitmap image) {
            imageView.setImageBitmap(image);
        }

        public void setImage(int resId) {
            imageView.setImageResource(resId);
        }

    }

    public static interface OnPagerItemClickListener {
        public void onItemClick(int position);
    }
}
