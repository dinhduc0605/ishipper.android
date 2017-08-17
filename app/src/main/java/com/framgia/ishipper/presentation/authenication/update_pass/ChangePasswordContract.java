package com.framgia.ishipper.presentation.authenication.update_pass;

/**
 * Created by framgia on 18/11/2016.
 */

public interface ChangePasswordContract {
    interface View {

    }

    interface Presenter {
        void changePassword(String oldPass, String newPass, String confirmPass);
    }
}
