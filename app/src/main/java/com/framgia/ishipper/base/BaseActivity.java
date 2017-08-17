package com.framgia.ishipper.base;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.framgia.ishipper.R;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.APIDefinition;
import com.framgia.ishipper.ui.listener.SocketCallback;

import org.java_websocket.WebSocket;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;

/**
 * Created by HungNT on 8/3/16.
 */
public abstract class BaseActivity extends AppCompatActivity {
    private static final String TAG = "BaseActivity";

    private WebSocketClient mWebSocketClient;
    protected ProgressDialog mDialog;

    protected boolean allowInitViews = true;

    public abstract void initViews();

    public abstract int getLayoutId();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        ButterKnife.bind(this);
        if (allowInitViews) initViews();
    }

    protected void connectWebSocket(final SocketCallback callback) {
        URI uri;
        User user = Config.getInstance().getUserInfo(BaseActivity.this);
        try {
            uri = new URI(APIDefinition.WebSockets.WEBSOCKETS_URL);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }
        Map<String, String> headers = new HashMap<>();
        headers.put(APIDefinition.WebSockets.PARAM_AUTHORIZATION, user.getAuthenticationToken());
        mWebSocketClient = new WebSocketClient(uri, new Draft_17(), headers,
                                               APIDefinition.WebSockets.TIMEOUT_DEFAULT) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                android.util.Log.i("Websocket", "Opened");
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onCallback(mWebSocketClient.getConnection(), message);
                        }
                    }
                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                android.util.Log.i("Websocket", "Closed " + s);
            }

            @Override
            public void onError(Exception e) {
                android.util.Log.i("Websocket", "Error " + e.getMessage());
                e.printStackTrace();
            }
        };
        mWebSocketClient.connect();
    }

    public void showUserMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void showUserMessage(@StringRes int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    public void showDialog() {
        if (mDialog == null) initDialog();
        if (mDialog != null && !mDialog.isShowing()) mDialog.show();
    }

    public void dismissDialog() {
        if (mDialog != null && mDialog.isShowing()) mDialog.dismiss();
    }

    private void initDialog() {
        mDialog = new ProgressDialog(this);
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.setMessage(getString(R.string.dialog_loading_message));
        mDialog.setIndeterminate(true);
        mDialog.setCanceledOnTouchOutside(false);
    }

    protected void disconnectWebSocket() {
        if (mWebSocketClient != null) mWebSocketClient.close();
    }

    protected WebSocket getWebSocket() {
        return mWebSocketClient.getConnection();
    }
}
