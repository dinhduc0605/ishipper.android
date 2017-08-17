package com.framgia.ishipper.presentation.fb_invoice;

/**
 * Created by dinhduc on 21/11/2016.
 */

class FBInvoiceContract {
    interface View {

    }

    interface Presenter {
        void showPinnedInvoice();

        void backToFacebookInvoice();
    }
}
