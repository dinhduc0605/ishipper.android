package com.framgia.ishipper.model;

import com.framgia.ishipper.util.Const;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vuduychuong1994 on 11/9/16.
 */

public class SocketResponse {
    @SerializedName("type") private String mType;
    @SerializedName("identifier") private String mIdentifier;
    @SerializedName("message") private Message mMessage;

    public String getType() {
        return mType;
    }

    public String getAction() {
        try {
            return mMessage.mAction;
        } catch (NullPointerException e) {
            return Const.ACTION_NONE;
        }
    }

    public int getUnreadNotification() {
        try {
            return mMessage.mData.mUnreadNotifiction;
        } catch (NullPointerException e) {
            return -1;
        }
    }

    public Invoice getInvoice() {
        try {
            return mMessage.mData.mInvoice;
        } catch (NullPointerException e) {
            return null;
        }
    }

    public User getUser() {
        try {
            return mMessage.mData.mUser;
        } catch (NullPointerException e) {
            return null;
        }
    }

    private class Message {
        @SerializedName("action") private String mAction;
        @SerializedName("data") private Data mData;

    }

    private class Data {
        @SerializedName("unread_notification") private int mUnreadNotifiction;
        @SerializedName("invoice") private Invoice mInvoice;
        @SerializedName("user") private User mUser;
    }
}
