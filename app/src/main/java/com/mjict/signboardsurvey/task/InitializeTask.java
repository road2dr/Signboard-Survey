package com.mjict.signboardsurvey.task;

import android.content.Context;
import android.util.Log;

import com.mjict.signboardsurvey.MJContext;
import com.mjict.signboardsurvey.R;
import com.mjict.signboardsurvey.autojudgement.AutoJudgementRuleManager;
import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.TaskResult;
import com.mjict.signboardsurvey.util.AppDataConfiguration;
import com.mjict.signboardsurvey.util.SettingDataManager;
import com.mjict.signboardsurvey.util.SyncConfiguration;
import com.mjict.signboardsurvey.util.Utilities;

import java.io.IOException;

/**
 * Created by Junseo on 2016-07-28.
 */
public class InitializeTask extends DefaultAsyncTask<Void, Integer, TaskResult> {

    private Context context;
//    private AsyncTaskListener taskListener;

    public InitializeTask(Context context) {
        this.context = context;
    }

//    public void setAsyncTaskListener(AsyncTaskListener listener) {
//        this.taskListener = listener;
//    }
//
//    @Override
//    protected void onPreExecute() {
//        if(taskListener != null)
//            taskListener.onTaskStart();
//
//        super.onPreExecute();
//    }

    @Override
    protected TaskResult doInBackground(Void... params) {

        boolean isFirst = true;
        boolean answer = true;
        try {
            isFirst = Utilities.isFirstTimeLaunch(context);
        } catch (IOException e1) {
            e1.printStackTrace();
            isFirst = false;
        }
        Log.d("junseo", "처음? "+isFirst);

        // 싱크용 프로퍼티 파일 로드
        if(SyncConfiguration.hasPropertyFile())
            answer = SyncConfiguration.load();
        else
            answer = SyncConfiguration.makePropertyFile();

        // 앱 내부용 프로파티 파일 로드
        if(AppDataConfiguration.hasPropertyFile(context))
            answer = AppDataConfiguration.load(context);
        else
            answer = AppDataConfiguration.makePropertyFile(context);

        //
        MJContext.load(context);

        if(answer == false)
            return new TaskResult(false, null, R.string.failed_to_initialize);

        if(isFirst == true) {
            answer = SyncConfiguration.makeDirectories();
        }

        Log.d("junseo", "디렉토리 만들기: "+answer);

        if(answer == false)
            return new TaskResult(false, null, R.string.failed_to_initialize);

        if(isFirst == true)
            SyncConfiguration.setDataChanged(true);

        // temp - 다이얼로그 오래 띄우기
        // TODO 나중에 이거 지우기
        long saveTime = System.currentTimeMillis();
        long currTime = 0;
        while( currTime - saveTime < 2000){
            currTime = System.currentTimeMillis();
        }

        SettingDataManager.getInstance().load(context);
        DatabaseManager dbm = DatabaseManager.getInstance(context);

        // 변경 -- 2017.02.01 DB 파일을 외부에서 바로 읽고 쓰게 한다
        //        boolean databaseChanged = SyncConfiguration.getDataChaged(); 체크 할 이유가 없어졌다.
        if(SyncConfiguration.hasDatabaseFileFoySync() == false)
                return new TaskResult(false, null, R.string.there_are_no_db_file);

        // 설정 로드
//        boolean databaseChanged = SyncConfiguration.getDataChaged();
//
//        Log.d("junseo", "Configuration datachanged: "+databaseChanged);
//        if(databaseChanged) {
//            if(SyncConfiguration.hasDatabaseFileFoySync() == false)
//                return new TaskResult(false, null, R.string.there_are_no_db_file);
//
//            try {
//                FileManager.copySyncDBFileToInternal(context);
//            } catch (SdNotMountedException e) {
//                e.printStackTrace();
//                return new TaskResult(false, e, R.string.sd_not_mounted);
//            } catch (IOException e) {
//
//                /////////////////////// TODO 나중에 수정 - 싱크 프로그램쪽 문제. 빈 db 파일이 있어야 싱크가 된다
//                String syncFile = SyncConfiguration.getDatabaseFileNameForSync();
//                try {
//                    FileManager.copyDbFileTo(context, syncFile);
//                } catch (SdNotMountedException e1) {
//                    e1.printStackTrace();
//                } catch (IOException e1) {
//                    e1.printStackTrace();
//                }
//
//                e.printStackTrace();
//                return new TaskResult(false, e, R.string.failed_to_copy_db_file);
//            }
//        }
//        Log.d("junseo", "복사 성공");

//        FileManager.deleteFile(SyncConfiguration.getDatabaseFileNameForSync());

        int mobildId = SyncConfiguration.getMobileNo();
        if(mobildId == -1)
            return new TaskResult(false, null, R.string.there_are_no_initialize_file);

        long inspectionSeq = SyncConfiguration.getLastInspectionSeq();
        if(inspectionSeq < 0) {
            SyncConfiguration.setLastInspectionSeq(0);
        }

        SyncConfiguration.setDataChanged(false);
        try {
            SyncConfiguration.save();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(isFirst)
            Utilities.setFalseToFirstTimeLaunch(context);

        SettingDataManager.getInstance().load(context);

        // 자동 판정룰 파일 로드
        boolean ruleLoad = AutoJudgementRuleManager.loadRuleFromXml();
        if(ruleLoad == false) {
            // TODO do something?
            Log.d("junseo", "자동 판정 룰 불러오기 실패");
        } else {
            Log.d("junseo", "자동 판정 룰 불러오기 성공");
        }

        return new TaskResult(true, null, -1);
    }

//    @Override
//    protected void onPostExecute(TaskResult result) {
//        if(taskListener != null)
//            taskListener.onTaskFinished(result);
////        activity.hideWaitingDialog();
////
////        if(result.success == false)
////            activity.showAlertDialog(result.errorMessage);
////        else {
////            UserDataLoadTask task = new UserDataLoadTask();
////            task.execute();
////        }
//
//        super.onPostExecute(result);
//    }

}
