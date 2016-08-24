package com.framgia.ishipper.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by HungNT on 8/10/16.
 */
public class StorageUtils {

    private static SharedPreferences getSharedPreferences(Context context) {
        if (context == null) {
            return null;
        }
        return context.getSharedPreferences(context.getPackageName(),
                Context.MODE_PRIVATE);
    }

    public static String getStringValue(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return "";
        }
        return preferences.getString(key, "");
    }

    public static boolean setValue(Context context, String key, String value) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public static boolean getBooleanValue(Context context, String key) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences != null && preferences.getBoolean(key, false);
    }

    public static boolean setBooleanValue(Context context, String key, boolean value) {
        SharedPreferences preferences = getSharedPreferences(context);
        if (preferences == null) {
            return false;
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        return editor.commit();
    }

    public static boolean clearAll(Context context) {
        return getSharedPreferences(context).edit().clear().commit();
    }
}
