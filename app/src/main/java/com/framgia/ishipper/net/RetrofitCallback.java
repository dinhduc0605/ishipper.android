package com.framgia.ishipper.net;

import com.framgia.ishipper.common.Log;
import com.framgia.ishipper.util.Const.ErrorMessage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by vuduychuong1994 on 8/23/16.
 */
public class RetrofitCallback<T> implements Callback<T> {
    private static final String TAG = "API";
    public static final int LOCAL_ERROR = 1111;
    public static final String SERVER_ERROR = "Server Error";

    private API.APICallback<T> mCallback;

    public RetrofitCallback(API.APICallback<T> callback) {
        mCallback = callback;
    }

    @Override
    public void onResponse(Call<T> call, Response<T> response) {
        APIResponse apiResponse = (APIResponse) response.body();
        if (apiResponse == null) {
            mCallback.onFailure(LOCAL_ERROR, ErrorMessage.ERROR_MSG_LOCAL);
            Log.d(TAG, SERVER_ERROR);
        } else if (apiResponse.isSuccess()) {
            mCallback.onResponse(response.body());
        } else {
            mCallback.onFailure(apiResponse.getCode(), apiResponse.getMessage());
        }
    }

    @Override
    public void onFailure(Call<T> call, Throwable t) {
        mCallback.onFailure(LOCAL_ERROR, ErrorMessage.ERROR_MSG_LOCAL);
    }
}
