package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.holder.ThreeShopRowViewHolder;
import com.mjict.signboardsurvey.model.ui.ShopInfo;
import com.mjict.signboardsurvey.model.ui.ThreeShopInfo;

/**
 * Created by Junseo on 2016-11-15.
 */
public class ThreeShopListAdapter extends ArrayAdapter<ThreeShopInfo> {

    private LayoutInflater inflater = null;
    private Context context;

    private OnColumnClickListener columnClickListener;

    public ThreeShopListAdapter(Context context) {
        super(context, R.layout.list_row_three_shop);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void add(ShopInfo shop) {
        int count = getCount();

        if(count == 0) {
            ThreeShopInfo newData = new ThreeShopInfo(shop, null, null);
            add(newData);
            notifyDataSetChanged();
            return;
        }

        ThreeShopInfo last = getItem(count-1);
        if(last.first != null && last.second != null && last.third != null) {
            ThreeShopInfo newData = new ThreeShopInfo(shop, null, null);
            add(newData);
        } else if(last.second == null)
            last.second = shop;
        else
            last.third = shop;

        notifyDataSetChanged();
    }

    public void replaceItem(int position, ShopInfo data) {
        if(position < 0 || position >= getCount()*3)
            return;

        ThreeShopInfo row = getItem(position/3);
        if(position % 3 == 0)
            row.first = data;
        else if(position % 3 == 1)
            row.second = data;
        else
            row.third = data;

        notifyDataSetChanged();
    }

    public ShopInfo getColumnItem(int index) {
        if(index < 0 || index >= getCount()*3)
            return null;

        ThreeShopInfo row = getItem(index/3);
        ShopInfo item = null;
        if(index % 3 == 0)
            item = row.first;
        else if(index % 3 == 1)
            item = row.second;
        else
            item = row.third;
//        ShopInfo item = (index % 3 == 0) ? row.first : row.second;

        return item;
    }

    public void removeColumnItem(int position) {
        if(position < 0 || position >= getCount()*3)
            return;

        ThreeShopInfo row = getItem(position/3);

        if(position % 3 == 0)
            remove(row);
        else if(position % 3 == 1)
            row.second = null;
        else
            row.third = null;
//        ShopInfo item = (index % 3 == 0) ? row.first : row.second;

        notifyDataSetChanged();
    }

    public void setOnColumnClickListener(OnColumnClickListener listener) {
        columnClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ThreeShopRowViewHolder holder = null;
        if(view == null) {
            view = inflater.inflate(R.layout.list_row_three_shop, parent, false);
            holder = new ThreeShopRowViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (ThreeShopRowViewHolder)view.getTag();
        }

        ThreeShopInfo row = getItem(position);
        ShopInfo first = row.first;
        ShopInfo second = row.second;
        ShopInfo third = row.third;

        int fIndex = position*3;
        int sIndex = fIndex+1;
        int tIndex = fIndex+2;

        holder.getFirstLayout().setTag(fIndex);
        holder.getFirstLayout().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(columnClickListener != null) {
                    int index = (Integer)v.getTag();
                    columnClickListener.onClick(index);
                }
            }
        });
        holder.getFirstLayout().setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(columnClickListener != null) {
                    int index = (Integer)v.getTag();
                    columnClickListener.onLongClick(index);
                }
                return true;
            }
        });

        int visibility = first.demolished ? View.VISIBLE : View.GONE;
        holder.getFirstNameTextView().setText(first.name);
        holder.getFirstCategoryTextView().setText(first.category);
        holder.getFirstPhoneTextView().setText(first.phone);
        holder.getFirstClosedView().setVisibility(visibility);

        if(second == null) {
            holder.getSecondLayout().setVisibility(View.INVISIBLE);
        } else {
            holder.getSecondLayout().setVisibility(View.VISIBLE);

            holder.getSecondLayout().setTag(sIndex);
            holder.getSecondLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(columnClickListener != null) {
                        int index = (Integer)v.getTag();
                        columnClickListener.onClick(index);
                    }
                }
            });
            holder.getSecondLayout().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(columnClickListener != null) {
                        int index = (Integer)v.getTag();
                        columnClickListener.onLongClick(index);
                    }
                    return true;
                }
            });

            visibility = second.demolished ? View.VISIBLE : View.GONE;
            holder.getSecondNameTextView().setText(second.name);
            holder.getSecondCategoryTextView().setText(second.category);
            holder.getSecondPhoneTextView().setText(second.phone);
            holder.getSecondClosedView().setVisibility(visibility);

        }

        if(third == null) {
            holder.getThirdLayout().setVisibility(View.INVISIBLE);
        } else {
            holder.getThirdLayout().setVisibility(View.VISIBLE);

            holder.getThirdLayout().setTag(tIndex);
            holder.getThirdLayout().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(columnClickListener != null) {
                        int index = (Integer)v.getTag();
                        columnClickListener.onClick(index);
                    }
                }
            });
            holder.getThirdLayout().setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(columnClickListener != null) {
                        int index = (Integer)v.getTag();
                        columnClickListener.onLongClick(index);
                    }
                    return true;
                }
            });

            visibility = third.demolished ? View.VISIBLE : View.GONE;
            holder.getThirdNameTextView().setText(third.name);
            holder.getThirdCategoryTextView().setText(third.category);
            holder.getThirdPhoneTextView().setText(third.phone);
            holder.getThirdClosedView().setVisibility(visibility);
        }

        return view;
    }

    public static interface OnColumnClickListener {
        public void onClick(int index);
        public void onLongClick(int index);
    }
}
