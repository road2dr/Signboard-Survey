package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.MJContext;
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

            // 2017.02.01 외부 DB 파일을 바로쓰는 관계로 필요 없어 졌다.
//            String syncFile = SyncConfiguration.getDatabaseFileNameForSync();
//            FileManager.copyDbFileTo(context, syncFile);

            MJContext.save(context);
        } catch (IOException e) {
            e.printStackTrace();
        } /*catch (SdNotMountedException e) {
            e.printStackTrace();
        }*/

        return null;
    }
}