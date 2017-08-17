package com.framgia.ishipper.presentation.authenication.update_pass;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.EditText;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseToolbarActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class ChangePasswordActivity extends BaseToolbarActivity implements ChangePasswordContract.View {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.edt_new_password) EditText mEdtNewPassword;
    @BindView(R.id.edt_confirm_password) EditText mEdtConfirmPassword;
    @BindView(R.id.btn_change_password) Button mBtnChangePassword;
    @BindView(R.id.edt_old_password) EditText mEdtOldPassword;

    private ChangePasswordPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public int getActivityTitle() {
        return R.string.title_activity_change_password;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_change_password;
    }

    @OnClick(R.id.btn_change_password)
    public void onClick() {
        mPresenter.changePassword(
                mEdtOldPassword.getText().toString(),
                mEdtNewPassword.getText().toString(),
                mEdtConfirmPassword.getText().toString()
        );
    }

    @Override
    public void initViews() {
        mPresenter = new ChangePasswordPresenter(this, this);
    }
}
