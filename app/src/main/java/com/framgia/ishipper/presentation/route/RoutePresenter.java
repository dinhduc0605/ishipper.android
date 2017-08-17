package com.framgia.ishipper.presentation.route;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.RoutingListener;
import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseToolbarActivity;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIDefinition;
import com.framgia.ishipper.net.data.ListRouteData;
import com.framgia.ishipper.util.Const;
import com.framgia.ishipper.util.MapUtils;
import com.framgia.ishipper.util.PermissionUtils;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.framgia.ishipper.util.Const.RequestCode.LOCATION_PERMISSION_REQUEST_CODE;

/**
 * Created by dinhduc on 22/11/2016.
 */

public class RoutePresenter implements RouteContract.Presenter {
    private static final String TAG = "RoutePresenter";
    private Context mContext;
    private RouteContract.View mView;
    private BaseToolbarActivity mActivity;

    public RoutePresenter(Context context, RouteContract.View view, BaseToolbarActivity activity) {
        mContext = context;
        mView = view;
        mActivity = activity;
    }

    @Override
    public void initMap(GoogleApiClient googleApiClient, Invoice invoice) {
        if (PermissionUtils.checkLocationPermission(mContext)) {
            return;
        }
        Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        switch (invoice.getStatusCode()) {
            case Invoice.STATUS_CODE_INIT:
                mView.setUpUI(
                        new LatLng(invoice.getLatStart(), invoice.getLngStart()),
                        new LatLng(invoice.getLatFinish(), invoice.getLngFinish()),
                        invoice.getAddressStart(),
                        invoice.getAddressFinish(),
                        R.drawable.ic_map_picker_start,
                        R.drawable.ic_map_picker_end
                );
                break;
            case Invoice.STATUS_CODE_WAITING:
                mView.setUpUI(
                        new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                        new LatLng(invoice.getLatStart(), invoice.getLngStart()),
                        mContext.getString(R.string.all_current_position),
                        invoice.getAddressStart(),
                        R.drawable.ic_current_position,
                        R.drawable.ic_map_picker_start
                );
                break;
            default:
                mView.setUpUI(
                        new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                        new LatLng(invoice.getLatFinish(), invoice.getLngFinish()),
                        mContext.getString(R.string.all_current_position),
                        invoice.getAddressFinish(),
                        R.drawable.ic_current_position,
                        R.drawable.ic_map_picker_end
                );
                break;
        }
    }

    @Override
    public void showPath(
            final GoogleMap map, final LatLng startLatLng, final LatLng finishLatLng) {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(mActivity, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        } else if (map != null) {
            map.setMyLocationEnabled(true);
            map.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    MapUtils.routing(finishLatLng, startLatLng, new RoutingListener() {
                        @Override
                        public void onRoutingFailure(RouteException e) {

                        }

                        @Override
                        public void onRoutingStart() {

                        }

                        @Override
                        public void onRoutingSuccess(ArrayList<Route> routes, int shortestRouteIndex) {
                            mView.drawRoute(routes);
                        }

                        @Override
                        public void onRoutingCancelled() {

                        }
                    });

                    map.setMyLocationEnabled(false);
                }
            });
        }
    }

    @Override
    public void getListStep(String startAddress, String finishAddress) {
        Map<String, String> userParams = new HashMap<>();
        userParams.put(APIDefinition.GetListRoutes.PARAM_ORIGIN, startAddress);
        userParams.put(APIDefinition.GetListRoutes.PARAM_DESTINATION, finishAddress);
        userParams.put(APIDefinition.GetListRoutes.PARAM_KEY, mContext.getString(R.string.google_maps_key));
        userParams.put(APIDefinition.PARAM_LANGUAGE, Const.Language.VIETNAMESE);
        mView.setVisibilityProgressBar(View.VISIBLE);
        API.getListRoutes(userParams, new Callback<ListRouteData>() {
            @Override
            public void onResponse(Call<ListRouteData> call, Response<ListRouteData> response) {
                mView.setVisibilityProgressBar(View.GONE);
                mView.onGetListStepSuccess(response);
            }

            @Override
            public void onFailure(Call<ListRouteData> call, Throwable t) {
                mView.setVisibilityProgressBar(View.GONE);
                mView.onGetListStepFail();
            }
        });
    }
}
