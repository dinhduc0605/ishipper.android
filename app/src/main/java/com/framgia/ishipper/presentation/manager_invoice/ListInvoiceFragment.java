package com.framgia.ishipper.presentation.manager_invoice;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.model.SocketResponse;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.ui.view.EmptyView;
import com.framgia.ishipper.util.Const;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;

public class ListInvoiceFragment extends BaseFragment
        implements InvoiceAdapter.OnClickCancelListener, InvoiceAdapter.OnClickActionListener,
        InvoiceAdapter.OnclickViewListener, ListInvoiceContract.View {
    private static final String TAG = "ListInvoiceFragment";
    private static final String TAB_TITLE = "title";
    private static final String STATUS_CODE = "status";

    @BindView(R.id.list) RecyclerView mRecyclerView;
    @BindView(R.id.layout_loading) RelativeLayout mLayoutLoading;
    @BindView(R.id.layout_refresh) SwipeRefreshLayout mLayoutRefresh;
    @BindView(R.id.edtSearch) EditText mEdtSearch;
    @BindView(R.id.imgSearch) ImageView mImgSearch;
    @BindView(R.id.emptyView) EmptyView mEmptyView;

    private String mTitle;
    private int mStatusCode;
    private OnActionClickListener mOnActionClickListener;
    private LinearLayoutManager mLayoutManager;
    private List<Invoice> mInvoiceList;
    private InvoiceAdapter mInvoiceAdapter;
    private User mCurrentUser;
    private Context mContext;
    private BaseActivity mActivity;
    private ListInvoiceContract.Presenter mPresenter;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent == null) { return; }
            switch (intent.getAction()) {
                case Const.ACTION_CANCEL_INVOICE:
                    String responseStr = intent.getStringExtra(Const.KEY_SOCKET_RESPONSE);
                    SocketResponse socketResponse =
                            new Gson().fromJson(responseStr, SocketResponse.class);
                    if (socketResponse != null) {
                        Invoice invoice = socketResponse.getInvoice();
                        if (invoice == null) { return; }
                        changeCountShipperReg(invoice);
                    }
                    break;
                case Const.ACTION_CREATE_INVOICE:
                case Const.ACTION_NEW_NOTIFICATION:
                    if (intent.hasExtra(Const.KEY_INVOICE)) {
                        Invoice invoice =
                                new Gson().fromJson(intent.getStringExtra(Const.KEY_INVOICE),
                                        Invoice.class);
                        switch (mStatusCode) {
                            case Invoice.STATUS_CODE_INIT:
                                if (mCurrentUser.isShop() &&
                                    intent.getAction().equals(Const.ACTION_NEW_NOTIFICATION)) {
                                    changeCountShipperReg(invoice);
                                } else {
                                    updateInvoice(invoice);
                                }
                                break;
                            default:
                                updateInvoice(invoice);
                                break;
                        }
                    }
                    break;
            }
        }
    };

    private void updateInvoice(Invoice invoice) {
        boolean isHave = false;
        for (Invoice in : mInvoiceList) {
            if (in.getId() == invoice.getId()) {
                isHave = true;
                if (invoice.getStatusCode() != mStatusCode) {
                    mInvoiceList.remove(in);
                    mInvoiceAdapter.setPositionHighlight(Const.POSITION_HIGHLIGHT_DEFAULT);
                    mInvoiceAdapter.notifyDataSetChanged();
                    if (mEmptyView != null) mEmptyView.active(mInvoiceList.isEmpty());
                    return;
                }
            }
        }
        if (!isHave && invoice.getStatusCode() == mStatusCode &&
            ((mCurrentUser.isShop() && !invoice.isReceived()) ||
             (!mCurrentUser.isShop() && invoice.isReceived()))) {
            mInvoiceList.add(Const.HEAD_LIST, invoice);
            mInvoiceAdapter.setPositionHighlight(Const.HEAD_LIST);
            if (mEmptyView != null) mEmptyView.active(mInvoiceList.isEmpty());
            mInvoiceAdapter.notifyDataSetChanged();
        }
    }

    private void changeCountShipperReg(Invoice invoice) {
        for (Invoice in : mInvoiceList) {
            if (in.getId() == invoice.getId()) {
                in.setNumOfRecipient(invoice.getNumOfRecipient());
                mInvoiceAdapter.notifyDataSetChanged();
                return;
            }
        }
    }

    public static ListInvoiceFragment newInstance(String title, int status) {
        ListInvoiceFragment fragment = new ListInvoiceFragment();
        Bundle args = new Bundle();
        args.putString(TAB_TITLE, title);
        args.putInt(STATUS_CODE, status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTitle = getArguments().getString(TAB_TITLE);
            mStatusCode = getArguments().getInt(STATUS_CODE, Invoice.STATUS_CODE_ALL);
        }
        mCurrentUser = Config.getInstance().getUserInfo(mContext);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Const.ACTION_NEW_NOTIFICATION);
        intentFilter.addAction(Const.ACTION_CANCEL_INVOICE);
        intentFilter.addAction(Const.ACTION_CREATE_INVOICE);
        getActivity().registerReceiver(mReceiver, intentFilter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setEvent();
    }

    @Override
    public int getLayoutId() {
        return R.layout.tab_order_list;
    }

    @Override
    public void initViews() {
        mCurrentUser = Config.getInstance().getUserInfo(mContext);
        mPresenter = new ListInvoicePresenter(mActivity, this);
        mLayoutManager = new LinearLayoutManager(mContext);
        mRecyclerView.setLayoutManager(mLayoutManager);
        initAdapter(mContext);
        mRecyclerView.setAdapter(mInvoiceAdapter);
        mLayoutRefresh.setColorSchemeColors(Color.GREEN, Color.RED, Color.BLUE);
        mPresenter.getInvoice(Config.getInstance().getUserInfo(mContext).getRole(),
                Config.getInstance().getUserInfo(mContext).getAuthenticationToken(), mStatusCode,
                null);
    }

    @Override
    public void setEvent() {
        mInvoiceAdapter.setClickCancelListener(this);
        mInvoiceAdapter.setClickActionListener(this);
        mLayoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getInvoice(mCurrentUser.getRole(), mCurrentUser.getAuthenticationToken(),
                        mStatusCode, null);
            }
        });
        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mEdtSearch.getText().toString().equals("")) {
                    mImgSearch.setVisibility(View.VISIBLE);
                } else {
                    mImgSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mInvoiceList.clear();
                mPresenter.searchInvoice(Config.getInstance().getUserInfo(getContext()).getRole(),
                        Config.getInstance().getUserInfo(getContext()).getAuthenticationToken(),
                        mStatusCode, mEdtSearch.getText().toString(), null);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK || data == null) return;
        Fragment fragment =
                getFragmentManager().findFragmentByTag(InvoiceManagerFragment.class.getName());
        if (fragment != null) fragment.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.RequestCode.REQUEST_CODE_CHOOSE_SHIPPER) {notifyChangedData(null);
        }
    }

    @Override
    public void addListInvoice(List<Invoice> invoiceList) {
        mInvoiceList.clear();
        mInvoiceList.addAll(invoiceList);
        if (mEmptyView != null) mEmptyView.active(mInvoiceList.isEmpty());
        mInvoiceAdapter.setPositionHighlight(Const.DEFAULT_HIGHLIGHT_POSITION);
        mInvoiceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCancelledReceiveInvoice() {
        mInvoiceAdapter.notifyDataSetChanged();
    }

    @Override
    public void removeInvoice(Invoice invoice, int position) {
        mInvoiceList.remove(position);
        mInvoiceAdapter.notifyItemRemoved(position);
    }

    @Override
    public void notifyChangedData(OnGetInvoiceListener listener) {
        initAdapter(mContext);
        if (mPresenter == null || mCurrentUser == null) return;
        mPresenter.getInvoice(mCurrentUser.getRole(), mCurrentUser.getAuthenticationToken(),
                mStatusCode, listener);
    }

    @Override
    public void moveListToInvoice(int invoiceId) {
        if (invoiceId < 0) return;
        for (int i = 0; i < mInvoiceList.size(); i++) {
            if (invoiceId == mInvoiceList.get(i).getId()) {
                mLayoutManager.scrollToPosition(i);
                mInvoiceAdapter.setPositionHighlight(i);
                mInvoiceAdapter.notifyItemChanged(i);
            }
        }
    }

    @Override
    public void initAdapter(Context context) {
        if (context == null) return;
        if (mInvoiceList == null) {
            mInvoiceList = new ArrayList<>();
        }
        if (mInvoiceAdapter == null) {
            mInvoiceAdapter = new InvoiceAdapter(context, mInvoiceList, this);
        }
    }

    @Override
    public void dismissLoading() {
        if (mLayoutLoading != null) mLayoutLoading.setVisibility(View.GONE);
        if (mRecyclerView != null) mRecyclerView.setVisibility(View.VISIBLE);
        if (mLayoutRefresh != null && mLayoutRefresh.isRefreshing()) {
            mLayoutRefresh.setRefreshing(false);
        }
    }

    @Override
    public void showLoading() {
        if (mLayoutLoading != null) mLayoutLoading.setVisibility(View.VISIBLE);
        if (mRecyclerView != null) mRecyclerView.setVisibility(View.GONE);
    }

    public List<Invoice> getInvoiceList() {
        return mInvoiceList;
    }

    public void setInvoiceList(List<Invoice> invoiceList) {
        mInvoiceList = invoiceList;
    }

    public InvoiceAdapter getInvoiceAdapter() {
        return mInvoiceAdapter;
    }

    public String getTitle() {
        if (mTitle != null) {
            return mTitle;
        } else {
            return "";
        }
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setOnActionClickListener(OnActionClickListener onActionClickListener) {
        mOnActionClickListener = onActionClickListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (context instanceof BaseActivity) {
            mActivity = (BaseActivity) context;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) getActivity().unregisterReceiver(mReceiver);
    }

    @Override
    public void onClickCancelListener(Invoice invoice, int position) {
        if (!mCurrentUser.isShop() && invoice.getStatusCode() == Invoice.STATUS_CODE_INIT) {
            mPresenter.cancelReceiveInvoice(mCurrentUser, invoice, position);
        } else if (mOnActionClickListener != null) {
            mOnActionClickListener.onClickCancel(invoice);
        }
    }

    @Override
    public void onClickActionListener(Invoice invoice) {
        if (mOnActionClickListener != null) {
            if (invoice.getStatusCode() == Invoice.STATUS_CODE_INIT) {
                mPresenter.startListShipperRegActivity(invoice);
            } else {
                mOnActionClickListener.onClickAction(invoice);
            }
        }
    }

    @Override
    public void onclickViewListener(Invoice invoice) {
        if (mOnActionClickListener != null) {
            mOnActionClickListener.onClickView(invoice);
        }
    }

    public boolean isAvaiable() {
        return !(mCurrentUser == null || mPresenter == null || mContext == null);
    }

    public interface OnActionClickListener {
        void onClickAction(Invoice invoice);

        void onClickCancel(Invoice invoice);

        void onClickView(Invoice invoice);
    }

    public interface OnGetInvoiceListener {
        void onGetInvoiceSuccess();

        void onGetInvoiceFail();
    }
}
