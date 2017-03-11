package com.mjict.signboardsurvey.task;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;

import com.mjict.signboardsurvey.util.Utilities;

/**
 * Created by Junseo on 2017-03-03.
 */
public class LoadImageFromUriTask extends DefaultAsyncTask<Uri, Bitmap, Boolean> {

    private Context context;

    public LoadImageFromUriTask(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Uri... params) {
        if(params == null)
            return null;

        for(int i=0; i<params.length; i++) {
            if(isCancelled())
                break;

            Bitmap image = Utilities.loadImageFromUri(context, params[i]);

            if(isCancelled() == false)
                publishProgress(image);
        }

        return true;

    }
}
