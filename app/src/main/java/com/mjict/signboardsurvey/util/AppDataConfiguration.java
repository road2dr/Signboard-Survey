package com.mjict.signboardsurvey.util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by Junseo on 2016-11-30.
 */
public class AppDataConfiguration {
    public static final String PROPERTY_FILE_NAME = "app_data_properties";
    public static final String RECENT_SIGN_PREFIX = "recent_sign";
    public static final String RECENT_BUILDING_PREFIX = "recent_building";
    public static final int MAX_RECENT_SIGN_COUNT = 10;
    public static final int MAX_RECENT_BUILDING_COUNT = 10;

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

    public static boolean load(Context context) {
        File propertyFile = context.getFileStreamPath(PROPERTY_FILE_NAME);

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
        File propertyFile = context.getFileStreamPath(PROPERTY_FILE_NAME);
        FileOutputStream fos = new FileOutputStream(propertyFile);

        properties.store(fos, null);
    }

    public static boolean hasPropertyFile(Context context) {
        File propertyFile = context.getFileStreamPath(PROPERTY_FILE_NAME);

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
