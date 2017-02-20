package com.mjict.signboardsurvey.activity;

import android.support.v4.view.ViewPager;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.adapter.SignSimpleCoverFlowAdapter;
import com.mjict.signboardsurvey.adapter.TempSignViewPagerAdapter;

import it.moondroid.coverflow.components.ui.containers.FeatureCoverFlow;

/**
 * Created by Junseo on 2016-11-16.
 */
public class TempSignListPagerActivity extends SABaseActivity {

    private ViewPager signListPager;
    private TempSignViewPagerAdapter adapter;

    private FeatureCoverFlow signCoverFlow;
    private SignSimpleCoverFlowAdapter coverFlowAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.activity_sign_list_pager;
    }

    @Override
    protected void init() {
        super.init();
        this.setTitle(R.string.signboard_list);

        signListPager = (ViewPager)this.findViewById(R.id.sign_list_pager);
        adapter = new TempSignViewPagerAdapter(this);
        signListPager.setAdapter(adapter);

        signCoverFlow = (FeatureCoverFlow)this.findViewById(R.id.sign_cover_flow);
        coverFlowAdapter = new SignSimpleCoverFlowAdapter(this);
        signCoverFlow.setAdapter(coverFlowAdapter);

        signListPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
            @Override
            public void onPageSelected(int position) {
                signCoverFlow.setSelection(position);
            }
            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        signCoverFlow.setOnScrollPositionListener(new FeatureCoverFlow.OnScrollPositionListener() {
            @Override
            public void onScrolledToPosition(int position) {
                signListPager.setCurrentItem(position);
            }

            @Override
            public void onScrolling() {

            }
        });

        // test
//        SignInfo[] tempData = {
//                new SignInfo(R.drawable.test_sign6, "CAFE DA", "2.5 X 3.3 X 0.5", "", -1, "", "무허가요건구비"),
//                new SignInfo(R.drawable.test_sign5, "김밥천국", "4.5 X 2.0", "", -1, "", "수량초과"),
//                new SignInfo(R.drawable.test_sign4, "간판 제목", "7.2 X 2.0", "", -1, "", "규격초과"),
//                new SignInfo(R.drawable.test_sign3, "할매 순대국밥", "6.0 X 1.2", "", -1, "", "무허가요건구비"),
//                new SignInfo(R.drawable.test_sign2, "레스모아", "6.0 X 1.2", "", -1, "", "허가"),
//                new SignInfo(R.drawable.test_sign1, "장충동 왁족발", "5.6 X 1.0 X 1.0", "", -1, "", "허가신고배제"),
//                new SignInfo(R.drawable.test_sign4, "뿅간다 약국", "6.0 X 1.4", "", -1, "", "신고"),
//
//        };
//        for(int i=0; i<tempData.length; i++) {
//            adapter.add(tempData[i]);
//            coverFlowAdapter.add(tempData[i]);
//        }
    }
}
