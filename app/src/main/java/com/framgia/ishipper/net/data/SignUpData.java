package com.framgia.ishipper.net.data;

import com.framgia.ishipper.model.User;
import com.google.gson.annotations.SerializedName;

/**
 * Created by dinhduc on 10/08/2016.
 */
public class SignUpData {

    @SerializedName("user") private User mUser;

    public User getUser() {
        return mUser;
    }

    public void setUser(User user) {
        mUser = user;
    }
}
