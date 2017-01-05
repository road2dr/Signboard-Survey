package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.MJContext;
import com.mjict.signboardsurvey.util.FileManager;
import com.mjict.signboardsurvey.util.SdNotMountedException;
import com.mjict.signboardsurvey.util.SyncConfiguration;

import java.io.IOException;

/**
 * Created by Junseo on 2016-09-01.
 */
public class QuitTask extends DefaultAsyncTask<Void, Integer, Void> {
    private Context context;

    public QuitTask(Context c) {
        context = c;
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            SyncConfiguration.save();
            String syncFile = SyncConfiguration.getDatabaseFileNameForSync();
            FileManager.copyDbFileTo(context, syncFile);

            MJContext.save(context);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SdNotMountedException e) {
            e.printStackTrace();
        }

        return null;
    }
}