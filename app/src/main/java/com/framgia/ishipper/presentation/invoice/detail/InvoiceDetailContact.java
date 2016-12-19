package com.framgia.ishipper.presentation.invoice.detail;

import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.model.User;

/**
 * Created by HungNT on 11/28/16.
 */

public class InvoiceDetailContact {
    interface View {
        void onGetInvoiceDetailSuccess(Invoice invoice);

        void setInvoiceStatus(Invoice invoice);

        void showActionButton(int statusCode);

        void showInvoiceData(Invoice invoice);

        void showUserData(User user);

        void onCancelledReceiveInvoice();
    }

    interface Presenter {
        void getInvoiceDetail(String invoiceId);

        void readNotification(String notiId);

        void receiveInvoice(String invoiceId);

        void report(Invoice invoice);

        void cancelInvoice(Invoice invoice);

        void takeInvoice(int invoiceId);

        void finishedInvoice(String stringId);

        void startMainActivity();

        void showRouteActivity(Invoice invoice);
    }
}
