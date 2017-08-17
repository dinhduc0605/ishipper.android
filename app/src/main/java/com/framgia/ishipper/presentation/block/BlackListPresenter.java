package com.framgia.ishipper.presentation.block;
import android.app.Dialog;
import android.content.Intent;
import android.widget.Toast;
import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.common.Log;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.AddBlacklistData;
import com.framgia.ishipper.net.data.EmptyData;
import com.framgia.ishipper.net.data.ListUserData;
import com.framgia.ishipper.presentation.profile.SearchUserActivity;
import com.framgia.ishipper.util.CommonUtils;
import com.framgia.ishipper.util.Const;

/**
 * Created by vuduychuong1994 on 11/21/16.
 */

public class BlackListPresenter implements BlackListContract.Presenter {
    private static final String TAG = BlackListPresenter.class.getName();

    private BaseActivity mActivity;
    private BlackListContract.View mView;

    public BlackListPresenter(BaseActivity activity, BlackListContract.View view) {
        mActivity = activity;
        mView = view;
    }

    @Override
    public void getBlackList(User user) {
        API.getBlackList(user.getAuthenticationToken(), user.getRole(),
             new API.APICallback<APIResponse<ListUserData>>() {
                 @Override
                 public void onResponse(APIResponse<ListUserData> response) {
                     Log.d(TAG, response.getMessage());
                     mView.showListUser(response.getData());
                 }

                 @Override
                 public void onFailure(int code, String message) {
                     Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                 }
             });
    }

    @Override
    public void deleteAllBlackList(User currentUser) {
        mActivity.showDialog();
        API.deleteAllBlackList(currentUser.getRole(), currentUser.getAuthenticationToken(),
           new API.APICallback<APIResponse<EmptyData>>() {
               @Override
               public void onResponse(
                       APIResponse<EmptyData> response) {
                   mView.showListUser(null);
                   mActivity.dismissDialog();
               }

               @Override
               public void onFailure(int code, String message) {
                   mActivity.dismissDialog();
                   Toast.makeText(mActivity, message,
                                  Toast.LENGTH_SHORT).show();
               }
           });
    }

    @Override
    public void addUserToBlackList(User currentUser, final User blockUser) {
        mActivity.showDialog();
        API.addUserToBlackList(currentUser.getRole(), currentUser.getAuthenticationToken(),
           blockUser.getId(),
           new API.APICallback<APIResponse<AddBlacklistData>>() {
               @Override
               public void onResponse(
                       APIResponse<AddBlacklistData> response) {
                   Toast.makeText(mActivity,
                                  response.getMessage(),
                                  Toast.LENGTH_SHORT).show();
                   mView.insertUser(Const.ZERO, response.getData().getUser());
                   mActivity.dismissDialog();
               }

               @Override
               public void onFailure(int code, String message) {
                   mActivity.dismissDialog();
                   Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
               }
           });
    }

    @Override
    public void startSearchUserActivity() {
        mActivity.startActivityForResult(new Intent(mActivity, SearchUserActivity.class),
                                         Const.RequestCode.REQUEST_SEARCH_BLACKLIST);
    }

    @Override
    public void sendRequestRemoveUser(final User user) {
        User currentUser = Config.getInstance().getUserInfo(mActivity);
        final Dialog loadingDialog = CommonUtils.showLoadingDialog(mActivity);
        API.deleteUserBlacklist(
            currentUser.getAuthenticationToken(),
            currentUser.getUserType(),
            user.getBlackListUserId(),
            new API.APICallback<APIResponse<EmptyData>>() {
                @Override
                public void onResponse(APIResponse<EmptyData> response) {
                    loadingDialog.dismiss();
                    mView.removeUser(user);
                }

                @Override
                public void onFailure(int code, String message) {
                    Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            }
        );
    }
}
