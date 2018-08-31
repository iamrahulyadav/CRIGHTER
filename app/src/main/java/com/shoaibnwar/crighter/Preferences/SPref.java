package com.shoaibnwar.crighter.Preferences;

import android.content.SharedPreferences;

/**
 * Created by gold on 8/10/2018.
 */

public class SPref {

    public static final String PREF_USER_CRED = "usercred";

    public static final String USER_ID = "USER_ID";
    public static final String USER_FULLNAME = "USER_FULLNAME";
    public static final String USER_PHONE = "USER_PHONE";
    public static final String USER_EMAIL = "USER_EMAIL";
    public static final String USER_GENDER = "USER_GENDER";
    public static final String AUTH_FULLNAME = "AUTH_FULLNAME";
    public static final String AUTH_PHONE = "AUTH_PHONE";
    public static final String AUTH_EMAIL = "AUTH_EMAIL";
    public static final String AUTH_GENDER = "AUTH_GENDER";
    public static final String RELATION = "RELATION";
    public static final String FIREBASE_KEY = "UDID_KEY";

    public static final String FIRST_TIME = "firstTime";
    public static final String SECOND_TIME = "secondTime";
    //for audio pref
    public static final String PRE_AUDIO = "pref_audio";
    public static final String AUDIO_FILE_PATH = "file_path";
    // for image pref
    public static final String PREF_IMAGS = "image_path";
    public static final String IMAGE_PATH = "image1";
    //for service running pref
    public static final String PREF_SERVICE = "pref_service";
    public static final String is_service_rnning = "isrunning";


    public static void StoreStringPrefAll(SharedPreferences sharedPreferences, String userID, String user_fullName, String userPhone, String userEmail,
                                       String userGender, String authFullName, String authPhone, String authEmail, String authGender, String realtion) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString(USER_ID, userID);
        editor.putString(USER_FULLNAME, user_fullName);
        editor.putString(USER_PHONE, userPhone);
        editor.putString(USER_EMAIL, userEmail);
        editor.putString(USER_GENDER, userGender);
        editor.putString(AUTH_FULLNAME, authFullName);
        editor.putString(AUTH_PHONE, authPhone);
        editor.putString(AUTH_EMAIL, authEmail);
        editor.putString(AUTH_GENDER, authGender);
        editor.putString(RELATION, RELATION);
        editor.commit();

    }


    public static String getStringPref(SharedPreferences sharedPreferences,String key) {
        return sharedPreferences.getString(key, "");
    }


    public static void storingFirstTimeAndsecondTime(SharedPreferences sharedPreferences, long firstTime, long secondTime)
    {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(FIRST_TIME, firstTime);
        editor.putLong(SECOND_TIME, secondTime);
        editor.commit();
    }

    public static long getTingFirstTime(SharedPreferences sharedPreferences,  String firstTimme){

        return sharedPreferences.getLong(firstTimme, 0);
    }

    public static void storingAudioFilePath(SharedPreferences sharedPreferences, String audioFilePath){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString(AUDIO_FILE_PATH, audioFilePath);
        editor.commit();
    }

    public static void storingIMAGEFilePath(SharedPreferences sharedPreferences, String imageFilePath){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putString(IMAGE_PATH, imageFilePath);
        editor.commit();
    }

    public static void storingServiceStatus(SharedPreferences sharedPreferences, boolean serviceStatus){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.putBoolean(is_service_rnning, serviceStatus);
        editor.commit();
    }

    public static boolean getServiceStatus(SharedPreferences sharedPreferences){
       return sharedPreferences.getBoolean(is_service_rnning, false);
    }

}
