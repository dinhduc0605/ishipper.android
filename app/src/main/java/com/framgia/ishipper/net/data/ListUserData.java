package com.framgia.ishipper.net.data;
import com.framgia.ishipper.model.User;
import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by vuduychuong1994 on 9/20/16.
 */
public class ListUserData {
    @SerializedName("users") private List<User> mShippersList = new ArrayList<>();

    public List<User> getShippersList() {
        return mShippersList;
    }
}
