package com.framgia.ishipper.ui.listener;

import com.framgia.ishipper.model.Invoice;

/**
 * Created by HungNT on 11/14/16.
 */

public interface OnInvoiceUpdate {
    void onInvoiceCreate(Invoice invoice);
    void onInvoiceRemove(Invoice invoice);
}
