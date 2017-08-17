package com.framgia.ishipper.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.framgia.ishipper.util.CommonUtils;
import com.framgia.ishipper.util.TextFormatUtils;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by vuduychuong1994 on 8/9/16.
 */
public class Invoice implements Parcelable {
    public static final String STATUS_INIT = "init";
    public static final String STATUS_WAITING = "waiting";
    public static final String STATUS_SHIPPING = "shipping";
    public static final String STATUS_SHIPPED = "shipped";
    public static final String STATUS_FINISHED = "finished";
    public static final String STATUS_CANCEL = "cancel";
    public static final String STATUS_ALL = "all";
    public static final int STATUS_CODE_INIT = 0;
    public static final int STATUS_CODE_WAITING = 1;
    public static final int STATUS_CODE_SHIPPING = 2;
    public static final int STATUS_CODE_SHIPPED = 3;
    public static final int STATUS_CODE_FINISHED = 4;
    public static final int STATUS_CODE_CANCEL = 5;
    public static final int STATUS_CODE_ALL = 6;
    public static final int INVALID_USER_INVOICE = 0;

    @SerializedName("id") private int mId;
    @SerializedName("name") private String mName;
    @SerializedName("address_start") private String mAddressStart;
    @SerializedName("latitude_start") private double mLatStart;
    @SerializedName("longitude_start") private double mLngStart;
    @SerializedName("address_finish") private String mAddressFinish;
    @SerializedName("latitude_finish") private double mLatFinish;
    @SerializedName("longitude_finish") private double mLngFinish;
    @SerializedName("delivery_time") private String mDeliveryTime;
    @SerializedName("distance_invoice") private double mDistance;
    @SerializedName("description") private String mDescription;
    @SerializedName("price") private double mPrice;
    @SerializedName("shipping_price") private double mShippingPrice;
    @SerializedName("status") private String mStatus;
    @SerializedName("weight") private double mWeight;
    @SerializedName("user") private User mUser;
    @SerializedName("customer_name") private String mCustomerName;
    @SerializedName("customer_number") private String mCustomerNumber;
    @SerializedName("received") private boolean mReceived;
    @SerializedName("number_of_recipients") private int mNumOfRecipient;
    @SerializedName("user_invoice_id") private int mUserInvoiceId;
    @SerializedName("status_histories") private ArrayList<InvoiceHistory> mHistories;

    public Invoice() {

    }

    public String toJson() {
        return new Gson().toJson(this);
    }

    public int getStatusCode() {
        if (mStatus.equals(Invoice.STATUS_INIT)) return STATUS_CODE_INIT;
        if (mStatus.equals(Invoice.STATUS_WAITING)) return STATUS_CODE_WAITING;
        if (mStatus.equals(Invoice.STATUS_SHIPPING)) return STATUS_CODE_SHIPPING;
        if (mStatus.equals(Invoice.STATUS_SHIPPED)) return STATUS_CODE_SHIPPED;
        if (mStatus.equals(Invoice.STATUS_FINISHED)) return STATUS_CODE_FINISHED;
        if (mStatus.equals(Invoice.STATUS_CANCEL)) return STATUS_CODE_CANCEL;
        if (mStatus.equals(Invoice.STATUS_ALL)) return STATUS_CODE_ALL;
        return -1;
    }

    public static String getStatusFromCode(int code) {
        if (code == Invoice.STATUS_CODE_INIT) return Invoice.STATUS_INIT;
        if (code == Invoice.STATUS_CODE_WAITING) return Invoice.STATUS_WAITING;
        if (code == Invoice.STATUS_CODE_SHIPPING) return Invoice.STATUS_SHIPPING;
        if (code == Invoice.STATUS_CODE_SHIPPED) return Invoice.STATUS_SHIPPED;
        if (code == Invoice.STATUS_CODE_FINISHED) return Invoice.STATUS_FINISHED;
        if (code == Invoice.STATUS_CODE_CANCEL) return Invoice.STATUS_CANCEL;
        if (code == Invoice.STATUS_CODE_ALL) return Invoice.STATUS_ALL;
        return "";
    }

    public int getUserInvoiceId() {
        return mUserInvoiceId;
    }

    public int getId() {
        return mId;
    }

