package com.framgia.ishipper.presentation.invoice.invoice_creation;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.util.TextFormatUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.framgia.ishipper.presentation.invoice.invoice_creation.ShopCreateInvoiceActivity.sInvoice;

public class ShopCreateInvoiceStep3Fragment extends BaseFragment implements ShopCreateInvoiceStep3Contract.View {

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.btn_detail_show_path) View mBtnDetailShowPath;
    @BindView(R.id.btn_detail_customer_call) View mBtnDetailCustomerCall;
    @BindView(R.id.btn_detail_receive_invoice) Button mBtnCreateOrder;
    @BindView(R.id.tv_detail_distance) TextView mTvDetailDistance;
    @BindView(R.id.tv_detail_start) TextView mTvDetailStart;
    @BindView(R.id.tv_detail_end) TextView mTvDetailEnd;
    @BindView(R.id.tv_detail_suggest) TextView mTvDetailSuggest;
    @BindView(R.id.tv_detail_invoice_name) TextView mTvDetailOrderName;
    @BindView(R.id.tv_detail_ship_price) TextView mTvDetailShipPrice;
    @BindView(R.id.tv_detail_invoice_price) TextView mTvDetailOrderPrice;
    @BindView(R.id.tv_detail_ship_time) TextView mTvDetailShipTime;
    @BindView(R.id.tv_detail_note) TextView mTvDetailNote;
    @BindView(R.id.tv_detail_shop_name) TextView mTvDetailShopName;
    @BindView(R.id.tv_detail_shop_phone) TextView mTvDetailShopPhone;
    @BindView(R.id.tv_detail_shipper_name) TextView mTvDetailShipperName;
    @BindView(R.id.tv_detail_shipper_phone) TextView mTvDetailShipperPhone;
    @BindView(R.id.tv_detail_customer_name) TextView mTvDetailCustomerName;
    @BindView(R.id.tv_detail_customer_phone) TextView mTvDetailCustomerPhone;
    @BindView(R.id.btn_detail_cancel_invoice) LinearLayout mBtnDetailCancelOrder;
    @BindView(R.id.layoutInvoiceStatus) CardView mLayoutInvoiceStatus;
    @BindView(R.id.layoutHistoryInvoice) View mLayoutHistoryInvoice;

    private Context mContext;
    private ShopCreateInvoiceStep3Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_invoice_detail;
    }

    @Override
    public void initViews() {
        mPresenter = new ShopCreateInvoiceStep3Presenter(this, this);
        mLayoutInvoiceStatus.setVisibility(View.GONE);
        mToolbar.setVisibility(View.GONE);
        mBtnCreateOrder.setText(R.string.create_order);
        mBtnCreateOrder.setVisibility(View.VISIBLE);
        mLayoutHistoryInvoice.setVisibility(View.GONE);
        mTvDetailDistance.setText(TextFormatUtils.formatDistance(sInvoice.getDistance()));
        mTvDetailStart.setText(sInvoice.getAddressStart());
        mTvDetailEnd.setText(sInvoice.getAddressFinish());
        mTvDetailOrderName.setText(sInvoice.getName());
        mTvDetailShipPrice.setText(TextFormatUtils.formatPrice(sInvoice.getShippingPrice()));
        mTvDetailOrderPrice.setText(TextFormatUtils.formatPrice(sInvoice.getPrice()));
        mTvDetailShipTime.setText(String.valueOf(sInvoice.getDeliveryTime()));
        mTvDetailNote.setText(sInvoice.getDescription());
        mTvDetailCustomerName.setText(sInvoice.getCustomerName());
        mTvDetailCustomerPhone.setText(sInvoice.getCustomerNumber());
    }

    @OnClick({
            R.id.btn_detail_show_path,
            R.id.btn_detail_shop_call,
            R.id.btn_detail_show_shop,
            R.id.btn_detail_receive_invoice
    })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_detail_show_path:
                mPresenter.startRouteActivity(sInvoice);
                break;
            case R.id.btn_detail_shop_call:
                break;
            case R.id.btn_detail_show_shop:
                break;
            case R.id.btn_detail_receive_invoice:
                sInvoice.setStatus(Invoice.STATUS_INIT);
                mPresenter.requestCreateInvoice(sInvoice);
                break;

        }
    }
}
