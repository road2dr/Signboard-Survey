package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.BuildingPicture;
import com.mjict.signboardsurvey.util.SyncConfiguration;

import java.io.File;

/**
 * Created by Junseo on 2016-08-04.
 */
public class DeleteBuildingPictureTask extends DefaultAsyncTask<BuildingPicture, Integer, Boolean> {
    private Context context;

    public DeleteBuildingPictureTask(Context context) {
        this.context = context;
    }
    @Override
    protected Boolean doInBackground(BuildingPicture... params) {
        BuildingPicture pic = params[0];

        // DB 에서 지우고
        DatabaseManager dmgr = DatabaseManager.getInstance(context);
        int result = dmgr.deleteBuildingPicture(pic);
        if(result != 1)  // 뭔가 에러 발생
            return false;

        // 파일 에서 지우고
        String picDir = SyncConfiguration.getDirectoryForBuildingPicture(pic.isSynchronized());
        String path = picDir + pic.getPath();

        File file = new File(path);
        return file.delete();
    }
}
