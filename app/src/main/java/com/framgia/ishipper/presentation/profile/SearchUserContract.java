package com.framgia.ishipper.presentation.profile;

import com.framgia.ishipper.model.User;

import java.util.List;

/**
 * Created by HungNT on 11/22/16.
 */

public class SearchUserContract {
    interface View {
        void updateListUser(List<User> listUsers);
    }

    interface Presenter {
        void searchUser(String name);
        void gotoAddUser(User user);
    }
}
