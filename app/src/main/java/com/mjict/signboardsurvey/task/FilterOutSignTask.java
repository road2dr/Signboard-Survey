package com.mjict.signboardsurvey.task;

import android.content.Context;
import android.util.Log;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.util.Utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Junseo on 2017-01-03.
 */
public class FilterOutSignTask extends DefaultAsyncTask<FilterOutSignTask.SearchFilter, Sign, Boolean> {

    private List<Sign> allSigns;
    private Context context;

    public FilterOutSignTask(Context context, List<Sign> signs) {
        this.context = context;
        allSigns = signs;
    }

    @Override
    protected Boolean doInBackground(SearchFilter... params) {
        if(allSigns == null || params == null)
            return false;

        SearchFilter filter = params[0];
        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        int addressId = dmgr.findAddressId(filter.province, filter.county, filter.town);

        int n = allSigns.size();
        for(int i=0; i<n; i++) {
            Sign sign = allSigns.get(i);

            // test
            if(sign.getModifyDate().startsWith("2017")) {
                Log.d("junseo", "it's 2017!");
            }

            if(addressId != -1) {
                if(sign.getAddressId() != addressId)
                    continue;;
            }

//            if(province != null && province.equals(building.getProvince()) == false)
//                continue;
//            if(county != null && county.equals(building.getCounty()) == false)
//                continue;
//            if(town != null && town.equals(building.getTown()) == false)
//                continue;

            Calendar searchTime = filter.searchTime;
            if(searchTime != null && sign.getModifyDate() != null && sign.getModifyDate().equals("") == false) {
                long timeOfStart = searchTime.getTime().getTime();
                long timeOfNow = Calendar.getInstance().getTime().getTime();

                Log.d("junseo", "시작 시간: "+searchTime.getTime());
                Log.d("junseo", "지금 시간: "+Calendar.getInstance().getTime());

                Date modyDate = Utilities.stringToDate(sign.getModifyDate());
                Log.d("junseo", "검사 시간: "+modyDate);
                if(modyDate == null)
                    continue;

                long modyTime = modyDate.getTime();

                if(modyTime < timeOfStart || modyTime >= timeOfNow) {
                    Log.d("junseo", "포함되지 않아서 스킵");
                    continue;
                }
            }

            publishProgress(sign);
        }

        return true;
    }

    public static class SearchFilter {
        public String province;
        public String county;
        public String town;
        public Calendar searchTime;

        public SearchFilter(String province, String county, String town, Calendar searchTime) {
            this.province = province;
            this.county = county;
            this.town = town;
            this.searchTime = searchTime;
        }
    }
}
