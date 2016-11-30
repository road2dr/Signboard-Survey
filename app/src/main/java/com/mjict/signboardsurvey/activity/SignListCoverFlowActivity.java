package com.mjict.signboardsurvey.activity;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.SignListCoverFlowAdapter;
import com.mjict.signboardsurvey.model.SignInfo;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by Junseo on 2016-11-16.
 */
public class SignListCoverFlowActivity extends SABaseActivity {

    private FeatureCoverFlow signCoverFlow;
    private SignListCoverFlowAdapter adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_list_cover_flow;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.signboard_list);

        signCoverFlow = (FeatureCoverFlow)this.findViewById(R.id.sign_cover_flow);
        adapter = new SignListCoverFlowAdapter(this);
        signCoverFlow.setAdapter(adapter);

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
