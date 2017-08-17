package com.framgia.ishipper.util;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.framgia.ishipper.R;
import com.framgia.ishipper.ui.listener.LocationSettingCallback;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import org.ocpsoft.prettytime.PrettyTime;
import java.util.Date;
import java.util.Locale;

/**
 * Created by framgia on 18/08/2016.
 */
public class CommonUtils {
    /**
     * Show loading dialog
     **/
    public static Dialog showLoadingDialog(Context context) {
        ProgressDialog dialog = new ProgressDialog(context);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setMessage(context.getString(R.string.dialog_loading_message));
        dialog.setIndeterminate(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        return dialog;
    }

    private static LocationRequest createLocationRequest() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(Const.LocationRequest.LOCATION_INTERVAL_UPDATE);
        mLocationRequest.setFastestInterval(Const.LocationRequest.LOCATION_FASTEST_INTERVAL_UPDATE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return mLocationRequest;
    }

    public static void checkLocationRequestSetting(
            final Activity activity, GoogleApiClient googleApiClient,
            final LocationSettingCallback callback) {
        LocationSettingsRequest.Builder builder =
                new LocationSettingsRequest.Builder().setAlwaysShow(true)
                                                     .addLocationRequest(createLocationRequest());
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(googleApiClient, builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
                Status status = locationSettingsResult.getStatus();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        callback.onSuccess();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        try {
                            status.startResolutionForResult(activity,
                                    Const.RequestCode.REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            e.printStackTrace();
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        Toast.makeText(activity, R.string.all_location_not_available,
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        });
    }

    /**
     * @param m value on metre
     * @return value on km
     */
    public static float convertMetreToKm(int m) {
        return m / 1000f;
    }

    public static void makePhoneCall(Context context, String number) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) !=
                PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        context.startActivity(intent);
    }

    public static Dialog showOkCancelDialog(
            Context context, String message, String namePositiveButton, String nameNegativeButton,
            DialogInterface.OnClickListener okClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(message);
        builder.setPositiveButton(namePositiveButton, okClickListener);
        builder.setNegativeButton(nameNegativeButton, okClickListener);
        builder.setCancelable(false);
        builder.show();
        return builder.create();
    }

    public static boolean stringIsValid(String str) {
        if (str != null && !str.trim().equals("") && !str.toLowerCase().equals("null")) {
            return true;
        }
        return false;
    }

    public static boolean isOpenFromNoti(Activity activity) {
        Bundle data = activity.getIntent().getExtras();
        return data != null && data.getString(Const.FirebaseData.INVOICE_ID) != null;
    }

    /**
     * Sets ListView height dynamically based on the height of the items.
     *
     * @param listView to be resized
     */
    public static void setListViewHeightBasedOnItems(ListView listView, int numberOfItems) {
        ListAdapter listAdapter = listView.getAdapter();
        // Get total height of all items.
        int totalItemsHeight = 0;
        for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
            View item = listAdapter.getView(itemPos, null, listView);
            item.measure(0, 0);
            totalItemsHeight += item.getMeasuredHeight();
        }

        // Get total height of all item dividers.
        int totalDividersHeight = listView.getDividerHeight() * (numberOfItems);
        // Set list height.
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalItemsHeight + totalDividersHeight;
        if (numberOfItems != Const.ZERO) params.height += fixHeight(listView);
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    private static int fixHeight(ListView listView) {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) listView.getLayoutParams();
        return listView.getPaddingTop() + listView.getPaddingBottom() + lp.bottomMargin +
                lp.topMargin;
    }

    /**
     * place the path in center of screen
     *
     * @param startPoint start point of path
     * @param endPoint   end point of pat
     * @return
     */
    public static LatLng configLatLng(LatLng startPoint, LatLng endPoint) {
        double diffHeight = startPoint.latitude - endPoint.latitude;
        double diffWidth = startPoint.longitude - endPoint.longitude;
        double scale = 1;
        LatLng latLng;
        if (Math.abs(diffHeight) > Math.abs(diffWidth)) {
            if (diffHeight > 0) {
                latLng =
                        new LatLng(endPoint.latitude - scale * diffHeight - Math.abs(diffWidth) / 2,
                                (startPoint.longitude + endPoint.longitude) / 2);
            } else {
                latLng = new LatLng(
                        startPoint.latitude + scale * diffHeight - Math.abs(diffWidth) / 2,
                        (startPoint.longitude + endPoint.longitude) / 2);
            }
        } else {
            if (diffWidth > 0) {
                latLng = new LatLng(startPoint.latitude - scale * diffWidth,
                        (startPoint.longitude + endPoint.longitude) / 2);
            } else {
                latLng = new LatLng(endPoint.latitude + scale * diffWidth,
                        (startPoint.longitude + endPoint.longitude) / 2);
            }
        }
        return latLng;
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static void hideKeyboardFrom(Context context, View view) {
        InputMethodManager imm =
                (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public static String showTimeAgoFrom(long milliSeconds) {
        Date date = new Date(milliSeconds);
        PrettyTime prettyTime = new PrettyTime(Locale.getDefault());
        return prettyTime.format(date);
    }
}
