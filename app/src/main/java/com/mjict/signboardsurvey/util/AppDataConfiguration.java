package com.mjict.signboardsurvey.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * Created by Junseo on 2016-11-30.
 */
public class AppDataConfiguration {
    public static final String PROPERTY_FILE_NAME = "app_data_properties";
    public static final String RECENT_SIGN_PREFIX = "recent_sign";
    public static final String RECENT_BUILDING_PREFIX = "recent_building";
    public static final String RECENT_KEYWORD = "recent_keyword";
    public static final int MAX_RECENT_SIGN_COUNT = 10;
    public static final int MAX_RECENT_BUILDING_COUNT = 10;
    public static final int MAX_RECENT_KEYWORD_COUNT = 10;

    private static Properties properties = new Properties();

    public static long getRecentSign(int order) {
        String name = RECENT_SIGN_PREFIX+order;

        String value = properties.getProperty(name);
        long sign = -1;
        try {
            sign = Long.parseLong(value);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return sign;
    }

    public static long getRecentBuilding(int order) {
        String name = RECENT_BUILDING_PREFIX+order;

        String value = properties.getProperty(name);
        long sign = -1;
        try {
            sign = Long.parseLong(value);
        }catch (Exception e) {
            e.printStackTrace();
        }

        return sign;
    }

    public static String getRecentKeyword(int order) {
        String name = RECENT_KEYWORD+order;

        String value = null;
        try {
            value = new String(properties.getProperty(name).getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            value = null;
        }

        return value;
    }

    public static boolean load(Context context) {
//        File propertyFile = context.getFileStreamPath(PROPERTY_FILE_NAME);
        // TODO 나중에 파일 경로 내부로 변경 =>
        String dir = SyncConfiguration.getBaseDirectory();
        String propertyFilePath = dir+PROPERTY_FILE_NAME;
        File propertyFile = new File(propertyFilePath);

        if(propertyFile.exists() == false)
            return false;

        try {
            FileInputStream fis = new FileInputStream(propertyFile);
            properties.load(fis);
            fis.close();
        }catch(IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static void save(Context context) throws IOException {
        // TODO 나중에 파일 경로 내부로 변경 =>
//        File propertyFile = context.getFileStreamPath(PROPERTY_FILE_NAME);
        String dir = SyncConfiguration.getBaseDirectory();
        String propertyFilePath = dir+PROPERTY_FILE_NAME;
        File propertyFile = new File(propertyFilePath);
        FileOutputStream fos = new FileOutputStream(propertyFile);

        properties.store(fos, null);
    }

    public static boolean hasPropertyFile(Context context) {
        // TODO 나중에 파일 경로 내부로 변경 =>
//        File propertyFile = context.getFileStreamPath(PROPERTY_FILE_NAME);
        String dir = SyncConfiguration.getBaseDirectory();
        String propertyFilePath = dir+PROPERTY_FILE_NAME;
        File propertyFile = new File(propertyFilePath);

        return propertyFile.exists();
    }

    public static boolean makePropertyFile(Context context) {
        try {
            save(context);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
