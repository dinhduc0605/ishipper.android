package com.framgia.ishipper.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.framgia.ishipper.R;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.common.Log;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.ListUserData;
import com.framgia.ishipper.ui.activity.SearchUserActivity;
import com.framgia.ishipper.ui.adapter.BlackListAdapter;
import com.framgia.ishipper.ui.adapter.GeneralFragmentPagerAdapter;
import com.framgia.ishipper.util.Const;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class BlacklistFragment extends Fragment {
    private static final String TAG = "BlacklistFragment";

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    private Unbinder mUnbinder;
    private Context mContext;
    private BlackListAdapter mBlackListAdapter;
    private List<User> mBlackListUser;
    private User mUser;

    public BlacklistFragment() {
    }

    public static BlacklistFragment newInstance() {
        return new BlacklistFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_blacklist_shipper, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        invalidView(view);
        return view;
    }

    private void invalidView(View view) {
        setHasOptionsMenu(true);
        mContext = view.getContext();
        mUser = Config.getInstance().getUserInfo(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        setUpRecycleView(mRecyclerView);
    }

    private void setUpRecycleView(RecyclerView recyclerView) {
        if (mBlackListUser == null)
            mBlackListUser = new ArrayList<>();
        if (mBlackListAdapter == null)
            mBlackListAdapter = new BlackListAdapter(mContext, mBlackListUser);
        recyclerView.setAdapter(mBlackListAdapter);
        if (mUser.getRole().equals(User.ROLE_SHOP)) {
            getBlackListShipper();
        } else {
            getBlackListShop();
        }
    }

    private void getBlackListShop() {
        API.getBlackListShop(mUser.getAuthenticationToken(),
                new API.APICallback<APIResponse<ListUserData>>() {
                    @Override
                    public void onResponse(APIResponse<ListUserData> response) {
                        Log.d(TAG, response.getMessage());
                        mBlackListUser.clear();
                        mBlackListUser.addAll(response.getData().getShippersList());
                        mBlackListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void getBlackListShipper() {
        API.getBlackListShipper(mUser.getAuthenticationToken(),
                new API.APICallback<APIResponse<ListUserData>>() {
                    @Override
                    public void onResponse(APIResponse<ListUserData> response) {
                        Log.d(TAG, response.getMessage());
                        mBlackListUser.clear();
                        mBlackListUser.addAll(response.getData().getShippersList());
                        mBlackListAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.favorite_add) {
            startActivityForResult(
                    new Intent(mContext, SearchUserActivity.class),
                    Const.RequestCode.REQUEST_CODE_SEARCH_USER
            );
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Const.RequestCode.REQUEST_CODE_SEARCH_USER) {
            if (resultCode == Activity.RESULT_OK) {
                User user = data.getParcelableExtra(Const.KEY_USER);
                if (user != null) {
                    mBlackListUser.add(user);
                    mBlackListAdapter.notifyDataSetChanged();
                }
            }
        }
    }
}