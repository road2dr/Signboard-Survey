package com.mjict.signboardsurvey.activity;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.SignListAdapter;
import com.mjict.signboardsurvey.handler.SignInformationActivity2Handler;
import com.mjict.signboardsurvey.handler.SignInformationActivityHandler;
import com.mjict.signboardsurvey.model.SignInfo;
import com.mjict.signboardsurvey.sframework.SActivityHandler;

/**
 * Created by Junseo on 2016-11-16.
 */
public class SignListActivity extends SABaseActivity {

    private ListView signListView;
    private SignListAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_list;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.signboard_list);

        signListView = (ListView)this.findViewById(R.id.sign_list_view);
        adapter = new SignListAdapter(this);
        signListView.setAdapter(adapter);

        signListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1) {
                    Intent intent = new Intent(SignListActivity.this, SignInformationActivity2.class);
                    intent.putExtra(SActivityHandler.HANDLER_CLASS, SignInformationActivity2Handler.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SignListActivity.this, SignInformationActivity.class);
                    intent.putExtra(SActivityHandler.HANDLER_CLASS, SignInformationActivityHandler.class);
                    startActivity(intent);
                }
            }
        });


        // test
        SignInfo[] tempData = {
                new SignInfo(R.drawable.test_sign6, "CAFE DA", "2.5 X 3.3 X 0.5", "", -1, "", "무허가요건구비"),
                new SignInfo(R.drawable.test_sign5, "김밥천국", "4.5 X 2.0", "", -1, "", "수량초과"),
                new SignInfo(R.drawable.test_sign4, "간판 제목", "7.2 X 2.0", "", -1, "", "규격초과"),
                new SignInfo(R.drawable.test_sign3, "할매 순대국밥", "6.0 X 1.2", "", -1, "", "무허가요건구비"),
                new SignInfo(R.drawable.test_sign2, "레스모아", "6.0 X 1.2", "", -1, "", "허가"),
                new SignInfo(R.drawable.test_sign1, "장충동 왁족발", "5.6 X 1.0 X 1.0", "", -1, "", "허가신고배제"),
                new SignInfo(R.drawable.test_sign4, "뿅간다 약국", "6.0 X 1.4", "", -1, "", "신고"),
        };
        for(int i=0; i<tempData.length; i++)
            adapter.add(tempData[i]);
    }
}
