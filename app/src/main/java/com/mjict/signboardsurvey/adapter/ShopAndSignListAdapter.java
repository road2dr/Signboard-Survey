package com.mjict.signboardsurvey.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.holder.SimpleSignRowViewHolder;
import com.mjict.signboardsurvey.model.ui.ShopAndSign;
import com.mjict.signboardsurvey.model.ui.SignInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2017-02-07.
 */
public class ShopAndSignListAdapter extends BaseExpandableListAdapter {
    private Context context;
    private List<ShopAndSign> dataList;
    private LayoutInflater inflater = null;

    private GroupOptionButtonOnClickListener groupOptionButtonClickListener;
    private ChildOptionButtonCOnClickListener childOptionButtonClickListener;
    private SignPictureOnClickListener signPictureClickListener;
//    private ListGroupRowClickListener groupRowClickListener;
//    private ListChildRowClickListener childRowClickListener;

    public ShopAndSignListAdapter(Context c) {
        context = c;
        dataList = new ArrayList<>();
        inflater = LayoutInflater.from(c);
    }

    public void setSignImage(int group, int child, Bitmap image) {
        if(group < 0 || group >= getGroupCount())
            return;

        if(child <0 || child >= getChildrenCount(group))
            return;

        SignInfo info = (SignInfo)getChild(group, child);
        info.image = image;
        notifyDataSetChanged();
    }

    public void addShopInformation(ShopAndSign data) {
        dataList.add(data);
        notifyDataSetChanged();
    }

    public void removeChild(int group, int child) {
        if(group < 0 || group >= getGroupCount())
            return;

        if(child <0 || child >= getChildrenCount(group))
            return;

        ShopAndSign shopInfo = dataList.get(group);
        shopInfo.signs.remove(child);
        notifyDataSetChanged();
    }

    public void removeGroup(int group) {
        if(group < 0 || group >= getGroupCount())
            return;

        dataList.remove(group);
        notifyDataSetChanged();
    }

    public void setShopInformation(int group, ShopAndSign data) {
        if(group < 0 || group >= getGroupCount())
            return;

        dataList.set(group, data);
        notifyDataSetChanged();
    }

    public void clear() {
        dataList.clear();
        notifyDataSetChanged();
    }

    public void setGroupOptionButtonOnClickListener(GroupOptionButtonOnClickListener listener) {
        groupOptionButtonClickListener = listener;
    }

    public void setChildOptionButtonOnClickListener(ChildOptionButtonCOnClickListener listener) {
        childOptionButtonClickListener = listener;
    }

    public void setSignPictureOnClickListener(SignPictureOnClickListener listener) {
        signPictureClickListener = listener;
    }

//    public void setGroupRowOnClickListener(ListGroupRowClickListener listener) {
//        groupRowClickListener = listener;
//    }
//
//    public void setChildRowOnClickListener(ListChildRowClickListener listener) {
//        childRowClickListener = listener;
//    }

    @Override
    public int getGroupCount() {
        return dataList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        ShopAndSign d = dataList.get(groupPosition);
        int size = d.signs != null ? d.signs.size() : 0;
        return size;
    }

    @Override
    public Object getGroup(int groupPosition) {
        ShopAndSign d = dataList.get(groupPosition);
        return d;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        ShopAndSign d = dataList.get(groupPosition);

        return d.signs != null ? d.signs.get(childPosition) : null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {

        return Long.valueOf(groupPosition+""+childPosition);
    }

    @Override
    public boolean hasStableIds() {

        return true;
    }


    @Override
    public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
        View v = convertView;

        if(v == null)
            v = inflater.inflate(R.layout.list_row_shop, parent, false);

        ShopAndSign data = dataList.get(groupPosition);

        TextView ownerTextView = (TextView)v.findViewById(R.id.owner_text_view);
        TextView shopNameTextView = (TextView)v.findViewById(R.id.shop_name_text_view);
        TextView categoryTextView = (TextView)v.findViewById(R.id.shop_category_text_view);
        TextView signCountTextView = (TextView)v.findViewById(R.id.sign_count_text_view);
        final ImageButton optionButton = (ImageButton)v.findViewById(R.id.option_button);

        String representative = data.shop.representative;
        String name = data.shop.name;
        String category = data.shop.category;
        int signCount = (data.signs == null) ? 0 : data.signs.size();
        String countText = context.getString(R.string.number_of_case, signCount);

        ownerTextView.setText(representative);
        shopNameTextView.setText(name);
        categoryTextView.setText(category);
        signCountTextView.setText(countText);

        optionButton.setFocusable(false);
        optionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(groupOptionButtonClickListener != null)
                    groupOptionButtonClickListener.onOptionButtonClicked(v, groupPosition);
            }
        });

        return v;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        View view = convertView;
        SimpleSignRowViewHolder holder = null;

        if(view == null) {
            view = inflater.inflate(R.layout.list_row_simple_sign, parent, false);
            holder = new SimpleSignRowViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (SimpleSignRowViewHolder) view.getTag();
        }

        SignInfo sign = (SignInfo) getChild(groupPosition, childPosition);
        if(sign == null)
            return view;

        String type = sign.type;
        String size = sign.size;
        String location = sign.location;
        String light = sign.light;
        String result = sign.result;

        holder.getTypeTextView().setText(type);
        holder.getSizeTextView().setText(size);
        holder.getLocationTextView().setText(location);
        holder.getLightTextView().setText(light);
        holder.getResultTextView().setText(result);

        if(sign.image != null)
            holder.getSignImageView().setImageBitmap(sign.image);
        else
            holder.getSignImageView().setImageResource(R.drawable.ic_no_image);


        holder.getSignImageView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(signPictureClickListener != null)
                    signPictureClickListener.onPictureClicked(v, groupPosition, childPosition);
            }
        });

        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public interface GroupOptionButtonOnClickListener {
        public void onOptionButtonClicked(View view, int position);
    }

    public interface ChildOptionButtonCOnClickListener {
        public void onOptionButtonClicked(View view, int gPosition, int cPosition);
    }

    public interface SignPictureOnClickListener {
        public void onPictureClicked(View view, int gpos, int cpos);
    }

//
//    public interface ListGroupRowClickListener {
//        public void onGroupRowClicked(View view, int position);
//    }
//
//    public interface ListChildRowClickListener {
//        public void onChildRowClicked(View view, int groupPosition, int childPosition);
//    }
}
