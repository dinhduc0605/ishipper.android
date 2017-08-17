package com.framgia.ishipper.presentation.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.ReviewUser;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.ui.adapter.ReviewUserAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

import static com.framgia.ishipper.R.layout.fragment_user_review;

public class UserReviewFragment extends BaseFragment implements UserReviewContract.View {
    private static final String KEY_USER = "User";

    @BindView(R.id.rcv_user_review) RecyclerView mListReview;

    private Context mContext;
    private List<ReviewUser> mReviewUsers = new ArrayList<>();
    private ReviewUserAdapter mAdapter;
    private User mUserInvoice;
    private User mCurrentUser;
    private UserReviewPresenter mPresenter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    public static UserReviewFragment newInstance(User user) {
        UserReviewFragment dialogFragment = new UserReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(KEY_USER, user);
        dialogFragment.setArguments(bundle);
        return dialogFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserInvoice = getArguments().getParcelable(KEY_USER);
        mCurrentUser = Config.getInstance().getUserInfo(mContext);
        mPresenter = new UserReviewPresenter(this, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_user_review;
    }

    @Override
    public void initViews() {
        mAdapter = new ReviewUserAdapter(mContext, R.layout.item_user_review, mReviewUsers);
        mListReview.setLayoutManager(new LinearLayoutManager(mContext));
        mListReview.setAdapter(mAdapter);
        mPresenter.getListReview(mCurrentUser, mUserInvoice.getId());
    }

    @Override
    public void notifyListReviewerChange(List<ReviewUser> reviewUsers) {
        mReviewUsers.clear();
        if (reviewUsers == null) return;
        mReviewUsers.addAll(reviewUsers);
        mAdapter.notifyDataSetChanged();

    }
}
