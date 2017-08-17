package com.framgia.ishipper.presentation.invoice.invoice_creation;

import android.app.TimePickerDialog;
import android.text.TextUtils;
import android.widget.TimePicker;

import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.util.NumberFormatTextWatcher;

import java.util.Calendar;
import java.util.Locale;

/**
 * Created by framgia on 18/11/2016.
 */

public class ShopCreateInvoiceStep2Presenter implements ShopCreateInvoiceStep2Contract.Presenter {
    private final ShopCreateInvoiceStep2Contract.View mView;
    private BaseFragment mFragment;

    public ShopCreateInvoiceStep2Presenter(ShopCreateInvoiceStep2Contract.View view, BaseFragment fragment) {
        mView = view;
        mFragment = fragment;
    }

    @Override
    public boolean validateDataInput(String name, String weight, String orderPrice, String shipPrice, String time) {
        if (TextUtils.isEmpty(name)) {
            mView.validateNameFailure();
            return false;
        }
        if (TextUtils.isEmpty(weight)) {
            mView.validateWeightFailure();
            return false;
        }
        if (TextUtils.isEmpty(orderPrice)) {
            mView.validateOrderPriceFailure();
            return false;
        }
        if (TextUtils.isEmpty(shipPrice)) {
            mView.validateShipPriceFailure();
            return false;
        }
        if (TextUtils.isEmpty(time)) {
            mView.validateTimeFailure();
            return false;
        }
        return true;
    }

    @Override
    public void saveInvoice(String name, String weight, String orderPrice, String shipPrice,
                            String time, String customerName, String customerPhone, String note) {
        ShopCreateInvoiceActivity.sInvoice.setName(name);
        ShopCreateInvoiceActivity.sInvoice.setWeight(Float.valueOf(weight));
        ShopCreateInvoiceActivity.sInvoice.setPrice(NumberFormatTextWatcher.getValueFromText(orderPrice));
        ShopCreateInvoiceActivity.sInvoice.setShippingPrice(NumberFormatTextWatcher.getValueFromText(shipPrice));
        ShopCreateInvoiceActivity.sInvoice.setDeliveryTime(time);
        ShopCreateInvoiceActivity.sInvoice.setCustomerName(customerName);
        ShopCreateInvoiceActivity.sInvoice.setCustomerNumber(customerPhone);
        ShopCreateInvoiceActivity.sInvoice.setDescription(note);
        ShopCreateInvoiceActivity.sInvoice.setStatus(Invoice.STATUS_INIT);

        if (mFragment.getActivity() instanceof ShopCreateInvoiceActivity) {
            ((ShopCreateInvoiceActivity) mFragment.getActivity())
                    .addFragment(new ShopCreateInvoiceStep3Fragment());
        }
    }

    @Override
    public void pickTime(final String userTime) {
        Calendar calendar = Calendar.getInstance(Locale.getDefault());
        TimePickerDialog dialog = new TimePickerDialog(mFragment.getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        mView.showTextTime(String.valueOf(i) + ":" + i1 + " " + userTime);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        dialog.show();
    }


}
