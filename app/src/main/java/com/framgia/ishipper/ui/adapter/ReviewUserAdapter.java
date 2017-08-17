package com.framgia.ishipper.ui.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatRatingBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.model.ReviewUser;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dinhduc on 16/09/2016.
 */
public class ReviewUserAdapter extends RecyclerView.Adapter<ReviewUserAdapter.ViewHolder> {
    Context mContext;
    int mLayoutId;
    List<ReviewUser> mReviewUsers;

    public ReviewUserAdapter(Context context, int layoutId, List<ReviewUser> reviewUsers) {
        mContext = context;
        mLayoutId = layoutId;
        mReviewUsers = reviewUsers;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(mLayoutId, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReviewUser reviewUser = mReviewUsers.get(position);
        holder.mRatingBar.setRating(Float.parseFloat(reviewUser.getRatingPoint()));
        holder.mTvContent.setText(reviewUser.getContent());
        holder.mTvDate.setText(reviewUser.getDate());
    }

    @Override
    public int getItemCount() {
        return mReviewUsers.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.rtb_item_review_rating) AppCompatRatingBar mRatingBar;
        @BindView(R.id.tv_item_review_date) TextView mTvDate;
        @BindView(R.id.tv_item_review_content) TextView mTvContent;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
