package com.framgia.ishipper.net.data;

import com.framgia.ishipper.model.Notification;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dinhduc on 27/10/2016.
 */

public class ListNotificationData {
    @SerializedName("notifications")
    private List<Notification> mNotifications = new ArrayList<>();
    @SerializedName("unread")
    private int mUnread;

    public List<Notification> getNotifications() {
        return mNotifications;
    }

    public void setNotifications(List<Notification> notifications) {
        mNotifications = notifications;
    }

    public int getUnread() {
        return mUnread;
    }
}
