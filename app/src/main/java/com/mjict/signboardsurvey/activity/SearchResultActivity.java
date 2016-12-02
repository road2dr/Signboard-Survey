package com.mjict.signboardsurvey.activity;

import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mjict.signboardsurvey.R;

/**
 * Created by Junseo on 2016-11-10.
 */
public class SearchResultActivity extends SABaseActivity {

    private LinearLayout addressResultLayout;
    private TextView addressResultLabel;
    private TextView addressResultMoreButton;

    LayoutInflater inflater;

    @Override
    protected void init() {
        super.init();
        this.showToolBar();
        this.setTitle(R.string.search_result);

        inflater = LayoutInflater.from(this);

        //
        addressResultLayout = (LinearLayout)this.findViewById(R.id.address_result_layout);
        addressResultLabel = (TextView)this.findViewById(R.id.address_result_label);
        addressResultMoreButton = (TextView)this.findViewById(R.id.address_result_more_button);

//        firstStreetTextView = (TextView)this.findViewById(R.id.first_street_text_view);
//        secondStreetTextView = (TextView)this.findViewById(R.id.second_street_text_view);
//        thirdStreetTextView = (TextView)this.findViewById(R.id.third_street_text_view);
//        fourthStreetTextView = (TextView)this.findViewById(R.id.fourth_street_text_view);
//        fifthStreetTextView = (TextView)this.findViewById(R.id.fifth_street_text_view);
//
//        // test
//        String address1 = "인천광역시 남동구 구월동 구월남로";
//        String address2 = "인천광역시 남동구 구월동 구월남로 21번길";
//        String address3 = "인천광역시 남동구 구월동 구월남로 167-1번길";
//        String address4 = "인천광역시 남동구 구월동 북구월남로";
//        String address5 = "인천광역시 남동구 구월동 북구월남로 17번길";
//
//        final SpannableStringBuilder sa1 = new SpannableStringBuilder(address1);
//        sa1.setSpan(new AbsoluteSizeSpan(40), 14, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        final SpannableStringBuilder sa2 = new SpannableStringBuilder(address2);
//        sa2.setSpan(new AbsoluteSizeSpan(40), 14, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        final SpannableStringBuilder sa3 = new SpannableStringBuilder(address3);
//        sa3.setSpan(new AbsoluteSizeSpan(40), 14, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        final SpannableStringBuilder sa4 = new SpannableStringBuilder(address4);
//        sa4.setSpan(new AbsoluteSizeSpan(40), 15, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        final SpannableStringBuilder sa5 = new SpannableStringBuilder(address5);
//        sa5.setSpan(new AbsoluteSizeSpan(40), 15, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        firstStreetTextView.setText(sa1);
//        secondStreetTextView.setText(sa2);
//        thirdStreetTextView.setText(sa3);
//        fourthStreetTextView.setText(sa4);
//        fifthStreetTextView.setText(sa5);
//
//
//        firstStreetTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SearchResultActivity.this, BuildingSearchActivity.class);
//                intent.putExtra(SActivityHandler.HANDLER_CLASS, BuildingSearchActivityHandler.class);
//                startActivity(intent);
//            }
//        });
    }

    public void addAddressResult(String result) {
        TextView tv = (TextView)inflater.inflate(R.layout.tv_search_result_address, addressResultLayout);
        if(addressResultLayout.getChildCount() > 0) {
            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) tv.getLayoutParams();
            param.setMargins(0, 5, 0, 0);
        }
    }

    @Override
    protected int getContentLayout() {
        return R.layout.activity_search_result;
    }

}
