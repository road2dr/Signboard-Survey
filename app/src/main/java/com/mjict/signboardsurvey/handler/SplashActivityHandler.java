package com.mjict.signboardsurvey.handler;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.LoginActivity;
import com.mjict.signboardsurvey.activity.SplashActivity;
import com.mjict.signboardsurvey.model.TaskResult;
import com.mjict.signboardsurvey.sframework.DefaultSActivityHandler;
import com.mjict.signboardsurvey.task.InitializeTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;

import java.util.ArrayList;

/**
 * Created by Junseo on 2016-11-29.
 */
public class SplashActivityHandler extends DefaultSActivityHandler {

    private SplashActivity activity;
    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        activity = (SplashActivity)this.getActivity();

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                startToInit();
            }
            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
                quitOnPermissionDenied();
            }
        };

        TedPermission tedPermission = new TedPermission(activity);
        tedPermission.setPermissionListener(permissionListener);
        tedPermission.setDeniedMessage(R.string.you_need_to_turn_on_permission_to_use_this_service);
        tedPermission.setPermissions(Manifest.permission.READ_EXTERNAL_STORAGE ,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION);
        tedPermission.check();



    }

    private void quitOnPermissionDenied() {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(R.string.quit)
                .setMessage(R.string.turn_on_permission)
                .setCancelable(false)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener(){
                    // 확인 버튼 클릭시 설정
                    public void onClick(DialogInterface dialog, int whichButton){
                        activity.finishAffinity();
                    }
                });

        AlertDialog dialog = builder.create();    // 알림창 객체 생성
        dialog.show();    // 알림창 띄우기
    }

    private void startToInit() {
        final InitializeTask initTask = new InitializeTask(activity.getApplicationContext());
        initTask.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<TaskResult>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_data_and_initialize);
            }
            @Override
            public void onTaskFinished(TaskResult taskResult) {
                activity.hideWaitingDialog();

                if(taskResult.success == false) {
                    activity.showAlertDialog(taskResult.errorMessage);
                } else {
                    // go to login
                    Intent intent = new Intent(activity, LoginActivity.class);
                    intent.putExtra(HANDLER_CLASS, LoginActivityHandler.class);
                    activity.startActivity(intent);
                    activity.finish();
                }
            }
        });
        initTask.execute();
    }
}
