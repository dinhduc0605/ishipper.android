package com.framgia.ishipper.presentation.authenication.login;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.util.CommonUtils;
import com.framgia.ishipper.util.Const;
import com.framgia.ishipper.util.Const.Firebase;
import com.google.firebase.messaging.FirebaseMessaging;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements
        AdapterView.OnItemSelectedListener,
        LoginContract.View {
    private static final String TAG = "LoginActivity";

    @BindView(R.id.edtPhoneNumber) EditText mEdtPhoneNumber;
    @BindView(R.id.edtPassword) EditText mEdtPassword;
    @BindView(R.id.spnPrefixPhoneNumber) Spinner mSpnPrefixPhoneNumber;

    private String mPrefixPhoneNumber;
    private LoginContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
    }

    public void initViews() {
        getConfig();
        mPresenter = new LoginPresenter(this, this);

        // Register news topic that user can be received all news, promotion, ...
        FirebaseMessaging.getInstance().subscribeToTopic(Firebase.TOPIC_NEWS);

        // Check login
        if (Config.getInstance().isLogin(getApplicationContext())) {
            mPresenter.startMainActivity();
        }

        setUpSpinner();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    public void setUpSpinner() {
        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.prefix_phone_number,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnPrefixPhoneNumber.setAdapter(adapter);
        mSpnPrefixPhoneNumber.setOnItemSelectedListener(this);
    }

    @OnClick({R.id.btnRegister, R.id.btnForgotPass, R.id.btnLogin})
    public void onClick(View view) {
        CommonUtils.hideKeyboard(this);
        switch (view.getId()) {
            case R.id.btnRegister:
                mPresenter.startRegisterActivity();
                break;
            case R.id.btnForgotPass:
                mPresenter.startForgotActivity();
                break;
            case R.id.btnLogin:
                mPresenter.login(mPrefixPhoneNumber, mEdtPhoneNumber, mEdtPassword);
                break;
        }
    }

    /**
     * Get screen size config
     */
    private void getConfig() {
        Const.SCREEN_WIDTH = getResources().getDisplayMetrics().widthPixels;
        Const.SCREEN_HEIGHT = getResources().getDisplayMetrics().heightPixels;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        mPrefixPhoneNumber = adapterView.getItemAtPosition(i).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        mPrefixPhoneNumber = "";
    }
}
