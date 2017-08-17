package com.framgia.ishipper.ui.listener;
import com.framgia.ishipper.model.User;

/**
 * Created by vuduychuong1994 on 11/18/16.
 */

public interface OnShipperUpdateListener {

    void onShipperOnline(User user);

    void onShipperOffline(User user);
}
