package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Sign;

import java.util.List;

/**
 * Created by Junseo on 2017-01-02.
 */
public class SearchUserSignDataTask extends DefaultAsyncTask<String, Integer, List<Sign>>{
    private Context context;

    public SearchUserSignDataTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<Sign> doInBackground(String... params) {
        if(params == null)
            return null;

        if(params.length < 0)
            return null;

        String userId = params[0];

        DatabaseManager dmgr = DatabaseManager.getInstance(context);

        List<Sign> signs = dmgr.findSignByUserId(userId);

//        int n = signs.size();
//        for(int i=0; i<n; i++) {
//            Sign sign = signs.get(i);
//            Shop shop = null;
//            Building building = null;
//            List<SignOwnership> ownerships = dmgr.findSignOwnershipBySignId(sign.getId());  // TODO SignOwnership 없어지면 바뀌겠지
//            if(ownerships != null && ownerships.size() > 0)
//                shop = dmgr.getShop(ownerships.get(0).getShopId());
//            if(shop != null)
//                building = dmgr.getBuilding(shop.getBuildingId());
//
//            if(building == null)
//                continue;
//
//            Log.d("junseo", "shop and builging: "+i);
//
//            DetailSign detailSign = new DetailSign(sign, shop, building);
//            publishProgress(detailSign);
//        }

        return signs;
    }

//    public static class SearchCondition {
//        public String userId;
//        public String province;
//        public String county;
//        public String town;
//        public Calendar searchTime;
//
//        public SearchCondition(String userId, String province, String county, String town, Calendar searchTime) {
//            this.userId = userId;
//            this.province = province;
//            this.county = county;
//            this.town = town;
//            this.searchTime = searchTime;
//        }
//    }
}
