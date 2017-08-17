package com.framgia.ishipper.service;

import android.widget.Toast;

import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.common.Log;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.EmptyData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by HungNT on 10/21/16.
 */

public class FirebaseInstanceService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(FirebaseInstanceService.class.getName(), "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        sendRegistrationToServer(refreshedToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
        boolean isLogin = Config.getInstance().isLogin(getBaseContext());

        if (isLogin) {
            String token = Config.getInstance().getUserInfo(getBaseContext()).getAuthenticationToken();
            API.putFCMRegistrationID(token, refreshedToken, new API.APICallback<APIResponse<EmptyData>>() {
                @Override
                public void onResponse(APIResponse<EmptyData> response) {
                }

                @Override
                public void onFailure(int code, String message) {
                    Toast.makeText(FirebaseInstanceService.this, message, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
