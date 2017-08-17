package com.framgia.ishipper.net.data;

import com.framgia.ishipper.model.BlackListResponse;
import com.framgia.ishipper.model.User;
import com.google.gson.annotations.SerializedName;

public class AddBlacklistData {

    @SerializedName("user") private User mUser;

    public User getUser() {
        return mUser;
    }
}
