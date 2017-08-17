package com.framgia.ishipper.presentation.favorite;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.data.ListUserData;
import com.framgia.ishipper.ui.listener.OnRemoveUserListener;
import com.framgia.ishipper.util.Const;
import com.framgia.ishipper.widget.dialog.ConfirmDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class FavoriteListFragment extends BaseFragment implements FavoriteListContract.View,
        OnRemoveUserListener {
    private static final String TAG = "FavoriteListFragment";

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.emptyView) LinearLayout mLayoutEmpty;
    private Context mContext;
    private FavoriteListAdapter mFavoriteListAdapter;
    private List<User> mFavoriteList = new ArrayList<>();
    private User mCurrentUser;
    private FavoriteListPresenter mPresenter;

    public static FavoriteListFragment newInstance() {
        return new FavoriteListFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter = new FavoriteListPresenter(mContext, this, this);
        mPresenter.getFavoriteList(mCurrentUser);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_favorite_user;
    }

    @Override
    public void initViews() {
        mCurrentUser = Config.getInstance().getUserInfo(mContext);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        mFavoriteListAdapter = new FavoriteListAdapter(mContext, mFavoriteList, this);
        mRecyclerView.setAdapter(mFavoriteListAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.clear();
        inflater.inflate(R.menu.menu_user_manage, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_favorite_add:
                mPresenter.startSearchUserActivity();
                return true;
            case R.id.menu_delete_all:
                if (mFavoriteList == null || mFavoriteList.size() == 0) return false;
                showConfirmDeleteAll();
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Const.RequestCode.REQUEST_SEARCH_FAVORITE) {
                final User user = data.getParcelableExtra(Const.KEY_USER);
                if (user != null) {
                    mPresenter.addFavoriteUser(mCurrentUser, user);
                }
            }
        }
    }

    @Override
    public void showConfirmDeleteAll() {
        new ConfirmDialog(mContext)
                .setIcon(R.drawable.ic_delete_white_24dp)
                .setMessage(getString(R.string.delete_all_dialog_message))
                .setTitle(getString(R.string.delete_all_dialog_title))
                .setButtonCallback(new ConfirmDialog.ConfirmDialogCallback() {
                    @Override
                    public void onPositiveButtonClick(ConfirmDialog confirmDialog) {
                        mPresenter.deleteAllFavoriteList(mCurrentUser);
                        confirmDialog.cancel();
                    }

                    @Override
                    public void onNegativeButtonClick(ConfirmDialog confirmDialog) {
                        confirmDialog.cancel();
                    }
                })
                .show();
    }

    @Override
    public void showListUser(ListUserData listUserData) {
        if (mFavoriteList == null || mFavoriteListAdapter == null) return;
        mFavoriteList.clear();
        if (listUserData != null) {
            mFavoriteList.addAll(listUserData.getShippersList());
        }
        mFavoriteListAdapter.notifyDataSetChanged();
        showEmptyLayout(mFavoriteList.isEmpty());
    }

    @Override
    public void insertUser(int index, User favoriteUser) {
        mFavoriteList.add(index, favoriteUser);
        mFavoriteListAdapter.notifyItemInserted(index);
        showEmptyLayout(mFavoriteList.isEmpty());
    }

    @Override
    public void showEmptyLayout(boolean active) {
        mLayoutEmpty.setVisibility(active ? View.VISIBLE : View.GONE);
    }

    @Override
    public void removeUser(User user) {
        if (mFavoriteList == null || mFavoriteListAdapter == null) return;
        int position = mFavoriteList.indexOf(user);
        try {
            mFavoriteList.remove(position);
            mFavoriteListAdapter.notifyItemRemoved(position);
            showEmptyLayout(mFavoriteList.isEmpty());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.layoutEmpty)
    public void onClick() {
        mPresenter.getFavoriteList(mCurrentUser);
    }

    @Override
    public void onRemove(User user, int position) {
        confirmRemoveUser(user);
    }

    @Override
    public void confirmRemoveUser(final User user) {
        new ConfirmDialog(mContext)
            .setMessage(mContext.getString(R.string.dialog_remove_user_from_favorite))
            .setTitle(mContext.getString(R.string.dialog_delete_message))
            .setIcon(R.drawable.ic_delete_white_24dp)
            .setButtonCallback(new ConfirmDialog.ConfirmDialogCallback() {
                @Override
                public void onPositiveButtonClick(ConfirmDialog confirmDialog) {
                    mPresenter.sendRequestRemoveUser(user);
                    confirmDialog.cancel();
                }

                @Override
                public void onNegativeButtonClick(ConfirmDialog confirmDialog) {
                    confirmDialog.cancel();
                }
            }).show();
    }
}
