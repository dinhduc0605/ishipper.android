package com.framgia.ishipper.presentation.main;

import android.content.Intent;

import com.framgia.ishipper.model.User;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.HashMap;
import java.util.List;

/**
 * Created by HungNT on 11/28/16.
 */

public interface NearbyShipperContract {
    interface View {

        void onSearchAreaComplete(CharSequence name, LatLng latLng);

        void onAddressChange(String string);

        void addListMarker(List<User> shipperList);

        void removeListMarker(List<User> shipperList);

        Marker addMark(LatLng latLng);

        void showMapLoadingIndicator(boolean isActive);

    }

    interface Presenter {
        int getListSize();

        void getPlace(Intent place);

        void getShipperInfo(User pos);

        void startSearchPlace();

        void requestShipperNearby(LatLng latLng);

        void getAddressFromLatLng(LatLng latLng);

        void addShipper(User user, HashMap<Marker, User> shipperMap);

        void removeShipper(User user, HashMap<Marker, User> shipperMap);

        void updateCurrentLocation(User currentUser);
    }
}
