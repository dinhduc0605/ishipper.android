package com.framgia.ishipper.presentation.authenication.login;

import android.widget.EditText;

/**
 * Created by framgia on 18/11/2016.
 */

public interface LoginContract {
    interface View {

    }

    interface Presenter {
        void login(String prefixPhoneNumber, EditText edtPhoneNumber, EditText edtPassword);

        void startMainActivity();

        void startForgotActivity();

        void startRegisterActivity();
    }
}
