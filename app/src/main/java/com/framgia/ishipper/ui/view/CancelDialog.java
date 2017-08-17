package com.framgia.ishipper.ui.view;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.framgia.ishipper.R;
import com.framgia.ishipper.model.ReviewUser;
import com.framgia.ishipper.util.InputValidate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dinhduc on 23/08/2016.
 */
public class CancelDialog {
    @BindView(R.id.edt_dialog_report_content) EditText mEdtContent;
    @BindView(R.id.rg_dialog_report_reasons) RadioGroup mRgReasons;
    @BindView(R.id.rb_shipper_not_come) RadioButton mRbShipperNotCome;
    @BindView(R.id.rb_not_have_good) RadioButton mRbNotHaveGood;
    @BindView(R.id.rb_not_have_shipper) RadioButton mRbNotHaveShipper;
    @BindView(R.id.rb_good_break) RadioButton mRbGoodBreak;
    @BindView(R.id.rb_other_reason) RadioButton mRbOtherReason;
    private AlertDialog mAlertDialog;
    private OnReportListener mListener;
    private ReviewUser mReviewUser = new ReviewUser();
    private Context mContext;

    public CancelDialog(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_cancel, null);
        ButterKnife.bind(this, view);
        mContext = context;
        mAlertDialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create();
    }

    @OnClick({R.id.btn_dialog_report_send, R.id.btn_dialog_report_cancel})
    public void onClick(View view) {
        String content = "";
        switch (view.getId()) {
            case R.id.btn_dialog_report_send:
                int id = mRgReasons.getCheckedRadioButtonId();
                if (id != R.id.rb_other_reason) {
                    content = ((RadioButton) mRgReasons
                            .findViewById(mRgReasons.getCheckedRadioButtonId()))
                            .getText().toString();
                } else {
                    if (InputValidate.notEmpty(mEdtContent, mContext)) {
                        content = mEdtContent.getText().toString();
                    }
                }
                if (mListener != null) {
                    mReviewUser.setContent(content);
                    mListener.onReportListener(mReviewUser);
                }
                break;
            case R.id.btn_dialog_report_cancel:
                mAlertDialog.cancel();
                break;
        }
    }

    public void show() {
        mAlertDialog.show();
    }

    public void cancel() {
        mAlertDialog.cancel();
    }

    public OnReportListener getListener() {
        return mListener;
    }

    public void setOnReportListener(OnReportListener listener) {
        mListener = listener;
    }

    public interface OnReportListener {
        void onReportListener(ReviewUser reviewUser);
    }
}
