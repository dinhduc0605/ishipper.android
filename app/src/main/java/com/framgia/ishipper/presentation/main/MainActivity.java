package com.framgia.ishipper.presentation.main;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseToolbarActivity;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.Invoice;
import com.framgia.ishipper.model.SocketResponse;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.EmptyData;
import com.framgia.ishipper.presentation.authenication.login.LoginActivity;
import com.framgia.ishipper.presentation.favorite.FavoriteListFragment;
import com.framgia.ishipper.presentation.fb_invoice.FBInvoiceFragment;
import com.framgia.ishipper.presentation.invoice.invoice_creation.ShopCreateInvoiceActivity;
import com.framgia.ishipper.presentation.manager_invoice.InvoiceManagerFragment;
import com.framgia.ishipper.presentation.manager_invoice.ShipperInvoiceManagerFragment;
import com.framgia.ishipper.presentation.manager_invoice.ShopInvoiceManagerFragment;
import com.framgia.ishipper.presentation.notification.NotificationActivity;
import com.framgia.ishipper.presentation.profile.UserProfileActivity;
import com.framgia.ishipper.presentation.settings.SettingActivity;
import com.framgia.ishipper.ui.listener.OnInvoiceUpdate;
import com.framgia.ishipper.ui.listener.OnShipperUpdateListener;
import com.framgia.ishipper.ui.listener.SocketCallback;
import com.framgia.ishipper.util.Const;
import com.framgia.ishipper.util.StorageUtils;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.mikhaellopez.circularimageview.CircularImageView;

import org.java_websocket.WebSocket;
import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;

