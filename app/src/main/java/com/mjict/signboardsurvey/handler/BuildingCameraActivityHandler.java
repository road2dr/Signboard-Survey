package com.mjict.signboardsurvey.handler;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mjict.signboardsurvey.MJConstants;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.CameraActivity;
import com.mjict.signboardsurvey.model.Building;
import com.mjict.signboardsurvey.sframework.DefaultSActivityHandler;
import com.mjict.signboardsurvey.task.SaveImageTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;

/**
 * Created by Junseo on 2016-07-19.
 */
public class BuildingCameraActivityHandler extends DefaultSActivityHandler {

    private CameraActivity activity;
    private Building building;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        activity = (CameraActivity)getActivity();

        Intent intent = activity.getIntent();
        building = (Building) intent.getSerializableExtra(MJConstants.BUILDING);
        if(building == null) {
            Toast.makeText(activity, R.string.there_are_nobuilding_info_for_take_picture, Toast.LENGTH_LONG).show();
            activity.finish();
            return;
        }

        activity.setShootButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shootButtonClicked();
            }
        });
    }

    private void shootButtonClicked() {
        activity.takePicture(new CameraActivity.onPictureTakenListener() {
            @Override
            public void ontPictureTaken(int orientation, byte[] data) {

                startToSavePicture(orientation, data);
            }
        });
    }

    private void startToSavePicture(int orientation, byte[] data) {
        String time = Utilities.getCurrentTimeAsString();
        int hash = Math.abs((int)Utilities.hash(time));
        String dir = SyncConfiguration.getDirectoryForBuildingPicture();
        final String fileName = String.format("building_%d_%d.jpg", building.getId(), hash);
        String path = dir + fileName;

        SaveImageTask task = new SaveImageTask(path, orientation);
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<Boolean>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.saving);
            }

            @Override
            public void onTaskFinished(Boolean result) {
                int msg = result ? R.string.succeeded_to_save : R.string.failed_to_save;
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();

                Intent responseIntent = new Intent();
                responseIntent.putExtra(MJConstants.RESPONSE, result);
                responseIntent.putExtra(MJConstants.PATH, fileName);
                activity.setResult(Activity.RESULT_OK, responseIntent);
                activity.finish();
            }
        });
        task.execute(data);
    }
}
