package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Shop;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.util.Utilities;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Junseo on 2017-01-23.
 */
public class LoadAllSignStatusTask extends DefaultAsyncTask<String, Integer, Long[]> {

    private Context context;

    public LoadAllSignStatusTask(Context c) {
        context = c;
    }

    @Override
    protected Long[] doInBackground(String... params) {
        if(params == null)
            return null;

        String userId = params[0];
        DatabaseManager dmgr = DatabaseManager.getInstance(context);

        long allSignCount = dmgr.getAllSignsCount();
        long allShopCount = dmgr.getAllShopsCount();
        long allBuildingCount = dmgr.getAllBuildingsCount();

        List<Sign> allSigns = dmgr.getAllSigns();
        int n = allSigns.size();
        long reviewSignCount = 0;
        long demolishSignCount = 0;
        for(int i=0; i<n; i++) {
            if(isCancelled())
                break;

            Sign s = allSigns.get(i);
            if(s.getStatsCode() == 1 || s.getStatsCode() == 2 || s.getStatsCode() == 3) {   // TODO 상수는 파일로 관리 혹은 enum
                demolishSignCount++;
            }
            if(s.getReviewCode() != 0) {
                reviewSignCount++;
            }
        }
        allSigns.clear();

        if(isCancelled())
            return null;

        List<Sign> userSigns = dmgr.findSignByUserId(userId);
        long allUserSignCount = userSigns.size();
        long userTodaySignCount = 0;
        long userReviewSignCount = 0;
        for(int i=0; i<allUserSignCount; i++) {
            if(isCancelled())
                break;

            Sign s = userSigns.get(i);

            if(s.getReviewCode() != 0)
                userReviewSignCount++;

            // 오늘 간판을 찾는다
            Calendar todayStartTime = Utilities.getStartTimeBeforeDays(0);
            if(s.getModifyDate() != null && s.getModifyDate().equals("") == false) {
                long timeOfStart = todayStartTime.getTime().getTime();
                long timeOfNow = Calendar.getInstance().getTime().getTime();

                Date modyDate = Utilities.stringToDate(s.getModifyDate());
                if(modyDate == null)
                    continue;

                long modyTime = modyDate.getTime();

                if(modyTime < timeOfStart || modyTime >= timeOfNow) {
                    continue;
                }

                userTodaySignCount++;
            }

        }

        List<Shop> userShops = dmgr.findShopByShopName(userId);
        long userShopCount = userShops.size();
        long userDemolishShopCount = 0;
        for(int i=0; i<userShopCount; i++) {
            Shop shop = userShops.get(i);
            if(shop.getBusinessCondition() == 1)
                userDemolishShopCount++;
        }

        Long[] result = new Long[10];
        result[0] = allBuildingCount;
        result[1] = allShopCount;
        result[2] = allSignCount;
        result[3] = reviewSignCount;
        result[4] = demolishSignCount;

        result[5] = allUserSignCount;
        result[6] = userTodaySignCount;
        result[7] = userReviewSignCount;
        result[8] = userShopCount;
        result[9] = userDemolishShopCount;

        return result;
    }
}
