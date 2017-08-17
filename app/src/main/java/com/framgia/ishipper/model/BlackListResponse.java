package com.framgia.ishipper.model;

import com.google.gson.annotations.SerializedName;

public class BlackListResponse {
    @SerializedName("black_list_user_id") private int mBlacklistUserId;
    @SerializedName("id") private int mBlacklistId;
    @SerializedName("owner_id") private int mOwnerId;

    public int getBlacklistUserId() {
        return mBlacklistUserId;
    }

    public int getOwnerId() {
        return mOwnerId;
    }

    public int getBlacklistId() {
        return mBlacklistId;
    }
}
