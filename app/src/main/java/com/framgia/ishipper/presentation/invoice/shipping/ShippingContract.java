package com.framgia.ishipper.presentation.invoice.shipping;

import com.framgia.ishipper.model.Invoice;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.List;

/**
 * Created by framgia on 18/11/2016.
 */

public interface ShippingContract {
    interface View {

        void showListShipping(List<Invoice> invoiceList);

        void showInvoiceToMap();

        void showEmptyData(boolean isEmpty);

        void showInvoiceDescSummary(Invoice invoice);

        void showPath(PolylineOptions polyOptions, LatLng currentPos);

        void addCurrentLocationToMap(LatLng location);
    }

    interface Presenter {
        void getListShippingInvoice();

        void showInvoiceDetailActivity(int id);

        void showAllRoute(List<Invoice> invoiceList);
    }
}
