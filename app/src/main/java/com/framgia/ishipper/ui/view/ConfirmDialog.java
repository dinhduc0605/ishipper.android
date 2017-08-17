package com.framgia.ishipper.ui.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.framgia.ishipper.R;


/**
 * Created by dinhduc on 28/08/2016.
 */
public class ConfirmDialog implements View.OnClickListener {
    private Context mContext;
    private TextView mTvTitle;
    private TextView mTvContent;
    private TextView mBtnPositive;
    private TextView mBtnNegative;
    private AlertDialog mAlertDialog;
    private ConfirmDialogCallback mCallback;

    public ConfirmDialog(Context context) {
        mContext = context;
        initView();
        mBtnPositive.setOnClickListener(this);
        mBtnNegative.setOnClickListener(this);
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.confirm_dialog_layout, null);
        mTvTitle = (TextView) view.findViewById(R.id.tv_confirm_dialog_title);
        mTvContent = (TextView) view.findViewById(R.id.tv_confirm_dialog_content);
        mBtnNegative = (TextView) view.findViewById(R.id.btn_confirm_dialog_negative);
        mBtnPositive = (TextView) view.findViewById(R.id.btn_confirm_dialog_positive);
        mAlertDialog = new AlertDialog.Builder(mContext)
                .setCancelable(false)
                .setView(view)
                .create();
    }

    public ConfirmDialog setTitle(String title) {
        mTvTitle.setText(title);
        return this;
    }

    public ConfirmDialog setMessage(String message) {
        mTvContent.setText(message);
        return this;
    }

    public ConfirmDialog setButtonCallback(ConfirmDialogCallback callback) {
        mCallback = callback;
        return this;
    }

    public ConfirmDialog setIcon(int iconRes) {
        mTvTitle.setCompoundDrawablesWithIntrinsicBounds(iconRes, 0, 0, 0);
        return this;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_confirm_dialog_positive:
                mCallback.onPositiveButtonClick(this);
                break;

            case R.id.btn_confirm_dialog_negative:
                mCallback.onNegativeButtonClick(this);
                break;
        }
    }

    public interface ConfirmDialogCallback {
        void onPositiveButtonClick(ConfirmDialog confirmDialog);

        void onNegativeButtonClick(ConfirmDialog confirmDialog);
    }

    public ConfirmDialog show() {
        mAlertDialog.show();
        return this;
    }

    public void cancel() {
        mAlertDialog.cancel();
    }

}
