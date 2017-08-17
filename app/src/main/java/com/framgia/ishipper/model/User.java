package com.framgia.ishipper.model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

/**
 * Created by vuduychuong1994 on 8/3/16.
 */
public class User implements Parcelable {
    public static final String ROLE_ADMIN = "admin";
    public static final String ROLE_SHOP = "Shop";
    public static final String ROLE_SHIPPER = "Shipper";

    @SerializedName("name") private String mName;
    @SerializedName("email") private String mEmail;
    @SerializedName("address") private String mAddress;
    @SerializedName("latitude") private double mLatitude;
    @SerializedName("longitude") private double mLongitude;
    @SerializedName("phone_number") private String mPhoneNumber;
    @SerializedName("plate_number") private String mPlateNumber;
    @SerializedName("status") private String mStatus;
    @SerializedName("role") private String mRole;
    @SerializedName("rate") private double mRate;
    @SerializedName("pin") private String mPin;
    @SerializedName("authentication_token") private String mAuthenticationToken;
    @SerializedName("signed_in") private boolean mIsSignedIn;
    @SerializedName("password") private String mPassword;
    @SerializedName("id") private String mId;
    @SerializedName("user_invoice_id") private String mUserInvoiceId;
    @SerializedName("black_list_id") private String mBlackListUserId;
    @SerializedName("favorite_list_id") private String mFavoriteListId;
    @SerializedName("notificationItem") private int mNotification;
    @SerializedName("count_success_invoice") private int mCountInvoiceSuccess;
    @SerializedName("count_total_invoice") private int mCountTotalInvoice;


    public User() {}

    protected User(Parcel in) {
        mName = in.readString();
        mEmail = in.readString();
        mAddress = in.readString();
        mLatitude = in.readDouble();
        mLongitude = in.readDouble();
        mPhoneNumber = in.readString();
        mPlateNumber = in.readString();
        mStatus = in.readString();
        mRole = in.readString();
        mRate = in.readDouble();
        mPin = in.readString();
        mAuthenticationToken = in.readString();
        mIsSignedIn = in.readByte() != 0;
        mPassword = in.readString();
        mId = in.readString();
        mUserInvoiceId = in.readString();
        mBlackListUserId = in.readString();
        mNotification = in.readInt();
    }

    public String getUserType() {
        return mRole.toLowerCase();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        User user = (User) o;
        return mId.equals(user.mId);
    }

    public boolean isShop() {
        return mRole != null && mRole.equals(ROLE_SHOP);
    }

    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this);
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String email) {
        mEmail = email;
    }

    public String getAddress() {
        return mAddress;
    }

    public void setAddress(String address) {
        mAddress = address;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double latitude) {
        mLatitude = latitude;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double longitude) {
        mLongitude = longitude;
    }

    public String getPhoneNumber() {
        return mPhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    public String getPlateNumber() {
        return mPlateNumber;
    }

    public void setPlateNumber(String plateNumber) {
        mPlateNumber = plateNumber;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getRole() {
        return mRole;
    }

    public void setRole(String role) {
        mRole = role;
    }

    public double getRate() {
        return mRate;
    }

    public void setRate(double rate) {
        mRate = rate;
    }

    public String getPin() {
        return mPin;
    }

    public void setPin(String pin) {
        mPin = pin;
    }

    public String getAuthenticationToken() {
        return mAuthenticationToken;
    }

    public void setAuthenticationToken(String authenticationToken) {
        mAuthenticationToken = authenticationToken;
    }

    public boolean isSignedIn() {
        return mIsSignedIn;
    }

    public void setSignedIn(boolean signedIn) {
        mIsSignedIn = signedIn;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public String getId() {
        return mId;
    }

    public String getUserInvoiceId() {
        return mUserInvoiceId;
    }

    public String getBlackListUserId() {
        return mBlackListUserId;
    }

    public void setBlackListUserId(String blackListUserId) {
        mBlackListUserId = blackListUserId;
    }

    public int getNotification() {
        return mNotification;
    }

    public void setNotification(int notification) {
        mNotification = notification;
    }

    public int getCountInvoiceSuccess() {
        return mCountInvoiceSuccess;
    }

    public void setCountInvoiceSuccess(int countInvoiceSuccess) {
        mCountInvoiceSuccess = countInvoiceSuccess;
    }

    public int getCountTotalInvoice() {
        return mCountTotalInvoice;
    }

    public void setCountTotalInvoice(int countTotalInvoice) {
        mCountTotalInvoice = countTotalInvoice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mName);
        parcel.writeString(mEmail);
        parcel.writeString(mAddress);
        parcel.writeDouble(mLatitude);
        parcel.writeDouble(mLongitude);
        parcel.writeString(mPhoneNumber);
        parcel.writeString(mPlateNumber);
        parcel.writeString(mStatus);
        parcel.writeString(mRole);
        parcel.writeDouble(mRate);
        parcel.writeString(mPin);
        parcel.writeString(mAuthenticationToken);
        parcel.writeByte((byte) (mIsSignedIn ? 1 : 0));
        parcel.writeString(mPassword);
        parcel.writeString(mId);
        parcel.writeString(mUserInvoiceId);
        parcel.writeString(mBlackListUserId);
        parcel.writeInt(mNotification);
    }

    public String getFavoriteListId() {
        return mFavoriteListId;
    }

    public void setFavoriteListId(String favoriteListId) {
        mFavoriteListId = favoriteListId;
    }
}
