package com.framgia.ishipper.presentation.manager_shipper_register;
import android.content.Context;
import android.content.Intent;
import com.framgia.ishipper.model.User;
import java.util.List;

/**
 * Created by vuduychuong1994 on 11/24/16.
 */

public interface ChooseShipperRegisterContract {

    interface View {

        void addListShipper(List<User> userList);

        void addUser(User user);

        void remove(User user);
    }

    interface Presenter {

        void getListShipper(int invoiceId);

        void updateNotificationStatus(User currentUser, String notiId);

        void startMainActivity();

        void actionAcceptShipper(User shipper, int invoiceId);

        void updateShipper(int invoiceId, Intent intent);

    }

}
