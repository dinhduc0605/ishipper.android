package com.framgia.ishipper.presentation.manager_invoice;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.util.Const;
import com.framgia.ishipper.util.TextFormatUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {

    private List<Invoice> mInvoiceList;
    private OnClickActionListener mClickActionListener;
    private OnClickCancelListener mClickCancelListener;
    private OnclickViewListener mClickViewListener;
    private Context mContext;
    private int mPositionHighlight = Const.POSITION_HIGHLIGHT_DEFAULT;

    public InvoiceAdapter(Context context, List<Invoice> invoiceList,
                          OnclickViewListener listener) {
        mContext = context;
        mInvoiceList = invoiceList;
        mClickViewListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (mPositionHighlight == position) highlightItem(holder.mView);
        holder.mInvoice = mInvoiceList.get(position);
        setStatus(holder);
        displayData(holder);
    }

    private void highlightItem(View view) {
        mPositionHighlight = Const.POSITION_HIGHLIGHT_DEFAULT;
        TransitionDrawable transition = (TransitionDrawable) view.getBackground();
        transition.startTransition(Const.TIME_HIGHLIGHT_INVOICE);
        transition.reverseTransition(Const.TIME_REVERSE_HIGHLIGHT_INVOICE);
    }

    private void displayData(ViewHolder holder) {
        holder.mTvNearbyShopName.setText(holder.mInvoice.getName());
        holder.mTvItemInvoiceFrom.setText(holder.mInvoice.getAddressStart());
        holder.mTvItemInvoiceTo.setText(holder.mInvoice.getAddressFinish());
        holder.mTvItemInvoiceShipTime.setText(holder.mInvoice.getDeliveryTime());
        holder.mTvItemInvoicePrice.setText(
                TextFormatUtils.formatPrice(holder.mInvoice.getPrice()));
        holder.mTvNearbyShipPrice.setText(
                TextFormatUtils.formatPrice(holder.mInvoice.getShippingPrice()));
        holder.mTvItemInvoiceDistance.setText(
                TextFormatUtils.formatDistance(holder.mInvoice.getDistance()));
    }

    private void setStatus(ViewHolder holder) {
        String textStatus;
        int status = holder.mInvoice.getStatusCode();
        String action = "";
        Drawable drawableStatus;
        int statusColor;
        holder.mLayoutAction.setVisibility(View.VISIBLE);
        holder.mBtnCancelAcceptInvoice.setVisibility(View.GONE);
        switch (status) {
            case Invoice.STATUS_CODE_INIT:
                if (Config.getInstance().isShop(mContext)) {
                    textStatus = mContext.getString(R.string.invoice_shop_status_wait);
                    action = mContext.getString(R.string.action_shop_wait);
                } else {
                    if (holder.mInvoice.isReceived()) {
                        textStatus = mContext.getString(R.string.invoice_status_wait);
                        holder.mBtnCancelAcceptInvoice.setVisibility(View.VISIBLE);
                        holder.mLayoutAction.setVisibility(View.GONE);
                    } else {
                        textStatus = mContext.getString(R.string.invoice_status_init);
                    }
                    action = "";
                }
                drawableStatus = ResourcesCompat.getDrawable(
                        mContext.getResources(),
                        R.drawable.ic_status_waiting,
                        null
                );
                statusColor = mContext.getResources().getColor(R.color.color_status_waiting);
                break;
            case Invoice.STATUS_CODE_WAITING:
                if (Config.getInstance().isShop(mContext)) {
                    textStatus = mContext.getString(R.string.invoice_shop_status_take);
                    action = "";
                } else {
                    textStatus = mContext.getString(R.string.invoice_status_take);
                    action = mContext.getString(R.string.action_shipper_take);
                }
                drawableStatus = ResourcesCompat.getDrawable(
                        mContext.getResources(),
                        R.drawable.ic_status_pick,
                        null
                );
                statusColor = mContext.getResources().getColor(R.color.color_status_pick);
                break;
            case Invoice.STATUS_CODE_SHIPPING:
                textStatus = mContext.getString(R.string.invoice_status_shipping);
                if (Config.getInstance().isShop(mContext)) {
                    action = "";
                } else {
                    action = mContext.getString(R.string.action_shipper_shipping);
                }
                drawableStatus = ResourcesCompat.getDrawable(
                        mContext.getResources(),
                        R.drawable.ic_status_delivering,
                        null
                );
                statusColor = mContext.getResources().getColor(R.color.color_status_shipping);
                break;
            case Invoice.STATUS_CODE_SHIPPED:
                textStatus = mContext.getString(R.string.invoice_status_delivered);
                if (Config.getInstance().isShop(mContext)) {
                    action = mContext.getString(R.string.action_shop_delivered);
                } else {
                    action = "";
                }
                drawableStatus = ResourcesCompat.getDrawable(
                        mContext.getResources(),
                        R.drawable.ic_status_delivered,
                        null
                );
                statusColor = mContext.getResources().getColor(R.color.color_status_delivered);
                break;
            case Invoice.STATUS_CODE_FINISHED:
                textStatus = mContext.getString(R.string.invoice_status_finished);
                drawableStatus = ResourcesCompat.getDrawable(
                        mContext.getResources(),
                        R.drawable.ic_status_finish,
                        null
                );
                statusColor = mContext.getResources().getColor(R.color.color_status_finish);
                break;
            case Invoice.STATUS_CODE_CANCEL:
                textStatus = mContext.getString(R.string.invoice_status_cancelled);
                drawableStatus = ResourcesCompat.getDrawable(
                        mContext.getResources(),
                        R.drawable.ic_cancel,
                        null
                );
                statusColor = mContext.getResources().getColor(R.color.color_status_cancelled);
                break;
            default:
                textStatus = "";
                action = mContext.getString(R.string.action);
                drawableStatus = ResourcesCompat.getDrawable(
                        mContext.getResources(),
                        R.drawable.ic_status_waiting,
                        null
                );
                statusColor = mContext.getResources().getColor(R.color.colorAccent);
                break;
        }
        holder.mShopInvoiceStatus.setVisibility(View.GONE);
        holder.mTvShippingInvoiceStatus.setVisibility(View.VISIBLE);
        holder.mTvShippingInvoiceStatus.setText(textStatus);
        holder.mTvShippingInvoiceStatus.setTextColor(statusColor);
        holder.mTvShippingInvoiceStatus.setCompoundDrawablesWithIntrinsicBounds(drawableStatus,
                                                                                null, null, null);
        try {
            if (holder.mInvoice.getStatusCode() == Invoice.STATUS_CODE_INIT &&
                Config.getInstance().isShop(mContext) &&
                holder.mInvoice.getNumOfRecipient() > Const.ZERO) {
                holder.mTvNumShipRegister.setVisibility(View.VISIBLE);
                holder.mTvNumShipRegister.setText(String.valueOf(holder.mInvoice.getNumOfRecipient()));
            } else {
                holder.mTvNumShipRegister.setVisibility(View.GONE);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            holder.mTvNumShipRegister.setVisibility(View.GONE);
        }
        if (action.equals("")) {
            holder.mBtnActionItemInvoice.setVisibility(View.GONE);
        } else {
            holder.mBtnActionItemInvoice.setVisibility(View.VISIBLE);
            holder.mBtnActionItemInvoice.setText(action);
        }
    }

    public List<Invoice> getInvoiceList() {
        return mInvoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        mInvoiceList = invoiceList;
    }

    public OnClickActionListener getClickActionListener() {
        return mClickActionListener;
    }

    public void setClickActionListener(OnClickActionListener clickActionListener) {
        mClickActionListener = clickActionListener;
    }

    public OnClickCancelListener getClickCancelListener() {
        return mClickCancelListener;
    }

    public void setClickCancelListener(OnClickCancelListener clickCancelListener) {
        mClickCancelListener = clickCancelListener;
    }

    public OnclickViewListener getClickViewListener() {
        return mClickViewListener;
    }

    public void setClickViewListener(
            OnclickViewListener clickViewListener) {
        mClickViewListener = clickViewListener;
    }

    public int getPositionHighlight() {
        return mPositionHighlight;
    }

    public void setPositionHighlight(int positionHighlight) {
        mPositionHighlight = positionHighlight;
    }

    @Override
    public int getItemCount() {
        if (mInvoiceList == null)
            return 0;
        return mInvoiceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final View mView;
        public Invoice mInvoice;

        @BindView(R.id.tv_shipping_invoice_status) TextView mTvShippingInvoiceStatus;
        @BindView(R.id.tv_item_invoice_shop_name) TextView mTvNearbyShopName;
        @BindView(R.id.ll_invoice_status) LinearLayout mLlInvoiceStatus;
        @BindView(R.id.tv_item_invoice_ship_price) TextView mTvNearbyShipPrice;
        @BindView(R.id.tv_item_invoice_from) TextView mTvItemInvoiceFrom;
        @BindView(R.id.tv_item_invoice_to) TextView mTvItemInvoiceTo;
        @BindView(R.id.delivery_to_address_box) LinearLayout mDeliveryToAddressBox;
        @BindView(R.id.action_detail_invoice) LinearLayout mActionDetailInvoice;
        @BindView(R.id.tv_item_invoice_distance) TextView mTvItemInvoiceDistance;
        @BindView(R.id.tv_item_invoice_ship_time) TextView mTvItemInvoiceShipTime;
        @BindView(R.id.tv_item_invoice_price) TextView mTvItemInvoicePrice;
        @BindView(R.id.ll_shop_invoice_status) LinearLayout mShopInvoiceStatus;
        @BindView(R.id.btn_cancel_item_invoice) TextView mBtnCancelItemInvoice;
        @BindView(R.id.btn_action_item_invoice) TextView mBtnActionItemInvoice;
        @BindView(R.id.rating_invoice_window) AppCompatRatingBar mRatingInvoiceWindow;
        @BindView(R.id.tv_number_shipper_register) TextView mTvNumShipRegister;
        @BindView(R.id.action_cancel_accept_invoice) TextView mBtnCancelAcceptInvoice;
        @BindView(R.id.layout_action) RelativeLayout mLayoutAction;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
            mView = view;
            mView.setOnClickListener(this);
            mBtnActionItemInvoice.setOnClickListener(this);
            mBtnCancelItemInvoice.setOnClickListener(this);
            mBtnCancelAcceptInvoice.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getId() == mView.getId() && mClickViewListener != null) {
                mClickViewListener.onclickViewListener(mInvoice);
            }
            if (view.getId() == R.id.action_cancel_accept_invoice && mClickCancelListener != null) {
                mClickCancelListener.onClickCancelListener(mInvoice, getAdapterPosition());
            }
            if (view.getId() == R.id.btn_action_item_invoice && mClickActionListener != null) {
                mClickActionListener.onClickActionListener(mInvoice);
            }
        }
    }

    public interface OnClickActionListener {
        void onClickActionListener(Invoice invoice);
    }

    public interface OnClickCancelListener {
        void onClickCancelListener(Invoice invoice, int adapterPosition);
    }

    public interface OnclickViewListener {
        void onclickViewListener(Invoice invoice);
    }
}
