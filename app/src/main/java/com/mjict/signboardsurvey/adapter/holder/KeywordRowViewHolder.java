package com.mjict.signboardsurvey.adapter.holder;

import android.view.View;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-12-02.
 */
public class KeywordRowViewHolder {
    private View parent;
    private TextView keywordTextView;

    public KeywordRowViewHolder(View view) {
        parent = view;
    }

    public TextView getKeywordTextView() {
        if(keywordTextView == null)
            keywordTextView = (TextView)parent.findViewById(R.id.keyword_text_view);

        return keywordTextView;
    }

}
