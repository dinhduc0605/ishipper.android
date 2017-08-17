package com.framgia.ishipper.presentation.authenication.register;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIDefinition;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.SignUpData;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by framgia on 18/11/2016.
 */

public class RegisterPresenter implements RegisterContract.Presenter {
    private final RegisterContract.View mView;
    private BaseActivity mActivity;

    public RegisterPresenter(RegisterContract.View view, BaseActivity activity) {
        mView = view;
        mActivity = activity;
    }

    @Override
    public void requestRegister(
            User currentUser, String plateNumber, String phoneNumber, String name, String password,
            String passwordConfirm, String prefixPhoneNumber) {
        if (!mView.validatePhoneNumber(phoneNumber) ||
            !mView.validatePassword(password, passwordConfirm) ||
            !mView.validatePlateNumber(plateNumber) ||
            !mView.validateName(name)) {
            return;
        }

        currentUser.setPhoneNumber(prefixPhoneNumber + phoneNumber);
        currentUser.setPassword(password);
        currentUser.setName(name);
        currentUser.setPlateNumber(plateNumber);
        Map<String, String> userParams = new HashMap<>();
        userParams.put(APIDefinition.RegisterUser.PARAM_USER_PHONE_NUMBER,
                       currentUser.getPhoneNumber());
        userParams.put(APIDefinition.RegisterUser.PARAM_USER_PASSWORD, currentUser.getPassword());
        userParams.put(APIDefinition.RegisterUser.PARAM_USER_PASSWORD_CONFIRMATION,
                       currentUser.getPassword());
        userParams.put(APIDefinition.RegisterUser.PARAM_USER_NAME, currentUser.getName());
        userParams.put(APIDefinition.RegisterUser.PARAM_USER_ROLE, currentUser.getRole().toLowerCase());
        userParams.put(APIDefinition.RegisterUser.PARAM_USER_PLATE_NUMBER,
                       currentUser.getPlateNumber());

        final String phoneNum = currentUser.getPhoneNumber();
        mActivity.showDialog();
        API.signUp(userParams, new API.APICallback<APIResponse<SignUpData>>() {
            @Override
            public void onResponse(APIResponse<SignUpData> response) {
                mActivity.dismissDialog();
                Config.getInstance().setUserInfo(mActivity.getBaseContext(),
                                                 response.getData().getUser());
                mView.showValidatePin(phoneNum);

            }

            @Override
            public void onFailure(int code, String message) {
                mActivity.dismissDialog();
                mActivity.showUserMessage(message);
            }
        });
    }
}
