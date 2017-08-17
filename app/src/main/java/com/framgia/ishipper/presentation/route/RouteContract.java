package com.framgia.ishipper.presentation.route;

import com.directions.route.Route;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.net.data.ListRouteData;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

import retrofit2.Response;

/**
 * Created by dinhduc on 22/11/2016.
 */

public class RouteContract {
    interface View {
        void drawRoute(ArrayList<Route> routes);
        void setUpUI(LatLng startLatLng,
                     LatLng finishLatLng,
                     String startAddress,
                     String finishAddress,
                     int startIcon,
                     int finishIcon);
        void onGetListStepSuccess(Response<ListRouteData> response);
        void onGetListStepFail();
        void setVisibilityProgressBar(int visibility);
    }

    interface Presenter {
        void initMap(GoogleApiClient googleApiClient,Invoice invoice);
        void showPath(GoogleMap map, LatLng startLatLng, LatLng finishLatLng);
        void getListStep(String startAddress, String finishAddress);
    }
}
