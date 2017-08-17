package com.framgia.ishipper.presentation.fb_invoice;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.model.FbInvoice;
import com.framgia.ishipper.util.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FBInvoicesAdapter extends RecyclerView.Adapter<FBInvoicesAdapter.ViewHolder> {
    private List<FbInvoice> mInvoiceList;
    private Context mContext;

    public FBInvoicesAdapter(Context context, List<FbInvoice> invoiceList) {
        mContext = context;
        mInvoiceList = invoiceList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_facebook_invoice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        FbInvoice item = mInvoiceList.get(position);
        holder.bindData(item);
    }

    @Override
    public int getItemCount() {
        if (mInvoiceList == null)
            return 0;
        return mInvoiceList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tvName) TextView tvName;
        @BindView(R.id.tvCreateTime) TextView tvCreateTime;
        @BindView(R.id.cbStar) CheckBox cbStar;
        @BindView(R.id.tvContent) TextView tvContent;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        public void bindData(FbInvoice invoice) {
            tvContent.setText(invoice.getContent());
            tvName.setText(invoice.getUserName());
            tvCreateTime.setText(invoice.getCreatedTime());
            cbStar.setChecked(invoice.isPinned());
        }

        @OnClick({R.id.btnInbox, R.id.btnComment, R.id.btnCall})
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.btnInbox:
                    //TODO Inbox
                    break;
                case R.id.btnComment:
                    //TODO Goto facebook post
                    break;
                case R.id.btnCall:
                    // TODO Get a phone call
                    FbInvoice item = mInvoiceList.get(getAdapterPosition());
                    if (!TextUtils.isEmpty(item.getPhoneNumber())) {
                        CommonUtils.makePhoneCall(mContext, item.getPhoneNumber());
                    }

                    break;
            }
        }
    }
}
