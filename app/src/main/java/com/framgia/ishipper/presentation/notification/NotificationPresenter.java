package com.framgia.ishipper.presentation.notification;

import com.framgia.ishipper.base.BaseToolbarActivity;
import com.framgia.ishipper.model.Notification;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.EmptyData;
import com.framgia.ishipper.net.data.ListNotificationData;
import com.framgia.ishipper.util.Const;

import java.util.List;

/**
 * Created by dinhduc on 22/11/2016.
 */

public class NotificationPresenter implements NotificationContract.Presenter {
    private BaseToolbarActivity mActivity;
    private NotificationContract.View mView;

    public NotificationPresenter(BaseToolbarActivity activity, NotificationContract.View view) {
        mActivity = activity;
        mView = view;
    }

    @Override
    public void loadMore(List<Notification> notifications,
                         NotificationAdapter adapter, final User currentUser, final int page) {
        notifications.add(null);
        adapter.notifyItemInserted(notifications.size() - 1);
        API.getAllNotification(
                currentUser.getAuthenticationToken(),
                currentUser.getRole(),
                page,
                Const.Setting.PER_PAGE,
                new API.APICallback<APIResponse<ListNotificationData>>() {
                    @Override
                    public void onResponse(APIResponse<ListNotificationData> response) {
                        mView.updateListNoti(response);
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        mActivity.showUserMessage(message);
                    }
                }
        );
    }

    @Override
    public void changeStateNotification(User currentUser, Notification notification, final int position) {
        API.updateNotification(currentUser.getUserType(),
               String.valueOf(notification.getId()),
               currentUser.getAuthenticationToken(), true,
               new API.APICallback<APIResponse<EmptyData>>() {
                   @Override
                   public void onResponse(
                           APIResponse<EmptyData> response) {
                       mView.markNotificationAsRead(position);
                   }

                   @Override
                   public void onFailure(int code, String message) {
                       //TODO: update state notification fail
                   }
               }
        );
    }
}
