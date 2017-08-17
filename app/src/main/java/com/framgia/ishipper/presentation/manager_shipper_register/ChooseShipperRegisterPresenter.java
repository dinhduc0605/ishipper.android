package com.framgia.ishipper.presentation.manager_shipper_register;
import android.app.Activity;
import android.content.Intent;
import android.widget.Toast;
import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.model.SocketResponse;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.EmptyData;
import com.framgia.ishipper.net.data.ListShipperData;
import com.framgia.ishipper.presentation.main.MainActivity;
import com.framgia.ishipper.util.Const;
import com.google.gson.Gson;

/**
 * Created by vuduychuong1994 on 11/24/16.
 */

public class ChooseShipperRegisterPresenter implements ChooseShipperRegisterContract.Presenter {

    private BaseActivity mActivity;
    private ChooseShipperRegisterContract.View mView;

    public ChooseShipperRegisterPresenter(
            BaseActivity activity, ChooseShipperRegisterContract.View view) {
        mActivity = activity;
        mView = view;
    }

    @Override
    public void getListShipper(int invoiceId) {
        API.getListShipperReceived(Config.getInstance().getUserInfo(mActivity).getAuthenticationToken(),
           String.valueOf(invoiceId),
           new API.APICallback<APIResponse<ListShipperData>>() {
               @Override
               public void onResponse(APIResponse<ListShipperData> response) {
                   mView.addListShipper(response.getData().getShippersList());
               }

               @Override
               public void onFailure(int code, String message) {
                   Toast.makeText(mActivity, message,
                                  Toast.LENGTH_SHORT).show();
               }
           });
    }

    @Override
    public void updateNotificationStatus(User currentUser, String notiId) {
        API.updateNotification(currentUser.getUserType(), notiId,
                               currentUser.getAuthenticationToken(), true,
                               new API.APICallback<APIResponse<EmptyData>>() {
                                   @Override
                                   public void onResponse(
                                           APIResponse<EmptyData> response) {
                                       //TODO: read notificationItem
                                   }

                                   @Override
                                   public void onFailure(int code, String message) {

                                   }
                               });
    }

    @Override
    public void startMainActivity() {
        Intent intent = new Intent(mActivity, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        mActivity.startActivity(intent);
    }

    @Override
    public void actionAcceptShipper(User shipper, final int invoiceId) {
        mActivity.showDialog();
        API.putShopReceiveShipper(Config.getInstance().getUserInfo(mActivity)
                                          .getAuthenticationToken(),
                                  shipper.getUserInvoiceId(), new API.APICallback<APIResponse<EmptyData>>() {
                    @Override
                    public void onResponse(APIResponse<EmptyData> response) {
                        mActivity.dismissDialog();
                        Toast.makeText(mActivity,
                                       R.string.accept_shipper_success, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent();
                        intent.putExtra(Const.KEY_INVOICE_ID, String.valueOf(invoiceId));
                        mActivity.setResult(Activity.RESULT_OK, intent);
                        mActivity.onBackPressed();
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        mActivity.dismissDialog();
                        Toast.makeText(mActivity, message,
                                       Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void updateShipper(int id, Intent intent) {
        if (intent == null) return;
        String responseStr = intent.getStringExtra(Const.KEY_SOCKET_RESPONSE);
        SocketResponse socketResponse = new Gson().fromJson(responseStr, SocketResponse.class);
        if (socketResponse != null) {
            Invoice invoice = socketResponse.getInvoice();
            if (invoice == null) return;
            if (invoice.getId() == id) {
                User user = socketResponse.getUser();
                if (user == null) return;
                mView.remove(user);
            }
        } else {
            String invoiceStr = intent.getStringExtra(Const.KEY_INVOICE);
            Invoice invoice = new Gson().fromJson(invoiceStr, Invoice.class);
            if (invoice == null) return;
            if (invoice.getId() == id) {
                String userStr = intent.getStringExtra(Const.KEY_USER);
                if (userStr == null) return;
                User user = new Gson().fromJson(userStr, User.class);
                mView.addUser(user);
            }
        }
    }
}
