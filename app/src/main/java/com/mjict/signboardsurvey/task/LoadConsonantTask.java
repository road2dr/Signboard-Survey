package com.mjict.signboardsurvey.task;

import android.content.Context;

import com.mjict.signboardsurvey.database.DatabaseManager;
import com.mjict.signboardsurvey.model.StreetAddress;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Junseo on 2016-08-02.
 */
public class LoadConsonantTask extends DefaultAsyncTask<String, Integer, String[]> {

    private Context context;

    public LoadConsonantTask(Context c) {
        context = c;
    }

    @Override
    protected String[] doInBackground(String... params) {
        if(params == null)
            return null;

        String town = params[0];
        DatabaseManager dmgr = DatabaseManager.getInstance(context);

        List<StreetAddress> addrs = dmgr.getStreetAddress(town);
        InitalConsonantSet set = new InitalConsonantSet();
        int n = addrs.size();
        for(int i=0; i<n; i++) {
            set.put(addrs.get(i));
        }

        return set.getInitialConsonantStrings();
    }


    private class InitalConsonantSet {
        ArrayList<StreetAddress> list;

        public InitalConsonantSet() {
            list = new ArrayList<>();
        }

        public void put(StreetAddress addr) {
            int n = list.size();
            boolean isExist = false;
            for(int i=0; i<n; i++) {
                StreetAddress a = list.get(i);
                if(a.getInitialConsonant().equals(addr.getInitialConsonant())) {
                    isExist = true;
                    break;
                }
            }
            if(isExist == false)
                list.add(addr);
        }

        public String[] getInitialConsonantStrings() {
            int n = list.size();
            String[] addr = new String[n];
            for(int i=0; i<n; i++)
                addr[i] = list.get(i).getInitialConsonant();

            return addr;
        }
    }

}
