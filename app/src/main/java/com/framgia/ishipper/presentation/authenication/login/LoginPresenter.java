package com.framgia.ishipper.presentation.authenication.login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.EditText;
import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIDefinition;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.EmptyData;
import com.framgia.ishipper.net.data.SignInData;
import com.framgia.ishipper.presentation.authenication.register.RegisterActivity;
import com.framgia.ishipper.presentation.authenication.update_pass.ForgetPasswordActivity;
import com.framgia.ishipper.presentation.main.MainActivity;
import com.framgia.ishipper.util.InputValidate;
import com.google.firebase.iid.FirebaseInstanceId;
import java.util.HashMap;

/**
 * Created by framgia on 18/11/2016.
 */

class LoginPresenter implements LoginContract.Presenter {
    private final LoginContract.View mLoginView;
    private BaseActivity mLoginActivity;

    LoginPresenter(@NonNull LoginContract.View loginView, BaseActivity loginActivity) {
        mLoginView = loginView;
        mLoginActivity = loginActivity;
    }

    @Override
    public void login(String prefixPhoneNumber, EditText edtPhoneNumber, EditText edtPassword) {
        String phoneNumber = prefixPhoneNumber + edtPhoneNumber.getText().toString();
        if (!InputValidate.checkPhoneNumber(edtPhoneNumber, mLoginActivity)) {
            return;
        }
        if (!InputValidate.checkPassword(edtPassword, mLoginActivity)) {
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put(APIDefinition.SignIn.PARAM_PHONE_NUMBER, phoneNumber);
        params.put(APIDefinition.SignIn.PARAM_PASSWORD, edtPassword.getText().toString());
        params.put(APIDefinition.SignIn.PARAM_REGISTRATION_ID,
                FirebaseInstanceId.getInstance().getToken());

        mLoginActivity.showDialog();
        API.signIn(params, new API.APICallback<APIResponse<SignInData>>() {
            @Override
            public void onResponse(APIResponse<SignInData> response) {
                mLoginActivity.dismissDialog();
                mLoginActivity.showUserMessage(response.getMessage());
                User user = response.getData().getUser();
                Config.getInstance().setUserInfo(mLoginActivity.getApplicationContext(), user);
                startMainActivity();
            }

            @Override
            public void onFailure(int code, String message) {
                mLoginActivity.dismissDialog();
                mLoginActivity.showUserMessage(message);
            }
        });
    }

    @Override
    public void startMainActivity() {
        mLoginActivity.startActivity(new Intent(mLoginActivity, MainActivity.class));
        mLoginActivity.finish();
    }

    @Override
    public void startForgotActivity() {
        mLoginActivity.startActivity(new Intent(mLoginActivity, ForgetPasswordActivity.class));
    }

    @Override
    public void startRegisterActivity() {
        mLoginActivity.startActivity(new Intent(mLoginActivity, RegisterActivity.class));
    }
}
