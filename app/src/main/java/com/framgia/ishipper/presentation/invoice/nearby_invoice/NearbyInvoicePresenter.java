package com.framgia.ishipper.presentation.invoice.nearby_invoice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.common.Log;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIDefinition;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.EmptyData;
import com.framgia.ishipper.net.data.ListInvoiceData;
import com.framgia.ishipper.net.data.ShowInvoiceData;
import com.framgia.ishipper.presentation.invoice.detail.InvoiceDetailActivity;
import com.framgia.ishipper.presentation.route.RouteActivity;
import com.framgia.ishipper.util.Const;
import com.framgia.ishipper.util.MapUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by dinhduc on 28/11/2016.
 */

public class NearbyInvoicePresenter implements NearbyInvoiceContract.Presenter {
    private static final String TAG = "NearbyInvoicePresenter";

    private Context mContext;
    private NearbyInvoiceContract.View mView;
    private BaseFragment mFragment;

    public NearbyInvoicePresenter(
            Context context, NearbyInvoiceContract.View view, BaseFragment fragment) {
        mContext = context;
        mView = view;
        mFragment = fragment;

    }

    @Override
    public void markInvoiceNearby(
            final ArrayList<Invoice> invoices, String authenToken, LatLng latLng, float radius) {
        Map<String, String> userParams = new HashMap<>();
        userParams.put(APIDefinition.GetInvoiceNearby.PARAM_USER_LAT, String.valueOf(latLng.latitude));
        userParams.put(APIDefinition.GetInvoiceNearby.PARAM_USER_LNG, String.valueOf(latLng.longitude));
        userParams.put(APIDefinition.GetInvoiceNearby.PARAM_USER_DISTANCE, String.valueOf(radius));
        API.getInvoiceNearby(authenToken, userParams,
                new API.APICallback<APIResponse<ListInvoiceData>>() {
                    @Override
                    public void onResponse(APIResponse<ListInvoiceData> response) {
                        Log.d(TAG, "onResponse: " + response.getMessage());
                        // update new invoices
                        ArrayList<Invoice> lastUpdateInvoices = (ArrayList<Invoice>) response
                                .getData().getInvoiceList();
                        // init common list invoices
                        ArrayList<Invoice> commonInvoices = new ArrayList<>(invoices);
                        // get common invoices between previous list invoices and new update invoices
                        commonInvoices.retainAll(lastUpdateInvoices);
                        // init list invoices need to be removed
                        ArrayList<Invoice> removeInvoices = new ArrayList<>(invoices);
                        // init list invoices need to be added
                        ArrayList<Invoice> addInvoices = new ArrayList<>(lastUpdateInvoices);
                        // get invoices need to be added
                        addInvoices.removeAll(commonInvoices);
                        // get invoices need to be removed
                        removeInvoices.removeAll(commonInvoices);
                        mView.addListMarker(addInvoices);
                        mView.removeListMarker(removeInvoices);
                        // update invoices
                        mView.updateInvoices(lastUpdateInvoices);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        mView.removeLoading();
                        mFragment.showUserMessage(message);
                        Log.d(TAG, message);
                    }
                });
    }

    @Override
    public void receiveInvoice(final String invoiceId) {
        mFragment.showLoadingDialog();
        API.postShipperReceiveInvoice(
                Config.getInstance().getUserInfo(mContext).getAuthenticationToken(),
                invoiceId,
                new API.APICallback<APIResponse<ShowInvoiceData>>() {
                    @Override
                    public void onResponse(APIResponse<ShowInvoiceData> response) {
                        if (response.getData().mInvoice == null) return;
                        mView.updateStatusReceiveInvoice(invoiceId,
                                response.getData().mInvoice.getUserInvoiceId());
                        mView.onReceiveInvoiceSuccess(response.getMessage(),
                                response.getData().mInvoice);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        mView.onReceiveInvoiceFail(message);
                    }
                }
        );
    }

    @Override
    public void getRoute(final LatLng startAddress, final LatLng finishAddress) {
        MapUtils.routing(startAddress, finishAddress,
                new RoutingListener() {
                    @Override
                    public void onRoutingFailure(RouteException e) {
                    }

                    @Override
                    public void onRoutingStart() {
                    }

                    @Override
                    public void onRoutingSuccess(ArrayList<Route> routes,
                                                 int shortestRouteIndex) {
                        mView.drawRoute(routes);
                    }

                    @Override
                    public void onRoutingCancelled() {
                    }
                });
    }

    @Override
    public void showPath(Invoice invoice) {
        Intent showPathIntent = new Intent(mContext, RouteActivity.class);
        showPathIntent.putExtra(Const.KeyIntent.KEY_INVOICE, invoice);
        mFragment.getActivity().startActivity(showPathIntent);
    }

    @Override
    public void clickSearchView() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .build();
            Intent searchIntent = new PlaceAutocomplete
                    .IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(typeFilter)
                    .build(mFragment.getActivity());
            mFragment.startActivityForResult(
                    searchIntent, Const.RequestCode.PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showInvoiceDetail(Invoice invoice) {
        Intent intent = new Intent(mContext, InvoiceDetailActivity.class);
        Bundle extras = new Bundle();
        extras.putString(Const.KEY_INVOICE_ID, invoice.getStringId());
        intent.putExtras(extras);
        mFragment.startActivityForResult(intent, Const.RequestCode.REQUEST_CODE_INVOICE_DETAIL);
    }

    @Override
    public void updateCurrentLocation(User currentUser) {
        ((BaseActivity) mContext).showDialog();
        HashMap<String, String> params = new HashMap<>();
        params.put(APIDefinition.UserSetting.PARAM_LATITUDE, String.valueOf(currentUser.getLatitude()));
        params.put(APIDefinition.UserSetting.PARAM_LONGITUDE, String.valueOf(currentUser.getLongitude()));
        API.updateUserSetting(currentUser.getAuthenticationToken(),
                params,
                new API.APICallback<APIResponse<EmptyData>>() {
                    @Override
                    public void onResponse(APIResponse<EmptyData> response) {
                        ((BaseActivity) mContext).dismissDialog();
                        //TODO: on update current location success
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        ((BaseActivity) mContext).dismissDialog();
                        //TODO: on update current location fail
                    }
                });
    }

    @Override
    public void cancelAcceptOrder(final Invoice invoice) {
        mFragment.showLoadingDialog();
        API.putCancelReceiveOrder(Config.getInstance().getUserInfo(mContext).getAuthenticationToken(),
                invoice.getUserInvoiceId(), new API.APICallback<APIResponse<EmptyData>>() {
                    @Override
                    public void onResponse(APIResponse<EmptyData> response) {
                        mFragment.dismissLoadingDialog();
                        mView.updateStatusReceiveInvoice(invoice.getStringId(), Invoice.INVALID_USER_INVOICE);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        mFragment.dismissLoadingDialog();
                        mFragment.showUserMessage(message);
                    }
                });
    }
}
