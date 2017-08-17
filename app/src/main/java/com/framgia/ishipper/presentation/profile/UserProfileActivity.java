package com.framgia.ishipper.presentation.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseToolbarActivity;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.presentation.authenication.update_pass.ChangePasswordActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class UserProfileActivity extends BaseToolbarActivity implements UserProfileContract.View {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.edt_profile_name) TextInputEditText mEdtProfileName;
    @BindView(R.id.edt_profile_plate) TextInputEditText mEdtProfilePlate;
    @BindView(R.id.edt_profile_phone) TextInputEditText mEdtProfilePhone;
    @BindView(R.id.edt_profile_address) TextInputEditText mEdtProfileAddress;
    @BindView(R.id.tv_count_total_invoice) TextView mTvCountTotalInvoice;
    @BindView(R.id.tv_count_success_invoice) TextView mTvCountSuccessInvoice;
    private TextView mEdtProfilePassword;
    private AlertDialog mInputPasswordDialog;
    private UserProfilePresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new UserProfilePresenter(this, this);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public int getActivityTitle() {
        return R.string.title_activity_user_profile;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_user_profile;
    }

    @OnClick({R.id.tv_change_password, R.id.btn_profile_update})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_change_password:
                startActivity(new Intent(this, ChangePasswordActivity.class));
                break;
            case R.id.btn_profile_update:
                showPasswordDialog();
                break;
        }
    }

    private void showPasswordDialog() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_input_password, null);
        mEdtProfilePassword = (TextView) view.findViewById(R.id.edt_profile_password);
        View btnPasswordOk = view.findViewById(R.id.dialog_password_ok);
        View btnPasswordCancel = view.findViewById(R.id.dialog_password_cancel);
        btnPasswordOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.updatePassword(
                        mEdtProfileName.getText().toString(),
                        mEdtProfilePassword.getText().toString(),
                        mEdtProfilePhone.getText().toString(),
                        mEdtProfilePlate.getText().toString(),
                        mEdtProfileAddress.getText().toString()
                );
                mInputPasswordDialog.cancel();
            }
        });
        btnPasswordCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mInputPasswordDialog.cancel();
            }
        });
        mInputPasswordDialog = new AlertDialog.Builder(this)
                .setView(view)
                .setCancelable(true)
                .show();
    }

    @Override
    public void initViews() {
        User currentUser = Config.getInstance().getUserInfo(getApplicationContext());
        mEdtProfileName.setText(currentUser.getName());
        mEdtProfilePhone.setText(currentUser.getPhoneNumber());
        mEdtProfileAddress.setText(currentUser.getAddress());
        mTvCountSuccessInvoice.setText(String.valueOf(currentUser.getCountInvoiceSuccess()));
        mTvCountTotalInvoice.setText(String.valueOf(currentUser.getCountTotalInvoice()));
        if (currentUser.isShop()) {
            mEdtProfilePlate.setVisibility(View.GONE);
        } else {
            mEdtProfilePlate.setText(currentUser.getPlateNumber());
        }
    }
}
