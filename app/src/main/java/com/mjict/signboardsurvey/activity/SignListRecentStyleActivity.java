package com.mjict.signboardsurvey.activity;

import android.view.View;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.SignListRecentStyleAdapter;
import com.mjict.signboardsurvey.model.SignInfo;
import com.mjict.signboardsurvey.widget.RecentsList;

/**
 * Created by Junseo on 2016-11-15.
 */
public class SignListRecentStyleActivity extends SABaseActivity {

    private RecentsList signListView;
    private SignListRecentStyleAdapter signListAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_list_recent_style;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.signboard_list);

        signListView = (RecentsList)this.findViewById(R.id.sign_list_view);
        signListAdapter = new SignListRecentStyleAdapter(this);
        signListView.setAdapter(signListAdapter);

        signListView.setOnItemClickListener(new RecentsList.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

            }
        });

        // test
//        int image, String content, String size, String status, int light, String location, String result
        SignInfo[] tempData = {
                new SignInfo(R.drawable.test_sign6, "CAFE DA", "2.5 X 3.3 X 0.5", "", -1, "", "무허가요건구비"),
                new SignInfo(R.drawable.test_sign5, "김밥천국", "4.5 X 2.0", "", -1, "", "수량초과"),
                new SignInfo(R.drawable.test_sign4, "간판 제목", "", "7.2 X 2.0", -1, "", "규격초과"),
                new SignInfo(R.drawable.test_sign3, "할매 순대국밥", "6.0 X 1.2", "", -1, "", "무허가요건구비"),
                new SignInfo(R.drawable.test_sign2, "레스모아", "", "6.0 X 1.2", -1, "", "허가"),
                new SignInfo(R.drawable.test_sign1, "장충동 왁족발", "5.6 X 1.0 X 1.0", "", -1, "", "허가신고배제"),
                new SignInfo(R.drawable.test_sign4, "뿅간다 약국", "6.0 X 1.4", "", -1, "", "신고"),

        };
        for(int i=0; i<tempData.length; i++)
            signListAdapter.add(tempData[i]);
    }
}
