package com.framgia.ishipper.presentation.invoice.detail;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseToolbarActivity;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.model.InvoiceHistory;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.ui.adapter.InvoiceHistoryAdapter;
import com.framgia.ishipper.util.CommonUtils;
import com.framgia.ishipper.util.Const;
import com.framgia.ishipper.util.TextFormatUtils;
import com.framgia.ishipper.widget.dialog.ReviewDialog;
import com.framgia.ishipper.widget.dialog.UserInfoDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.framgia.ishipper.util.Const.KEY_INVOICE_ID;

public class InvoiceDetailActivity extends BaseToolbarActivity implements InvoiceDetailContract.View {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.tv_detail_distance) TextView mTvDetailDistance;
    @BindView(R.id.tv_detail_start) TextView mTvDetailStart;
    @BindView(R.id.tv_detail_end) TextView mTvDetailEnd;
    @BindView(R.id.tv_detail_suggest) TextView mTvDetailSuggest;
    @BindView(R.id.tv_detail_invoice_name) TextView mTvDetailInvoiceName;
    @BindView(R.id.tv_detail_ship_price) TextView mTvDetailShipPrice;
    @BindView(R.id.tv_detail_invoice_price) TextView mTvDetailInvoicePrice;
    @BindView(R.id.tv_detail_ship_time) TextView mTvDetailShipTime;
    @BindView(R.id.tv_detail_note) TextView mTvDetailNote;
    @BindView(R.id.tv_detail_shop_name) TextView mTvDetailShopName;
    @BindView(R.id.tv_detail_shop_phone) TextView mTvDetailShopPhone;
    @BindView(R.id.tv_detail_shipper_name) TextView mTvDetailShipperName;
    @BindView(R.id.tv_detail_shipper_phone) TextView mTvDetailShipperPhone;
    @BindView(R.id.cardview_detail_shop_infor) CardView mCardviewDetailShopInfor;
    @BindView(R.id.cardview_detail_shipper_infor) CardView mCardviewDetailShipperInfor;
    @BindView(R.id.tv_detail_customer_name) TextView mDetailCustomerName;
    @BindView(R.id.tv_detail_customer_phone) TextView mDetailCustomerPhone;
    @BindView(R.id.btn_detail_receive_invoice) Button mBtnDetailReceiveInvoice;
    @BindView(R.id.btn_detail_cancel_register_invoice) Button mBtnDetailCancelRegisterInvoice;
    @BindView(R.id.btn_detail_cancel_invoice) View mBtnDetailCancelInvoice;
    @BindView(R.id.btn_report_user) View mBtnReportUser;
    @BindView(R.id.btn_finished_invoice) Button mBtnFinishedInvoice;
    @BindView(R.id.btn_take_invoice) Button mBtnTakeInvoice;
    @BindView(R.id.tv_shipping_invoice_status) TextView mTvInvoiceStatus;
    @BindView(R.id.layoutHistoryInvoice) View mLayoutHistoryInvoice;
    @BindView(R.id.layout_customer) CardView mLayoutCustomer;
    @BindView(R.id.lv_detail_history) ListView mLvHistoryList;
    @BindView(R.id.iv_detail_expand) ImageView mIvDetailExpand;

    private User mCurrentUser;
    private User mInvoiceUser;
    private Invoice mInvoice;
    private List<InvoiceHistory> mInvoiceHistories;
    private boolean mIsExpanded;
    private InvoiceDetailPresenter mPresenter;
    private boolean isStateChanged;

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public int getActivityTitle() {
        return R.string.title_activity_invoice_detail;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_invoice_detail;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_invoice_detail, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (mInvoice == null) return super.onPrepareOptionsMenu(menu);
        if (mInvoice.getStatusCode() == Invoice.STATUS_CODE_SHIPPED ||
                mInvoice.getStatusCode() == Invoice.STATUS_CODE_FINISHED) {
            return super.onPrepareOptionsMenu(menu);
        }
        MenuItem menuItem = menu.findItem(R.id.menu_rating);
        if (menuItem == null) return super.onPrepareOptionsMenu(menu);
        menuItem.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            case R.id.menu_rating:
                new ReviewDialog(this, mInvoice.getStringId()).show();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (CommonUtils.isOpenFromNoti(this)) {
            mPresenter.startMainActivity();
        } else if (isStateChanged) {
            isStateChanged = false;
            mPresenter.finishActivity(mInvoice.getStringId());
        }
        super.onBackPressed();
    }

    @OnClick({
            R.id.btn_detail_show_shipper,
            R.id.btn_detail_show_shop,
            R.id.btn_detail_show_path,
            R.id.btn_detail_shop_call,
            R.id.btn_detail_receive_invoice,
            R.id.btn_detail_cancel_invoice,
            R.id.btn_cancel_invoice,
            R.id.btn_detail_cancel_register_invoice,
            R.id.btn_report_user,
            R.id.btn_finished_invoice,
            R.id.btn_take_invoice,
            R.id.iv_detail_expand
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_detail_show_shop:
            case R.id.btn_detail_show_shipper:
                UserInfoDialogFragment dialogFragment = UserInfoDialogFragment.newInstance(mInvoiceUser);
                dialogFragment.show(getSupportFragmentManager(), "dialog");
                break;
            case R.id.btn_detail_show_path:
                mPresenter.showRouteActivity(mInvoice);
                break;
            case R.id.btn_detail_shop_call:
                break;
            case R.id.btn_detail_receive_invoice:
                isStateChanged = true;
                mPresenter.receiveInvoice(mInvoice.getStringId());
                break;
            case R.id.btn_detail_cancel_invoice:
            case R.id.btn_cancel_invoice:
                if (mInvoice.getStatusCode() == Invoice.STATUS_CODE_INIT) {
                    mPresenter.cancelInvoice(mInvoice);
                    isStateChanged = true;
                } else {
                    mPresenter.report(mInvoice);
                }
                break;
            case R.id.btn_detail_cancel_register_invoice:
                // TODO: 25/08/2016 cancel register order
                break;
            case R.id.btn_report_user:
                mPresenter.report(mInvoice);
                break;
            case R.id.btn_finished_invoice:
                mPresenter.finishedInvoice(mInvoice.getStringId());
                break;
            case R.id.btn_take_invoice:
                mPresenter.takeInvoice(mInvoice.getStringId());
                break;
            case R.id.iv_detail_expand:
                if (mIsExpanded) {
                    mIvDetailExpand.setImageResource(R.drawable.ic_expand_more_32dp);
                    CommonUtils.setListViewHeightBasedOnItems(mLvHistoryList, Const.ZERO);
                } else {
                    mIvDetailExpand.setImageResource(R.drawable.ic_expand_less_32dp);
                    CommonUtils.setListViewHeightBasedOnItems(mLvHistoryList, mInvoiceHistories.size());
                }
                mIsExpanded = !mIsExpanded;
        }
    }

    @Override
    public void initViews() {
        mPresenter = new InvoiceDetailPresenter(this, this);
        mCurrentUser = Config.getInstance().getUserInfo(this);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle == null) return;
        String invoiceId;

        if (CommonUtils.isOpenFromNoti(this)) {
            // Explicit Intent
            invoiceId = bundle.getString(Const.FirebaseData.INVOICE_ID);
            String notiId = getIntent().getExtras().getString(Const.FirebaseData.NOTIFICATION_ID);
            mPresenter.readNotification(notiId);
        } else {
            // Implicit Intent
            invoiceId = bundle.getString(KEY_INVOICE_ID);
        }

        mPresenter.getInvoiceDetail(invoiceId);
    }

    @Override
    public void onGetInvoiceDetailSuccess(Invoice invoice) {
        invalidateOptionsMenu();
        mInvoice = invoice;
        mInvoiceUser = mInvoice.getUser();
    }

    @Override
    public void setInvoiceStatus(Invoice invoice) {
        String textStatus;
        int status = invoice.getStatusCode();
        Drawable drawableStatus;
        int statusColor;
        switch (status) {
            case Invoice.STATUS_CODE_INIT:
                if (mCurrentUser.isShop()) {
                    textStatus = getString(R.string.invoice_shop_status_wait);
                } else {
                    textStatus = invoice.isReceived() ? getString(R.string.invoice_status_wait) :
                            getString(R.string.invoice_status_init);
                    mLayoutCustomer.setVisibility(View.GONE);
                }
                drawableStatus = ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_status_waiting,
                        null
                );
                mLayoutHistoryInvoice.setVisibility(View.GONE);
                statusColor = getResources().getColor(R.color.color_status_waiting);
                break;
            case Invoice.STATUS_CODE_WAITING:
                if (mCurrentUser.isShop()) {
                    textStatus = getString(R.string.invoice_shop_status_take);
                } else {
                    textStatus = getString(R.string.invoice_status_take);
                    mLayoutCustomer.setVisibility(View.GONE);
                }
                drawableStatus = ResourcesCompat.getDrawable(
                        getResources(),
                        R.drawable.ic_status_pick,
                        null
                );
                statusColor = getResources().getColor(R.color.color_status_pick);
                break;
            case Invoice.STATUS_CODE_SHIPPING:
                textStatus = getString(R.string.invoice_status_shipping);
                drawableStatus = ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_status_delivering,
                        null
                );
                statusColor = getResources().getColor(R.color.color_status_shipping);
                break;
            case Invoice.STATUS_CODE_SHIPPED:
                textStatus = getString(R.string.invoice_status_delivered);
                drawableStatus = ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_status_delivered,
                        null
                );
                statusColor = getResources().getColor(R.color.color_status_delivered);
                break;
            case Invoice.STATUS_CODE_FINISHED:
                textStatus = getString(R.string.invoice_status_finished);
                drawableStatus = ResourcesCompat.getDrawable(getResources(),
                        R.drawable.ic_status_finish,
                        null
                );
                statusColor = getResources().getColor(R.color.color_status_finish);
                break;
            case Invoice.STATUS_CODE_CANCEL:
                textStatus = getString(R.string.invoice_status_cancelled);
                drawableStatus = ResourcesCompat.getDrawable(
                        getResources(),
                        R.drawable.ic_cancel,
                        null
                );
                statusColor = getResources().getColor(R.color.color_status_cancelled);
                break;
            default:
                textStatus = "";
                drawableStatus = ResourcesCompat.getDrawable(
                        getResources(),
                        R.drawable.ic_status_waiting,
                        null
                );
                statusColor = getResources().getColor(R.color.colorAccent);
                break;
        }
        mTvInvoiceStatus.setText(textStatus);
        mTvInvoiceStatus.setTextColor(statusColor);
        mTvInvoiceStatus.setCompoundDrawablesWithIntrinsicBounds(drawableStatus, null, null, null);
    }

    @Override
    public void showActionButton(int statusCode) {
        mBtnDetailCancelInvoice.setVisibility(View.VISIBLE);
        if (mCurrentUser.isShop()) {
            switch (statusCode) {
                case Invoice.STATUS_CODE_SHIPPED:
                    mBtnFinishedInvoice.setVisibility(View.VISIBLE);
                    break;
                case Invoice.STATUS_CODE_FINISHED:
                    mBtnDetailCancelInvoice.setVisibility(View.GONE);
                    break;
                case Invoice.STATUS_CODE_CANCEL:
//                    mBtnReportUser.setVisibility(View.VISIBLE);
                    mBtnDetailCancelInvoice.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        } else {
            switch (statusCode) {
                case Invoice.STATUS_CODE_INIT:
                    mBtnDetailReceiveInvoice.setVisibility(mInvoice.isReceived() ? View.GONE : View.VISIBLE);
                    mBtnDetailCancelInvoice.setVisibility(mInvoice.isReceived() ? View.VISIBLE : View.GONE);
                    break;
                case Invoice.STATUS_CODE_WAITING:
                    mBtnTakeInvoice.setVisibility(View.VISIBLE);
                    break;
                case Invoice.STATUS_CODE_SHIPPING:
                    mBtnFinishedInvoice.setVisibility(View.VISIBLE);
                    break;
                case Invoice.STATUS_CODE_SHIPPED:
                    mBtnDetailCancelInvoice.setVisibility(View.GONE);
                    break;
                case Invoice.STATUS_CODE_FINISHED:
                    mBtnDetailCancelInvoice.setVisibility(View.GONE);
                    break;
                case Invoice.STATUS_CODE_CANCEL:
                    mBtnDetailCancelInvoice.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void showInvoiceData(Invoice invoice) {
        mTvDetailDistance.setText(TextFormatUtils.formatDistance(invoice.getDistance()));
        mTvDetailStart.setText(invoice.getAddressStart());
        mTvDetailEnd.setText(invoice.getAddressFinish());
        mTvDetailInvoiceName.setText(invoice.getName());
        mTvDetailInvoicePrice.setText(TextFormatUtils.formatPrice(invoice.getPrice()));
        mTvDetailShipPrice.setText(TextFormatUtils.formatPrice(invoice.getShippingPrice()));
        mTvDetailShipTime.setText(invoice.getDeliveryTime());
        mTvDetailNote.setText(invoice.getDescription());
        mInvoiceHistories = mInvoice.getHistories();
        mDetailCustomerName.setText(mInvoice.getCustomerName());
        mDetailCustomerPhone.setText(mInvoice.getCustomerNumber());
        if (mInvoiceHistories == null) return;
        InvoiceHistoryAdapter adapter = new InvoiceHistoryAdapter(
                getBaseContext(), R.layout.item_invoice_history, mInvoiceHistories);
        mLvHistoryList.setAdapter(adapter);
        CommonUtils.setListViewHeightBasedOnItems(mLvHistoryList, Const.ZERO);

    }

    @Override
    public void showUserData(User user) {
        if (user.isShop()) {
            mCardviewDetailShopInfor.setVisibility(View.VISIBLE);
            mCardviewDetailShipperInfor.setVisibility(View.GONE);
            mTvDetailShopName.setText(user.getName());
            mTvDetailShopPhone.setText(user.getPhoneNumber());
        } else {
            mCardviewDetailShopInfor.setVisibility(View.GONE);
            if (mInvoice.getStatus().equals(Invoice.STATUS_INIT)) {
                mCardviewDetailShipperInfor.setVisibility(View.GONE);
            } else {
                mCardviewDetailShipperInfor.setVisibility(View.VISIBLE);
                mTvDetailShipperName.setText(user.getName());
                mTvDetailShipperPhone.setText(user.getPhoneNumber());
            }
        }
    }

    @Override
    public void onCancelledReceiveInvoice() {
        mBtnDetailCancelInvoice.setVisibility(View.GONE);
        mBtnDetailReceiveInvoice.setVisibility(View.VISIBLE);
        mTvInvoiceStatus.setText(getString(R.string.invoice_status_init));
    }

    @Override
    public void showRatingDialog(final String invoiceId) {
        ReviewDialog dialog = new ReviewDialog(this, invoiceId);
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mPresenter.finishActivity(invoiceId);
            }
        });
        dialog.show();
    }
}
