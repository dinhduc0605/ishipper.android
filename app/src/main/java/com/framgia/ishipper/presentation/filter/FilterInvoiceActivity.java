package com.framgia.ishipper.presentation.filter;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseToolbarActivity;

import org.florescu.android.rangeseekbar.RangeSeekBar;

import butterknife.BindView;
import butterknife.OnClick;

public class FilterInvoiceActivity extends BaseToolbarActivity implements FilterInvoiceContract.View {
    private static final String TAG = "FilterInvoiceActivity";
    public static final String INTENT_FILTER_DATA = "INTENT_FILTER_DATA";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.seekbar_filter_order_price) RangeSeekBar<Integer> mSeekbarFilterOrderPrice;
    @BindView(R.id.seekbar_filter_ship_price) RangeSeekBar<Integer> mSeekbarFilterShipPrice;
    @BindView(R.id.seekbar_filter_distance) RangeSeekBar<Integer> mSeekbarFilterDistance;
    @BindView(R.id.seekbar_filter_weight) RangeSeekBar<Integer> mSeekbarFilterWeight;
    @BindView(R.id.edt_address_start) EditText mEdtAddressStart;
    @BindView(R.id.seekbar_filter_radius) RangeSeekBar<Integer> mSeekbarFilterRadius;
    @BindView(R.id.btn_filter_invoice) Button mBtnFilterInvoice;

    private FilterInvoiceContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public int getActivityTitle() {
        return R.string.all_filter_order;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_filter_order;
    }

    @OnClick({R.id.tv_filter_order_price,
            R.id.tv_filter_ship_price,
            R.id.tv_filter_distance,
            R.id.tv_filter_weight,
            R.id.tv_filter_radius,
            R.id.btn_filter_invoice})
    public void onClick(View view) {
        mPresenter.filterInvoice(
                (TextView) view,
                mSeekbarFilterOrderPrice,
                mSeekbarFilterShipPrice,
                mSeekbarFilterDistance,
                mSeekbarFilterWeight,
                mSeekbarFilterRadius,
                mEdtAddressStart.getText().toString());
    }

    @Override
    public void showView(TextView textView, RangeSeekBar seekBar) {
        textView.setCompoundDrawablesWithIntrinsicBounds(0, 0,
                seekBar.getVisibility() == View.VISIBLE
                        ? R.drawable.ic_arrow_drop_down_24dp
                        : R.drawable.ic_arrow_drop_up_24dp, 0);
        seekBar.setVisibility(seekBar.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean isVisible(View view) {
        return view.getVisibility() == View.VISIBLE;
    }

    @Override
    public void initViews() {
        mPresenter = new FilterInvoicePresenter(this, this);
        // getWidgetControls
        mSeekbarFilterOrderPrice.setNotifyWhileDragging(true);
        mSeekbarFilterDistance.setNotifyWhileDragging(true);
        mSeekbarFilterRadius.setNotifyWhileDragging(true);
        mSeekbarFilterShipPrice.setNotifyWhileDragging(true);
        mSeekbarFilterWeight.setNotifyWhileDragging(true);
    }
}
