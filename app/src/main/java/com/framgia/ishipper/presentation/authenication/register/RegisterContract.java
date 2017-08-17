package com.framgia.ishipper.presentation.authenication.register;

import android.widget.EditText;
import com.framgia.ishipper.model.User;

/**
 * Created by framgia on 18/11/2016.
 */

public interface RegisterContract {
    interface View {

        boolean validatePlateNumber(String plateNumber);

        boolean validatePhoneNumber(String phoneNumber);

        boolean validateName(String name);

        boolean validatePassword(String password, String confirmPassword);

        void clearError();

        void showValidatePin(String phoneNum);
    }

    interface Presenter {
        void requestRegister(
                User currentUser, String plateNumber, String phoneNumber, String name,
                String password, String passwordConfirm, String prefixPhoneNumber);
    }
}
