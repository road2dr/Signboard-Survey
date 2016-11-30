package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.User;

import java.util.List;

/**
 * Created by Junseo on 2016-07-29.
 */
public class LoadUserDataTask extends DefaultAsyncTask<Void, Integer, List<User>> {

    private Context context;

    public LoadUserDataTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<User> doInBackground(Void... params) {
        // 유저 목록 불러오기
        DatabaseManager dbm = DatabaseManager.getInstance(context);
        List<User> users = dbm.getAllUsers();
        if(users == null)
            return null;

        return users;
    }
}
