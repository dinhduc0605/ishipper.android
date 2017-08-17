package com.framgia.ishipper.presentation.invoice.invoice_creation;

import android.content.Context;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.util.CommonUtils;
import com.framgia.ishipper.util.MapUtils;
import com.framgia.ishipper.util.PermissionUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.seatgeek.placesautocomplete.DetailsCallback;
import com.seatgeek.placesautocomplete.OnPlaceSelectedListener;
import com.seatgeek.placesautocomplete.PlacesAutocompleteTextView;
import com.seatgeek.placesautocomplete.model.Place;
import com.seatgeek.placesautocomplete.model.PlaceDetails;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnFocusChange;

import static com.framgia.ishipper.presentation.invoice.invoice_creation.ShopCreateInvoiceActivity.sInvoice;
import static com.framgia.ishipper.util.Const.AUTO_COMPLETE_PLACE_LANGUAGE_CODE;
import static com.framgia.ishipper.util.Const.AUTO_COMPLETE_PLACE_RADIUS;

public class ShopCreateInvoiceStep1Fragment extends BaseFragment implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ShopCreateInvoiceStep1Contract.View, GoogleMap.OnCameraIdleListener {
    private static final String TAG = "ShopCreateOrder1";
    private final int NONE = 1;
    private final int PICK_START_POINT = 2;
    private final int PICK_END_POINT = 3;

    @BindView(R.id.layoutMapContainer) FrameLayout mFrameMapContainer;
    @BindView(R.id.imgPickPosition) ImageView mImgPickPosition;
    @BindView(R.id.edt_address_start) PlacesAutocompleteTextView mEdtAddressStart;
    @BindView(R.id.edt_address_end) PlacesAutocompleteTextView mEdtAddressEnd;
    @BindView(R.id.btnPickStart) ImageView mBtnPickStart;
    @BindView(R.id.btnPickEnd) ImageView mBtnPickEnd;
    @BindView(R.id.tvDistance) TextView mTvDistance;
    @BindView(R.id.progress_map_loading) ProgressBar mProgressLoading;

