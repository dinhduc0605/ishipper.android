package com.framgia.ishipper.presentation.favorite;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;
import com.framgia.ishipper.R;
import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.base.BaseFragment;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.common.Log;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.AddFavoriteListData;
import com.framgia.ishipper.net.data.EmptyData;
import com.framgia.ishipper.net.data.ListUserData;
import com.framgia.ishipper.presentation.profile.SearchUserActivity;
import com.framgia.ishipper.util.CommonUtils;
import com.framgia.ishipper.util.Const;
import com.framgia.ishipper.widget.dialog.ConfirmDialog;

/**
 * Created by vuduychuong1994 on 11/21/16.
 */

public class FavoriteListPresenter implements FavoriteListContract.Presenter {
    private static final String TAG = FavoriteListPresenter.class.getName();

    private Context mContext;
    private BaseFragment mFragment;
    private FavoriteListContract.View mView;

    public FavoriteListPresenter(Context context, BaseFragment fragment,
                                 FavoriteListContract.View view) {
        mContext = context;
        mFragment = fragment;
        mView = view;
    }

    @Override
    public void getFavoriteList(User currentUser) {
        ((BaseActivity) mContext).showDialog();
        API.getFavoriteList(currentUser.getAuthenticationToken(), currentUser.getRole(),
                            new API.APICallback<APIResponse<ListUserData>>() {
                                @Override
                                public void onResponse(APIResponse<ListUserData> response) {
                                    Log.d(TAG, response.getMessage());
                                    mView.showListUser(response.getData());
                                    ((BaseActivity) mContext).dismissDialog();
                                }

                                @Override
                                public void onFailure(int code, String message) {
                                    ((BaseActivity) mContext).dismissDialog();
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            });
    }

    @Override
    public void deleteAllFavoriteList(User currentUser) {
        ((BaseActivity) mContext).showDialog();
        API.deleteAllFavoriteList(currentUser.getRole(), currentUser.getAuthenticationToken(),
                                  new API.APICallback<APIResponse<EmptyData>>() {
                                      @Override
                                      public void onResponse(APIResponse<EmptyData> response) {
                                          mView.showListUser(null);
                                          ((BaseActivity) mContext).dismissDialog();
                                          Toast.makeText(mContext, R.string.toast_delete_all,
                                                         Toast.LENGTH_SHORT).show();
                                      }

                                      @Override
                                      public void onFailure(int code, String message) {
                                          ((BaseActivity) mContext).dismissDialog();
                                          Toast.makeText(mContext, message,
                                                         Toast.LENGTH_SHORT).show();
                                      }
                                  });
    }

    @Override
    public void addFavoriteUser(User currentUser, final User favoriteUser) {
        ((BaseActivity) mContext).showDialog();
        API.addFavoriteUser(currentUser.getUserType(), currentUser.getAuthenticationToken(),
                            favoriteUser.getId(),
                            new API.APICallback<APIResponse<AddFavoriteListData>>() {
                                @Override
                                public void onResponse(APIResponse<AddFavoriteListData> response) {
                                    mView.insertUser(Const.ZERO, response.getData().getUser());
                                    ((BaseActivity) mContext).dismissDialog();
                                }

                                @Override
                                public void onFailure(int code, String message) {
                                    ((BaseActivity) mContext).dismissDialog();
                                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                                }
                            });
    }

    @Override
    public void startSearchUserActivity() {
        mFragment.startActivityForResult(new Intent(mContext, SearchUserActivity.class),
                                         Const.RequestCode.REQUEST_SEARCH_FAVORITE);
    }

    @Override
    public void sendRequestRemoveUser(final User user) {
        User currentUser = Config.getInstance().getUserInfo(mContext);
        final Dialog loadingDialog = CommonUtils.showLoadingDialog(mContext);
        API.deleteUserFavorite(
            currentUser.getAuthenticationToken(),
            currentUser.getUserType(),
            user.getFavoriteListId(),
            new API.APICallback<APIResponse<EmptyData>>() {
                @Override
                public void onResponse(APIResponse<EmptyData> response) {
                    loadingDialog.dismiss();
                    mView.removeUser(user);
                }

                @Override
                public void onFailure(int code, String message) {
                    Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
                    loadingDialog.dismiss();
                }
            }
        );
    }
}
