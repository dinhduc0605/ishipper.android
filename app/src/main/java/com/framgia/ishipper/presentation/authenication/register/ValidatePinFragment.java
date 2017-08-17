package com.framgia.ishipper.presentation.authenication.register;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.EmptyData;
import com.framgia.ishipper.net.data.SignUpData;
import com.framgia.ishipper.presentation.authenication.update_pass.ResetPasswordNewFragment;
import com.framgia.ishipper.presentation.main.MainActivity;
import com.framgia.ishipper.util.CommonUtils;
import com.framgia.ishipper.util.InputValidate;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by HungNT on 8/3/16.
 */
public class ValidatePinFragment extends BaseFragment {

    public static final int ACTION_ACTIVATE = 1;
    public static final int ACTION_FORGOT_PASSWORD = 2;
    private static final String BUNDLE_PHONE = "BUNDLE_PHONE";
    private static final String BUNDLE_ACTION = "BUNDLE_ACTION";

    @BindView(R.id.tvDetailText) TextView mTvDetailText;
    @BindView(R.id.edtPhoneNumber) EditText mEdtPhoneNumber;

    private String mPhoneNumber;
    private int mAction;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_pin_validate;
    }

    @Override
    public void initViews() {
        mPhoneNumber = getArguments().getString(BUNDLE_PHONE);
        mAction = getArguments().getInt(BUNDLE_ACTION);
        mTvDetailText.setText(getString(R.string.message_phone_validate_sent, mPhoneNumber));
    }

    @OnClick({R.id.btnDone, R.id.btnResent})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDone:
                if (!InputValidate.checkPin(mEdtPhoneNumber, getContext())) {
                    break;
                }
                final ProgressDialog pd = new ProgressDialog(getActivity());
                pd.show();
                if (mAction == ACTION_FORGOT_PASSWORD) {
                    /* Forgot password */
                    API.getCheckPin(mPhoneNumber, mEdtPhoneNumber.getText().toString(),
                            new API.APICallback<APIResponse<EmptyData>>() {
                                @Override
                                public void onResponse(APIResponse<EmptyData> response) {
                                    pd.dismiss();
                                    getActivity().getSupportFragmentManager().beginTransaction()
                                                 .add(R.id.container, ResetPasswordNewFragment
                                                         .newInstance(mPhoneNumber,
                                                                 mEdtPhoneNumber.getText()
                                                                                .toString()))
                                                 .addToBackStack(null).commit();
                                }

                                @Override
                                public void onFailure(int code, String message) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
                                         .show();
                                }
                            });
                } else {
                     /* Sign up */
                    API.confirmationPinInSignUp(mPhoneNumber, mEdtPhoneNumber.getText().toString(),
                            new API.APICallback<APIResponse<SignUpData>>() {
                                @Override
                                public void onResponse(
                                        APIResponse<SignUpData> response) {
                                    pd.dismiss();
                                    Config.getInstance()
                                          .setUserInfo(getContext(), response.getData().getUser());
                                    Intent mainIntent =
                                            new Intent(getActivity(), MainActivity.class);
                                    startActivity(mainIntent);
                                    getActivity().finish();
                                    Toast.makeText(getContext(),
                                            getString(R.string.register_success),
                                            Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onFailure(int code, String message) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT)
                                         .show();
                                }
                            });
                }
                break;
            case R.id.btnResent:
                getConfirmationPin();
                break;
        }
    }

    private void getConfirmationPin() {
        final Dialog pd = CommonUtils.showLoadingDialog(getContext());

        API.getConfirmationPin(mPhoneNumber, new API.APICallback<APIResponse<EmptyData>>() {
            @Override
            public void onResponse(APIResponse<EmptyData> response) {
                pd.dismiss();
                Toast.makeText(getActivity(), response.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int code, String message) {
                pd.dismiss();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static ValidatePinFragment newInstance(String phoneNumber, int action) {

        Bundle args = new Bundle();
        args.putString(BUNDLE_PHONE, phoneNumber);
        args.putInt(BUNDLE_ACTION, action);
        ValidatePinFragment fragment = new ValidatePinFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
