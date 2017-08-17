package com.framgia.ishipper.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.framgia.ishipper.R;

/**
 * Created by HungNT on 9/5/16.
 */
public class InputValidate {

    public static boolean checkPhoneNumber(EditText editText, Context context) {
        return notEmpty(editText, context);
    }

    public static boolean confirmPassword(EditText edtNewPassword, EditText edtConfirmPassword, Context context) {
        if (checkPassword(edtNewPassword, context) && checkPassword(edtConfirmPassword, context)) {
            if (!edtConfirmPassword.getText().toString().equals(edtNewPassword.getText().toString())) {
                Toast.makeText(context, R.string.error_password_match, Toast.LENGTH_SHORT).show();
            } else if (edtNewPassword.getText().toString().length() < Const.MIN_PASSWORD_LENGTH)
                Toast.makeText(context, R.string.edt_password_short, Toast.LENGTH_SHORT).show();
            else {
                return true;
            }
        }

        return false;
    }

    public static boolean checkPassword(EditText editText, Context context) {
        String password = editText.getText().toString();
        if (password.isEmpty()) {
            Toast.makeText(context, R.string.error_password_empty, Toast.LENGTH_SHORT).show();
            editText.requestFocus();
        } else if (password.contains(" ")) {
            Toast.makeText(context, R.string.error_password_space, Toast.LENGTH_SHORT).show();
            editText.requestFocus();
        } else if (password.length() < Const.MIN_PASSWORD_LENGTH) {
            Toast.makeText(context, R.string.edt_password_short, Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }

    public static boolean notEmpty(EditText editText, Context context) {
        if (TextUtils.isEmpty(editText.getText())) {
            Toast.makeText(context, R.string.err_msg_not_empty, Toast.LENGTH_SHORT).show();
            editText.requestFocus();
            return false;
        }

        return true;

    }

    public static boolean checkPin(EditText edtPin, Context context) {
        String pin = edtPin.getText().toString();
        if (pin.length() != Const.PIN_LENGTH) {
            Toast.makeText(context, R.string.edt_pin_invalid, Toast.LENGTH_SHORT).show();
        } else {
            return true;
        }
        return false;
    }
}