public class MainActivity extends BaseToolbarActivity implements SocketCallback {
    private static final String TAG = "MainActivity";

    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.main_navigation) NavigationView mNavigationView;
    @BindView(R.id.main_drawer_layout) DrawerLayout mDrawerLayout;
    @BindView(R.id.appbar) AppBarLayout mAppbar;

    public static boolean firstTime = true;
    private User mCurrentUser;
    private int mSelectedId;
    private boolean doubleBackToExitPressedOnce;
    private TextView mTvNotifyCount;
    private int mNotifyCount = 0;

    private OnInvoiceUpdate mOnInvoiceUpdate;
    private OnShipperUpdateListener mShipperUpdateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        connectWebSocket(this);
    }

    @Override
    public void initViews() {
        mCurrentUser = Config.getInstance().getUserInfo(this);
        if (mCurrentUser.isShop()) {
            mNavigationView.inflateMenu(R.menu.menu_nav_drawer_shop);
        } else {
            mNavigationView.inflateMenu(R.menu.menu_nav_drawer_shipper);
        }
        mNavigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem item) {
                        selectItem(item.getItemId());
                        return true;
                    }
                });
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(
                this,
                mDrawerLayout,
                getToolbar(),
                R.string.drawer_open,
                R.string.drawer_close);
        mDrawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
        /** Set listener to image icon in drawer */
        View view = mNavigationView.getHeaderView(0);
        CircularImageView imageView = (CircularImageView) view.findViewById(R.id.nav_user_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getBaseContext(), UserProfileActivity.class));
            }
        });
        TextView tvName = (TextView) view.findViewById(R.id.nav_header_user_name);
        TextView tvCountSuccessInvoice = (TextView) view.findViewById(R.id.tv_invoice_success);
        TextView tvCountAllInvoice = (TextView) view.findViewById(R.id.tv_all_invoice);
        tvName.setText(mCurrentUser.getName());
        tvCountAllInvoice.setText(String.valueOf(mCurrentUser.getCountTotalInvoice()));
        tvCountSuccessInvoice.setText(String.valueOf(mCurrentUser.getCountInvoiceSuccess()));

        if (mCurrentUser.isShop()) {
            selectItem(R.id.nav_nearby_shipper);
        } else {
            selectItem(R.id.nav_nearby_order);
        }
    }

    private void selectItem(int id) {
        mNavigationView.setCheckedItem(id);
        mDrawerLayout.closeDrawer(mNavigationView);
        if (mSelectedId == id) return;
        setElevationAppBar(Const.ElevationLevel.DEFAULT);
        Fragment fragment;
        String tag = null;
        switch (id) {
            case R.id.nav_nearby_order:
                mSelectedId = id;
                fragment = new MainContentFragment();
                getToolbar().setTitle(getString(R.string.nav_nearby_order_item));
                tag = MainContentFragment.class.getName();
                break;
            case R.id.nav_nearby_shipper:
                mSelectedId = id;
                fragment = new MainContentFragment();
                getToolbar().setTitle(getString(R.string.nav_nearby_shipper_item));
                tag = MainContentFragment.class.getName();
                break;
            case R.id.nav_order_management:
                setElevationAppBar(Const.ElevationLevel.NONE);
                mSelectedId = id;
                getToolbar().setTitle(getString(R.string.title_activity_order_manager));
                if (mCurrentUser.isShop()) {
                    fragment =
                            ShipperInvoiceManagerFragment.instantiate(MainActivity.this,
                                                                      ShopInvoiceManagerFragment.class.getName(),
                                                                      null);
                    tag = InvoiceManagerFragment.class.getName();
                } else {
                    fragment =
                            ShipperInvoiceManagerFragment.instantiate(MainActivity.this,
                                                                      ShipperInvoiceManagerFragment.class.getName(),
                                                                      null);
                    tag = InvoiceManagerFragment.class.getName();
                }
                break;
            case R.id.nav_fb_order:
                mSelectedId = id;
                getToolbar().setTitle(getString(R.string.title_facebook_order));
                fragment = FBInvoiceFragment.newInstance(true);
                tag = FBInvoiceFragment.class.getName();
                break;
            case R.id.nav_ship_management:
                mSelectedId = id;
                getToolbar().setTitle(getString(R.string.nav_ship_management_item));
                fragment = FavoriteListFragment.newInstance();
                tag = FavoriteListFragment.class.getName();
                break;
            case R.id.nav_shop_management:
                mSelectedId = id;
                getToolbar().setTitle(getString(R.string.nav_shop_management_item));
                fragment = FavoriteListFragment.newInstance();
                tag = FavoriteListFragment.class.getName();
                break;
            case R.id.nav_create_order:
                startActivity(new Intent(this, ShopCreateInvoiceActivity.class));
                return;
            case R.id.nav_sign_out:
                signOut();
                return;
            case R.id.nav_setting:
                mNavigationView.setCheckedItem(mSelectedId);
                startActivityForResult(new Intent(this, SettingActivity.class),
                        Const.REQUEST_SETTING);
                return;
            default:
                return;
        }
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main_container, fragment, tag)
                    .commit();
        }
    }

    private void signOut() {
        StorageUtils.clearAll(getApplicationContext());
        API.signOut(mCurrentUser.getAuthenticationToken(),
                mCurrentUser.getPhoneNumber(),
                new API.APICallback<APIResponse<EmptyData>>() {
                    @Override
                    public void onResponse(APIResponse<EmptyData> response) {
                        //TODO: on logout success
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        //TODO: on logout failed
                    }
                });
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_content, menu);
        MenuItem item = menu.findItem(R.id.menu_notification);
        MenuItemCompat.setActionView(item, R.layout.icon_notification);
        View view = MenuItemCompat.getActionView(item);
        mTvNotifyCount = (TextView) view.findViewById(R.id.tvNotifCount);
        setNotifCount(mNotifyCount);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setNotifCount(Const.ZERO);
                startActivity(new Intent(getBaseContext(), NotificationActivity.class));
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    private void setNotifCount(int count) {
        if (mTvNotifyCount == null) return;
        if (count <= Const.ZERO) {
            mTvNotifyCount.setVisibility(View.GONE);
        } else {
            mTvNotifyCount.setVisibility(View.VISIBLE);
            mTvNotifyCount.setText(String.valueOf(count));
        }
    }

    private void setElevationAppBar(int level) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mAppbar.setElevation(level);
        }
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public int getActivityTitle() {
        return R.string.title_activity_main;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        getSupportFragmentManager()
                .findFragmentByTag(MainContentFragment.class.getName())
                .onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == Const.RequestCode.REQUEST_CHECK_SETTINGS) {
            getSupportFragmentManager()
                    .findFragmentByTag(MainContentFragment.class.getName())
                    .onActivityResult(requestCode, resultCode, data);
        } else if (requestCode == Const.REQUEST_SETTING) {
            if (!Config.getInstance().getUserInfo(getApplicationContext()).isShop()) {
                if (mSelectedId == R.id.nav_nearby_order) {
                    // Don't need to update nearby invoice if user is shop
                    getSupportFragmentManager()
                            .findFragmentByTag(MainContentFragment.class.getName())
                            .onActivityResult(requestCode, resultCode, data);
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > Const.ZERO) {
            super.onBackPressed();
            return;
        }

        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        // set double click back to exit
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, R.string.message_back_to_exit, Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, Const.TIME_DELAY_EXIT);
    }

    @Override
    protected void onStop() {
        super.onStop();
        disconnectWebSocket();
    }

    @Override
    public void onCallback(WebSocket websocket, String text) {
        //TODO: handle data from socket server
        SocketResponse response;
        try {
            response = new Gson().fromJson(text, SocketResponse.class);
        } catch (JsonSyntaxException e) {
            return;
        }
        if (text.contains(Const.WELCOME)) {
            subscribeChannel(mCurrentUser.getAuthenticationToken(), websocket, Const.CHANNEL_REALTIME);
        }
        if (response.getAction() == null) return;
        switch (response.getAction()) {
            case Const.ACTION_UNREAD_NOTIFICATION:
                if (response.getUnreadNotification() >= Const.ZERO) {
                    mNotifyCount = response.getUnreadNotification();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setNotifCount(mNotifyCount);
                        }
                    });
                }
                break;
            case Const.ACTION_NEW_INVOICE:
                Invoice invoice = response.getInvoice();
                if (mOnInvoiceUpdate != null) {
                    mOnInvoiceUpdate.onInvoiceCreate(invoice);
                }
                break;
            case Const.ACTION_REMOVE_INVOICE:
                Invoice data = response.getInvoice();
                if (mOnInvoiceUpdate != null) {
                    mOnInvoiceUpdate.onInvoiceRemove(data);
                }
                break;
            case Const.ACTION_SHIPPER_ONLINE:
                if (mShipperUpdateListener != null) {
                    mShipperUpdateListener.onShipperOnline(response.getUser());
                }
                break;
            case Const.ACTION_SHIPPER_OFFLINE:
                if (mShipperUpdateListener != null) {
                    mShipperUpdateListener.onShipperOffline(response.getUser());
                }
                break;
            case  Const.ACTION_CANCEL_INVOICE:
                sendBroadcastCancelInvoice(text);
                break;
            //TODO: add other action
            default:
                break;
        }
    }

    private void sendBroadcastCancelInvoice(String sockerResponse) {
        Intent intent = new Intent();
        intent.setAction(Const.ACTION_CANCEL_INVOICE);
        intent.putExtra(Const.KEY_SOCKET_RESPONSE, sockerResponse);
        sendBroadcast(intent);

    }

    public void setOnInvoiceUpdate(OnInvoiceUpdate onInvoiceUpdate) {
        mOnInvoiceUpdate = onInvoiceUpdate;
    }

    public void setShipperUpdateListener(OnShipperUpdateListener shipperUpdateListener) {
        mShipperUpdateListener = shipperUpdateListener;
    }

    private void subscribeChannel(String token, WebSocket websocket, String channel) {
        JSONObject object = new JSONObject();
        JSONObject identifyObject = new JSONObject();
        try {
            identifyObject.put(Const.CHANNEL, channel);
            identifyObject.put(Const.AUTHENTICATION_TOKEN, token);
            object.put(Const.COMMAND, Const.COMMAND_SUBSCRIBE);
            object.put(Const.IDENTIFIER, identifyObject.toString());
            websocket.send(object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
