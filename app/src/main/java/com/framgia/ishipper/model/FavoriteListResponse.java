package com.framgia.ishipper.model;

import com.google.gson.annotations.SerializedName;

public class FavoriteListResponse {
    @SerializedName("favorite_list_user_id") private int mFavoritelistUserId;
    @SerializedName("id") private int mFavoritelistId;

    public int getFavoritelistUserId() {
        return mFavoritelistUserId;
    }

    public int getFavoritelistId() {
        return mFavoritelistId;
    }
}
