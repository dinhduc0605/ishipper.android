package com.framgia.ishipper.presentation.invoice.invoice_creation;

import android.content.Context;
import android.content.Intent;

import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIDefinition;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.CreateInVoiceData;
import com.framgia.ishipper.presentation.route.RouteActivity;
import com.framgia.ishipper.util.Const;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by framgia on 18/11/2016.
 */

public class ShopCreateInvoiceStep3Presenter implements ShopCreateInvoiceStep3Contract.Presenter {
    private final ShopCreateInvoiceStep3Contract.View mView;
    private BaseFragment mFragment;
    private Context mContext;

    public ShopCreateInvoiceStep3Presenter(ShopCreateInvoiceStep3Contract.View view, BaseFragment fragment) {
        mView = view;
        mFragment = fragment;
        mContext = fragment.getContext();
    }

    @Override
    public void requestCreateInvoice(Invoice newInvoice) {
        Map<String, String> params = new HashMap<>();
        params.put(APIDefinition.CreateInvoice.PARAM_NAME,
                newInvoice.getName());
        params.put(APIDefinition.CreateInvoice.PARAM_ADDRESS_START,
                newInvoice.getAddressStart());
        params.put(APIDefinition.CreateInvoice.PARAM_LATITUDE_START,
                String.valueOf(newInvoice.getLatStart()));
        params.put(APIDefinition.CreateInvoice.PARAM_LONGITUDE_START,
                String.valueOf(newInvoice.getLngStart()));
        params.put(APIDefinition.CreateInvoice.PARAM_ADDRESS_FINISH,
                newInvoice.getAddressFinish());
        params.put(APIDefinition.CreateInvoice.PARAM_LATITUDE_FINISH,
                String.valueOf(newInvoice.getLatFinish()));
        params.put(APIDefinition.CreateInvoice.PARAM_LONGITUDE_FINISH,
                String.valueOf(newInvoice.getLngFinish()));
        params.put(APIDefinition.CreateInvoice.PARAM_DELIVERY_TIME,
                newInvoice.getDeliveryTime());
        params.put(APIDefinition.CreateInvoice.PARAM_DISTANCE,
                String.valueOf(newInvoice.getDistance()));
        params.put(APIDefinition.CreateInvoice.PARAM_DESCRIPTION,
                newInvoice.getDescription());
        params.put(APIDefinition.CreateInvoice.PARAM_PRICE,
                String.valueOf(newInvoice.getPrice()));
        params.put(APIDefinition.CreateInvoice.PARAM_SHIPPING_PRICE,
                String.valueOf(newInvoice.getShippingPrice()));
        params.put(APIDefinition.CreateInvoice.PARAM_STATUS,
                newInvoice.getStatus());
        params.put(APIDefinition.CreateInvoice.PARAM_WEIGHT,
                String.valueOf(newInvoice.getWeight()));
        params.put(APIDefinition.CreateInvoice.PARAM_CUSTOMER_NAME,
                newInvoice.getCustomerName());
        params.put(APIDefinition.CreateInvoice.PARAM_CUSTOMER_NUMBER,
                newInvoice.getCustomerNumber());

        mFragment.showLoadingDialog();
        API.createInvoice(Config.getInstance().getUserInfo(mContext).getAuthenticationToken(),
                params,
                new API.APICallback<APIResponse<CreateInVoiceData>>() {
                    @Override
                    public void onResponse(APIResponse<CreateInVoiceData> response) {
                        mFragment.dismissLoadingDialog();
                        // TODO: Go to invoice manager
                        mFragment.showUserMessage(response.getMessage());
                        Intent intent = new Intent(Const.ACTION_CREATE_INVOICE);
                        intent.putExtra(Const.KEY_INVOICE, response.getData().getInvoice().toJson());
                        mContext.sendBroadcast(intent);
                        mFragment.getActivity().finish();
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        mFragment.dismissLoadingDialog();
                        mFragment.showUserMessage(message);
                    }
                });
    }

    @Override
    public void startRouteActivity(Invoice invoice) {
        Intent showPathIntent = new Intent(mContext, RouteActivity.class);
        showPathIntent.putExtra(Const.KeyIntent.KEY_INVOICE, invoice);
        mFragment.startActivity(showPathIntent);
    }
}
