package com.framgia.ishipper.common;

import android.content.Context;
import android.text.TextUtils;

import com.framgia.ishipper.model.User;
import com.framgia.ishipper.util.Const.Storage;
import com.framgia.ishipper.util.StorageUtils;
import com.google.gson.Gson;

/**
 * Created by HungNT on 7/21/16.
 */
public class Config {
    public static final boolean DEBUG = true; // Develop-only-purpose, turn off it for released production
    public static final boolean IS_DEV = true;

    private static Config sInstance;

    public static Config getInstance() {
        if (sInstance == null) {
            sInstance = new Config();
        }
        return sInstance;
    }

    // User info config
    private User mUserInfo;

    /**
     * Get user information in share-preference
     * @param context context
     * @return current user
     */
    public User getUserInfo(Context context) {
        if (mUserInfo == null) {
            restoreUserInfoSetting(context);
        }
        return mUserInfo;
    }

    public boolean isShop(Context context) {
        return getUserInfo(context).isShop();
    }

    public void restoreUserInfoSetting(Context context) {
        if (context == null) {
            return;
        }

        String savedUserInfoJson = StorageUtils.getStringValue(context, Storage.KEY_USER_INFO);

        if (TextUtils.isEmpty(savedUserInfoJson)) {
            return;
        }

        mUserInfo = new Gson().fromJson(savedUserInfoJson, User.class);
    }

    /**
     * Check if user is logging or not
     * @param context context
     * @return true if user is logging, false is not
     */
    public boolean isLogin(Context context) {
        if (context == null) {
            return false;
        }

        return StorageUtils.getBooleanValue(context, Storage.KEY_IS_LOGIN) && getUserInfo(context) != null;
    }

    /**
     * save logged in user information to share-preference
     * @param context context
     * @param userInfo user
     * @return true if save info successfully
     */
    public synchronized boolean setUserInfo(Context context, User userInfo) {
        if (context == null || userInfo == null) {
            return false;
        }

        mUserInfo = userInfo;

        String userInfoJson = new Gson().toJson(userInfo);
        StorageUtils.setValue(context, Storage.KEY_IS_LOGIN, true);
        return StorageUtils.setValue(context, Storage.KEY_USER_INFO, userInfoJson);
    }
}
