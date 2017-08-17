package com.framgia.ishipper.presentation.authenication.update_pass;

import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIDefinition;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.EmptyData;

import java.util.HashMap;

/**
 * Created by framgia on 18/11/2016.
 */

public class ResetPasswordPresenter implements ResetPasswordContract.Presenter {
    private final ResetPasswordContract.View mView;
    private BaseFragment mFragment;

    public ResetPasswordPresenter(ResetPasswordContract.View view, BaseFragment fragment) {
        mView = view;
        mFragment = fragment;
    }

    @Override
    public void requestResetPassword(String phoneNumber, String newPassword, String confirmPassword, String pin) {
        mFragment.showLoadingDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put(APIDefinition.PutResetPassword.PARAM_PHONE, phoneNumber);
        params.put(APIDefinition.PutResetPassword.PARAM_PASSWORD, newPassword);
        params.put(APIDefinition.PutResetPassword.PARAM_PASSWORD_CONFIRM, confirmPassword);
        params.put(APIDefinition.PutResetPassword.PARAM_PIN, pin);
        API.postResetPassword(params, new API.APICallback<APIResponse<EmptyData>>() {
            @Override
            public void onResponse(APIResponse<EmptyData> response) {
                mFragment.dismissLoadingDialog();
                mFragment.getActivity().finish();
                mFragment.showUserMessage(response.getMessage());
            }

            @Override
            public void onFailure(int code, String message) {
                mFragment.dismissLoadingDialog();
                mFragment.showUserMessage(message);
            }
        });
    }
}
