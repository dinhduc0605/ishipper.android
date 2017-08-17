package com.framgia.ishipper.model;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vuduychuong1994 on 11/22/16.
 */

public class UserSetting {
    @SerializedName("id") private String mId;
    @SerializedName("receive_notification") private boolean mIsReceiveNotification;
    @SerializedName("favorite_location") private boolean mIsFavoriteLocation;
    @SerializedName("favorite_address") private String mAddress;
    @SerializedName("favorite_latitude") private String mLatitude;
    @SerializedName("favorite_longitude") private String mLongitude;
    @SerializedName("radius_display") private int mRadiusDisplay;


    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public boolean isReceiveNotification() {
        return mIsReceiveNotification;
    }

    public void setReceiveNotification(boolean receiveNotification) {
        mIsReceiveNotification = receiveNotification;
    }

    public boolean isFavoriteLocation() {
        return mIsFavoriteLocation;
    }

    public void setFavoriteLocation(boolean favoriteLocation) {
        mIsFavoriteLocation = favoriteLocation;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public String getLatitude() {
        return mLatitude;
    }

    public void setLatitude(String latitude) {
        mLatitude = latitude;
    }

    public String getLongitude() {
        return mLongitude;
    }

    public void setLongitude(String longitude) {
        mLongitude = longitude;
    }

    public int getRadiusDisplay() {
        return mRadiusDisplay;
    }

    public void setRadiusDisplay(int radiusDisplay) {
        mRadiusDisplay = radiusDisplay;
    }
}
