package com.mjict.signboardsurvey;

import android.content.Context;

import com.mjict.signboardsurvey.model.User;
import com.mjict.signboardsurvey.util.AppDataConfiguration;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by Junseo on 2016-11-30.
 * 앱 전체에 걸쳐 두루 사용되는 설정이나 데이터를 포함한 클래스.
 * 예로들면 파일 경로 정보, gps 데이터, 현재 사용자, 최근 조사한 간판 등등..
 *
 */
public class MJContext {
    public static final String DAUM_MAP_ANDROID_API_KEY = "193832e0d69608c39dc15a455aae9680";


    private static User currentUser;
    private static int deviceNumber = 25;   // TODO 나중에 바꿔

    private static Queue<Long> recentSigns = new LinkedList<>();
    private static Queue<Long> recentBuildings = new LinkedList<>();

    public static User getCurrentUser() {
        return currentUser;
    }

    // TODO 로그아웃 버튼 누르거나 로그인 화면 으로 돌아오거나 종료 누르면 null 하도록 해야겠지
    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static int getDeviceNumber() {
        return deviceNumber;
    }

    public static void setDeviceNumber(int number) {
        deviceNumber = number;
    }

    public static void addRecentSing(long id) {
        int max = AppDataConfiguration.MAX_RECENT_SIGN_COUNT;
        if(recentSigns.size() >= max)
            recentSigns.remove();

        recentSigns.offer(id);
    }

    public Long[] getRecentSings() {
        Long[] ids = new Long[recentSigns.size()];
        ids = recentSigns.toArray(ids);

        return ids;
    }

    public static void addRecentBuilding(long id) {
        int max = AppDataConfiguration.MAX_RECENT_BUILDING_COUNT;
        if(recentBuildings.size() >= max)
            recentBuildings.remove();

        recentBuildings.offer(id);
    }

    public Long[] getRecentBuildings() {
        Long[] ids = new Long[recentBuildings.size()];
        ids = recentBuildings.toArray(ids);

        return ids;
    }

    public static boolean save() {
        return true;
    }

    public static boolean load(Context context) {
        boolean answer = AppDataConfiguration.load(context);
        int smax = AppDataConfiguration.MAX_RECENT_SIGN_COUNT;
        for(int i=0; i<smax; i++) {
            long id = AppDataConfiguration.getRecentSign(i);
            recentSigns.offer(id);
        }

        int bmax = AppDataConfiguration.MAX_RECENT_BUILDING_COUNT;
        for(int i=0; i<bmax; i++) {
            long id = AppDataConfiguration.getRecentBuilding(i);
            recentBuildings.offer(id);
        }

        return answer;
    }
}
