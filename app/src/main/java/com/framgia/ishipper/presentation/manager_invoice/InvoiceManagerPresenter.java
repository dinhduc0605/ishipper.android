package com.framgia.ishipper.presentation.manager_invoice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.Toast;
import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.InvoiceData;
import com.framgia.ishipper.presentation.invoice.detail.InvoiceDetailActivity;
import com.framgia.ishipper.util.Const;
import java.util.List;

/**
 * Created by vuduychuong1994 on 11/23/16.
 */

public class InvoiceManagerPresenter implements InvoiceManagerContract.Presenter {

    private BaseActivity mActivity;
    private InvoiceManagerContract.View mView;
    private BaseFragment mFragment;

    public InvoiceManagerPresenter(BaseFragment fragment,
            BaseActivity activity, InvoiceManagerContract.View view) {
        mFragment = fragment;
        mActivity = activity;
        mView = view;
    }

    @Override
    public void actionTakeInvoice(final int statusCode, final Invoice invoice) {
        mActivity.showDialog();
        API.putUpdateInvoiceStatus(User.ROLE_SHIPPER, invoice.getStringId(),
                                   Config.getInstance().getUserInfo(mActivity).getAuthenticationToken(),
                                   Invoice.STATUS_SHIPPING, new API.APICallback<APIResponse<InvoiceData>>() {
                    @Override
                    public void onResponse(APIResponse<InvoiceData> response) {
                        mView.notifyChangeTab(statusCode);
                        mView.notifyChangeTab(Invoice.STATUS_CODE_SHIPPING, true, invoice.getId());
                        mActivity.dismissDialog();
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        mActivity.dismissDialog();
                        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void actionShippedInvoice(final int statusCode, final Invoice invoice) {
        API.putUpdateInvoiceStatus(User.ROLE_SHIPPER, String.valueOf(invoice.getId()),
                                   Config.getInstance().getUserInfo(mActivity).getAuthenticationToken(),
                                   Invoice.STATUS_SHIPPED, new API.APICallback<APIResponse<InvoiceData>>() {
                    @Override
                    public void onResponse(APIResponse<InvoiceData> response) {
                        mView.notifyChangeTab(statusCode);
                        mView.notifyChangeTab(Invoice.STATUS_CODE_SHIPPED, true, invoice.getId());
                        mView.showReviewDialog(invoice);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void actionFinishInvoice(final int statusCode, final Invoice invoice) {
        API.putUpdateInvoiceStatus(User.ROLE_SHOP.toLowerCase(), String.valueOf(invoice.getId()),
                                   Config.getInstance().getUserInfo(mActivity).getAuthenticationToken(),
                                   Invoice.STATUS_FINISHED, new API.APICallback<APIResponse<InvoiceData>>() {
                    @Override
                    public void onResponse(APIResponse<InvoiceData> response) {
                        mView.notifyChangeTab(statusCode);
                        mView.notifyChangeTab(Invoice.STATUS_CODE_FINISHED, true, invoice.getId());
                        mView.showReviewDialog(invoice);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void startDetailInvoiceActivity(Invoice invoice) {
        Intent intent = new Intent(mActivity, InvoiceDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString(Const.KEY_INVOICE_ID, invoice.getStringId());
        intent.putExtras(extras);
        mFragment.startActivityForResult(intent, Const.RequestCode.REQUEST_CODE_INVOICE_DETAIL);
    }

    @Override
    public void syncData(
            ViewPager viewPager, List<ListInvoiceFragment> listOrderFragment,
            boolean isSyncCurrentTab) {
        if (viewPager == null || listOrderFragment == null) return;
        if (isSyncCurrentTab) listOrderFragment.get(viewPager.getCurrentItem()).notifyChangedData(null);
        for (int i = 1; i <= viewPager.getOffscreenPageLimit(); i++) {
            if (viewPager.getCurrentItem() + i <= listOrderFragment.size()) {
                ListInvoiceFragment nextListFragment =
                        listOrderFragment.get(viewPager.getCurrentItem() + i);
                if (nextListFragment != null) nextListFragment.notifyChangedData(null);
            }
            if (viewPager.getCurrentItem() - i >= 0) {
                ListInvoiceFragment prevListFragment =
                        listOrderFragment.get(viewPager.getCurrentItem() - i);
                if (prevListFragment != null) prevListFragment.notifyChangedData(null);
            }
        }
    }
}
