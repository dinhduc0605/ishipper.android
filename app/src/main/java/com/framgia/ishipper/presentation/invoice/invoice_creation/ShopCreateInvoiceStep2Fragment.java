package com.framgia.ishipper.presentation.invoice.invoice_creation;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.util.CommonUtils;
import com.framgia.ishipper.util.NumberFormatTextWatcher;

import butterknife.BindView;
import butterknife.OnClick;

import static com.framgia.ishipper.R.string.today;
import static com.framgia.ishipper.R.string.tomorrow;

/**
 * Created by vuduychuong1994 on 7/22/16.
 */
public class ShopCreateInvoiceStep2Fragment extends BaseFragment
        implements RadioGroup.OnCheckedChangeListener, ShopCreateInvoiceStep2Contract.View {

    @BindView(R.id.edt_order_name) EditText mEdtOrderName;
    @BindView(R.id.edt_order_weight) EditText mEdtOrderWeight;
    @BindView(R.id.edt_order_price) EditText mEdtOrderPrice;
    @BindView(R.id.edt_ship_price) EditText mEdtShipPrice;
    @BindView(R.id.text_time) TextView mTextTime;
    @BindView(R.id.edt_customer_name) EditText mEdtCustomerName;
    @BindView(R.id.edt_customer_phone) EditText mEdtCustomerPhone;
    @BindView(R.id.rg_pick_time) RadioGroup mRgPickTime;
    @BindView(R.id.btn_submit) Button mBtnSubmit;
    @BindView(R.id.edt_note) EditText mEdtNote;

    private ShopCreateInvoiceStep2Presenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_shop_create_order_step2;
    }

    @Override
    public void initViews() {
        mPresenter = new ShopCreateInvoiceStep2Presenter(this, this);
        mEdtOrderPrice.addTextChangedListener(new NumberFormatTextWatcher(mEdtOrderPrice));
        mEdtShipPrice.addTextChangedListener(new NumberFormatTextWatcher(mEdtShipPrice));
        mRgPickTime.setOnCheckedChangeListener(this);
    }

    @OnClick({R.id.text_time, R.id.btn_submit, R.id.rb_today_select, R.id.rb_tomorrow_select})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_time:
                mRgPickTime.setVisibility(
                        mRgPickTime.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.btn_submit:
                // Set invoice
                CommonUtils.hideKeyboard(getActivity());
                if (!mPresenter.validateDataInput(
                        mEdtCustomerName.getText().toString(),
                        mEdtOrderWeight.getText().toString(),
                        mEdtOrderPrice.getText().toString(),
                        mEdtShipPrice.getText().toString(),
                        mTextTime.getText().toString())) {
                    return;
                }

                mPresenter.saveInvoice(mEdtOrderName.getText().toString(),
                        mEdtOrderWeight.getText().toString(),
                        mEdtOrderPrice.getText().toString(),
                        mEdtShipPrice.getText().toString(),
                        mTextTime.getText().toString(),
                        mEdtCustomerName.getText().toString(),
                        mEdtCustomerPhone.getText().toString(),
                        mEdtNote.getText().toString()
                );
                break;
            case R.id.rb_today_select:
                mPresenter.pickTime(getString(today));
                break;
            case R.id.rb_tomorrow_select:
                mPresenter.pickTime(getString(tomorrow));
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch (i) {
            case R.id.rb_now:
                mTextTime.setText(R.string.pick_time_now);
                break;
            case R.id.rb_today_morning:
                mTextTime.setText(R.string.pick_time_today_morning);
                break;
            case R.id.rb_today_afternoon:
                mTextTime.setText(R.string.pick_time_today_afternoon);
                break;
            case R.id.rb_today_evening:
                mTextTime.setText(R.string.pick_time_today_evening);
                break;
            case R.id.rb_tomorrow_morning:
                mTextTime.setText(R.string.pick_time_tomorrow_morning);
                break;
            case R.id.rb_tomorrow_afternoon:
                mTextTime.setText(R.string.pick_time_tomorrow_afternoon);
                break;
            case R.id.rb_tomorrow_evening:
                mTextTime.setText(R.string.pick_time_tomorrow_evening);
                break;
            default:
                break;
        }
        radioGroup.setVisibility(View.GONE);
    }

    @Override
    public void validateNameFailure() {
        showUserMessage(R.string.create_step_2_require_customer_name);
        mEdtCustomerName.requestFocus();
    }

    @Override
    public void validateWeightFailure() {
        showUserMessage(R.string.create_step_2_require_weight);
        mEdtOrderWeight.requestFocus();
    }

    @Override
    public void validateOrderPriceFailure() {
        showUserMessage(R.string.create_step_2_require_invoice_price);
        mEdtOrderPrice.requestFocus();
    }

    @Override
    public void validateShipPriceFailure() {
        showUserMessage(R.string.create_step_2_require_ship_price);
        mEdtShipPrice.requestFocus();
    }

    @Override
    public void validateTimeFailure() {
        showUserMessage(R.string.fragment_create_order_time_require);
    }

    @Override
    public void showTextTime(String stringTime) {
        mTextTime.setText(stringTime);
    }
}
