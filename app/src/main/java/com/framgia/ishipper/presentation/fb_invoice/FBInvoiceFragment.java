package com.framgia.ishipper.presentation.fb_invoice;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.base.BaseToolbarActivity;
import com.framgia.ishipper.model.FbInvoice;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FBInvoiceFragment extends BaseFragment implements FBInvoiceContract.View {
    private static final String BUNDLE_SHOW_PIN = "BUNDLE_SHOW_PIN";
    @BindView(R.id.rvInvoices) RecyclerView mRvInvoices;
    @BindView(R.id.btnPinnedInvoice) RelativeLayout mBtnPinnedInvoice;
    @BindView(R.id.btnBackToFacebook) RelativeLayout mBtnBackToFacebook;
    private FBInvoicesAdapter mAdapter;
    private List<FbInvoice> mListInvoices = new ArrayList<>();
    private FBInvoicePresenter mPresenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public int getLayoutId() {
        return R.layout.fragment_facebook_invoice;
    }


    @Override
    public void initViews() {
        mPresenter = new FBInvoicePresenter(this, (BaseToolbarActivity) getActivity());
        // INVISIBLE show pin button
        if (!getArguments().getBoolean(BUNDLE_SHOW_PIN, true)) {
            mBtnPinnedInvoice.setVisibility(View.GONE);
            mBtnBackToFacebook.setVisibility(View.VISIBLE);
        }
        getSampleData();
        mAdapter = new FBInvoicesAdapter(getContext(), mListInvoices);
        mRvInvoices.setLayoutManager(new LinearLayoutManager(getContext()));
        mRvInvoices.setAdapter(mAdapter);
    }

    public static FBInvoiceFragment newInstance(boolean showPinButton) {
        Bundle args = new Bundle();
        FBInvoiceFragment fragment = new FBInvoiceFragment();
        args.putBoolean(BUNDLE_SHOW_PIN, showPinButton);
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.btnPinnedInvoice, R.id.btnBackToFacebook})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnPinnedInvoice:
                mPresenter.showPinnedInvoice();
                break;
            case R.id.btnBackToFacebook:
                mPresenter.backToFacebookInvoice();
                break;
        }
    }

    private void getSampleData() {
        for (int i = 0; i < 30; i++) {
            mListInvoices.add(FbInvoice.getSample());
        }
    }

}
