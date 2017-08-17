package com.framgia.ishipper.presentation.settings;

import android.content.Intent;

import com.google.android.gms.location.places.Place;

/**
 * Created by HungNT on 11/22/16.
 */

class SettingContact {
    interface View {
        void setFavoriteCheckbox(boolean isEnable);

        void setPlace(String name);

        void activeNotification(boolean receiveNotification);

        void setRadiusDisplay(int radiusDisplay);
    }

    interface Presenter {
        void saveSetting(boolean receiveNotification, boolean favoriteLocation, int invoiceRadius, Place mPlace);

        void saveSettingLocal(boolean receiveNotification, boolean favoriteLocation, int invoiceRadius, Place mPlace);

        void startBlacklistActivity();

        void pickFavoriteLocation(int requestCode);

        void getSetting();

        void onPickFavoritePlace(Intent data);
    }
}
