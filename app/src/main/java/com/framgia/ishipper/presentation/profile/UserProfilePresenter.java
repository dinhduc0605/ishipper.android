package com.framgia.ishipper.presentation.profile;

import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIDefinition;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.UpdateProfileData;

import java.util.HashMap;

/**
 * Created by HungNT on 11/22/16.
 */

public class UserProfilePresenter implements UserProfileContract.Presenter {
    private final UserProfileContract.View mView;
    private BaseActivity mActivity;

    public UserProfilePresenter(UserProfileContract.View view, BaseActivity activity) {
        mView = view;
        mActivity = activity;
    }

    @Override
    public void updatePassword(String name, String password, String phone, String plate, String address) {
        HashMap<String, String> params = new HashMap<>();
        params.put(APIDefinition.PutUpdateProfile.PARAM_NAME, name);
        params.put(APIDefinition.PutUpdateProfile.USER_CURRENT_PASSWORD, password);
        params.put(APIDefinition.PutUpdateProfile.PARAM_PHONE_NUMBER, phone);
        params.put(APIDefinition.PutUpdateProfile.PARAM_PLATE_NUMBER, plate);
        params.put(APIDefinition.PutUpdateProfile.PARAM_ADDRESS, address);

        mActivity.showDialog();
        API.putUpdateProfile(params, new API.APICallback<APIResponse<UpdateProfileData>>() {
            @Override
            public void onResponse(APIResponse<UpdateProfileData> response) {
                mActivity.dismissDialog();
                User updatedUser = response.getData().user;
                updatedUser.setAuthenticationToken(Config.getInstance().getUserInfo(mActivity.getBaseContext())
                        .getAuthenticationToken());
                Config.getInstance().setUserInfo(mActivity.getApplicationContext(), updatedUser);
                mActivity.showUserMessage(response.getMessage());
            }

            @Override
            public void onFailure(int code, String message) {
                mActivity.dismissDialog();
                mActivity.showUserMessage(message);
            }
        });
    }
}
