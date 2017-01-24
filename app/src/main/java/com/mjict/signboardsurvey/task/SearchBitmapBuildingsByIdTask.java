package com.mjict.signboardsurvey.task;

import android.content.Context;
import android.graphics.Bitmap;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.BitmapBuilding;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.BuildingPicture;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;

import java.util.List;

/**
 * Created by Junseo on 2016-12-01.
 */
public class SearchBitmapBuildingsByIdTask extends DefaultAsyncTask<Long, BitmapBuilding, Boolean> {
    private Context context;

    public SearchBitmapBuildingsByIdTask(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Long... params) {
        if(params == null)
            return false;

        DatabaseManager dmgr = DatabaseManager.getInstance(context);

        for(int i=0; i<params.length; i++) {
            if (isCancelled())
                break;

            Building building = dmgr.getBuilding(params[i]);
            BitmapBuilding bb = null;
            Bitmap image = null;

            if (building == null)
                continue;

            List<BuildingPicture> pics = dmgr.findBuildingPictureByBuildingId(params[i]);

            if (pics != null) {
                for (int j = 0; j < pics.size(); j++) {
                    String path = SyncConfiguration.getDirectoryForBuildingPicture(building.isSync()) + pics.get(j).getPath();
                    image = Utilities.loadImage(path, 8);
                    if (image != null)
                        break;
                }
            }

            bb = new BitmapBuilding(image, building);
            publishProgress(bb);
        }
        return true;
    }
}
