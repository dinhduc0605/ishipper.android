package com.framgia.ishipper.common;

public class Log {
    
    public static void d(String tag, String content) {
        if (Config.DEBUG) {
            android.util.Log.d(tag, content);
        }
    }

    public static void e(String tag, String content) {
        if (Config.DEBUG) {
            android.util.Log.e(tag, content);
        }
    }

    public static void i(String tag, String content) {
        if (Config.DEBUG) {
            android.util.Log.i(tag, content);
        }
    }

    public static void v(String tag, String content) {
        if (Config.DEBUG) {
            android.util.Log.v(tag, content);
        }
    }

    public static void w(String tag, String content) {
        if (Config.DEBUG) {
            android.util.Log.w(tag, content);
        }
    }
}
