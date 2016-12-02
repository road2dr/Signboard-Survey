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
    private static Queue<String> recentKeywords = new LinkedList<>();

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

    public static Long[] getRecentSings() {
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

    public static Long[] getRecentBuildings() {
        Long[] ids = new Long[recentBuildings.size()];
        ids = recentBuildings.toArray(ids);

        return ids;
    }

    public static void addRecentKeyword(String keyword) {
        int max = AppDataConfiguration.MAX_RECENT_KEYWORD_COUNT;
        if(recentKeywords.size() >= max)
            recentKeywords.remove();

        recentKeywords.offer(keyword);
    }

    // 순서는 오래된 놈이 헤드 위치에
    public static String[] getRecentKeywords() {
        String[] keywords = new String[recentKeywords.size()];
        keywords = recentKeywords.toArray(keywords);

        return keywords;
    }

    public static boolean save() {
        return true;
    }

    public static boolean load(Context context) {
        boolean answer = AppDataConfiguration.load(context);
        int smax = AppDataConfiguration.MAX_RECENT_SIGN_COUNT;
        for(int i=(smax-1); i>=0; i--) {
            long id = AppDataConfiguration.getRecentSign(i);
            recentSigns.offer(id);
        }

        int bmax = AppDataConfiguration.MAX_RECENT_BUILDING_COUNT;
        for(int i=(bmax-1); i>=0; i--) {
            long id = AppDataConfiguration.getRecentBuilding(i);
            recentBuildings.offer(id);
        }

        int kmax = AppDataConfiguration.MAX_RECENT_KEYWORD_COUNT;
        for(int i=(kmax-1); i>=0; i--) {
            String keyword = AppDataConfiguration.getRecentKeyword(i);
            recentKeywords.offer(keyword);
        }

        return answer;
    }
}
