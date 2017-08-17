package com.framgia.ishipper.presentation.invoice.shipping;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.ListInvoiceData;
import com.framgia.ishipper.presentation.invoice.detail.InvoiceDetailActivity;
import com.framgia.ishipper.util.Const;
import com.framgia.ishipper.util.MapUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by framgia on 18/11/2016.
 */

public class ShippingPresenter implements ShippingContract.Presenter {
    private static final int POLY_LINE_WIDTH = 8;
    private static final int[] COLORS = new int[]{
            Color.RED, Color.BLUE,
            Color.BLACK, Color.GREEN,
            Color.CYAN};

    private final ShippingContract.View mView;
    private BaseFragment mFragment;
    private Context mContext;
    private LatLng mCurrentLocation;
    private int mColorPos;

    public ShippingPresenter(ShippingContract.View view, BaseFragment fragment) {
        mView = view;
        mFragment = fragment;
        mContext = fragment.getContext();
    }

    @Override
    public void getListShippingInvoice() {
        API.getListShipperInvoices(Config.getInstance().getUserInfo(mContext).getAuthenticationToken(),
                Invoice.STATUS_SHIPPING, new API.APICallback<APIResponse<ListInvoiceData>>() {
                    @Override
                    public void onResponse(APIResponse<ListInvoiceData> response) {
                        List<Invoice> data = response.getData().getInvoiceList();
                        if (data == null || data.isEmpty()) {
                            mView.showEmptyData(true);
                            return;
                        }
                        mView.showEmptyData(false);
                        mView.showListShipping(data);
                        mView.showInvoiceToMap();
                        mView.showInvoiceDescSummary(data.get(0));
                        if (mCurrentLocation != null) showAllRoute(data);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        mView.showEmptyData(true);
                        mFragment.showUserMessage(message);
                    }
                });
    }

    @Override
    public void showInvoiceDetailActivity(int id) {
        Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Const.KEY_INVOICE_ID, String.valueOf(id));
        intent.putExtras(bundle);
        mFragment.startActivity(intent);
    }

    @Override
    public void showAllRoute(List<Invoice> invoiceList) {
        if (invoiceList == null || invoiceList.isEmpty()) return;
        if (mCurrentLocation == null) return;
        mView.addCurrentLocationToMap(mCurrentLocation);

        for (Invoice item : invoiceList) {
            MapUtils.routing(mCurrentLocation, new LatLng(item.getLatStart(), item.getLngStart()),
                    new RoutingListener() {
                        @Override
                        public void onRoutingFailure(RouteException e) {

                        }

                        @Override
                        public void onRoutingStart() {

                        }

                        @Override
                        public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
                            PolylineOptions polyOptions = new PolylineOptions();

                            for (int i = 0; i < route.size(); i++) {
                                int colorIndex = mColorPos++ % COLORS.length;

                                polyOptions.color(COLORS[colorIndex]);
                                polyOptions.width(POLY_LINE_WIDTH);
                                polyOptions.addAll(route.get(i).getPoints());
                            }
                            mView.showPath(polyOptions, mCurrentLocation);
                        }

                        @Override
                        public void onRoutingCancelled() {

                        }
                    });
        }
    }

    public void setCurrentLocation(LatLng currentLocation) {
        mCurrentLocation = currentLocation;
    }

    LatLng getCurrentLocation() {
        return mCurrentLocation;
    }
}
