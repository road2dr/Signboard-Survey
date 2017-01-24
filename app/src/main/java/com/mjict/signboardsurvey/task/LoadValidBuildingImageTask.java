package com.mjict.signboardsurvey.task;

import android.content.Context;
import android.graphics.Bitmap;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.model.BuildingPicture;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;

import java.util.List;

/**
 * Created by Junseo on 2016-08-03.
 */
public class LoadValidBuildingImageTask extends DefaultAsyncTask<Building, IndexBitmap, Boolean> {

    private Context context;
//    private boolean canceled = false;

    public LoadValidBuildingImageTask(Context c) {
        context = c;
    }

    @Override
    protected Boolean doInBackground(Building... params) {
        if(params == null)
            return false;

        DatabaseManager dmgr = DatabaseManager.getInstance(context);
//        List<Building> buildings = params[0];

        for(int i=0; i<params.length; i++) {
            Building b = params[i];
            if(isCancelled())
                break;

            List<BuildingPicture> pics = dmgr.findBuildingPictureByBuildingId(b.getId());
            Bitmap image = null;
            if (pics != null) {
                for (int j = 0; j < pics.size(); j++) {

                    String path = SyncConfiguration.getDirectoryForBuildingPicture(b.isSync()) + pics.get(j).getPath();
                    image = Utilities.loadImage(path, 8);
                    if(image != null)
                        break;
                }
            }
            if(image != null && isCancelled() == false)
                publishProgress(new IndexBitmap(i, image));

        }

        return true;
    }


}


