package com.framgia.ishipper.net.data;

import com.framgia.ishipper.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dinhduc on 10/08/2016.
 */
public class ShipperNearbyData {


    private List<User> users = new ArrayList<>();

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}