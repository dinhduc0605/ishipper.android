package com.framgia.ishipper.presentation.main;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIDefinition;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.EmptyData;
import com.framgia.ishipper.net.data.GetUserData;
import com.framgia.ishipper.net.data.ShipperNearbyData;
import com.framgia.ishipper.util.Const;
import com.framgia.ishipper.util.MapUtils;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by HungNT on 11/28/16.
 */

public class NearbyShipperPresenter implements NearbyShipperContract.Presenter {
    private static final String TAG = NearbyShipperPresenter.class.getName();
    private BaseFragment mFragment;
    private NearbyShipperContract.View mView;
    private Context mContext;
    private FetchAddressTask task;
    private ArrayList<User> mShippers = new ArrayList<>();

    public NearbyShipperPresenter(BaseFragment fragment, NearbyShipperContract.View view) {
        mFragment = fragment;
        mView = view;
        mContext = fragment.getContext();
    }

    @Override
    public int getListSize() {
        return mShippers.size();
    }

    @Override
    public void getPlace(Intent place) {
        Place data = PlaceAutocomplete.getPlace(mFragment.getContext(), place);
        mView.onSearchAreaComplete(data.getName(), data.getLatLng());
    }

    @Override
    public void getShipperInfo(User user) {
        /** get shop information */
        API.getUser(Config.getInstance().getUserInfo(mContext).getAuthenticationToken(),
                    String.valueOf(user.getId()),
                    new API.APICallback<APIResponse<GetUserData>>() {
                        @Override
                        public void onResponse(APIResponse<GetUserData> response) {
                            User shipper = response.getData().getUser();
                            mFragment.showUserMessage(shipper.getName());
                            // TODO: 30/08/2016
                        }

                        @Override
                        public void onFailure(int code, String message) {
                            mFragment.showUserMessage(message);
                        }
                    });
    }

    @Override
    public void startSearchPlace() {
        try {
            AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                    .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                    .build();

            Intent searchIntent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN)
                    .setFilter(typeFilter)
                    .build(mFragment.getActivity());
            mFragment.startActivityForResult(searchIntent, Const.PLACE_AUTOCOMPLETE_REQUEST_CODE);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    /**
     * mark all shippers near a location on map
     */
    @Override
    public void requestShipperNearby(LatLng latLng) {
        Map<String, String> userParams = new HashMap<>();
        userParams.put(APIDefinition.GetShipperNearby.PARAM_USER_LAT,
                       String.valueOf(latLng.latitude));
        userParams.put(APIDefinition.GetShipperNearby.PARAM_USER_LNG,
                       String.valueOf(latLng.longitude));
        userParams.put(APIDefinition.GetShipperNearby.PARAM_USER_DISTANCE,
                       String.valueOf(Const.SHIPPER_NEARBY_RADIUS));
        API.getShipperNearby(Config.getInstance().getUserInfo(mContext).getAuthenticationToken(),
                             userParams, new API.APICallback<APIResponse<ShipperNearbyData>>() {
            @Override
            public void onResponse(APIResponse<ShipperNearbyData> response) {
                ArrayList<User> lastUpdateShippers = (ArrayList<User>) response.getData().getUsers();
                // init common list invoices
                ArrayList<User> commonShippers = new ArrayList<>(mShippers);
                // get common invoices between previous list invoices and new update invoices
                commonShippers.retainAll(lastUpdateShippers);
                // init list invoices need to be removed
                ArrayList<User> removeShippers = new ArrayList<>(mShippers);
                // init list invoices need to be added
                ArrayList<User> addShippers = new ArrayList<>(lastUpdateShippers);
                // get invoices need to be added
                addShippers.removeAll(commonShippers);
                // get invoices need to be removed
                removeShippers.removeAll(commonShippers);
                mView.removeListMarker(removeShippers);
                mView.addListMarker(addShippers);
                // update invoices
                mShippers.clear();
                mShippers.addAll(lastUpdateShippers);
//                        mView.onGetShipperNearbyComplete(mShippers);
            }

            @Override
            public void onFailure(int code, String message) {
                mFragment.showUserMessage(message);
            }
        });
    }

    @Override
    public void getAddressFromLatLng(LatLng latLng) {
        if (task != null) task.cancel(true);
        task = new FetchAddressTask();
        task.execute(latLng);
    }

    @Override
    public void addShipper(
            User shipper, HashMap<Marker, User> shipperMap) {
        LatLng latLng = new LatLng(shipper.getLatitude(), shipper.getLongitude());
        final Marker marker = mView.addMark(latLng);
        for (Map.Entry<Marker, User> entry : shipperMap.entrySet()) {
            Marker key = entry.getKey();
            User value = entry.getValue();
            if (value.equals(shipper)) {
                shipperMap.remove(key);
                mShippers.remove(value);
                MapUtils.setAnimatedOutMarker(key);
                break;
            }
        }
        if (!shipperMap.containsValue(shipper)) {
            mShippers.add(shipper);
            shipperMap.put(marker, shipper);
            MapUtils.setAnimatedInMarker(marker);
        }
    }

    @Override
    public void removeShipper(
            User mShipper, HashMap<Marker, User> shipperMap) {
        for (User shipper : mShippers) {
            if (shipper.getId().equals(mShipper.getId())) {
                /**
                 *  Find shipper on Hashmap and remove it
                 */
                for (Map.Entry<Marker, User> entry : shipperMap.entrySet()) {
                    Marker key = entry.getKey();
                    User value = entry.getValue();
                    if (value.getId().equals(mShipper.getId())) {
                        MapUtils.setAnimatedOutMarker(key);
                        shipperMap.remove(key);
                        break;
                    }
                }
                mShippers.remove(shipper);
                break;
            }
        }
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

    /**
     * Task fetch address from location in another thread
     */
    private class FetchAddressTask extends AsyncTask<LatLng, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mView.onAddressChange(mContext.getString(R.string.all_symbol_loading));
            mView.showMapLoadingIndicator(true);
        }

        @Override
        protected String doInBackground(LatLng... latLngs) {
            double latitude = latLngs[0].latitude;
            double longitude = latLngs[0].longitude;
            requestShipperNearby(new LatLng(latitude, longitude));
            return MapUtils.getAddressFromLocation(mContext, new LatLng(latitude, longitude));
        }

        @Override
        protected void onPostExecute(String address) {
            super.onPostExecute(address);
            mView.onAddressChange(address);
            mView.showMapLoadingIndicator(false);
        }
    }
}

