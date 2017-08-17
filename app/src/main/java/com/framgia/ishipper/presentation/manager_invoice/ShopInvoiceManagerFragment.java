package com.framgia.ishipper.presentation.manager_invoice;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.ishipper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vuduychuong1994 on 7/20/16.
 */
public class ShopInvoiceManagerFragment extends InvoiceManagerFragment {

    private List<String> mListInvoiceTitle;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        init();
        super.setOrderTitleList(mListInvoiceTitle);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void init() {
        mListInvoiceTitle = new ArrayList<>();
        mListInvoiceTitle.add(getString(R.string.tab_title_shop_order_wait));
        mListInvoiceTitle.add(getString(R.string.tab_title_shop_take_order));
        mListInvoiceTitle.add(getString(R.string.tab_title_shipping));
        mListInvoiceTitle.add(getString(R.string.tab_title_delivered));
        mListInvoiceTitle.add(getString(R.string.tab_title_finished));
        mListInvoiceTitle.add(getString(R.string.tab_title_cancelled));
        mListInvoiceTitle.add(getString(R.string.tab_title_all));
    }

}
