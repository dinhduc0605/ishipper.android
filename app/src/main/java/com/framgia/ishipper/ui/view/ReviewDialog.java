package com.framgia.ishipper.ui.view;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatRatingBar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.framgia.ishipper.R;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIDefinition;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.EmptyData;
import com.framgia.ishipper.util.CommonUtils;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dinhduc on 24/08/2016.
 */
public class ReviewDialog {
    @BindView(R.id.edt_dialog_review) EditText mEdtDialogReview;
    @BindView(R.id.rating_dialog_review) AppCompatRatingBar mRatingDialogReview;

    private AlertDialog mDialog;
    private Context mContext;
    private String mInvoiceId;

    public ReviewDialog(Context context, String invoiceId) {
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_review, null);
        ButterKnife.bind(this, view);
        mInvoiceId = invoiceId;
        mContext = context;
        mDialog = new AlertDialog.Builder(context)
                .setView(view)
                .setCancelable(false)
                .create();
    }

    @OnClick({R.id.btn_dialog_review_send, R.id.btn_dialog_review_cancel})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_dialog_review_send:
                if (mRatingDialogReview.getProgress() == 0) {
                    Toast.makeText(mContext, R.string.err_msg_not_rating, Toast.LENGTH_SHORT).show();
                    return;
                }

                HashMap<String, String> params = new HashMap<>();
                params.put(APIDefinition.PostRating.PARAM_CONTENT, mEdtDialogReview.getText().toString());
                params.put(APIDefinition.PostRating.PARAM_INVOICE_ID, mInvoiceId);
                params.put(APIDefinition.PostRating.PARAM_RATING_POINT, String.valueOf(mRatingDialogReview.getProgress()));
                params.put(APIDefinition.PostRating.PARAM_REVIEW_TYPE, APIDefinition.PostRating.REVIEW_TYPE);
                final Dialog loadingDialog = CommonUtils.showLoadingDialog(mContext);
                User user = Config.getInstance().getUserInfo(mContext);
                API.postRating(params, user.getAuthenticationToken(), user.getRole(),
                        new API.APICallback<APIResponse<EmptyData>>() {
                            @Override
                            public void onResponse(APIResponse<EmptyData> response) {
                                Toast.makeText(mContext, response.getMessage(), Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                                mDialog.dismiss();
                            }

                            @Override
                            public void onFailure(int code, String message) {
                                Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                loadingDialog.dismiss();
                            }
                        });
                break;
            case R.id.btn_dialog_review_cancel:
                mDialog.cancel();
                break;
        }
    }

    public void show() {
        mDialog.show();
    }

    public void cancel() {
        mDialog.cancel();
    }
}
