package com.framgia.ishipper.presentation.authenication.update_pass;

/**
 * Created by framgia on 18/11/2016.
 */

public interface ForgetPasswordContract {
    interface View {

    }

    interface Presenter {
        void requestForgotPassword(String phoneNumber);
    }
}
