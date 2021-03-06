package com.umb.cs682.projectlupus.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SharedPreferenceManager {
    private static String PREFS_FILE = "App_Prefs";
    private static SharedPreferences prefs;
    private static Context context;// = LupusMate.getAppContext();

    public static void setContext(Context newContext){
        context = newContext;
    }

    //todo remove when done
    public static void initPrefs(){
        prefs = context.getSharedPreferences(PREFS_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.remove(Constants.IS_FIRST_RUN);
        editor.remove(Constants.DONE_SHARED_DATA);
        editor.putBoolean(Constants.ACTIVITY_SENSE_SETTING,false);
        //editor.putBoolean(Constants.SHARE_DATA_SETTING,false);
        editor.commit();
    }

    public static boolean isFirstRun(){
        prefs = context.getSharedPreferences(PREFS_FILE,0);
        return prefs.getBoolean(Constants.IS_FIRST_RUN, true);
    }

    public static boolean contains(String prefKey){
        prefs = context.getSharedPreferences(PREFS_FILE, 0);
        return prefs.contains(prefKey);
    }

    public static void setBooleanPref(String TAG, String prefKey, boolean prefValue){
        boolean isSuccess = false;
        prefs = context.getSharedPreferences(PREFS_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(prefKey, prefValue);
        isSuccess = editor.commit();
        if(isSuccess) {
            Log.d(TAG, Boolean.toString(getBooleanPref(prefKey)));
        }
    }

    public static boolean getBooleanPref(String prefKey){
        prefs = context.getSharedPreferences(PREFS_FILE, 0);
        return prefs.getBoolean(prefKey, false);
    }

    public static void setIntPref(String TAG, String prefKey, int prefValue){
        boolean isSuccess = false;
        prefs = context.getSharedPreferences(PREFS_FILE, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt(prefKey, prefValue);
        isSuccess = editor.commit();
        if(isSuccess) {
            Log.d(TAG, Integer.toString(getIntPref(prefKey)));
        }
    }

    public static int getIntPref(String prefKey){
        prefs = context.getSharedPreferences(PREFS_FILE, 0);
        return prefs.getInt(prefKey, -1);
    }
}
