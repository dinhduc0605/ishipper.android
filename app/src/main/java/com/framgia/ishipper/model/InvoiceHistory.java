package com.framgia.ishipper.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by dinhduc on 23/11/2016.
 */

public class InvoiceHistory {
    @SerializedName("status")
    private String mStatus;
    @SerializedName("time")
    private String mTime;
    @SerializedName("content")
    private String mContent;

    public InvoiceHistory(String time, String content) {
        mTime = time;
        mContent = content;
    }

    public String getStatus() {
        return mStatus;
    }

    public void setStatus(String status) {
        mStatus = status;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
