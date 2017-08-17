package com.framgia.ishipper.presentation.authenication.update_pass;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseToolbarActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by HungNT on 8/3/16.
 */
public class ForgetPasswordActivity extends BaseToolbarActivity
        implements ForgetPasswordContract.View {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.edtPhoneNumber) EditText mEdtPhoneNumber;

    private ForgetPasswordPresenter mPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public int getActivityTitle() {
        return R.string.forgot_pass;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_forget_password;
    }

    @OnClick(R.id.btnDone)
    public void onClick() {
        mPresenter.requestForgotPassword(mEdtPhoneNumber.getText().toString());
    }

    @Override
    public void initViews() {
        mPresenter = new ForgetPasswordPresenter(this, this);
    }
}
