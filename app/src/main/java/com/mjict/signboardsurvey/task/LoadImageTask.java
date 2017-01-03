package com.mjict.signboardsurvey.task;

import android.graphics.Bitmap;

import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.util.Utilities;

/**
 * Created by Junseo on 2016-08-03.
 */
public class LoadImageTask extends DefaultAsyncTask<String, IndexBitmap, Boolean> {

    private int inSampleSize = 4;
    public void setSampleSize(int size) {
        inSampleSize = size;
    }

    @Override
    protected Boolean doInBackground(String... params) {
        if(params == null)
            return null;

        for(int i=0; i<params.length; i++) {
            if(isCancelled())
                break;

            String path = params[i];
            Bitmap image = null;
            if(path != null) {
                image = Utilities.loadImage(path, inSampleSize);
            }
            IndexBitmap li = new IndexBitmap(i, image);

            if(isCancelled() == false)
                publishProgress(li);
        }

        return true;

    }
}
