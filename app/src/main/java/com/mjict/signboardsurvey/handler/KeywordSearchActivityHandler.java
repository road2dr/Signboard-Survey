package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.MJContext;
import com.mjict.signboardsurvey.activity.KeywordSearchActivity;
import com.mjict.signboardsurvey.activity.SearchResultActivity;
import com.mjict.signboardsurvey.sframework.DefaultSActivityHandler;

/**
 * Created by Junseo on 2016-12-02.
 */
public class KeywordSearchActivityHandler extends DefaultSActivityHandler {
    private KeywordSearchActivity activity;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (KeywordSearchActivity)this.getActivity();

        // register listener
        activity.setKeyboardActionListener(new KeywordSearchActivity.OnKeyboardActionListener() {
            @Override
            public void onDone() {
                String keyword = activity.getInputKeyword();
                saveKeyword(keyword);
                goToSearchResult(keyword);
            }
        });

        activity.setKeywordEditTextOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String keyword = activity.getInputKeyword();
                saveKeyword(keyword);
                goToSearchResult(keyword);
            }
        });

        activity.setSearchButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String keyword = activity.getInputKeyword();
                saveKeyword(keyword);
                goToSearchResult(keyword);
            }
        });

        // init


        // do first job
        loadRecentKeyword();
    }

//    private void keywordEditTextFocusChanged(boolean hasFocus) {
//        if(hasFocus)
//            activity.showKeywordListView();
//        else
//            activity.hideKeywordListView();
//    }

    private void goToSearchResult(String keyword) {
        Intent intent = new Intent(activity, SearchResultActivity.class);
        intent.putExtra(HANDLER_CLASS, SearchResultActivityHandler.class);
        intent.putExtra(MJConstants.KEYWORD, keyword);
        activity.startActivity(intent);
    }

    private void saveKeyword(String keyword) {
        MJContext.addRecentKeyword(keyword);
    }
    private void loadRecentKeyword() {
        String[] keywords = MJContext.getRecentKeywords();
        if(keywords == null)
            return;

        for(int i=(keywords.length-1); i>=0; i--)
            activity.addToKeywordListView(keywords[i]);
    }
}
