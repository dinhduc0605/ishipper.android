package com.framgia.ishipper.presentation.invoice.invoice_creation;

import com.framgia.ishipper.model.Invoice;

/**
 * Created by framgia on 18/11/2016.
 */

public interface ShopCreateInvoiceStep3Contract {
    interface View {

    }

    interface Presenter {
        void requestCreateInvoice(Invoice newInvoice);
        void startRouteActivity(Invoice invoice);
    }
}
