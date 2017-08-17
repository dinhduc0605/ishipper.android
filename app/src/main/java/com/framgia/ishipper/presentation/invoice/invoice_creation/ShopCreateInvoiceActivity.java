package com.framgia.ishipper.presentation.invoice.invoice_creation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseToolbarActivity;
import com.framgia.ishipper.model.Invoice;

import butterknife.BindView;

public class ShopCreateInvoiceActivity extends BaseToolbarActivity {
    private static final String TAG = "ShopCreateInvoiceActivity";
    public static Invoice sInvoice = new Invoice();
    @BindView(R.id.toolbar) Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void addFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(fragment.getClass().getName())
                .commit();
        Log.d(TAG, "addFragment: " + fragment.getClass().getName());
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public int getActivityTitle() {
        return R.string.title_activity_create_invoice;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_shop_create_order;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    @Override
    public void initViews() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, new ShopCreateInvoiceStep1Fragment())
                .commit();
    }
}
