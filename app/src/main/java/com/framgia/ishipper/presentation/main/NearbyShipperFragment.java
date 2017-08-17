package com.framgia.ishipper.presentation.main;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.ui.listener.LocationSettingCallback;
import com.framgia.ishipper.ui.listener.OnShipperUpdateListener;
import com.framgia.ishipper.util.CommonUtils;
import com.framgia.ishipper.util.Const;
import com.framgia.ishipper.util.MapUtils;
import com.framgia.ishipper.util.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.HashMap;
import java.util.List;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by dinhduc on 20/07/2016.
 */
public class NearbyShipperFragment extends BaseFragment
        implements NearbyShipperContract.View, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnShipperUpdateListener, GoogleMap.OnCameraIdleListener {
    private static final String TAG = "NearbyShipperFragment";
    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private GoogleMap mGoogleMap;
    private HashMap<Marker, User> mShipperMap = new HashMap<>();
    private SupportMapFragment mMapFragment;
    private User mCurrentUser;
    private Context mContext;
    private Dialog mDialog;
    private NearbyShipperPresenter mPresenter;

    @BindView(R.id.tv_main_search_area) TextView mTvSearchArea;
    @BindView(R.id.progress_map_loading) ProgressBar mMapLoadingProgress;
    private LatLng mLastCameraPosition;

    public static NearbyShipperFragment newInstance() {
        NearbyShipperFragment fragment = new NearbyShipperFragment();
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_nearby_shipper;
    }

    @Override
    public void initViews() {
        mPresenter = new NearbyShipperPresenter(this, this);
        mCurrentUser = Config.getInstance().getUserInfo(mContext);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setShipperUpdateListener(this);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_nearby_shipper);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this)
                                                                    .addOnConnectionFailedListener(
                                                                            this)
                                                                    .addApi(LocationServices.API)
                                                                    .addApi(Places.GEO_DATA_API)
                                                                    .addApi(Places.PLACE_DETECTION_API)
                                                                    .build();
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (PermissionUtils.checkLocationPermission(mContext)) return;
        mGoogleMap = googleMap;
        googleMap.setMyLocationEnabled(true);
        // Init map
        // Disable tilt and rotate map
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(false);

        if (PermissionUtils.checkLocationPermission(mContext)) return;
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation == null) {
            mDialog = CommonUtils.showLoadingDialog(mContext);
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, new LocationRequest(),
                            NearbyShipperFragment.this);
        } else {
            onLocationChange(mLocation);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        // check location permission
        if (!PermissionUtils.checkLocationPermission(mContext)) {
            // check location setting
            CommonUtils.checkLocationRequestSetting(getActivity(), mGoogleApiClient,
                    new LocationSettingCallback() {
                        @Override
                        public void onSuccess() {
                            mMapFragment.getMapAsync(NearbyShipperFragment.this);
                        }
                    });
        } else {
            // request location permission
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(),
                    Const.RequestCode.LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    mPresenter.getPlace(data);
                    break;
                case PlaceAutocomplete.RESULT_ERROR:
                    Status status = PlaceAutocomplete.getStatus(mContext, data);
                    showUserMessage(status.getStatusMessage());
                    break;
            }
        } else if (requestCode == Const.RequestCode.REQUEST_CHECK_SETTINGS) {
            //            location setting is set up
            if (resultCode == Activity.RESULT_OK) {
                mMapFragment.getMapAsync(NearbyShipperFragment.this);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            CommonUtils.checkLocationRequestSetting(getActivity(), mGoogleApiClient,
                    new LocationSettingCallback() {
                        @Override
                        public void onSuccess() {
                            mMapFragment.getMapAsync(NearbyShipperFragment.this);
                        }
                    });
        }
    }

    @OnClick(R.id.rl_search_view)
    public void onClick() {
        mPresenter.startSearchPlace();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        onLocationChange(location);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        mDialog.dismiss();
    }

    private void onLocationChange(Location location) {
        mCurrentUser.setLatitude(location.getLatitude());
        mCurrentUser.setLongitude(location.getLongitude());
        MapUtils.zoomToPosition(mGoogleMap,
                new LatLng(location.getLatitude(), location.getLongitude()));
        mPresenter.updateCurrentLocation(mCurrentUser);
        configGoogleMap();
    }

    private void configGoogleMap() {
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.setPadding(Const.MapPadding.LEFT_PADDING, Const.MapPadding.TOP_PADDING,
                Const.MapPadding.RIGHT_PADDING, Const.MapPadding.BOTTOM_PADDING);
        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                mPresenter.getShipperInfo(mShipperMap.get(marker));
                return true;
            }
        });
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                // TODO: 30/08/2016  
            }
        });
    }

    @Override
    public void onCameraIdle() {
        // The camera has stopped moving.
        if (mLastCameraPosition != null &&
                mLastCameraPosition.equals(mGoogleMap.getCameraPosition().target)) {
            return;
        }
        mLastCameraPosition = mGoogleMap.getCameraPosition().target;

        mPresenter.getAddressFromLatLng(mLastCameraPosition);
    }

    @Override
    public void onSearchAreaComplete(CharSequence name, LatLng latLng) {
        mTvSearchArea.setText(name);
        MapUtils.zoomToPosition(mGoogleMap, latLng);
    }

    @Override
    public void onAddressChange(String string) {
        if (mTvSearchArea != null) mTvSearchArea.setText(string);
    }

    @Override
    public void addListMarker(List<User> shipperList) {
        for (User shipper : shipperList) {
            mPresenter.addShipper(shipper, mShipperMap);
        }
    }

    @Override
    public void removeListMarker(List<User> shipperList) {
        for (User shipper : shipperList) {
            mPresenter.removeShipper(shipper, mShipperMap);
        }
    }

    @Override
    public Marker addMark(LatLng latLng) {
        return mGoogleMap.addMarker(new MarkerOptions().position(latLng)
                                                       .icon(BitmapDescriptorFactory.fromResource(
                                                               R.drawable.ic_marker_shipper)));
    }

    @Override
    public void onShipperOnline(final User shipper) {
        if (shipper == null || getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPresenter.addShipper(shipper, mShipperMap);
            }
        });
    }

    @Override
    public void showMapLoadingIndicator(boolean isActive) {
        mMapLoadingProgress.setVisibility(isActive ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onShipperOffline(final User shipper) {
        if (shipper == null || getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mPresenter.removeShipper(shipper, mShipperMap);
            }
        });
    }
}
