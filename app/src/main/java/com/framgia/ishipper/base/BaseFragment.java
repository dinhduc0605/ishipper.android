package com.framgia.ishipper.base;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.framgia.ishipper.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dinhduc on 21/11/2016.
 */

public abstract class BaseFragment extends Fragment {

    private Unbinder mUnbinder;
    protected ProgressDialog mDialog;

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutId(), null);
        mUnbinder = ButterKnife.bind(this, view);
        initViews();
        return view;
    }

    public void showLoadingDialog() {
        if (mDialog == null) initDialog();
        if (mDialog != null && !mDialog.isShowing()) mDialog.show();
    }

    public void dismissLoadingDialog() {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
    }

    private void initDialog() {
        mDialog = new ProgressDialog(getContext());
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage(getString(R.string.dialog_loading_message));
        mDialog.setIndeterminate(true);
        mDialog.setCanceledOnTouchOutside(false);
    }

    public abstract int getLayoutId();

    public abstract void initViews();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    public void showUserMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void showUserMessage(@StringRes int resId) {
        Toast.makeText(getActivity(), resId, Toast.LENGTH_SHORT).show();
    }
}
