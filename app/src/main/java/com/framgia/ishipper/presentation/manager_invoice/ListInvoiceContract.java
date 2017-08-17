package com.framgia.ishipper.presentation.manager_invoice;
import android.content.Context;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.model.User;
import java.util.List;

/**
 * Created by vuduychuong1994 on 11/24/16.
 */

public interface ListInvoiceContract {

    interface View {

        void showLoading();

        void dismissLoading();

        void initAdapter(Context context);

        void notifyChangedData(ListInvoiceFragment.OnGetInvoiceListener listener);

        void moveListToInvoice(int invoiceId);

        void setEvent();

        void addListInvoice(List<Invoice> invoiceList);

        void onCancelledReceiveInvoice();

        void removeInvoice(Invoice invoice, int position);
    }

    interface Presenter {

        void getInvoice(String role, String authenticationToken, int statusCode,
                        ListInvoiceFragment.OnGetInvoiceListener callback);

        void searchInvoice(
                String role, String authenticationToken, int statusCode, String nameSearch,
                ListInvoiceFragment.OnGetInvoiceListener callback);

        void cancelReceiveInvoice(User currentUser, Invoice invoice, int position);

        void startListShipperRegActivity(Invoice invoice);
    }
}
