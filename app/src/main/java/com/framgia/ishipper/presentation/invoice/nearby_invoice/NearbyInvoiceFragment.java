package com.framgia.ishipper.presentation.invoice.nearby_invoice;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;
import com.directions.route.Route;
import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.common.Log;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.data.ListInvoiceData;
import com.framgia.ishipper.presentation.filter.FilterInvoiceActivity;
import com.framgia.ishipper.presentation.main.MainActivity;
import com.framgia.ishipper.ui.adapter.NewInvoiceAdapter;
import com.framgia.ishipper.ui.listener.LocationSettingCallback;
import com.framgia.ishipper.ui.listener.OnInvoiceUpdate;
import com.framgia.ishipper.util.CommonUtils;
import com.framgia.ishipper.util.Const;
import com.framgia.ishipper.util.MapUtils;
import com.framgia.ishipper.util.PermissionUtils;
import com.framgia.ishipper.util.StorageUtils;
import com.framgia.ishipper.util.TextFormatUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import butterknife.BindView;
import butterknife.OnClick;

public class NearbyInvoiceFragment extends BaseFragment
        implements NearbyInvoiceContract.View, OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener, OnInvoiceUpdate, NewInvoiceAdapter.OnItemClickListener,
        GoogleMap.OnCameraIdleListener {

    private static final String TAG = "NearbyOrderFragment";
    private static final int REQUEST_FILTER = 0x1234;

    @BindView(R.id.img_nearby_order_pos_marker) ImageView mImgPosMarker;
    @BindView(R.id.tv_item_invoice_ship_price) TextView mTvNearbyShipPrice;
    @BindView(R.id.tv_item_invoice_from) TextView mTvNearbyFrom;
    @BindView(R.id.tv_item_invoice_to) TextView mTvNearbyTo;
    @BindView(R.id.tv_item_invoice_distance) TextView mTvNearbyDistance;
    @BindView(R.id.tv_item_invoice_ship_time) TextView mTvNearbyShipTime;
    @BindView(R.id.tv_item_invoice_price) TextView mTvNearbyOrderPrice;
    @BindView(R.id.btn_item_invoice_show_path) TextView mBtnNearbyShowPath;
    @BindView(R.id.btn_item_invoice_register) TextView mBtnNearbyReceiveOrder;
    @BindView(R.id.layout_invoice_detail) RelativeLayout mRlOrderDetail;
    @BindView(R.id.tv_main_search_area) TextView mTvSearchArea;
    @BindView(R.id.rating_invoice_window) AppCompatRatingBar mRatingOrderWindow;
    @BindView(R.id.tv_item_invoice_shop_name) TextView mTvItemOrderShopName;
    @BindView(R.id.ll_shop_invoice_status) LinearLayout mLlShopOrderStatus;
    @BindView(R.id.rv_list_invoice) RecyclerView mRvListInvoice;
    @BindView(R.id.switcher_layout) ViewSwitcher mSwitcherLayout;
    @BindView(R.id.tv_invoice_count) TextView mTvInvoiceCount;
    @BindView(R.id.layout_action) RelativeLayout mLayoutAction;
    @BindView(R.id.btn_view_change) TextView mBtnViewChange;
    @BindView(R.id.action_cancel_accept_invoice) View mBtnCancelAcceptOrder;
    @BindView(R.id.tv_empty) View mLayoutEmpty;
    @BindView(R.id.layout_refresh) SwipeRefreshLayout mLayoutRefresh;
    @BindView(R.id.rl_search_view) LinearLayout mLayoutSearch;
    @BindView(R.id.layout_expand_item) LinearLayout mLayoutExpandItem;
    @BindView(R.id.img_collapse_item_invoice) ImageView mImageCollapseItemInvoice;
    @BindView(R.id.progress_map_loading) ProgressBar mMapLoadingDialog;

    private GoogleApiClient mGoogleApiClient;
    private Location mLocation;
    private GoogleMap mGoogleMap;
    private SupportMapFragment mMapFragment;
    private Polyline mPolylineRoute;
    private Marker mMakerEndOrder;
    private Context mContext;
    private User mCurrentUser;
    private Invoice mInvoice;
    private FetchAddressTask mTask;
    private ArrayList<Invoice> mInvoices = new ArrayList<>();
    private HashMap<Marker, Invoice> mInvoiceMap = new HashMap<>();
    private boolean mAutoRefresh = true;
    private int mRadius;
    private NearbyInvoiceContract.Presenter mPresenter;
    private AlertDialog mReceiveDialog;
    private NewInvoiceAdapter mAdapter;
    private boolean mIsCollapse;
    private LatLng mLastCameraPosition;

    public static NearbyInvoiceFragment newInstance() {
        return new NearbyInvoiceFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.d(TAG, "onMapReady: ");
        if (PermissionUtils.checkLocationPermission(mContext)) return;
        mGoogleMap = googleMap;
        initMap();
    }

    //google api client connected
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.d(TAG, "onConnected: ");
        //check location permission
        if (!PermissionUtils.checkLocationPermission(mContext)) {
            // check location setting
            CommonUtils.checkLocationRequestSetting(getActivity(), mGoogleApiClient,
                    new LocationSettingCallback() {
                        @Override
                        public void onSuccess() {
                            mMapFragment.getMapAsync(NearbyInvoiceFragment.this);
                        }
                    });
        } else {
            // request permission
            PermissionUtils.requestPermission((AppCompatActivity) getActivity(),
                    Const.RequestCode.LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, false);
        }
    }

    // initialize map
    private void initMap() {
        configGoogleMap();
        if (PermissionUtils.checkLocationPermission(mContext)) {
            return;
        }
        mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLocation == null) {
            showLoadingDialog();
            LocationServices.FusedLocationApi
                    .requestLocationUpdates(mGoogleApiClient, new LocationRequest(),
                            NearbyInvoiceFragment.this);
        } else {
            onLocationChange(mLocation);
            android.util.Log.d(TAG, "initMap: " + mLocation);
        }
    }

    private void onLocationChange(Location location) {
        mCurrentUser.setLatitude(location.getLatitude());
        mCurrentUser.setLongitude(location.getLongitude());
        MapUtils.zoomToPosition(mGoogleMap,
                new LatLng(location.getLatitude(), location.getLongitude()));
        mPresenter.markInvoiceNearby(mInvoices, mCurrentUser.getAuthenticationToken(),
                new LatLng(mCurrentUser.getLatitude(), mCurrentUser.getLongitude()), mRadius);
        mPresenter.updateCurrentLocation(mCurrentUser);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_shipper_filter, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.menu_filter) {
            Intent intent = new Intent(mContext, FilterInvoiceActivity.class);
            startActivityForResult(intent, REQUEST_FILTER);
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Const.RequestCode.PLACE_AUTOCOMPLETE_REQUEST_CODE:
                    Place place = PlaceAutocomplete.getPlace(mContext, data);
                    Log.d(TAG, "onActivityResult: " + place.getName());
                    mTvSearchArea.setText(place.getName());
                    MapUtils.zoomToPosition(mGoogleMap,
                            new LatLng(place.getLatLng().latitude, place.getLatLng().longitude));
                    break;
                case Const.RequestCode.REQUEST_CHECK_SETTINGS:
                    mMapFragment.getMapAsync(NearbyInvoiceFragment.this);
                    break;
                case Const.REQUEST_SETTING:
                    initMap();
                    break;
                case REQUEST_FILTER:
                    String jsonData = data.getStringExtra(FilterInvoiceActivity.INTENT_FILTER_DATA);
                    ListInvoiceData listInvoiceData =
                            new Gson().fromJson(jsonData, ListInvoiceData.class);
                    if (listInvoiceData == null) return;
                    removeListMarker(mInvoices);
                    mInvoices.clear();
                    mAdapter.notifyDataSetChanged();
                    addListMarker(listInvoiceData.getInvoiceList());
                    mTvInvoiceCount.setText(Html.fromHtml(
                            getString(R.string.fragment_nearby_invoice_count, mInvoices.size())));
                    break;
                case Const.RequestCode.REQUEST_CODE_INVOICE_DETAIL:
                    if (mPresenter == null) return;
                    String invoiceId = data.getStringExtra(Const.KEY_INVOICE_ID);
                    if (invoiceId != null) {
                        updateStatusReceiveInvoice(invoiceId, Invoice.INVALID_USER_INVOICE);
                    }
                    mPresenter.markInvoiceNearby(mInvoices, mCurrentUser.getAuthenticationToken(),
                            new LatLng(mCurrentUser.getLatitude(), mCurrentUser.getLongitude()),
                            mRadius);
                    break;
            }
        } else if (resultCode == PlaceAutocomplete.RESULT_ERROR &&
                requestCode == Const.RequestCode.PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            Status status = PlaceAutocomplete.getStatus(mContext, data);
            Toast.makeText(mContext, status.getStatusMessage(), Toast.LENGTH_SHORT).show();
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
                            mMapFragment.getMapAsync(NearbyInvoiceFragment.this);
                        }
                    });
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onDestroy() {
        mGoogleApiClient.disconnect();
        super.onDestroy();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentUser.setLatitude(location.getLatitude());
        mCurrentUser.setLongitude(location.getLongitude());
        onLocationChange(location);
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
        dismissLoadingDialog();
    }

    @Override
    public void updateInvoices(List<Invoice> invoices) {
        mInvoices.clear();
        mInvoices.addAll(invoices);
        mAdapter.notifyDataSetChanged();
        mLayoutEmpty.setVisibility(invoices.isEmpty() ? View.VISIBLE : View.GONE);
        mTvInvoiceCount.setText(
                Html.fromHtml(getString(R.string.fragment_nearby_invoice_count, mInvoices.size())));
    }

    @Override
    public void onReceiveInvoiceSuccess(String message, Invoice invoice) {
        dismissLoadingDialog();
        mReceiveDialog.dismiss();
        showUserMessage(message);
    }

    @Override
    public void onReceiveInvoiceFail(String message) {
        dismissLoadingDialog();
        mReceiveDialog.dismiss();
        showUserMessage(message);
    }

    /**
     * add list invoice marker to Map
     *
     * @param invoiceList list invoice
     */
    @Override
    public void addListMarker(List<Invoice> invoiceList) {
        removeLoading();
        for (Invoice invoice : invoiceList) {
            addNewMarkerInvoice(invoice);
        }
    }

    @Override
    public void removeListMarker(List<Invoice> invoiceList) {
        for (Invoice invoice : invoiceList) {
            for (Map.Entry<Marker, Invoice> entry : mInvoiceMap.entrySet()) {
                Marker key = entry.getKey();
                Invoice value = entry.getValue();
                if (value.getId() == invoice.getId()) {
                    mInvoiceMap.remove(key);
                    key.remove();
                    break;
                }
            }
        }
    }

    private void addNewMarkerInvoice(Invoice invoice) {
        mInvoices.add(Const.HEAD_LIST, invoice);
        mAdapter.notifyItemInserted(Const.HEAD_LIST);
        if (mRvListInvoice == null) return;
        mRvListInvoice.getLayoutManager().scrollToPosition(Const.HEAD_LIST);
        mTvInvoiceCount.setText(
                Html.fromHtml(getString(R.string.fragment_nearby_invoice_count, mInvoices.size())));
        LatLng latLng = new LatLng(invoice.getLatStart(), invoice.getLngStart());
        int markerResId = invoice.isReceived() ? R.drawable.ic_marker_shop_received :
                R.drawable.ic_marker_shop;
        final Marker marker = mGoogleMap.addMarker(new MarkerOptions().position(latLng).alpha(0)
                                                                      .icon(BitmapDescriptorFactory
                                                                              .fromResource(
                                                                                      markerResId)));
        MapUtils.setAnimatedInMarker(marker);

        /** save invoice with marker to hashmap */
        mInvoiceMap.put(marker, invoice);
    }

    private Point getConfigSizeMap() {
        int widthMap = mMapFragment.getView().getWidth();
        int heightMap;
        heightMap = mMapFragment.getView().getHeight();
        return new Point(widthMap, heightMap);
    }

    /**
     * turn on/of auto fetch address
     *
     * @param status true/false
     */
    private void switchAutoRefresh(boolean status) {
        mAutoRefresh = status;
        mImgPosMarker.setVisibility(status ? View.VISIBLE : View.GONE);
    }

    /**
     * Config google with setting like camera change listener, marker clicked listener...
     */
    private void configGoogleMap() {
        // Disable tilt and rotate map
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(false);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(false);
        if (PermissionUtils.checkLocationPermission(mContext)) return;
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.setPadding(Const.MapPadding.LEFT_PADDING, Const.MapPadding.TOP_PADDING,
                Const.MapPadding.RIGHT_PADDING, Const.MapPadding.BOTTOM_PADDING);
        mGoogleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                // Check if marker is not in list of marker invoices, don't need to do anything
                if (!mInvoiceMap.containsKey(marker)) {
                    return false;
                }
                switchAutoRefresh(false);
                mInvoice = mInvoiceMap.get(marker);
                removeRoute();
                hideSearchArea();
                mMakerEndOrder = mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(mInvoice.getLatFinish(), mInvoice.getLngFinish())));
                mPresenter.getRoute(new LatLng(mInvoice.getLatStart(), mInvoice.getLngStart()),
                        new LatLng(mInvoice.getLatFinish(), mInvoice.getLngFinish()));
                showInvoiceDetailWindow(mInvoice);
                return true;
            }
        });
        mGoogleMap.setOnCameraIdleListener(this);
        mGoogleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                switchAutoRefresh(true);
                showSearchArea();
                hideInvoiceDetailWindow();
                removeRoute();
            }
        });
    }

    @Override
    public void onCameraIdle() {
        // The camera has stopped moving.
        if (!mAutoRefresh) return;
        if (mLastCameraPosition != null &&
                mLastCameraPosition.equals(mGoogleMap.getCameraPosition().target)) {
            return;
        }
        mLastCameraPosition = mGoogleMap.getCameraPosition().target;
        if (mTask != null) {
            mTask.cancel(true);
        }
        mTask = new FetchAddressTask();
        mTask.execute(mLastCameraPosition);
    }

    private void removeRoute() {
        if (mPolylineRoute != null) {
            mPolylineRoute.remove();
            mMakerEndOrder.remove();
        }
    }

    @Override
    public void showInvoiceDetailWindow(Invoice invoice) {
        mRlOrderDetail.setVisibility(View.VISIBLE);
        mImageCollapseItemInvoice.setVisibility(View.VISIBLE);
        User mUser = invoice.getUser();
        if (invoice.isReceived()) {
            mBtnCancelAcceptOrder.setVisibility(View.VISIBLE);
            mBtnNearbyReceiveOrder.setVisibility(View.GONE);
            mBtnCancelAcceptOrder.setTag(invoice.getStringId());
        } else {
            mBtnNearbyReceiveOrder.setTag(invoice.getStringId());
            mBtnCancelAcceptOrder.setVisibility(View.GONE);
            mBtnNearbyReceiveOrder.setVisibility(View.VISIBLE);
        }
        mTvItemOrderShopName.setText(mUser.getName());
        mRatingOrderWindow.setRating((float) mUser.getRate());
        mTvNearbyDistance.setText(TextFormatUtils.formatDistance(invoice.getDistance()));
        mTvNearbyFrom.setText(invoice.getAddressStart());
        mTvNearbyTo.setText(invoice.getAddressFinish());
        mTvNearbyShipTime.setText(invoice.getDeliveryTime());
        mTvNearbyShipPrice.setText(TextFormatUtils.formatPrice(invoice.getShippingPrice()));
        mTvNearbyOrderPrice.setText(TextFormatUtils.formatPrice(invoice.getPrice()));
    }

    @Override
    public void hideInvoiceDetailWindow() {
        if (mRlOrderDetail != null) mRlOrderDetail.setVisibility(View.GONE);
        mImageCollapseItemInvoice.setVisibility(View.GONE);
    }

    @Override
    public void drawRoute(ArrayList<Route> routes) {
        PolylineOptions polyOptions = new PolylineOptions();
        for (int i = 0; i < routes.size(); i++) {
            polyOptions.color(ContextCompat
                    .getColor(mContext.getApplicationContext(), R.color.colorGreen));
            polyOptions.width(8);
            polyOptions.addAll(routes.get(i).getPoints());
        }
        if (mPolylineRoute != null && mPolylineRoute.isVisible()) {
            mPolylineRoute.remove();
        }
        mPolylineRoute = mGoogleMap.addPolyline(polyOptions);
        MapUtils.zoomToBounds(mGoogleMap, polyOptions);
    }

    @Override
    public void updateStatusReceiveInvoice(String invoiceId, int userInvoiceId) {
        mRlOrderDetail.setVisibility(View.GONE);
        Invoice item = findInvoiceById(invoiceId);
        if (item == null) return;
        item.setUserInvoiceId(userInvoiceId);
        mAdapter.notifyDataSetChanged();
        Marker marker = findMarkerByInvoice(item);
        int markerResId =
                item.isReceived() ? R.drawable.ic_marker_shop_received : R.drawable.ic_marker_shop;
        if (marker == null) return;
        marker.setIcon(BitmapDescriptorFactory.fromResource(markerResId));
        mInvoiceMap.get(marker).setUserInvoiceId(userInvoiceId);
    }

    @Override
    public void removeLoading() {
        if (mLayoutRefresh != null && mLayoutRefresh.isRefreshing()) {
            mLayoutRefresh.setRefreshing(false);
        }
    }

    @Override
    public void hideSearchArea() {
        if (mLayoutSearch != null && mLayoutSearch.getVisibility() == View.VISIBLE)
            mLayoutSearch.setVisibility(View.GONE);
    }

    @Override
    public void showSearchArea() {
        if (mLayoutSearch != null && mLayoutSearch.getVisibility() == View.GONE) {
            mLayoutSearch.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showReceiveDialog(final String invoiceId) {
        View view =
                LayoutInflater.from(mContext).inflate(R.layout.dialog_nearby_receive_order, null);
        view.findViewById(R.id.confirm_dialog_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.receiveInvoice(invoiceId);
            }
        });
        view.findViewById(R.id.confirm_dialog_cancel)
            .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mReceiveDialog.dismiss();
                }
            });
        mReceiveDialog = new AlertDialog.Builder(mContext).setView(view).show();
    }

    @Override
    public void onInvoiceCreate(final Invoice invoice) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                addNewMarkerInvoice(invoice);
            }
        });
    }

    @Override
    public void onInvoiceRemove(final Invoice invoice) {
        if (getActivity() == null) return;
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                removeRoute();
                int invoiceId = invoice.getId();
                for (Invoice item : mInvoices) {
                    if (item.getId() == invoiceId) {
                        mInvoices.remove(item);
                        mAdapter.notifyDataSetChanged();
                        break;
                    }
                }
                mTvInvoiceCount.setText(Html.fromHtml(
                        getString(R.string.fragment_nearby_invoice_count, mInvoices.size())));
                Marker marker = findMarkerByInvoice(invoice);
                if (marker != null) {
                    MapUtils.setAnimatedOutMarker(marker);
                    mInvoiceMap.remove(marker);
                }
            }
        });
    }

    private Marker findMarkerByInvoice(Invoice invoice) {
        for (Map.Entry<Marker, Invoice> entry : mInvoiceMap.entrySet()) {
            final Marker key = entry.getKey();
            Invoice value = entry.getValue();
            if (value.getId() == invoice.getId()) {
                return key;
            }
        }
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_nearby_invoice;
    }

    @Override
    public void initViews() {
        mMapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map_nearby_order);
        mPresenter = new NearbyInvoicePresenter(mContext, this, this);
        mBtnNearbyShowPath.setVisibility(View.VISIBLE);
        mBtnNearbyReceiveOrder.setVisibility(View.VISIBLE);
        mCurrentUser = Config.getInstance().getUserInfo(mContext);
        mRlOrderDetail.setVisibility(View.GONE);
        mRadius = StorageUtils.getIntValue(mContext, Const.Storage.KEY_SETTING_INVOICE_RADIUS,
                Const.SETTING_INVOICE_RADIUS_DEFAULT);
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mContext).addConnectionCallbacks(this)
                                                                    .addOnConnectionFailedListener(
                                                                            this)
                                                                    .addApi(LocationServices.API)
                                                                    .addApi(Places.GEO_DATA_API)
                                                                    .addApi(Places.PLACE_DETECTION_API)
                                                                    .build();
        }
        setHasOptionsMenu(true);
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).setOnInvoiceUpdate(this);
        }
        settingRecyclerView();
        mLayoutRefresh.setColorSchemeColors(Color.GREEN, Color.RED, Color.BLUE);
        mLayoutRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.markInvoiceNearby(mInvoices, mCurrentUser.getAuthenticationToken(),
                        new LatLng(mCurrentUser.getLatitude(), mCurrentUser.getLongitude()),
                        mRadius);
            }
        });

        Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_180);
        anim.setFillAfter(true);
        mImageCollapseItemInvoice.startAnimation(anim);
    }

    @Override
    public void onInvoiceReceiveItemClick(Invoice invoice) {
        showReceiveDialog(invoice.getStringId());
    }

    @Override
    public void onInvoiceItemClick(Invoice invoice) {
        mPresenter.showInvoiceDetail(invoice);
    }

    @Override
    public void onCancelAcceptInvoice(Invoice invoice) {
        mPresenter.cancelAcceptOrder(invoice);
    }

    @OnClick({R.id.btn_item_invoice_show_path, R.id.btn_item_invoice_register,
                     R.id.action_detail_invoice, R.id.rl_search_view, R.id.btn_view_change,
                     R.id.layoutInvoiceSummary, R.id.tv_empty, R.id.action_cancel_accept_invoice,
                     R.id.img_collapse_item_invoice})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_view_change:
                if (mSwitcherLayout.getCurrentView().getId() == R.id.layout_refresh) {
                    // Map
                    mSwitcherLayout.setInAnimation(getContext(), R.anim.enter_from_right);
                    mSwitcherLayout.setOutAnimation(getContext(), R.anim.exit_from_left);
                    mSwitcherLayout.showNext();
                    mBtnViewChange.setText(R.string.fragment_nearby_order_view_in_list);
                } else {
                    // List
                    mSwitcherLayout.setInAnimation(getContext(), R.anim.enter_from_left);
                    mSwitcherLayout.setOutAnimation(getContext(), R.anim.exit_to_right);
                    mSwitcherLayout.showPrevious();
                    mBtnViewChange.setText(R.string.fragment_nearby_order_view_in_map);
                }
                break;
            case R.id.btn_item_invoice_show_path:
                mPresenter.showPath(mInvoice);
                break;
            case R.id.btn_item_invoice_register:
                String invoiceId = (String) view.getTag();
                showReceiveDialog(invoiceId);
                break;
            case R.id.rl_search_view:
                mPresenter.clickSearchView();
                break;
            case R.id.tv_empty:
                mPresenter.markInvoiceNearby(mInvoices, mCurrentUser.getAuthenticationToken(),
                        new LatLng(mCurrentUser.getLatitude(), mCurrentUser.getLongitude()),
                        mRadius);
                break;
            case R.id.action_cancel_accept_invoice:
                mPresenter.cancelAcceptOrder(findInvoiceById((String) view.getTag()));
                break;
            case R.id.layoutInvoiceSummary:
            case R.id.action_detail_invoice:
                mPresenter.showInvoiceDetail(mInvoice);
                break;
            case R.id.img_collapse_item_invoice:
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_180);
                anim.setFillAfter(true);
                if (mIsCollapse) {
                    mImageCollapseItemInvoice.startAnimation(anim);
                    mLayoutExpandItem.setVisibility(View.VISIBLE);
                } else {
                    anim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_180_reverse);
                    mImageCollapseItemInvoice.startAnimation(anim);
                    mLayoutExpandItem.setVisibility(View.GONE);
                }
                mIsCollapse = !mIsCollapse;
                break;
        }
    }

    private void settingRecyclerView() {
        mAdapter = new NewInvoiceAdapter(mInvoices, this);
        mRvListInvoice.setLayoutManager(
                new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRvListInvoice.setAdapter(mAdapter);
    }

    private Invoice findInvoiceById(String invoiceId) {
        for (Invoice invoice : mInvoices) {
            if (invoice.getStringId().equals(invoiceId)) return invoice;
        }
        return null;
    }

    @Override
    public void showMapLoadingIndicator(boolean isActive) {
        mMapLoadingDialog.setVisibility(isActive ? View.VISIBLE : View.GONE);
    }

    /**
     * Task fetch address from location in another thread
     */
    private class FetchAddressTask extends AsyncTask<LatLng, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (mTvSearchArea != null) mTvSearchArea.setText(R.string.all_symbol_loading);
            showMapLoadingIndicator(true);
        }

        @Override
        protected String doInBackground(LatLng... latLngs) {
            mPresenter
                    .markInvoiceNearby(mInvoices, mCurrentUser.getAuthenticationToken(), latLngs[0],
                            mRadius);
            return MapUtils.getAddressFromLocation(mContext, latLngs[0]);
        }

        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            if (mTvSearchArea != null) mTvSearchArea.setText(address);
            showMapLoadingIndicator(false);
        }
    }
}
