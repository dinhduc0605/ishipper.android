package com.framgia.ishipper.presentation.route;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.directions.route.Route;
import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseToolbarActivity;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.net.data.ListRouteData;
import com.framgia.ishipper.util.Const;
import com.framgia.ishipper.util.MapUtils;
import com.framgia.ishipper.util.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Response;

public class RouteActivity extends BaseToolbarActivity implements
        RouteContract.View,
        OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private static final String TAG = "RouteActivity";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    @BindView(R.id.pb_route) ProgressBar mPbRoute;
    private Invoice mInvoice;
    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private LatLng mStartLatLng;
    private LatLng mFinishLatLng;
    private GoogleApiClient mGoogleApiClient;
    private BottomSheetBehavior<View> mBottomSheetBehavior;
    private RouteContract.Presenter mPresenter;

    @BindView(R.id.rv_detail_route) RecyclerView mRvGuidePath;
    @BindView(R.id.img_start_address) ImageView mImgStartAddress;
    @BindView(R.id.img_finish_address) ImageView mImgFinishAddress;
    @BindView(R.id.shipping_from) TextView mTvStartAddress;
    @BindView(R.id.orderEndAddress) TextView mTvFinishAddress;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.rc_empty_route) TextView mTvEmpty;
    @BindView(R.id.layout_bottom_sheet) View mBottomSheetView;

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mPresenter.initMap(mGoogleApiClient, mInvoice);
        mPresenter.showPath(mMap, mStartLatLng, mFinishLatLng);
        View view = ButterKnife.findById(RouteActivity.this, R.id.layoutSearch);
        int height = view.getMeasuredHeight() +
                ((ViewGroup.MarginLayoutParams) view.getLayoutParams()).topMargin * 2 +
                MapUtils.MAP_PADDING;
        mMap.setPadding(0, height, 0, (int) getResources().getDimension(R.dimen.rv_guide_path_height));
    }


    @Override
    public void setUpUI(LatLng startLatLng, LatLng finishLatLng, String startAddress,
                        String finishAddress, int startIcon, int finishIcon) {
        mTvStartAddress.setText(startAddress);
        mTvFinishAddress.setText(finishAddress);
        mStartLatLng = startLatLng;
        mFinishLatLng = finishLatLng;
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(startIcon))
                .position(mStartLatLng));
        mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(finishIcon))
                .position(mFinishLatLng));
    }

    @Override
    public void onGetListStepSuccess(Response<ListRouteData> response) {
        if (response.body() != null) {
            ArrayList<ListRouteData.Route> routes = response.body().getRoutes();
            if (routes != null && routes.size() > 0) {
                ArrayList<ListRouteData.Leg> legs = routes.get(0).getLegs();
                if (legs != null && legs.size() > 0) {
                    ArrayList<ListRouteData.Step> steps = legs.get(0).getSteps();
                    if (steps != null && steps.size() > 0) {
                        RouteGuideAdapter routeGuideAdapter = new RouteGuideAdapter(this, steps);
                        mRvGuidePath.setLayoutManager(new LinearLayoutManager(this));
                        mRvGuidePath.setAdapter(routeGuideAdapter);
                        mRvGuidePath.setVisibility(View.VISIBLE);
                    } else {
                        mTvEmpty.setText(R.string.not_have_guide_for_route);
                        mTvEmpty.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }

    @Override
    public void onGetListStepFail() {
        mTvEmpty.setVisibility(View.VISIBLE);
        mTvEmpty.setText(R.string.cant_get_guide_for_route);
    }

    @Override
    public void setVisibilityProgressBar(int visibility) {
        mPbRoute.setVisibility(visibility);
    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(
                permissions, grantResults, Manifest.permission.ACCESS_FINE_LOCATION)) {
            mPresenter.showPath(mMap, mStartLatLng, mFinishLatLng);
        }
    }

    @Override
    public void drawRoute(ArrayList<Route> routes) {
        PolylineOptions polyOptions = new PolylineOptions();
        for (int i = 0; i < routes.size(); i++) {
            Route route = routes.get(i);
            polyOptions.color(ContextCompat.getColor(getApplicationContext(), R.color.colorGreen));
            polyOptions.width(8);
            polyOptions.addAll(route.getPoints());
            Log.d(TAG, route.getPoints().size() + "");
        }
        mMap.addPolyline(polyOptions);
        MapUtils.zoomToBounds(mMap, polyOptions);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public int getActivityTitle() {
        return R.string.title_activity_route;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_route;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mMapFragment.getMapAsync(this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void initViews() {
        mPresenter = new RoutePresenter(this, this, this);
        mMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mInvoice = getIntent().getParcelableExtra(Const.KeyIntent.KEY_INVOICE);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();
        }
        mPresenter.getListStep(mInvoice.getAddressStart(), mInvoice.getAddressFinish());
        mBottomSheetBehavior = BottomSheetBehavior.from(mBottomSheetView);
    }
}
