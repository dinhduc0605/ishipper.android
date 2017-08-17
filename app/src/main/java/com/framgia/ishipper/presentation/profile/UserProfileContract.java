package com.framgia.ishipper.presentation.profile;

/**
 * Created by HungNT on 11/22/16.
 */

public class UserProfileContract {
    interface View {
    }

    interface Presenter {
        void updatePassword(String name, String password, String phone, String plate, String address);
    }
}
