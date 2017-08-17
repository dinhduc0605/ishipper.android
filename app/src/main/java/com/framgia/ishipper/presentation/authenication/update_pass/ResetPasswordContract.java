package com.framgia.ishipper.presentation.authenication.update_pass;

/**
 * Created by framgia on 18/11/2016.
 */

public interface ResetPasswordContract {
    interface View {

    }

    interface Presenter {
        void requestResetPassword(String phoneNumber, String newPassword,
                                  String confirmPassword, String pin);
    }
}
