package com.framgia.ishipper.presentation.filter;

import android.content.Intent;
import android.widget.TextView;
import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseToolbarActivity;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIDefinition;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.FilterInvoiceData;
import com.framgia.ishipper.util.MapUtils;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import org.florescu.android.rangeseekbar.RangeSeekBar;
import java.util.HashMap;
import java.util.Map;
import static android.app.Activity.RESULT_OK;
import static com.framgia.ishipper.presentation.filter.FilterInvoiceActivity.INTENT_FILTER_DATA;

/**
 * Created by dinhduc on 22/11/2016.
 */

public class FilterInvoicePresenter implements FilterInvoiceContract.Presenter {
    private FilterInvoiceContract.View mView;
    private BaseToolbarActivity mActivity;

    public FilterInvoicePresenter(FilterInvoiceContract.View view, BaseToolbarActivity activity) {
        mView = view;
        mActivity = activity;
    }

    @Override
    public void filterInvoice(
            TextView textView, RangeSeekBar seekbarFilterOrderPrice,
            RangeSeekBar seekbarFilterShipPrice, RangeSeekBar seekbarFilterDistance,
            RangeSeekBar seekbarFilterWeight, RangeSeekBar seekbarFilterRadius,
            String startAddress) {
        switch (textView.getId()) {
            case R.id.tv_filter_order_price:
                mView.showView(textView, seekbarFilterOrderPrice);
                break;
            case R.id.tv_filter_ship_price:
                mView.showView(textView, seekbarFilterShipPrice);
                break;
            case R.id.tv_filter_distance:
                mView.showView(textView, seekbarFilterDistance);
                break;
            case R.id.tv_filter_weight:
                mView.showView(textView, seekbarFilterWeight);
                break;
            case R.id.tv_filter_radius:
                mView.showView(textView, seekbarFilterRadius);
                break;
            case R.id.btn_filter_invoice:
                Map<String, String> params = new HashMap<>();

                if (mView.isVisible(seekbarFilterRadius)) {
                    LatLng currentLatLng = MapUtils.getLocationFromAddress(mActivity, startAddress);
                    if (currentLatLng == null) {
                        mActivity.showUserMessage(
                                mActivity.getString(R.string.activity_filter_msg_start_address));
                        return;
                    } else {
                        params.put(APIDefinition.FilterInvoice.PARAM_CURRENT_LAT,
                                currentLatLng.latitude + "");
                        params.put(APIDefinition.FilterInvoice.PARAM_CURRENT_LONG,
                                currentLatLng.longitude + "");
                        params.put(APIDefinition.FilterInvoice.PARAM_RADIUS,
                                seekbarFilterRadius.getSelectedMaxValue() + "");
                    }
                }


                if (mView.isVisible(seekbarFilterDistance)) {
                    params.put(APIDefinition.FilterInvoice.PARAM_MIN_DISTANCE,
                            seekbarFilterDistance.getSelectedMinValue() + "");
                    params.put(APIDefinition.FilterInvoice.PARAM_MAX_DISTANCE,
                            seekbarFilterDistance.getSelectedMaxValue() + "");
                }
                if (mView.isVisible(seekbarFilterOrderPrice)) {
                    params.put(APIDefinition.FilterInvoice.PARAM_MIN_ORDER_PRICE,
                            (int) seekbarFilterOrderPrice.getSelectedMinValue() * 1000 + "");
                    params.put(APIDefinition.FilterInvoice.PARAM_MAX_ORDER_PRICE,
                            (int) seekbarFilterOrderPrice.getSelectedMaxValue() * 1000 + "");
                }
                if (mView.isVisible(seekbarFilterShipPrice)) {
                    params.put(APIDefinition.FilterInvoice.PARAM_MIN_SHIP_PRICE,
                            (int) seekbarFilterShipPrice.getSelectedMinValue() * 1000 + "");
                    params.put(APIDefinition.FilterInvoice.PARAM_MAX_SHIP_PRICE,
                            (int) seekbarFilterShipPrice.getSelectedMaxValue() * 1000 + "");
                }
                if (mView.isVisible(seekbarFilterWeight)) {
                    params.put(APIDefinition.FilterInvoice.PARAM_MIN_WEIGHT,
                            seekbarFilterWeight.getSelectedMinValue() + "");
                    params.put(APIDefinition.FilterInvoice.PARAM_MAX_WEIGHT,
                            seekbarFilterWeight.getSelectedMaxValue() + "");
                }

                User currentUser = Config.getInstance().getUserInfo(mActivity);

                if (params.size() == 0) {
                    mActivity.showUserMessage(
                            mActivity.getString(R.string.activity_filter_msg_more_info));
                }

                API.filterInvoice(currentUser.getAuthenticationToken(), params,
                        new API.APICallback<APIResponse<FilterInvoiceData>>() {
                            @Override
                            public void onResponse(APIResponse<FilterInvoiceData> response) {
                                Intent intent = new Intent();
                                String data = new Gson().toJson(response.getData());
                                intent.putExtra(INTENT_FILTER_DATA, data);
                                mActivity.setResult(RESULT_OK, intent);
                                mActivity.finish();
                            }

                            @Override
                            public void onFailure(int code, String message) {
                                mActivity.showUserMessage(message);
                            }
                        });
                break;
        }
    }
}
