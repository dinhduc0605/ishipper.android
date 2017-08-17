package com.framgia.ishipper.presentation.profile;

import android.content.Intent;

import com.framgia.ishipper.base.BaseActivity;
import com.framgia.ishipper.common.Config;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.API;
import com.framgia.ishipper.net.APIResponse;
import com.framgia.ishipper.net.data.ListUserData;
import com.framgia.ishipper.util.Const;

import static android.app.Activity.RESULT_OK;

/**
 * Created by HungNT on 11/22/16.
 */

class SearchUserPresenter implements SearchUserContract.Presenter {
    private final SearchUserContract.View mView;
    private BaseActivity mActivity;

    SearchUserPresenter(SearchUserContract.View view, BaseActivity loginActivity) {
        mView = view;
        mActivity = loginActivity;
    }

    @Override
    public void searchUser(String name) {
        API.getSearchUser(
                Config.getInstance().getUserInfo(mActivity).getAuthenticationToken(),
                name, new API.APICallback<APIResponse<ListUserData>>() {
                    @Override
                    public void onResponse(APIResponse<ListUserData> response) {
                        mView.updateListUser(response.getData().getShippersList());
                    }

                    @Override
                    public void onFailure(int code, String message) {
                        mActivity.showUserMessage(message);
                    }
                });
    }

    @Override
    public void gotoAddUser(User user) {
        Intent intent = mActivity.getIntent();
        intent.putExtra(Const.KEY_USER, user);
        mActivity.setResult(RESULT_OK, intent);
        mActivity.finish();
    }
}
