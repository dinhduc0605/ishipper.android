package com.framgia.ishipper.presentation.invoice.shipping;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.ui.adapter.InvoiceShippingAdapter;
import com.framgia.ishipper.util.MapUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ShippingFragment extends BaseFragment implements OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, InvoiceShippingAdapter.OnItemClickListener, ShippingContract.View {

    @BindView(R.id.layoutMap) View mLayoutMap;
    @BindView(R.id.rvOrders) RecyclerView mRvOrders;
    @BindView(R.id.textInvoiceDesc) TextView mTextInvoiceDesc;
    @BindView(R.id.layoutOrderDetail) View mLayoutOrderDetail;
    @BindView(R.id.imgExpandMap) ImageView mImgExpandMap;
    @BindView(R.id.textAddressFrom) TextView mTextAddressFrom;
    @BindView(R.id.textAddressTo) TextView mTextAddressTo;
    @BindView(R.id.layoutEmpty) View mLayoutEmpty;

    private SupportMapFragment mMapFragment;
    private GoogleMap mMap;
    private List<Invoice> mInvoiceList = new ArrayList<>();
    private boolean mIsCheck;
    private boolean mPermissionDenied;
    private boolean mIsExpand;
    private InvoiceShippingAdapter mAdapter;
    private Context mContext;
    private ShippingPresenter mPresenter;
    private HashMap<Marker, Invoice> mHashMapMarker = new HashMap<>();
    private Marker mCurrentMarker;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shipping;
    }

    @Override
    public void initViews() {
        mMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mMapFragment.getMapAsync(this);
        mPresenter = new ShippingPresenter(this, this);
        mAdapter = new InvoiceShippingAdapter(mInvoiceList, this);
        mRvOrders.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        mRvOrders.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);
        mPresenter.getListShippingInvoice();
        enableMyLocation();
        googleMap.setOnMarkerClickListener(this);

    }

    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
        } else if (mMap != null) {
            mMap.setMyLocationEnabled(true);
            mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
                @Override
                public void onMyLocationChange(Location location) {
                    if (mIsCheck) return;
                    mIsCheck = true;

                    mPresenter.setCurrentLocation(new LatLng(location.getLatitude(), location.getLongitude()));
                    mPresenter.showAllRoute(mInvoiceList);

                    if (ActivityCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                            && ActivityCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }

                    mMap.setMyLocationEnabled(false);
                }
            });
        }
    }

    @OnClick({R.id.btn_expand_map, R.id.layoutEmpty})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_expand_map:
                int height;
                Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_180);
                anim.setFillAfter(true);
                if (!mIsExpand) {
                    height = (int) getResources().getDimension(R.dimen.fragment_shipping_list_max_height);
                    mIsExpand = true;
                    mLayoutOrderDetail.setVisibility(View.GONE);
                    mImgExpandMap.startAnimation(anim);
                    mRvOrders.setVisibility(View.VISIBLE);
                } else {
                    height = (int) getResources().getDimension(R.dimen.fragment_shipping_list_min_height);
                    mIsExpand = false;
                    anim = AnimationUtils.loadAnimation(mContext, R.anim.rotate_180_reverse);
                    mLayoutOrderDetail.setVisibility(View.VISIBLE);
                    mImgExpandMap.startAnimation(anim);
                    mRvOrders.setVisibility(View.GONE);
                }

                mRvOrders.getLayoutParams().height = height;
                mRvOrders.requestLayout();
                break;
            case R.id.layoutEmpty:
                mPresenter.getListShippingInvoice();
                break;
        }

    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Invoice invoice = mHashMapMarker.get(marker);
        showInvoiceDescSummary(invoice);
        return false;
    }

    @Override
    public void onClick(int position) {
        mPresenter.showInvoiceDetailActivity(mInvoiceList.get(position).getId());
    }

    @Override
    public void showListShipping(List<Invoice> invoiceList) {
        mInvoiceList.clear();
        mHashMapMarker.clear();
        mInvoiceList.addAll(invoiceList);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void showInvoiceToMap() {
        if (mMap == null) return;
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        Marker marker;
        mMap.clear();
        ArrayList<LatLng> latLngs = new ArrayList<>();

        for (Invoice item : mInvoiceList) {
            LatLng latLng = new LatLng(item.getLatStart(), item.getLngStart());
            latLngs.add(latLng);
            builder.include(latLng);
            marker = mMap.addMarker(new MarkerOptions().position(latLng).icon(
                    BitmapDescriptorFactory.fromResource(R.drawable.ic_marker_shop)));
            mHashMapMarker.put(marker, item);
        }

        if (mPresenter.getCurrentLocation() != null) latLngs.add(mPresenter.getCurrentLocation());
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        MapUtils.updateZoomMap(mMap, width, height, latLngs);
    }

    @Override
    public void showEmptyData(boolean isEmpty) {
        if (isEmpty && mLayoutEmpty != null) {
            mLayoutEmpty.setVisibility(View.VISIBLE);
        } else {
            mLayoutEmpty.setVisibility(View.GONE);
        }
    }

    @Override
    public void showInvoiceDescSummary(Invoice invoice) {
        mTextAddressFrom.setText(invoice.getAddressStart());
        mTextAddressTo.setText(invoice.getAddressFinish());
        mTextInvoiceDesc.setText(invoice.getInvoiceDesc());
    }

    @Override
    public void showPath(PolylineOptions polyOptions, LatLng currentPos) {
        mMap.addPolyline(polyOptions);
    }

    @Override
    public void addCurrentLocationToMap(LatLng location) {
        if (mCurrentMarker != null) mCurrentMarker.remove();
        ArrayList<LatLng> latLngs = new ArrayList<>();
        for (Invoice invoice : mInvoiceList) {
            latLngs.add(new LatLng(invoice.getLatStart(), invoice.getLngStart()));
        }
        latLngs.add(location);
        mCurrentMarker = mMap.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_current_position))
                .position(location));
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        MapUtils.updateZoomMap(mMap, width, height, latLngs);
    }
}