    public String getStringId() {
        return String.valueOf(mId);
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getAddressStart() {
        return mAddressStart;
    }

    public void setAddressStart(String addressStart) {
        mAddressStart = addressStart;
    }

    public double getLatStart() {
        return mLatStart;
    }

    public void setLatStart(double latStart) {
        mLatStart = latStart;
    }

    public double getLngStart() {
        return mLngStart;
    }

    public void setLngStart(double lngStart) {
        mLngStart = lngStart;
    }

    public String getAddressFinish() {
        return mAddressFinish;
    }

    public void setAddressFinish(String addressFinish) {
        mAddressFinish = addressFinish;
    }

    public double getLatFinish() {
        return mLatFinish;
    }

    public void setLatFinish(double latFinish) {
        mLatFinish = latFinish;
    }

    public double getLngFinish() {
        return mLngFinish;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }

    public void setLngFinish(double lngFinish) {
        mLngFinish = lngFinish;
    }

    public String getDeliveryTime() {
        return mDeliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        mDeliveryTime = deliveryTime;
    }

    public double getDistance() {
        return mDistance;
    }

    public void setDistance(double distance) {
        mDistance = distance;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public double getPrice() {
        return mPrice;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public double getShippingPrice() {
        return mShippingPrice;
    }

    public void setShippingPrice(double shippingPrice) {
        mShippingPrice = shippingPrice;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public double getWeight() {
        return mWeight;
    }

    public void setWeight(double weight) {
        mWeight = weight;
    }

    public String getCustomerName() {
        return mCustomerName;
    }

    public void setCustomerName(String customerName) {
        mCustomerName = customerName;
    }

    public String getCustomerNumber() {
        return mCustomerNumber;
    }

    public void setCustomerNumber(String customerNumber) {
        mCustomerNumber = customerNumber;
    }

    public String getInvoiceDesc() {
        if (!CommonUtils.stringIsValid(mDescription)) mDescription = "";
        return String.format("Ship +%s, Thu %s\n%s", TextFormatUtils.formatPrice(mShippingPrice),
                TextFormatUtils.formatPrice(mPrice), mDescription);
    }

    public boolean isReceived() {
        return mUserInvoiceId != INVALID_USER_INVOICE;
    }

    public int getNumOfRecipient() {
        return mNumOfRecipient;
    }

    public void setNumOfRecipient(int numOfRecipient) {
        mNumOfRecipient = numOfRecipient;
    }

    public ArrayList<InvoiceHistory> getHistories() {
        return mHistories;
    }

    public void setHistories(ArrayList<InvoiceHistory> histories) {
        mHistories = histories;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(mId);
        parcel.writeString(mName);
        parcel.writeString(mAddressStart);
        parcel.writeString(mAddressFinish);
        parcel.writeDouble(mLatStart);
        parcel.writeDouble(mLngStart);
        parcel.writeDouble(mLatFinish);
        parcel.writeDouble(mLngFinish);
        parcel.writeDouble(mPrice);
        parcel.writeDouble(mShippingPrice);
        parcel.writeString(mStatus);
        parcel.writeString(mDeliveryTime);
        parcel.writeDouble(mDistance);
        parcel.writeString(mDescription);
        parcel.writeDouble(mWeight);
        parcel.writeString(mCustomerName);
        parcel.writeString(mCustomerNumber);
        parcel.writeByte((byte) (mReceived ? 1 : 0));
        parcel.writeInt(mNumOfRecipient);
    }

    protected Invoice(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mAddressStart = in.readString();
        mAddressFinish = in.readString();
        mLatStart = in.readDouble();
        mLngStart = in.readDouble();
        mLatFinish = in.readDouble();
        mLngFinish = in.readDouble();
        mPrice = in.readDouble();
        mShippingPrice = in.readDouble();
        mStatus = in.readString();
        mDeliveryTime = in.readString();
        mDistance = in.readDouble();
        mDescription = in.readString();
        mWeight = in.readDouble();
        mCustomerName = in.readString();
        mCustomerNumber = in.readString();
        mReceived = in.readByte() != 0;
        mNumOfRecipient = in.readInt();
    }

    public static final Creator<Invoice> CREATOR = new Creator<Invoice>() {
        @Override
        public Invoice createFromParcel(Parcel in) {
            return new Invoice(in);
        }

        @Override
        public Invoice[] newArray(int size) {
            return new Invoice[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        if (o instanceof Invoice) {
            return ((Invoice) o).getId() == getId();
        }
        return super.equals(o);
    }

    public void setUserInvoiceId(int userInvoiceId) {
        mUserInvoiceId = userInvoiceId;
    }
}
