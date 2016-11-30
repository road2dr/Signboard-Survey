package com.mjict.signboardsurvey.handler;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.activity.LoginActivity;
import com.mjict.signboardsurvey.activity.SummaryActivity;
import com.mjict.signboardsurvey.model.User;
import com.mjict.signboardsurvey.sframework.DefaultSActivityHandler;
import com.mjict.signboardsurvey.task.LoadUserDataTask;
import com.mjict.signboardsurvey.task.SimpleAsyncTaskListener;

import java.util.List;

/**
 * Created by Junseo on 2016-11-09.
 */
public class LoginActivityHandler extends DefaultSActivityHandler {

    private LoginActivity activity;

    @Override
    public void onActivityCreate(Bundle savedInstanceState) {
        super.onActivityCreate(savedInstanceState);

        // register listener
        activity = (LoginActivity)this.getActivity();
        activity.setLoginButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = ((UserWrapper)activity.getSelectedUser()).user;
                String password = activity.getInputPassword();
                if(user == null) {
                    Toast.makeText(activity, R.string.user_not_selected, Toast.LENGTH_SHORT).show();
                    return;
                }
                if(password.equals("")) {
                    Toast.makeText(activity, R.string.input_password, Toast.LENGTH_SHORT).show();
                    return;
                }

                if(user.getPassword().equals(password)) {
                    goToSummary();
                } else {
                    Toast.makeText(activity, R.string.password_not_matched, Toast.LENGTH_SHORT).show();
                }

            }
        });

        // do first job
        startToLoadUsersData();
    }

    private void startToLoadUsersData() {
        LoadUserDataTask task = new LoadUserDataTask(activity.getApplicationContext());
        task.setSimpleAsyncTaskListener(new SimpleAsyncTaskListener<List<User>>() {
            @Override
            public void onTaskStart() {
                activity.showWaitingDialog(R.string.loading_user_data);
            }
            @Override
            public void onTaskFinished(List<com.mjict.signboardsurvey.model.User> users) {
                activity.hideWaitingDialog();
                if(users == null) {
                    activity.showAlertDialog(R.string.failed_to_load_user_data);
                    // TODO 사용자 데이터 불러오기 실패 => 앱을 종료 해야 할까?
                } else {
                    for(int i=0; i<users.size(); i++) {
                        UserWrapper wrapper = new UserWrapper(users.get(i));
                        activity.addToUserSpinner(i, wrapper);
                    }
                }
            }
        });
        task.execute();
    }

    private void goToSummary() {
        Intent intent = new Intent(activity, SummaryActivity.class);
        intent.putExtra(HANDLER_CLASS, SummaryActivityHandler.class);
        activity.startActivity(intent);
        activity.finish();
    }

    private static class UserWrapper {
        public User user;

        public UserWrapper(User u) {
            user = u;
        }

        @Override
        public String toString() {
            return user.getUserId();
        }
    }
}
