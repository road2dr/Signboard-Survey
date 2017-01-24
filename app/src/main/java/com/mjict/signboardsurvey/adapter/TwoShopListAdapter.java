package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.holder.TwoShopRowViewHolder;
import com.mjict.signboardsurvey.model.ui.ShopInfo;
import com.mjict.signboardsurvey.model.ui.TwoShopInfo;

/**
 * Created by Junseo on 2016-11-14.
 */
public class TwoShopListAdapter extends ArrayAdapter<TwoShopInfo> {

    private LayoutInflater inflater = null;
    private Context context;

    private OnColumnClickListener columnClickListener;

    public TwoShopListAdapter(Context context) {
        super(context, R.layout.list_row_two_shop);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void add(ShopInfo shop) {
        int count = getCount();

        if(count == 0) {
            TwoShopInfo newData = new TwoShopInfo(shop, null);
            add(newData);
            notifyDataSetChanged();
            return;
        }

        TwoShopInfo last = getItem(count-1);
        if(last.second == null)
            last.second = shop;
        else {
            TwoShopInfo newData = new TwoShopInfo(shop, null);
            add(newData);
        }

        notifyDataSetChanged();
    }

    public void replaceItem(int position, ShopInfo data) {
        if(position < 0 || position >= getCount())
            return;

        TwoShopInfo row = getItem(position/2);
        if(position % 2 == 0)
            row.first = data;
        else
            row.second = data;

        notifyDataSetChanged();
    }

    public ShopInfo getColumnItem(int index) {
        if(index < 0 || index >= getCount()*2)
            return null;

        TwoShopInfo row = getItem(index/2);
        ShopInfo item = (index % 2 == 0) ? row.first : row.second;

        return item;
    }

    public void setOnColumnClickListener(OnColumnClickListener listener) {
        columnClickListener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        TwoShopRowViewHolder holder = null;
        if(view == null) {
            view = inflater.inflate(R.layout.list_row_two_shop, parent, false);
            holder = new TwoShopRowViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (TwoShopRowViewHolder)view.getTag();
        }

        TwoShopInfo row = getItem(position);
        ShopInfo first = row.first;
        ShopInfo second = row.second;
        int lIndex = position*2;
        int rIndex = lIndex+1;

        holder.getFirstLayout().setTag(lIndex);
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
                int index = (Integer)v.getTag();
                columnClickListener.onLongClick(index);
                return true;
            }
        });

        holder.getFirstNameTextView().setText(first.name);
        holder.getFirstCategoryTextView().setText(first.category);
        holder.getFirstPhoneTextView().setText(first.phone);

        if(second == null) {
            holder.getSecondLayout().setVisibility(View.INVISIBLE);
        } else {
            holder.getSecondLayout().setVisibility(View.VISIBLE);

            holder.getSecondLayout().setTag(rIndex);
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
                    int index = (Integer)v.getTag();
                    columnClickListener.onLongClick(index);
                    return true;
                }
            });

            holder.getSecondNameTextView().setText(second.name);
            holder.getSecondCategoryTextView().setText(second.category);
            holder.getSecondPhoneTextView().setText(second.phone);

        }

        return view;
    }

    public static interface OnColumnClickListener {
        public void onClick(int index);
        public void onLongClick(int index);
    }
}
