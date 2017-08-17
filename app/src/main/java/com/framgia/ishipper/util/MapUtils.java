package com.framgia.ishipper.util;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.util.Log;

import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.framgia.ishipper.presentation.main.MainActivity;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by HungNT on 7/18/16.
 */
public class MapUtils {
    private static final int ZOOM_LEVEL = 15;
    private static final String TAG = "MapUtils";

    private static final int MARKER_FADE_DURATION = 500;
    public static final int MAP_PADDING = 30;

    public static final float MAP_PADDING_PERCENT = 0.12f;

    /**
     * Route between 2 point start & end
     *
     * @param listener callback interface
     */
    public static AsyncTask routing(LatLng start, LatLng end, RoutingListener listener) {
        Routing routing = new Routing.Builder()
                .travelMode(Routing.TravelMode.DRIVING)
                .withListener(listener)
                .waypoints(start, end)
                .build();
        return routing.execute();
    }

    /**
     * Update config map zoom can show all points on input.
     *
     * @param width  of map
     * @param height of map
     * @param points all point will show
     */
    public static void updateZoomMap(GoogleMap googleMap, int width, int height, LatLng... points) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng item : points) {
            builder.include(item);
        }
        LatLngBounds bounds = builder.build();

        int padding = (int) (width * MAP_PADDING_PERCENT);

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.animateCamera(cu);
    }

    public static void updateZoomMap(GoogleMap googleMap, int width, int height, List<LatLng>
            points) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng item : points) {
            builder.include(item);
        }
        LatLngBounds bounds = builder.build();
        int padding = (int) (width * MAP_PADDING_PERCENT);

        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, width, height, padding);
        googleMap.animateCamera(cu);
    }

    /**
     * Animate camera to a position.
     */
    public static void zoomToPosition(GoogleMap map, LatLng latLng) {
        CameraUpdate center = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_LEVEL);
        if (MainActivity.firstTime) {
            map.animateCamera(center);
            MainActivity.firstTime = false;
        } else {
            map.moveCamera(center);
        }
    }

    /**
     * get a Address from LatLng
     *
     * @return Address summary in String. If cannot find address return a empty String.
     */
    public static String getAddressFromLocation(Context context, LatLng latLng) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (addresses.size() > 0) {
                return addresses.get(0).getAddressLine(0) + ", " + addresses.get(0).getAddressLine(1);
            }
        } catch (IOException e) {
            Log.d(TAG, "getAddressFromLocation: " + e.getMessage());
            e.printStackTrace();
        }
        return "";
    }

    /**
     * Get LatLng from an String address.
     *
     * @param address in String.
     * @return return LatLng of address, if cannot find return null.
     */
    public static LatLng getLocationFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocationName(address, 1);
            if (addresses.size() > 0) {
                return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setAnimatedInMarker(final Marker marker) {
        // Add fade in animation
        ValueAnimator ani = ValueAnimator.ofFloat(0, 1);
        ani.setDuration(MARKER_FADE_DURATION);
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                marker.setAlpha((float) animation.getAnimatedValue());
            }
        });
        ani.start();
    }

    public static void setAnimatedOutMarker(final Marker marker) {
        // Add fade out animation
        ValueAnimator ani = ValueAnimator.ofFloat(1, 0);
        ani.setDuration(MARKER_FADE_DURATION);
        ani.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                marker.setAlpha((float) animation.getAnimatedValue());
            }
        });
        ani.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                marker.remove();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
        ani.start();
    }

    public static void zoomToBounds(GoogleMap map, PolylineOptions p) {
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        List<LatLng> arr = p.getPoints();
        for (int i = 0; i < arr.size(); i++) {
            builder.include(arr.get(i));
        }
        LatLngBounds bounds = builder.build();
        int padding = MAP_PADDING; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        map.animateCamera(cu);
    }
}
