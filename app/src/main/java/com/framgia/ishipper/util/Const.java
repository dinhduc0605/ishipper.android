package com.framgia.ishipper.util;

/**
 * Created by vuduychuong1994 on 8/3/16.
 */
public class Const {
    public static final String KEY_USER = "user";
    public static final int REQUEST_SETTING = 3;
    public static final long TIME_DELAY_EXIT = 2000;
    public static final int MIN_PASSWORD_LENGTH = 6;
    public static final int PIN_LENGTH = 8;
    public static final int SETTING_MAX_INVOICE_RADIUS = 14;
    public static final int SETTING_MIN_INVOICE_RADIUS = 1;
    public static final int SHIPPER_NEARBY_RADIUS = 5;
    public static final int SETTING_INVOICE_RADIUS_DEFAULT = 5;
    public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 0x2233;
    public static final int INVALID = -1;
    public static String VIETNAM_PREFIX = "+84";
    public static int SCREEN_WIDTH;
    public static int SCREEN_HEIGHT;

    public static final int ZERO = 0;
    public static final String WELCOME = "welcome";
    public static final String CHANNEL = "channel";
    public static final String CHANNEL_REALTIME = "RealtimeChannel";
    public static final String AUTHENTICATION_TOKEN = "authentication_token";
    public static final String COMMAND = "command";
    public static final String COMMAND_SUBSCRIBE = "subscribe";
    public static final String IDENTIFIER = "identifier";
    public static final int SIZE_INVOICE_STATUS = 6;
    public static final int DEFAULT_HIGHLIGHT_POSITION = - 1;
    public static final String KEY_TITLE = "title";
    public static final String KEY_BODY = "body";
    public static final int HEAD_LIST = 0;
    public static final String KEY_INVOICE_ID = "KEY_INVOICE_ID";
    public static final String KEY_INVOICE = "invoice";
    public static final int POSITION_HIGHLIGHT_DEFAULT = -1;
    public static final int INVOICE_ID_DEFAULT = - 1;
    public static final long AUTO_COMPLETE_PLACE_RADIUS = 2000;
    public static final String AUTO_COMPLETE_PLACE_LANGUAGE_CODE = "vi";
    public static final int TIME_HIGHLIGHT_INVOICE = 500;
    public static final int TIME_REVERSE_HIGHLIGHT_INVOICE = 2000;
    public static final String KEY_ACTION = "click_action";
    public static final String KEY_SOCKET_RESPONSE = "socket_response";
    public static final String KEY_NOTIFICATION_ID = "notification_id";
    public static final String ACTION_NEW_NOTIFICATION = "new_notification";
    public static final String ACTION_UNREAD_NOTIFICATION = "unread_notification";
    public static final String ACTION_NEW_INVOICE = "new_invoice";
    public static final String ACTION_REMOVE_INVOICE = "remove_invoice";
    public static final String ACTION_SHIPPER_ONLINE = "shipper_online";
    public static final String ACTION_SHIPPER_OFFLINE = "shipper_offline";
    public static final String ACTION_CANCEL_INVOICE = "cancel_invoice";
    public static final String ACTION_NONE = "oops";
    public static final String ACTION_CREATE_INVOICE = "create_invoice";

    public class RequestCode {

        public static final int REQUEST_SEARCH_BLACKLIST = 1;
        public static final int REQUEST_SEARCH_FAVORITE = 2;
        public static final int REQUEST_CODE_CHOOSE_SHIPPER = 3;
        public static final int LOCATION_PERMISSION_REQUEST_CODE = 4;
        public static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 5;
        public static final int REQUEST_CHECK_SETTINGS = 6;
        public static final int REQUEST_CODE_INVOICE_DETAIL = 7;
    }

    public class ErrorMessage {
        public static final String ERROR_MSG_LOCAL =
                "Có lỗi xảy ra trong quá trình kết nối, hãy thử lại!";
    }

    public class Firebase {
        public static final String TOPIC_NEWS = "news";
    }

    public class Storage {
        public static final String KEY_USER_INFO = "UserStorageKey";
        public static final String KEY_IS_LOGIN = "IsLoginKey";
        public static final String KEY_SETTING_NOTIFICATION = "SettingNotification";
        public static final String KEY_SETTING_INVOICE_RADIUS = "SettingInvoiceRadius";
        public static final String KEY_SETTING_ADDRESS = "SettingAddress";
        public static final String KEY_SETTING_LATITUDE = "SettingLatitude";
        public static final String KEY_SETTING_LONGITUDE = "SettingLongitude";
        public static final String KEY_SETTING_LOCATION = "SettingLocation";
    }

    public class LocationRequest {
        public static final int LOCATION_INTERVAL_UPDATE = 10000;
        public static final int LOCATION_FASTEST_INTERVAL_UPDATE = 5000;
    }

    public class MapPadding {
        public static final int TOP_PADDING = 150;
        public static final int RIGHT_PADDING = 0;
        public static final int BOTTOM_PADDING = 0;
        public static final int LEFT_PADDING = 0;
    }

    public class Notification {
        public static final int ON = 1;
        public static final int OFF = 0;
        public static final int ID = 1;
    }

    public class FirebaseData {
        public static final String CLICK_ACTION = "click_action";
        public static final String INVOICE_ID = "invoice_id";
        public static final String NOTIFICATION_ID = "notification_id";
        public static final String KEY_CREATED_TIME = "created_time";
    }

    public class Setting {
        public static final int PER_PAGE = 30;
    }

    public class Broadcast {
        public static final String NEW_NOTIFICATION_ACTION = "com.framgia.ishipper.NEW_NOTIFICATION_ACTION";
    }

    public class ElevationLevel {
        public static final int DEFAULT = 8;
        public static final int NONE = 0;
    }

    public class KeyIntent {
        public static final String KEY_INVOICE = "invoice";
    }

    public class Language {
        public static final String VIETNAMESE = "vi";
    }

    public class IntentFilter {
        public static final String INVOICE_DETAIL = "com.framgia.ishipper.INVOICE_DETAIL";
        public static final String LIST_SHIPPER_REGISTER = "com.framgia.ishipper.LIST_SHIPPER_REGISTER";
    }
}
