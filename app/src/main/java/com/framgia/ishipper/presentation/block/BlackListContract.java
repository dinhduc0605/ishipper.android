package com.framgia.ishipper.presentation.block;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.data.ListUserData;

/**
 * Created by vuduychuong1994 on 11/21/16.
 */

public interface BlackListContract {

    interface View {

        void initViews();

        void showConfirmDialog();

        void showListUser(ListUserData listUserData);

        void insertUser(int index, User blockUser);

        void showEmptyLayout(boolean active);

        void confirmRemoveUser(User user);

        void removeUser(User user);
    }

    interface Presenter {

        void getBlackList(User currentUser);

        void deleteAllBlackList(User currentUser);

        void addUserToBlackList(User currentUser, User blockUser);

        void startSearchUserActivity();

        void sendRequestRemoveUser(User user);
    }
}
