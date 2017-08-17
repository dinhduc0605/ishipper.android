package com.framgia.ishipper.presentation.filter;

import android.widget.TextView;

import org.florescu.android.rangeseekbar.RangeSeekBar;

/**
 * Created by dinhduc on 22/11/2016.
 */

public class FilterInvoiceContract {
    interface View {
        void showView(TextView textView, RangeSeekBar seekBar);

        boolean isVisible(android.view.View view);
    }

    interface Presenter {
        void filterInvoice(
                TextView textView,
                RangeSeekBar seekbarFilterOrderPrice,
                RangeSeekBar seekbarFilterShipPrice,
                RangeSeekBar seekbarFilterDistance,
                RangeSeekBar seekbarFilterWeight,
                RangeSeekBar seekbarFilterRadius,
                String startAddress);

    }
}
