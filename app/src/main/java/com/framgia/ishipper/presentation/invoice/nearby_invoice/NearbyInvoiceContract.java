package com.framgia.ishipper.presentation.invoice.nearby_invoice;

import com.directions.route.Route;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.model.User;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dinhduc on 28/11/2016.
 */

public interface NearbyInvoiceContract {
    interface View {
        void showReceiveDialog(final String invoiceId);

        void addListMarker(List<Invoice> invoiceList);

        void removeListMarker(List<Invoice> invoiceList);

        void updateInvoices(List<Invoice> invoices);

        void onReceiveInvoiceSuccess(String message, Invoice invoice);

        void onReceiveInvoiceFail(String message);

        void showInvoiceDetailWindow(Invoice invoice);

        void hideInvoiceDetailWindow();

        void drawRoute(ArrayList<Route> route);

        void updateStatusReceiveInvoice(String invoiceId, int userInvoiceId);

        void removeLoading();

        void hideSearchArea();

        void showSearchArea();

        void showMapLoadingIndicator(boolean isActive);

    }

    interface Presenter {
        void markInvoiceNearby(
                ArrayList<Invoice> invoices, String authenToken, LatLng latLng, float radius);

        void receiveInvoice(String invoiceId);

        void getRoute(LatLng startAddress, LatLng finishAddress);

        void showPath(Invoice invoice);

        void clickSearchView();

        void showInvoiceDetail(Invoice invoice);

        void updateCurrentLocation(User currentUser);

        void cancelAcceptOrder(Invoice invoice);
    }
}
