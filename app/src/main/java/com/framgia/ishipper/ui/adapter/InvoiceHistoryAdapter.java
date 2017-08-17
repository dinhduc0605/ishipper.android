package com.framgia.ishipper.ui.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.model.InvoiceHistory;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dinhduc on 23/11/2016.
 */

public class InvoiceHistoryAdapter extends ArrayAdapter<InvoiceHistory> {
    private Context mContext;
    private int layoutId;
    private List<InvoiceHistory> mInvoiceHistories;

    @BindView(R.id.tv_history_time)
    TextView mTvTime;
    @BindView(R.id.tv_history_content)
    TextView mTvContent;

    public InvoiceHistoryAdapter(Context context, int resource, List<InvoiceHistory> objects) {
        super(context, resource, objects);
        mContext = context;
        layoutId = resource;
        mInvoiceHistories = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(layoutId, parent, false);
        }
        ButterKnife.bind(this, convertView);
        if (mInvoiceHistories != null && !mInvoiceHistories.isEmpty()) {
            InvoiceHistory invoiceHistory = mInvoiceHistories.get(position);
            mTvTime.setText(invoiceHistory.getTime());
            mTvContent.setText(invoiceHistory.getContent());
        }
        return convertView;
    }
}
