package com.framgia.ishipper.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.util.TextFormatUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by HungNT on 10/21/16.
 */

public class NewInvoiceAdapter extends RecyclerView.Adapter<NewInvoiceAdapter.ViewHolder> {

    private List<Invoice> mInvoiceList;
    private OnItemClickListener mCallback;
    private Context mContext;

    public NewInvoiceAdapter(List<Invoice> invoiceList, OnItemClickListener callback) {
        mInvoiceList = invoiceList;
        mCallback = callback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_invoice, parent, false);
        return new NewInvoiceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindData(mInvoiceList.get(position));
    }

    @Override
    public int getItemCount() {
        return mInvoiceList == null ? 0 : mInvoiceList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_invoice_shop_name) TextView mInvoiceShopName;
        @BindView(R.id.tv_item_invoice_to) TextView mInvoiceEndAddress;
        @BindView(R.id.tv_item_invoice_from) TextView mInvoiceStartAddress;
        @BindView(R.id.tv_item_invoice_price) TextView mInvoicePrePay;
        @BindView(R.id.tv_item_invoice_ship_price) TextView mInvoiceShippingCost;
        @BindView(R.id.tv_item_invoice_ship_time) TextView mInvoiceShippingTime;
        @BindView(R.id.btn_item_invoice_register) TextView mBtnReceiveInvoice;
        @BindView(R.id.tv_item_invoice_distance) TextView mTvDistance;
        @BindView(R.id.action_cancel_accept_invoice) TextView mBtnCancelAcceptInvoice;
        @BindView(R.id.rating_invoice_window) AppCompatRatingBar mRateOfShop;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mBtnReceiveInvoice.setVisibility(View.VISIBLE);
        }

        @OnClick({R.id.layoutInvoiceSummary, R.id.action_detail_invoice})
        void showInvoiceDetail() {
            mCallback.onInvoiceItemClick(mInvoiceList.get(getAdapterPosition()));
        }

        @OnClick(R.id.btn_item_invoice_register)
        void receiveInvoice() {
            mCallback.onInvoiceReceiveItemClick(mInvoiceList.get(getAdapterPosition()));
        }

        @OnClick(R.id.action_cancel_accept_invoice)
        void cancelInvoice() {
            mCallback.onCancelAcceptInvoice(mInvoiceList.get(getAdapterPosition()));
        }

        void bindData(Invoice invoice) {
            if (invoice.isReceived()) {
                mBtnCancelAcceptInvoice.setVisibility(View.VISIBLE);
                mBtnReceiveInvoice.setVisibility(View.GONE);
            } else {
                mBtnCancelAcceptInvoice.setVisibility(View.GONE);
                mBtnReceiveInvoice.setVisibility(View.VISIBLE);
            }

            mInvoiceShopName.setText(invoice.getUser().getName());
            mInvoiceEndAddress.setText(invoice.getAddressFinish());
            mInvoiceStartAddress.setText(invoice.getAddressStart());
            mInvoicePrePay.setText(String.valueOf(TextFormatUtils.formatPrice(invoice.getPrice())));
            mInvoiceShippingCost.setText(TextFormatUtils.formatPrice(invoice.getShippingPrice()));
            mInvoiceShippingTime.setText(invoice.getDeliveryTime());
            mTvDistance.setText(TextFormatUtils.formatDistance(invoice.getDistance()));
            mRateOfShop.setRating((float) invoice.getUser().getRate());
        }
    }

    public interface OnItemClickListener {
        void onInvoiceReceiveItemClick(Invoice invoice);

        void onInvoiceItemClick(Invoice invoice);

        void onCancelAcceptInvoice(Invoice invoice);
    }
}
