package com.framgia.ishipper.presentation.authenication.update_pass;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by HungNT on 8/3/16.
 */
public class ResetPasswordNewFragment extends BaseFragment implements ResetPasswordContract.View {

    private static final String BUNDLE_PHONE = "BUNDLE_PHONE";
    private static final String BUNDLE_PIN = "BUNDLE_PIN";
    @BindView(R.id.edt_new_password) EditText mEdtNewPassword;
    @BindView(R.id.edt_password_again) EditText mEdtPasswordAgain;

    private String mPhoneNumber;
    private String mPin;
    private ResetPasswordPresenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_reset_password_new;
    }

    @Override
    public void initViews() {
        mPresenter = new ResetPasswordPresenter(this, this);
        mPhoneNumber = getArguments().getString(BUNDLE_PHONE);
        mPin = getArguments().getString(BUNDLE_PIN);
    }

    @OnClick({R.id.btnDone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDone:
                mPresenter.requestResetPassword(mPhoneNumber, mEdtNewPassword.getText().toString(),
                        mEdtPasswordAgain.getText().toString(), mPin);
                break;
        }
    }

    public static ResetPasswordNewFragment newInstance(String phoneNumber, String pin) {
        Bundle args = new Bundle();
        args.putString(BUNDLE_PHONE, phoneNumber);
        args.putString(BUNDLE_PIN, pin);
        ResetPasswordNewFragment fragment = new ResetPasswordNewFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
