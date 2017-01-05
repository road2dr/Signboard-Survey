package com.mjict.signboardsurvey.activity;

import android.widget.AdapterView;
import android.widget.ListView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.TextViewAdapter;

/**
 * Created by Junseo on 2017-01-05.
 */
public class TextListActivity extends SABaseActivity {
    private ListView listView;
    private TextViewAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_list;
    }

    @Override
    protected void init() {
        super.init();

        listView = (ListView)this.findViewById(R.id.list_view);
        adapter = new TextViewAdapter(this);
        listView.setAdapter(adapter);
    }

    public void addToList(CharSequence text) {
        adapter.add(text);
    }

    public void setListOnItemClickListener(AdapterView.OnItemClickListener listener) {
        listView.setOnItemClickListener(listener);
    }
}
