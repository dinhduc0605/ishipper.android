package com.framgia.ishipper.presentation.profile;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseToolbarActivity;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.ui.adapter.SearchUserAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class SearchUserActivity extends BaseToolbarActivity
        implements SearchUserAdapter.OnSearchItemCallback,
        SearchUserContract.View {
    private static final String EXTRA_REQUEST_CODE = "EXTRA_REQUEST_CODE";
    private static final String RESULT_USER = "RESULT_USER";
    @BindView(R.id.edtSearch) EditText mEdtSearch;
    @BindView(R.id.imgSearch) ImageView mImgSearch;
    @BindView(R.id.layoutSearch) RelativeLayout mLayoutSearch;
    @BindView(R.id.rvResult) RecyclerView mRvResult;
    @BindView(R.id.toolbar) Toolbar mToolbar;

    private SearchUserAdapter mAdapter;
    private List<User> mListUsers = new ArrayList<>();
    private SearchUserPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new SearchUserPresenter(this, this);
    }

    @Override
    public Toolbar getToolbar() {
        return mToolbar;
    }

    @Override
    public int getActivityTitle() {
        return R.string.activity_search_title;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_search_user;
    }

    @Override
    public void onAddButtonClick(User data) {
        mPresenter.gotoAddUser(data);
    }

    @Override
    public void onItemClick(User data) {
        // TODO show user info
    }

    @Override
    public void initViews() {
        mAdapter = new SearchUserAdapter(mListUsers, this);
        mRvResult.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mRvResult.setAdapter(mAdapter);

        mEdtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (mEdtSearch.getText().toString().equals("")) {
                    mImgSearch.setVisibility(View.VISIBLE);
                } else {
                    mImgSearch.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
                mAdapter.notifyDataSetChanged();
                if (editable.length() == 0) return;
                mPresenter.searchUser(editable.toString());
            }
        });
    }

    @Override
    public void updateListUser(List<User> listUsers) {
        mListUsers.clear();
        mListUsers.addAll(listUsers);
        mAdapter.notifyDataSetChanged();
    }
}
