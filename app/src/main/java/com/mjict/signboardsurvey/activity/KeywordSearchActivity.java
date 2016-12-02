package com.mjict.signboardsurvey.activity;

import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.KeywordListAdapter;

/**
 * Created by Junseo on 2016-12-02.
 */
public class KeywordSearchActivity extends SABaseActivity {

    private AutoCompleteTextView keywordEditText;
    private ImageButton searchButton;
    private KeywordListAdapter KeywordListAdapter;
    private OnKeyboardActionListener keyboardActionListener;

    @Override
    protected void init() {
        super.init();
        this.hideToolBar();
        this.disableNavigation();

        keywordEditText = (AutoCompleteTextView)this.findViewById(R.id.keyword_edit_text);
        searchButton = (ImageButton)this.findViewById(R.id.search_button);

        KeywordListAdapter = new KeywordListAdapter(this);
        keywordEditText.setAdapter(KeywordListAdapter);
        keywordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(v.getId()==R.id.keyword_edit_text && actionId== EditorInfo.IME_ACTION_DONE){
                    if(keyboardActionListener != null)
                        keyboardActionListener.onDone();
                }
                return false;
            }
        });
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_keyword_search;
    }

    public String getInputKeyword() {
        return keywordEditText.getText().toString();
    }

    public void setKeywordEditTextOnItemClickListener(AdapterView.OnItemClickListener listener) {
        keywordEditText.setOnItemClickListener(listener);
    }

    public void addToKeywordListView(String keyword) {
        KeywordListAdapter.add(keyword);
    }

    public void setSearchButtonOnClickListener(View.OnClickListener listener) {
        searchButton.setOnClickListener(listener);
    }

    public void setKeyboardActionListener(OnKeyboardActionListener listener) {
        keyboardActionListener = listener;
    }

    public static interface OnKeyboardActionListener {
        public void onDone();
    }
}
