package com.framgia.ishipper.presentation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.ishipper.R;
import com.framgia.ishipper.ui.adapter.MainTabAdapter;
import com.framgia.ishipper.widget.view.CustomViewPager;
import com.framgia.ishipper.util.Const;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by dinhduc on 18/07/2016.
 */
public class MainContentFragment extends Fragment {
    private static final String TAG = "MainContentFragment";
    private Unbinder mUnbinder;

    @BindView(R.id.tablayout_main)
    TabLayout mTabLayout;
    @BindView(R.id.main_fragment_container)
    CustomViewPager mViewPager;

    private MainTabAdapter mAdapter;


    public MainContentFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main_content, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        mViewPager.setPagingEnabled(false);
        mAdapter = new MainTabAdapter(getChildFragmentManager(), getContext());
        mViewPager.setAdapter(mAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        return view;
    }

    @Override
    public void onDestroy() {
        mUnbinder.unbind();
        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        ((MainTabAdapter) mViewPager.getAdapter())
                .getFragment(mViewPager.getCurrentItem())
                .onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.RequestCode.REQUEST_CHECK_SETTINGS ||
            requestCode == Const.REQUEST_SETTING) {
            ((MainTabAdapter) mViewPager.getAdapter())
                    .getFragment(mViewPager.getCurrentItem())
                    .onActivityResult(requestCode, resultCode, data);
        }
    }
}
