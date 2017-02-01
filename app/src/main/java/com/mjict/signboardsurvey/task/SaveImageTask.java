package com.mjict.signboardsurvey.task;


import com.mjict.signboardsurvey.MJContext;
import com.mjict.signboardsurvey.util.FileManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;

import java.util.Calendar;

/**
 * Created by Junseo on 2016-08-11.
 */
public class SaveImageTask extends DefaultAsyncTask<byte[], Integer, Boolean> {
    private String fileName = null;
    private int orientation = -1;

    public SaveImageTask(String name, int ori) {
        fileName = name;
        orientation = ori;
    }

    @Override
    protected Boolean doInBackground(byte[]... params) {
        if(params == null)
            return false;

        byte[] data = params[0];

        String filePath = null;
        if(fileName == null) {
            long time = Calendar.getInstance().getTimeInMillis();
            String picDir = SyncConfiguration.getTempDirectory();
            filePath = picDir+time+""+ MJContext.getCurrentUser().getMobileId()+".jpg";
        } else {
            filePath = fileName;
        }

        boolean rotate = (orientation == 1 || orientation == 3) ? true : false;
        boolean result = FileManager.makeAndRoateJpg(data, filePath, rotate);

        return result;
    }
}
