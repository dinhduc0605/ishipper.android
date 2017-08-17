package com.framgia.ishipper.presentation.manager_invoice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.common.Log;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.presentation.manager_invoice.InvoiceManagerContract.Presenter;
import com.framgia.ishipper.util.Const;
import com.framgia.ishipper.widget.dialog.ReviewDialog;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;

public class InvoiceManagerFragment extends BaseFragment implements
        ListInvoiceFragment.OnActionClickListener, InvoiceManagerContract.View {
    private static final String TAG = "InvoiceManagerFragment";
    @BindView(R.id.viewpager) ViewPager mViewPager;
    @BindView(R.id.tabs) TabLayout mTabLayout;

    private List<ListInvoiceFragment> mListOrderFragment;
    private OrderManagerPagerAdapter mOrderManagerPagerAdapter;
    private List<String> mOrderTitleList;
    private Context mContext;
    private BaseActivity mActivity;
    private Presenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (mContext instanceof BaseActivity) mActivity = (BaseActivity) mContext;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        mPresenter = new InvoiceManagerPresenter(this, mActivity, this);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_order_manager;
    }

    @Override
    public void initViews() {
        mListOrderFragment = new ArrayList<>();
        mOrderManagerPagerAdapter =
                new OrderManagerPagerAdapter(getFragmentManager(), mListOrderFragment, mOrderTitleList);
        mViewPager.setAdapter(mOrderManagerPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        for (int type = 0; type <= Const.SIZE_INVOICE_STATUS; type++) {
            String titleTab;
            if (mOrderTitleList.size() > type) {
                titleTab = mOrderTitleList.get(type);
            } else {
                titleTab = getContext().getString(R.string.tab_title_unknow);
            }
            ListInvoiceFragment listInvoiceFragment =
                    ListInvoiceFragment.newInstance(titleTab, type);
            listInvoiceFragment.setOnActionClickListener(this);
            mListOrderFragment.add(listInvoiceFragment);
            mOrderManagerPagerAdapter.notifyDataSetChanged();
        }
    }

    public void setOrderTitleList(List<String> orderTitleList) {
        mOrderTitleList = orderTitleList;
    }

    @Override
    public void notifyChangeTab(final int type) {
        if (mListOrderFragment.size() > type && mListOrderFragment.get(type).isAvaiable()) {
            mListOrderFragment.get(type).notifyChangedData(null);
        }
    }

    @Override
    public void notifyChangeTab(final int type, final boolean isShowNewInvoice, final int invoiceId) {
        mViewPager.setCurrentItem(type, true);
        if (mListOrderFragment.size() > type && mListOrderFragment.get(type).isAvaiable()) {
            mListOrderFragment.get(type)
                    .notifyChangedData(new ListInvoiceFragment.OnGetInvoiceListener() {
                        @Override
                        public void onGetInvoiceSuccess() {
                            if (isShowNewInvoice) showInvoiceOnScreen(type, invoiceId);
                        }

                        @Override
                        public void onGetInvoiceFail() {
                            //TODO: when error
                        }
                    });
        }
    }

    @Override
    public void showReviewDialog(Invoice invoice) {
        new ReviewDialog(mActivity, invoice.getStringId()).show();
    }

    @Override
    public void removeInvoice(int invoiceId) {
        ListInvoiceFragment listFragment = mListOrderFragment.get(mViewPager.getCurrentItem());
        if (listFragment == null) return;
        Iterator<Invoice> iterator = listFragment.getInvoiceList().iterator();
        while (iterator.hasNext()) {
            Invoice invoice = iterator.next();
            if (invoice.getId() == invoiceId) {
                listFragment.getInvoiceList().remove(invoice);
                listFragment.getInvoiceAdapter().notifyDataSetChanged();
                break;
            }
        }
        mPresenter.syncData(mViewPager, mListOrderFragment, false);
    }

    @Override
    public void showInvoiceOnScreen(int statusCodeShipping, int invoiceId) {
        mListOrderFragment.get(statusCodeShipping).moveListToInvoice(invoiceId);
    }

    @Override
    public void onClickAction(final Invoice invoice) {
        switch (invoice.getStatusCode()) {
            case Invoice.STATUS_CODE_WAITING:
                mPresenter.actionTakeInvoice(mViewPager.getCurrentItem(), invoice);
                break;
            case Invoice.STATUS_CODE_SHIPPING:
                mPresenter.actionShippedInvoice(mViewPager.getCurrentItem(), invoice);
                break;
            case Invoice.STATUS_CODE_SHIPPED:
                mPresenter.actionFinishInvoice(mViewPager.getCurrentItem(), invoice);
                break;
        }
    }

    @Override
    public void onClickCancel(Invoice invoice) {
        notifyChangeTab(invoice.getStatusCode());
        notifyChangeTab(Invoice.STATUS_CODE_CANCEL, true, invoice.getId());
    }

    @Override
    public void onClickView(Invoice invoice) {
        Log.d("onClick item fragment", invoice.getAddressStart() + "null");
        mPresenter.startDetailInvoiceActivity(invoice);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (data == null) return;
            int invoiceId;
            try {
                invoiceId = Integer.parseInt(data.getStringExtra(Const.KEY_INVOICE_ID));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return;
            }
            switch (requestCode) {
                case Const.RequestCode.REQUEST_CODE_CHOOSE_SHIPPER:
                    notifyChangeTab(Invoice.STATUS_CODE_WAITING, true, invoiceId);
                    break;
                case Const.RequestCode.REQUEST_CODE_INVOICE_DETAIL:
                    removeInvoice(invoiceId);
                    if (mPresenter != null) mPresenter.syncData(mViewPager, mListOrderFragment, false);
                    break;
            }
        }
    }

    public class OrderManagerPagerAdapter extends FragmentStatePagerAdapter {
        private List<ListInvoiceFragment> mListInvoiceFragments;

        private List<String> mListTitle;

        OrderManagerPagerAdapter(FragmentManager fm, List<ListInvoiceFragment> fragmentList,
                                 List<String> orderTitleList) {
            super(fm);
            mListInvoiceFragments = fragmentList;
            mListTitle = orderTitleList;
        }

        @Override
        public Fragment getItem(int position) {
            return mListInvoiceFragments.get(position);
        }

        @Override
        public int getCount() {
            return mListInvoiceFragments == null ? 0 : mListInvoiceFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mListTitle.get(position);
        }

    }
}
