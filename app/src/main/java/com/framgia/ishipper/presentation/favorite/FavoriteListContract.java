package com.framgia.ishipper.presentation.favorite;
import com.framgia.ishipper.model.User;
import com.framgia.ishipper.net.data.ListUserData;
import java.util.List;

/**
 * Created by vuduychuong1994 on 11/21/16.
 */

public interface FavoriteListContract {

    interface View {

        void showConfirmDeleteAll();

        void showListUser(ListUserData listUserData);

        void insertUser(int index, User favoriteUser);

        void showEmptyLayout(boolean active);

        void removeUser(User user);

        void confirmRemoveUser(User user);
    }

    interface Presenter {

        void getFavoriteList(User currentUser);

        void deleteAllFavoriteList(User currentUser);

        void addFavoriteUser(User currentUser, User favoriteUser);

        void startSearchUserActivity();

        void sendRequestRemoveUser(User user);
    }
}
