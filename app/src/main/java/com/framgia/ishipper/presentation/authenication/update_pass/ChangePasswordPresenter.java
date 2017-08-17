package com.framgia.ishipper.presentation.authenication.update_pass;

import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIDefinition;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.ChangePasswordData;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by framgia on 18/11/2016.
 */

class ChangePasswordPresenter implements ChangePasswordContract.Presenter {
    private final ChangePasswordContract.View mView;
    private BaseActivity mActivity;

    ChangePasswordPresenter(ChangePasswordContract.View loginView, BaseActivity activity) {
        mView = loginView;
        mActivity = activity;
    }

    @Override
    public void changePassword(String oldPass, String newPass, String confirmPass) {
        User currentUser = Config.getInstance().getUserInfo(mActivity);
        Map<String, String> params = new HashMap<>();
        params.put(
                APIDefinition.ChangePassword.PARAM_PHONE_NUMBER,
                currentUser.getPhoneNumber()
        );
        params.put(APIDefinition.ChangePassword.PARAM_CURRENT_PASSWORD, oldPass);
        params.put(APIDefinition.ChangePassword.PARAM_PASSWORD, newPass);
        params.put(APIDefinition.ChangePassword.PARAM_PASSWORD_CONFIRMATION, confirmPass);
        mActivity.showDialog();
        API.changePassword(
                currentUser.getAuthenticationToken(),
                params,
                new API.APICallback<APIResponse<ChangePasswordData>>() {
                    @Override
                    public void onResponse(APIResponse<ChangePasswordData> response) {
                        mActivity.dismissDialog();
                        mActivity.showUserMessage(response.getMessage());
                        mActivity.finish();
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        mActivity.dismissDialog();
                        mActivity.showUserMessage(message);
                    }
                }
        );
    }
}
