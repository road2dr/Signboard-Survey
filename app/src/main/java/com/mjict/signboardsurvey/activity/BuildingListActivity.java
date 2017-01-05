package com.mjict.signboardsurvey.activity;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.DetailBuildingListAdapter;
import com.mjict.signboardsurvey.model.ui.BuildingResult;

/**
 * Created by Junseo on 2017-01-05.
 */
public class BuildingListActivity extends SABaseActivity {
    private TextView statusTextView;
    private ListView listView;
    private DetailBuildingListAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_list;
    }

    @Override
    protected void init() {
        super.init();

        statusTextView = (TextView)this.findViewById(R.id.status_text_view);
        listView = (ListView)this.findViewById(R.id.list_view);
        adapter = new DetailBuildingListAdapter(this);
        listView.setAdapter(adapter);
    }

    public void setStatusTextVisible() {
        statusTextView.setVisibility(View.VISIBLE);
    }
    public void setStatusText(CharSequence text) {
        statusTextView.setText(text);
    }

    public void addToList(BuildingResult br) {
        adapter.add(br);
    }

    public void setListOnItemClickListener(AdapterView.OnItemClickListener listener) {
        listView.setOnItemClickListener(listener);
    }
}
