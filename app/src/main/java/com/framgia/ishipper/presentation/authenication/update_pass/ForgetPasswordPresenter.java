package com.framgia.ishipper.presentation.authenication.update_pass;

import android.text.TextUtils;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.EmptyData;
import com.framgia.ishipper.presentation.authenication.register.ValidatePinFragment;
import com.framgia.ishipper.util.Const;

/**
 * Created by framgia on 18/11/2016.
 */

public class ForgetPasswordPresenter implements ForgetPasswordContract.Presenter {
    private final ForgetPasswordContract.View mView;
    private BaseActivity mActivity;

    public ForgetPasswordPresenter(ForgetPasswordContract.View view, BaseActivity activity) {
        mView = view;
        mActivity = activity;
    }

    @Override
    public void requestForgotPassword(String phoneNumber) {
        if (TextUtils.isEmpty(phoneNumber)) {
            mActivity.showUserMessage(R.string.error_empty_string);
            return;
        }

        final String phone = Const.VIETNAM_PREFIX + phoneNumber;
        mActivity.showDialog();
        API.getConfirmationPin(phone, new API.APICallback<APIResponse<EmptyData>>() {
                    @Override
                    public void onResponse(APIResponse<EmptyData> response) {
                        mActivity.dismissDialog();
                        mActivity.getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.container,
                                        ValidatePinFragment.newInstance(phone,
                                                ValidatePinFragment.ACTION_FORGOT_PASSWORD))
                                .addToBackStack(null)
                                .commit();
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
