package com.framgia.ishipper.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

/**
 * Created by HungNT on 8/3/16.
 */
public abstract class BaseToolbarActivity extends BaseActivity {
    private static final String TAG = "BaseToolbarActivity";

    public abstract Toolbar getToolbar();

    public abstract int getActivityTitle();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        allowInitViews = false;
        super.onCreate(savedInstanceState);
        setToolbar(getToolbar());
        initViews();
    }

    protected void setToolbar(Toolbar toolbar) {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getActivityTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
