package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.PictureActivity;
import com.mjict.signboardsurvey.model.IndexBitmap;
import com.mjict.signboardsurvey.task.AsyncTaskListener;
import com.mjict.signboardsurvey.task.LoadImageTask;

/**
 * Created by Junseo on 2016-12-21.
 */
public class SignPictureActivityHandler extends SABaseActivityHandler {

    private PictureActivity activity;
    private String targetPath;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        // init
        activity = (PictureActivity) getActivity();
        Intent intent = activity.getIntent();
        targetPath = intent.getStringExtra(MJConstants.PATH);
        if(targetPath == null) {    // 보여줄 파일 경로 없음
            activity.setResult(Activity.RESULT_CANCELED);
            activity.finish();
            return;
        }

        activity.hideButtonLayout();

        // register listener

        // do first job
        startToLoadImage();

    }

    private void startToLoadImage() {
        if(targetPath == null)
            return;

        LoadImageTask task = new LoadImageTask();
        task.setSampleSize(1);
        task.setDefaultAsyncTaskListener(new AsyncTaskListener<IndexBitmap, Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_sign_picture);
            }
            @Override
            public void onTaskProgressUpdate(IndexBitmap... values) {
                if(values == null)
                    return;

                if(values.length > 0)
                    activity.setImage(values[0].image);
            }

            @Override
            public void onTaskFinished(Boolean aBoolean) {
                activity.hideWaitingDialog();
            }
        });
        task.execute(targetPath);
    }
}
