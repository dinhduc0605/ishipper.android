package com.framgia.ishipper.ui.adapter;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.ishipper.R;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.InvoiceData;
import com.framgia.ishipper.util.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class InvoiceShippingAdapter extends RecyclerView.Adapter<InvoiceShippingAdapter.ViewHolder> {

    private List<Invoice> mInvoiceList;
    private OnItemClickListener mListener;
    private Context mContext;

    public InvoiceShippingAdapter(List<Invoice> invoiceList, OnItemClickListener listener) {
        mInvoiceList = invoiceList;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_invoice, parent, false);
        mContext = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Invoice item = mInvoiceList.get(position);
        holder.mOrderEndAddress.setText(item.getAddressFinish());
        holder.mOrderStartAddress.setText(item.getAddressStart());
        holder.mOrderPrePay.setText(item.getPrice() + "");
        holder.mOrderShippingCost.setText(item.getShippingPrice() + "");
        holder.mOrderShippingTime.setText(item.getDeliveryTime());
    }

    @Override
    public int getItemCount() {
        return mInvoiceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_item_invoice_to) TextView mOrderEndAddress;
        @BindView(R.id.tv_item_invoice_from) TextView mOrderStartAddress;
        @BindView(R.id.tv_item_invoice_price) TextView mOrderPrePay;
        @BindView(R.id.tv_item_invoice_ship_price) TextView mOrderShippingCost;
        @BindView(R.id.tv_item_invoice_ship_time) TextView mOrderShippingTime;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            ButterKnife.findById(view, R.id.btn_nearby_done_invoice).setVisibility(View.VISIBLE);
            ButterKnife.findById(view, R.id.action_call_invoice).setVisibility(View.VISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onClick(getAdapterPosition());
                }
            });
        }

        @OnClick(R.id.btn_nearby_done_invoice)
        public void doneOrder() {
            final int pos = getAdapterPosition();
            Invoice invoice = mInvoiceList.get(pos);

            final Dialog loadingDialog = CommonUtils.showLoadingDialog(mContext);
            API.putUpdateInvoiceStatus(User.ROLE_SHIPPER, invoice.getStringId(),
                    Config.getInstance().getUserInfo(mContext).getAuthenticationToken(),
                    Invoice.STATUS_SHIPPED, new API.APICallback<APIResponse<InvoiceData>>() {
                        @Override
                        public void onResponse(APIResponse<InvoiceData> response) {
                            mInvoiceList.remove(pos);
                            notifyItemRemoved(pos);
                            loadingDialog.dismiss();
                        }

                        @Override
                        public void onFailure(int code, String message) {
                            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                            loadingDialog.dismiss();
                        }
                    });
        }
    }

    public interface OnItemClickListener {
        void onClick(int position);
    }


}
