package com.framgia.ishipper.presentation.notification;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.framgia.ishipper.R;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.Notification;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.util.CommonUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by HungNT on 10/21/16.
 */

public class NotificationAdapter extends
        RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int VIEW_TYPE_ITEM = 0;
    private final int VIEW_TYPE_LOADING = 1;

    private Context mContext;
    private List<Notification> mNotificationList;
    private OnItemClickListener mClickListener;

    public NotificationAdapter(Context context, List<Notification> notificationList, OnItemClickListener listener) {
        mContext = context;
        mClickListener = listener;
        mNotificationList = notificationList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_notification, parent, false);
            return new NotificationViewHolder(view);
        } else if (viewType == VIEW_TYPE_LOADING) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_loading, parent, false);
            return new LoadingViewHolder(view);
        }
        return null;

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof LoadingViewHolder) {
            Log.d("NotificationActivity", "position " + position);
            ((LoadingViewHolder) holder).bindData();
        } else {
            holder.itemView.setSelected(mNotificationList.get(position).isRead());
            ((NotificationViewHolder) holder).bindData(mNotificationList.get(position), position);
        }
    }

    @Override
    public int getItemCount() {
        return mNotificationList == null ? 0 : mNotificationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mNotificationList.get(position) == null ? VIEW_TYPE_LOADING : VIEW_TYPE_ITEM;
    }

    public class LoadingViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.item_loading_progress) ProgressBar mProgressBar;

        public LoadingViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        private void bindData() {
            mProgressBar.setIndeterminate(true);
        }
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        public Notification notificationItem;
        User currentUser;
        @BindView(R.id.imgAvatar) ImageView mImgAvatar;
        @BindView(R.id.tvContent) TextView mTvContent;
        @BindView(R.id.tvCreatedAt) TextView mTvTimePost;
        View mView;

        public NotificationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            currentUser = Config.getInstance().getUserInfo(mContext);
            mView = itemView;
        }

        private void bindData(final Notification item, final int position) {
            mTvContent.setText(item.getContent());
            notificationItem = item;
            mTvTimePost.setText(CommonUtils.showTimeAgoFrom(item.getTimePost()));
            mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mClickListener.onClick(item, position);

                }
            });
        }
    }

    public interface OnItemClickListener {
        void onClick(Notification notification, int position);
    }
}
