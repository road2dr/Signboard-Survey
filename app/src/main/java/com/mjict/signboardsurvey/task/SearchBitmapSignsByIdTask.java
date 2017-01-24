package com.mjict.signboardsurvey.task;

import android.content.Context;
import android.graphics.Bitmap;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.BitmapSign;
import com.mjict.signboardsurvey.model.Sign;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;

/**
 * Created by Junseo on 2016-12-01.
 */
public class SearchBitmapSignsByIdTask extends DefaultAsyncTask<Long, BitmapSign, Boolean> {
    private Context context;

    public SearchBitmapSignsByIdTask(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Long... params) {
        if(params == null)
            return false;

        DatabaseManager dmgr = DatabaseManager.getInstance(context);

        for(int i=0; i<params.length; i++) {
            if(isCancelled())
                break;

            BitmapSign bs = null;
            Sign sign = dmgr.getSign(params[i]);
            if(sign == null)
                continue;

            String path = SyncConfiguration.getDirectoryForSingPicture(sign.isSynchronized()) + sign.getPicNumber();
            Bitmap image = Utilities.loadImage(path, 8);
            bs = new BitmapSign(image, sign);

            publishProgress(bs);
        }

        return true;
    }
}
