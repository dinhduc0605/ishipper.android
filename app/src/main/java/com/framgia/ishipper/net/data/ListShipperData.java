package com.framgia.ishipper.net.data;

import com.framgia.ishipper.model.User;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HungNT on 8/24/16.
 */
public class ListShipperData {
    @SerializedName("shippers") private List<User> mShippersList = new ArrayList<>();

    public List<User> getShippersList() {
        return mShippersList;
    }
}
