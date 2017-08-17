package com.framgia.ishipper.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by HungNT on 9/28/16.
 */

public class NumberFormatTextWatcher implements TextWatcher {
    private EditText mEditText;

    public NumberFormatTextWatcher(EditText input) {
        mEditText = input;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        mEditText.removeTextChangedListener(this);

        try {
            String originalString = s.toString();

            long val;
            if (originalString.contains(",")) {
                originalString = originalString.replaceAll(",", "");
            }
            val = Long.parseLong(originalString);

            DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
            formatter.applyPattern("#,###,###,###");
            String formattedString = formatter.format(val);

            //setting text after format to EditText
            mEditText.setText(formattedString);
            mEditText.setSelection(mEditText.getText().length());
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        }

        mEditText.addTextChangedListener(this);
    }

    public static long getValueFromText(String originalString) {
        if (originalString.contains(",")) {
            originalString = originalString.replaceAll(",", "");
        }
        return Long.parseLong(originalString);
    }
}
