package com.framgia.ishipper.net.data;

import com.framgia.ishipper.model.User;
import com.google.gson.annotations.SerializedName;

public class AddFavoriteListData {

    @SerializedName("user") private User mUser;

    public User getUser() {
        return mUser;
    }
}
