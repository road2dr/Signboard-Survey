package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.Sign;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2017-01-23.
 */
public class LoadSignResultStatusTask extends DefaultAsyncTask<Void, Integer, long[]> {

    private Context context;

    public ArrayList<String> lawfulSignTypes;
    public ArrayList<String> illegalSignTypes;

    public LoadSignResultStatusTask(Context c) {
        context = c;

        lawfulSignTypes = new ArrayList<>();
        illegalSignTypes = new ArrayList<>();
    }

    public void setLawfulSignTypes(String... codes) {
        lawfulSignTypes.clear();
        for(int i=0; i<codes.length; i++)
            lawfulSignTypes.add(codes[i]);
    }

    public void setIllegalSignTypes(String... codes) {
        illegalSignTypes.clear();
        for(int i=0; i<codes.length; i++)
            illegalSignTypes.add(codes[i]);
    }


    @Override
    protected long[] doInBackground(Void... params) {
        DatabaseManager dmgr = DatabaseManager.getInstance(context);

        long allSignCount = dmgr.getAllSignsCount();

        long lawfulSignCount = 0;
        long illegalSignCount = 0;

        for(int i=0; i<lawfulSignTypes.size(); i++) {
            if(this.isCancelled())
                break;

//            long n = dmgr.getSignsCountByResult(lawfulSignTypes.get(i));  // TODO ormlite 버그인가 count 연산이 제대로 안된다;;;
            List<Sign> signs = dmgr.getSignsByResult(lawfulSignTypes.get(i));
            if(signs == null)
                return null;

            int n = signs.size();
            lawfulSignCount = lawfulSignCount + n;
        }

        for(int i=0; i<illegalSignTypes.size(); i++) {
            if(this.isCancelled())
                break;

//            long n = dmgr.getSignsCountByResult(illegalSignTypes.get(i));
            List<Sign> signs = dmgr.getSignsByResult(illegalSignTypes.get(i));
            int n = signs.size();
            illegalSignCount = illegalSignCount + n;
        }

        long etcSignsCount = allSignCount - lawfulSignCount - illegalSignCount;

        long[] values = new long[3];
        values[0] = lawfulSignCount;
        values[1] = illegalSignCount;
        values[2] = etcSignsCount;

        return values;
    }
}
