package com.framgia.ishipper.presentation.invoice.invoice_creation;

import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.util.CommonUtils;
import com.framgia.ishipper.util.MapUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import java.util.ArrayList;

/**
 * Created by framgia on 18/11/2016.
 */

public class ShopCreateInvoiceStep1Presenter implements ShopCreateInvoiceStep1Contract.Presenter {
    private final ShopCreateInvoiceStep1Contract.View mView;
    private BaseFragment mFragment;
    private AsyncTask mGetDistanceTask;
    private LatLng mLatLngStart, mLatLngFinish;

    public ShopCreateInvoiceStep1Presenter(
            ShopCreateInvoiceStep1Contract.View view, BaseFragment fragment) {
        mView = view;
        mFragment = fragment;
    }

    @Override
    public void getDistance() {
        if (mLatLngStart == null || mLatLngFinish == null) return;
        if (mGetDistanceTask != null && !mGetDistanceTask.isCancelled()) {
            mGetDistanceTask.cancel(true);
        }
        mView.showMapLoadingIndicator(true);
        mGetDistanceTask = MapUtils.routing(mLatLngStart, mLatLngFinish, new RoutingListener() {
            @Override
            public void onRoutingFailure(RouteException e) {
                mView.showMapLoadingIndicator(false);
                mView.onDistanceError();
            }

            @Override
            public void onRoutingStart() {
                mView.showGetDistanceLoading();
            }

            @Override
            public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
                mView.showMapLoadingIndicator(false);
                int distance = arrayList.get(0).getDistanceValue();
                // Show distance
                mView.onDistanceResponse(CommonUtils.convertMetreToKm(distance));
            }

            @Override
            public void onRoutingCancelled() {
                mView.showMapLoadingIndicator(false);
            }
        });
    }

    @Override
    public void confirmPickLocation() {
        if (!validateInput()) return;
        mView.updateDoneUI();
        MapUtils.routing(mLatLngStart, mLatLngFinish, new RoutingListener() {
            @Override
            public void onRoutingFailure(RouteException e) {
                mView.showMapLoadingIndicator(false);
            }

            @Override
            public void onRoutingStart() {

            }

            @Override
            public void onRoutingSuccess(ArrayList<Route> arrayList, int i) {
                PolylineOptions polyOptions = new PolylineOptions();
                for (int j = 0; j < arrayList.size(); j++) {
                    polyOptions.color(ContextCompat
                            .getColor(mFragment.getContext().getApplicationContext(),
                                    R.color.colorGreen));
                    polyOptions.width(8);
                    polyOptions.addAll(arrayList.get(j).getPoints());
                }
                mView.onRoutingSuccess(polyOptions);
            }

            @Override
            public void onRoutingCancelled() {
            }
        });
    }

    public boolean validateInput() {
        if (mLatLngStart == null) {
            mFragment.showUserMessage(R.string.msg_start_location_require);
            return false;
        } else if (mLatLngFinish == null) {
            mFragment.showUserMessage(R.string.msg_end_location_require);
            return false;
        }
        return true;
    }

    @Override
    public void saveInvoiceData(String startAddress, String endAddress, float distance) {
        ShopCreateInvoiceActivity.sInvoice.setAddressStart(startAddress);
        ShopCreateInvoiceActivity.sInvoice.setLatStart((float) mLatLngStart.latitude);
        ShopCreateInvoiceActivity.sInvoice.setLngStart((float) mLatLngStart.longitude);
        ShopCreateInvoiceActivity.sInvoice.setAddressFinish(endAddress);
        ShopCreateInvoiceActivity.sInvoice.setLatFinish((float) mLatLngFinish.latitude);
        ShopCreateInvoiceActivity.sInvoice.setLngFinish((float) mLatLngFinish.longitude);
        ShopCreateInvoiceActivity.sInvoice.setDistance(distance);
    }

    @Override
    public void reset() {
        mLatLngStart = null;
        mLatLngFinish = null;
        mView.clear();
    }

    @Override
    public void saveLatLngStart(LatLng latLng) {
        mLatLngStart = latLng;
    }

    @Override
    public void saveLatLngEnd(LatLng latLng) {
        mLatLngFinish = latLng;
    }

    @Override
    public void setEndLocation(LatLng position) {
        mLatLngFinish = position;
        mView.onEndLocationSave(position);
    }

    @Override
    public void setStartLocation(LatLng position) {
        mLatLngStart = position;
        mView.onStartLocationSave(position);
    }

    @Override
    public void onSuggestStartClick(LatLng position) {
        mView.pickStartLocation();
        if (mLatLngFinish == null) return;
        setEndLocation(position);
    }

    @Override
    public void onSuggestEndClick(LatLng position) {
        mView.pickEndLocation();
        setStartLocation(position);
    }
}