    private GoogleApiClient mGoogleApiClient;
    private GoogleMap mMap;
    private Marker mMarkerStart, mMarkerEnd;
    private int mStatus = NONE;
    private Polyline mPolylineRoute;
    private Context mContext;
    private FetchAddressTask task;
    private float mDistance;
    private boolean mDisableCameraChange;
    private ShopCreateInvoiceStep1Presenter mPresenter;
    private boolean mAlreadyLoaded;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shop_create_order_step1;
    }

    @Override
    public void initViews() {
        if (mAlreadyLoaded) {
            updateDoneUI();
            onDistanceResponse((float) sInvoice.getDistance());
            return;
        }
        mAlreadyLoaded = true;
        SupportMapFragment mapFragment = SupportMapFragment.newInstance();
        mapFragment.getMapAsync(this);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.layoutMapContainer, mapFragment).commit();

        // Connect To Google Api
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getContext())
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mEdtAddressStart.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                CommonUtils.hideKeyboardFrom(getContext(), mEdtAddressStart);
                mEdtAddressStart.clearFocus();
                mEdtAddressStart.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(PlaceDetails placeDetails) {
                        LatLng place = new LatLng(placeDetails.geometry.location.lat,
                                placeDetails.geometry.location.lng);
                        mPresenter.setStartLocation(place);
                        mDisableCameraChange = true;
                        MapUtils.zoomToPosition(mMap, place);
                        mPresenter.getDistance();
                        mPresenter.confirmPickLocation();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Dont update
                    }
                });
            }
        });

        mEdtAddressEnd.setOnPlaceSelectedListener(new OnPlaceSelectedListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                CommonUtils.hideKeyboardFrom(getContext(), mEdtAddressEnd);
                mEdtAddressEnd.clearFocus();
                mEdtAddressEnd.getDetailsFor(place, new DetailsCallback() {
                    @Override
                    public void onSuccess(PlaceDetails placeDetails) {
                        LatLng place = new LatLng(placeDetails.geometry.location.lat,
                                placeDetails.geometry.location.lng);
                        mDisableCameraChange = true;
                        mPresenter.setEndLocation(place);
                        MapUtils.zoomToPosition(mMap, place);
                        mPresenter.getDistance();
                        mPresenter.confirmPickLocation();
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Dont update
                    }
                });
            }
        });
        mPresenter = new ShopCreateInvoiceStep1Presenter(this, this);
    }

    @OnFocusChange({R.id.edt_address_start, R.id.edt_address_end})
    public void onFocusChange(View view, boolean hasFocus) {
        switch (view.getId()) {
            case R.id.edt_address_start:
                if (hasFocus && mStatus != PICK_START_POINT) {
                    // save previous end location
                    if (mStatus == PICK_END_POINT) {
                        mPresenter.setEndLocation(mMap.getCameraPosition().target);
                    }
                    pickStartLocation();
                }
                break;
            case R.id.edt_address_end:
                if (hasFocus && mStatus != PICK_END_POINT) {
                    // save previous start location
                    if (mStatus == PICK_START_POINT) {
                        mPresenter.setStartLocation(mMap.getCameraPosition().target);
                    }
                    pickEndLocation();
                }
                break;
        }
    }

    @OnClick({R.id.btnPickStart, R.id.btnPickEnd, R.id.btnContinue})
    public void onClick(View view) {
        CommonUtils.hideKeyboard(getActivity());
        if (mProgressLoading.getVisibility() == View.VISIBLE) return;
        switch (view.getId()) {
            case R.id.btnPickStart:
                // user is picking start point, goto done pick
                if (mStatus == PICK_START_POINT) {
                    mPresenter.setStartLocation(mMap.getCameraPosition().target);
                    // if user haven't pick end point, go to pick end location
                    if (mMarkerEnd == null) {
                        pickEndLocation();
                    } else {
                        // all pick, confirm
                        mPresenter.confirmPickLocation();
                    }
                    // user is picking end point
                } else if (mStatus == PICK_END_POINT) {
                    mPresenter.setEndLocation(mMap.getCameraPosition().target);
                    mPresenter.confirmPickLocation();
                    // if user isn't picking start point or end point
                } else {
                    mImgPickPosition.setImageResource(R.drawable.ic_map_picker_start);
                    mPresenter.reset();
                    pickStartLocation();
                }
                break;
            case R.id.btnPickEnd:
                // if user is picking end point
                if (mStatus == PICK_END_POINT) {
                    mPresenter.setEndLocation(mMap.getCameraPosition().target);
                    mPresenter.confirmPickLocation();
                    // if user is picking start point
                } else if (mStatus == PICK_START_POINT) {
                    mPresenter.setStartLocation(mMap.getCameraPosition().target);
                    if (mMarkerEnd == null) {
                        pickEndLocation();
                    } else {
                        mPresenter.confirmPickLocation();
                    }
                    // if user isn't picking start point or end point
                } else {
                    pickEndLocation();
                }
                break;
            case R.id.btnContinue:
                if (!mPresenter.validateInput()) return;
                if (mDistance == 0.0f) {
                    showUserMessage(R.string.get_distance_error);
                    return;
                }
                if (mStatus == PICK_END_POINT) {
                    mPresenter.setEndLocation(mMap.getCameraPosition().target);
                }
                mPresenter.confirmPickLocation();
                mPresenter.saveInvoiceData(mEdtAddressStart.getText().toString(),
                        mEdtAddressEnd.getText().toString(), mDistance);
                ((ShopCreateInvoiceActivity) getActivity()).addFragment(
                        new ShopCreateInvoiceStep2Fragment());
                break;
        }
    }

    @Override
    public void moveTo(Marker marker) {
        mDisableCameraChange = true;
        if (marker != null) {
            marker.remove();
            MapUtils.zoomToPosition(mMap, marker.getPosition());
            marker = null;
        }
    }

    @Override
    public void onDistanceResponse(float distance) {
        mDistance = distance;
        mTvDistance.setText(getString(R.string.text_distance, mDistance));
    }

    @Override
    public void showGetDistanceLoading() {
        mTvDistance.setText(R.string.all_symbol_loading);
    }

    @Override
    public void onRoutingSuccess(PolylineOptions polyOptions) {
        removePath();
        mPolylineRoute = mMap.addPolyline(polyOptions);
        MapUtils.zoomToBounds(mMap, polyOptions);
    }

    @Override
    public void updateDoneUI() {
        mStatus = NONE;
        mImgPickPosition.setImageDrawable(null);
    }

    @Override
    public void pickStartLocation() {
        mStatus = PICK_START_POINT;
        // remove start marker from map
        if (mMarkerStart != null) {
            moveTo(mMarkerStart);
            mMarkerStart.remove();
            mMarkerStart = null;
        }

        mBtnPickEnd.setImageResource(R.drawable.ic_map_picker_end);
        mImgPickPosition.setImageResource(R.drawable.ic_map_picker_start);
        mBtnPickStart.setImageResource(R.drawable.ic_done);
        removePath();
    }

    @Override
    public void pickEndLocation() {
        mStatus = PICK_END_POINT;
        if (mMarkerEnd != null) {
            moveTo(mMarkerEnd);
            mMarkerEnd.remove();
            mMarkerEnd = null;
        }
        mBtnPickStart.setImageResource(R.drawable.ic_map_picker_start);
        mImgPickPosition.setImageResource(R.drawable.ic_map_picker_end);
        mBtnPickEnd.setImageResource(R.drawable.ic_done);
        removePath();
    }

    @Override
    public void clear() {
        // if user haven't pick end point then reset
        mMarkerEnd = null;
        mMap.clear();
        mTvDistance.setText("");
        mEdtAddressEnd.setText("");
        removePath();
    }

    @Override
    public void onEndLocationSave(LatLng endLocation) {
        mBtnPickEnd.setImageResource(R.drawable.ic_map_picker_end);
        if (mMarkerEnd != null) mMarkerEnd.remove();
        mMarkerEnd = mMap.addMarker(
                new MarkerOptions().position(endLocation)
                        .icon(BitmapDescriptorFactory.fromResource(
                                R.drawable.ic_map_picker_end))
        );
    }

    @Override
    public void onStartLocationSave(LatLng startLocation) {
        mBtnPickStart.setImageResource(R.drawable.ic_map_picker_start);
        mMarkerStart = mMap.addMarker(
                new MarkerOptions()
                        .position(startLocation)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_map_picker_start)));
    }

    @Override
    public void showMapLoadingIndicator(boolean isActive) {
        mProgressLoading.setVisibility(isActive ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onDistanceError() {
        mDistance = 0.0f;
    }

    private void removePath() {
        if (mPolylineRoute != null) {
            mPolylineRoute.remove();
            mPolylineRoute = null;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // Disable tilt and rotate map
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);

        if (PermissionUtils.checkLocationPermission(mContext)) return;

        googleMap.setMyLocationEnabled(true);
        googleMap.setOnCameraIdleListener(this);
    }

    @Override
    public void onCameraIdle() {
        // The camera has stopped moving.
        if (mDisableCameraChange) {
            mDisableCameraChange = false;
            return;
        }
        CommonUtils.hideKeyboard(getActivity());
        if (mStatus == NONE) return;
        LatLng position = new LatLng(mMap.getCameraPosition().target.latitude,
                mMap.getCameraPosition().target.longitude);
        if (mStatus == PICK_START_POINT) {
            mPresenter.saveLatLngStart(position);
        } else if (mStatus == PICK_END_POINT) {
            mPresenter.saveLatLngEnd(position);
        }
        if (task != null) {
            task.cancel(true);
        }
        task = new FetchAddressTask();
        task.execute(position);
        mPresenter.getDistance();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (PermissionUtils.checkLocationPermission(mContext)) return;
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (location != null) {
            MapUtils.zoomToPosition(mMap, new LatLng(location.getLatitude(), location.getLongitude()));
            mEdtAddressStart.setCurrentLocation(location);
            mEdtAddressStart.setRadiusMeters(AUTO_COMPLETE_PLACE_RADIUS);
            mEdtAddressStart.setLanguageCode(AUTO_COMPLETE_PLACE_LANGUAGE_CODE);
            mEdtAddressEnd.setCurrentLocation(location);
            mEdtAddressEnd.setRadiusMeters(AUTO_COMPLETE_PLACE_RADIUS);
            mEdtAddressEnd.setLanguageCode(AUTO_COMPLETE_PLACE_LANGUAGE_CODE);
            mPresenter.saveLatLngStart(new LatLng(location.getLatitude(), location.getLongitude()));
            pickStartLocation();
        } else {
            showUserMessage(R.string.all_cant_get_location);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
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

    /**
     * Task fetch address from location in another thread
     */
    private class FetchAddressTask extends AsyncTask<LatLng, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showMapLoadingIndicator(true);
            if (mStatus == PICK_START_POINT) {
                mEdtAddressStart.setText(R.string.all_symbol_loading);
            } else if (mStatus == PICK_END_POINT) {
                mEdtAddressEnd.setText(R.string.all_symbol_loading);
            }
        }

        @Override
        protected String doInBackground(LatLng... latLngs) {
            double latitude = latLngs[0].latitude;
            double longitude = latLngs[0].longitude;
            return MapUtils.getAddressFromLocation(mContext, new LatLng(latitude, longitude));
        }

        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            showMapLoadingIndicator(false);
            if (mStatus == NONE) return;
            PlacesAutocompleteTextView tvShowing;
            tvShowing = mStatus == PICK_START_POINT ? mEdtAddressStart : mEdtAddressEnd;
            // Disable dropdown showing up
            tvShowing.clearFocus();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                tvShowing.setText(address, false);
            } else {
                tvShowing.setFocusable(false);
                tvShowing.setFocusableInTouchMode(false);
                tvShowing.setText(address);
                tvShowing.setFocusable(true);
                tvShowing.setFocusableInTouchMode(true);
            }
        }
    }
}
