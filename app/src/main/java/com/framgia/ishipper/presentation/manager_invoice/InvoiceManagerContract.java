package com.framgia.ishipper.presentation.manager_invoice;
import android.support.v4.view.ViewPager;
import com.framgia.ishipper.model.Invoice;
import java.util.List;

/**
 * Created by vuduychuong1994 on 11/23/16.
 */

public interface InvoiceManagerContract {
    interface View {

        void showReviewDialog(Invoice invoice);

        void removeInvoice(int invoiceId);

        void showInvoiceOnScreen(int statusCodeShipping, int invoiceId);

        void notifyChangeTab(final int type);

        void notifyChangeTab(final int type, final boolean isShowNewInvoice, final int invoiceId);
    }

    interface Presenter {

        void actionTakeInvoice(int statusCode, Invoice invoice);

        void actionShippedInvoice(int statusCode, Invoice invoice);

        void actionFinishInvoice(int statusCode, Invoice invoice);

        void startDetailInvoiceActivity(Invoice invoice);

        void syncData(ViewPager viewPager, List<ListInvoiceFragment> listOrderFragment, boolean isSyncCurrentTab);
    }
}
