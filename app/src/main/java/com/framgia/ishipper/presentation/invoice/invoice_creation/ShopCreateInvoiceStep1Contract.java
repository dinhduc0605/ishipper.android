package com.framgia.ishipper.presentation.invoice.invoice_creation;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

/**
 * Created by framgia on 18/11/2016.
 */

public interface ShopCreateInvoiceStep1Contract {
    interface View {
        void moveTo(Marker marker);

        void onDistanceResponse(float distance);

        void showGetDistanceLoading();

        void onRoutingSuccess(PolylineOptions polyOptions);

        void updateDoneUI();

        void pickStartLocation();

        void pickEndLocation();

        void clear();

        void onEndLocationSave(LatLng endLocation);

        void onStartLocationSave(LatLng startLocation);

        void showMapLoadingIndicator(boolean isActive);

        void onDistanceError();
    }

    interface Presenter {
        void getDistance();

        void confirmPickLocation();

        void saveInvoiceData(String startAddress, String endAddress, float distance);

        void reset();

        void saveLatLngStart(LatLng latLng);

        void saveLatLngEnd(LatLng latLng);

        void setEndLocation(LatLng position);

        void setStartLocation(LatLng position);

        void onSuggestStartClick(LatLng position);

        void onSuggestEndClick(LatLng position);
    }
}
