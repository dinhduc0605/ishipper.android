package com.framgia.ishipper.net;

import com.framgia.ishipper.common.Config;

/**
 * Created by HungNT on 8/5/16.
 */
public class APIDefinition {

    private static final String DEV_URL = "http://www.ishipper.tk";
    private static final String SOCKET_URL = "http://www.ishipper.tk";
//    private static final String DEV_URL = "http://192.168.1.142:3000/";
//    private static final String SOCKET_URL = "http://192.168.1.142:3000/";
    private static final String PROD_URL = "";

    public static final String BASE_GOOGLE_MAP_API = "https://maps.googleapis.com";

    public static final String HEADER_AUTHORIZE = "Authorization";

    public static final String PARAM_LANGUAGE = "language";
    public static final String PARAM_USER_TYPE = "user_type";

    public static class RegisterUser {
        public static final String PATH = "/api/sign_up/";
        public static final String PARAM_USER_PHONE_NUMBER = "user[phone_number]";
        public static final String PARAM_USER_PASSWORD = "user[password]";
        public static final String PARAM_USER_PASSWORD_CONFIRMATION = "user[password_confirmation]";
        public static final String PARAM_USER_PLATE_NUMBER = "user[plate_number]";
        public static final String PARAM_USER_ROLE = "user[role]";
        public static final String PARAM_USER_NAME = "user[name]";
    }

    /**
     * Confirm Pin when register
     */
    public static class ConfirmationPin {
        public static final String PATH = "/api/confirmation";
    }

    public static class PutUpdateProfile {
        public static final String TAG = "Update Profile";
        public static final String PATH = "/api/users/{id}";

        public static final String PATH_ID = "id";

        public static final String PARAM_PHONE_NUMBER = "user[phone_number]";
        public static final String PARAM_PASSWORD = "user[password]";
        public static final String PARAM_PASSWORD_CONFIRMATION = "user[password_confirmation]";
        public static final String PARAM_NAME = "user[name]";
        public static final String USER_CURRENT_PASSWORD = "user[current_password]";
        public static final String PARAM_PLATE_NUMBER = "user[plate_number]";
        public static final String PARAM_ADDRESS = "user[address]";
        public static final String PARAM_NOTIFICATION = "user[receive_notification]";
        public static final String PARAM_LATITUDE = "user[latitude]";
        public static final String PARAM_LONGITUDE = "user[longitude]";
    }

    public static class ForgotPassword {
        public static final String TAG = "Forgot Password";
        public static final String PATH = "";
    }

    public static class ChangePassword {
        private static final String TAG = "ChangePassword";
        public static final String PATH = "/api/password";
        public static final String PARAM_PHONE_NUMBER = "user[phone_number]";
        public static final String PARAM_CURRENT_PASSWORD = "user[current_password]";
        public static final String PARAM_PASSWORD = "user[password]";
        public static final String PARAM_PASSWORD_CONFIRMATION = "user[password_confirmation]";
    }

    public static class SignIn {
        public static final String PATH = "api/sign_in";
        public static final String PARAM_PHONE_NUMBER = "user[phone_number]";
        public static final String PARAM_PASSWORD = "user[password]";
        public static final String PARAM_REGISTRATION_ID = "user[registration_id]";
    }

    /**
     * Get Pin when forgot password
     */
    public static class GetPin {
        public static final String TAG = "Get Pin";
        public static final String PATH = "/api/confirmation";

        public static final String PARAM_PHONE = "user[phone_number]";
    }

    /**
     * Check Pin when reset password
     */
    public static class GetCheckPin {
        public static final String TAG = "Get Pin";
        public static final String PATH = "/api/password";

        public static final String PARAM_PHONE = "user[phone_number]";
        public static final String PARAM_PIN = "user[pin]";
    }

    public static class GetUserInformation {
        public static final String TAG = "Get User Detail";
        public static final String PATH = "/api/users";
    }

    public static class PutResetPassword {
        public static final String TAG = "Put Reset Password";
        public static final String PATH = "/api/password";

        public static final String PARAM_PHONE = "user[phone_number]";
        public static final String PARAM_PASSWORD = "user[password]";
        public static final String PARAM_PASSWORD_CONFIRM = "user[password_confirmation]";
        public static final String PARAM_PIN = "user[pin]";
    }

    public static class GetShipperNearby {

        public static final String PATH = "/api/users";
        public static final String PARAM_USER_LAT = "user[latitude]";
        public static final String PARAM_USER_LNG = "user[longitude]";
        public static final String PARAM_USER_DISTANCE = "user[distance]";
    }


    public static class GetInvoiceNearby {
        public static final String PATH = "/api/invoices";
        public static final String PARAM_USER_LAT = "user[latitude]";
        public static final String PARAM_USER_LNG = "user[longitude]";
        public static final String PARAM_USER_DISTANCE = "user[distance]";
    }

    public static class SignOut {
        public static final String PATH = "/api/sign_out";
        public static final String PARAM_PHONE_NUMBER = "user[phone_number]";
        public static final String AUTHENTICATE_TOKEN = "user[authentication_token]";
    }

    public static class PutUpdateInvoice {
        public static final String PATH = "/api/invoices/{id}";

        public static final String PATH_ID = "id";

        public static final String PARAM_NAME = "invoice[name]";
        public static final String PARAM_ADDRESS_START = "invoice[address_start]";
        public static final String PARAM_LAT_START = "invoice[latitude_start]";
        public static final String PARAM_LNG_START = "invoice[longitude_start]";
        public static final String PARAM_ADDRESS_FINISH = "invoice[address_finish]";
        public static final String PARAM_LAT_FINISH = "invoice[latitude_finish]";
        public static final String PARAM_LNG_FINISH = "invoice[longitude_finish]";
        public static final String PARAM_DELIVERY_TIME = "invoice[delivery_time]";
        public static final String PARAM_DISTANCE = "invoice[distance]";
        public static final String PARAM_DESCRIPTION = "invoice[description]";
        public static final String PARAM_PRICE = "invoice[price]";
        public static final String PARAM_SHIPPING_PRICE = "invoice[shipping_price]";
        public static final String PARAM_STATUS = "invoice[status]";
        public static final String PARAM_WEIGHT = "invoice[weight]";
        public static final String PARAM_CUSTOMER_NAME = "invoice[customer_name]";
        public static final String PARAM_CUSTOMER_NUMBER = "invoice[customer_number]";
    }

    public static String getBaseIShipperUrl() {
        return Config.IS_DEV ? DEV_URL : PROD_URL;
    }

    public static class CreateInvoice {
        public static final String PATH = "/api/shop/invoices";
        public static final String PARAM_NAME = "invoice[name]";
        public static final String PARAM_ADDRESS_START = "invoice[address_start]";
        public static final String PARAM_LATITUDE_START = "invoice[latitude_start]";
        public static final String PARAM_LONGITUDE_START = "invoice[longitude_start]";
        public static final String PARAM_ADDRESS_FINISH = "invoice[address_finish]";
        public static final String PARAM_LATITUDE_FINISH = "invoice[latitude_finish]";
        public static final String PARAM_LONGITUDE_FINISH = "invoice[longitude_finish]";
        public static final String PARAM_DELIVERY_TIME = "invoice[delivery_time]";
        public static final String PARAM_DISTANCE = "invoice[distance_invoice]";
        public static final String PARAM_DESCRIPTION = "invoice[description]";
        public static final String PARAM_PRICE = "invoice[price]";
        public static final String PARAM_SHIPPING_PRICE = "invoice[shipping_price]";
        public static final String PARAM_STATUS = "invoice[status]";
        public static final String PARAM_WEIGHT = "invoice[weight]";
        public static final String PARAM_CUSTOMER_NAME = "invoice[customer_name]";
        public static final String PARAM_CUSTOMER_NUMBER = "invoice[customer_number]";
    }

    public static class FilterInvoice {
        public static final String PATH = "/api/invoices";
        public static final String PARAM_MIN_ORDER_PRICE = "invoice[price][min]";
        public static final String PARAM_MAX_ORDER_PRICE = "invoice[price][max]";
        public static final String PARAM_MIN_SHIP_PRICE = "invoice[shipping_price][min]";
        public static final String PARAM_MAX_SHIP_PRICE = "invoice[shipping_price][max]";
        public static final String PARAM_MIN_DISTANCE = "invoice[distance_invoice][min]";
        public static final String PARAM_MAX_DISTANCE = "invoice[distance_invoice][max]";
        public static final String PARAM_MIN_WEIGHT = "invoice[weight][min]";
        public static final String PARAM_MAX_WEIGHT = "invoice[weight][max]";
        public static final String PARAM_CURRENT_LAT = "invoice[latitude]";
        public static final String PARAM_CURRENT_LONG = "invoice[longitude]";
        public static final String PARAM_RADIUS = "invoice[radius]";

    }

    public static class GetListInvoice {
        public static final String PATH = "/api/{user_type}/invoices";
        public static final String PARAM_USER_TYPE = "user_type";
        public static final String PARAM_STATUS = "status";
        public static final String PARAM_QUERY = "query";
    }

    public static class PutUpdateInvoiceStatus {
        public static final String PARAM_USER_TYPE = "user_type";
        public static final String PARAM_ID = "id";

        public static final String PATH = "/api/{" + PARAM_USER_TYPE + "}/invoices/{" + PARAM_ID + "}";

        public static final String PARAM_STATUS = "status";
    }

    public static class GetUser {
        public static final String PATH = "/api/users/{id}";
        public static final String PARAM_USER_ID = "id";
    }

    public static class GetListShipperReceived {
        public static final String PATH = "/api/shop/list_shippers";
        public static final String PARAM_INVOICE_ID = "invoice[id]";
    }

    public static class PutShopReceiveShipper {
        public static final String PARAM_USER_INVOICE_ID = "user_invoice_id";
        public static final String PATH = "/api/shop/user_invoices/{" + PARAM_USER_INVOICE_ID + "}";

        public static final String PARAMS_STATUS = "status";
    }

    public static class PostShipperReceiveInvoice {
        public static final String PATH = "/api/shipper/user_invoices";

        public static final String PARAMS_INVOICE_ID = "user_invoice[invoice_id]";
    }

    public static class ShowInvoice {
        public static final String PARAM_USER_TYPE = "user_type";
        public static final String PARAM_ID = "id";
        public static final String PATH = "/api/{" + PARAM_USER_TYPE + "}/invoices/{" + PARAM_ID + "}";
    }

    public static class PostRating {
        public static final String USER_TYPE = "user_type";
        public static final String PATH = "/api/{" + USER_TYPE + "}/rates";
        public static final String PARAM_INVOICE_ID = "rate[invoice_id]";
        public static final String PARAM_REVIEW_TYPE = "rate[review_type]";
        public static final String PARAM_RATING_POINT = "rate[rating_point]";
        public static final String PARAM_CONTENT = "rate[content]";

        public static final String REVIEW_TYPE = "rate";

    }

    public static class ReportUser {
        public static final String PARAM_USER_TYPE = "user_type";
        public static final String PATH = "/api/{" + PARAM_USER_TYPE + "}/reports";
        public static final String PARAM_INVOICE_ID = "report[invoice_id]";
        public static final String PARAM_REVIEW_TYPE = "report[review_type]";
        public static final String PARAM_CONTENT = "report[content]";
    }

    public static class GetSearchInvoice {
        public static final String PATH = "/api/shop/invoices";
        public static final String PARAM_STATUS = "status";
        public static final String PARAM_QUERY = "query";
    }

    public class GetBlackList {
        public static final String PARAM_USER_TYPE = "user_type";
        public static final String PATH = "/api/{" + PARAM_USER_TYPE + "}/black_lists";
    }

    public class GetFavoriteList {
        public static final String PARAM_USER_TYPE = "user_type";
        public static final String PATH = "/api/{" + PARAM_USER_TYPE + "}/favorite_lists";
    }

    public class PostAddFavoriteList {
        public static final String PARAM_USER_TYPE = "user_type";
        public static final String PATH = "/api/{" + PARAM_USER_TYPE + "}/favorite_lists";

        public static final String PARAMS_FAVORITE_USER_ID = "favorite_list[favorite_list_user_id]";
    }

    public class PostAddBlacklist {
        public static final String PARAM_USER_TYPE = "user_type";
        public static final String PATH = "/api/{" + PARAM_USER_TYPE + "}/black_lists";

        public static final String PARAMS_BLACKLIST_USER_ID = "black_list[black_list_user_id]";
    }

    public class DeleteAllBlackList {
        public static final String PARAM_USER_TYPE = "user_type";
        public static final String PATH = "/api/{" + PARAM_USER_TYPE + "}/destroy_all_black_lists";
    }

    public class DeleteAllFavoriteList {
        public static final String PARAM_USER_TYPE = "user_type";
        public static final String PATH = "/api/{" + PARAM_USER_TYPE + "}/destroy_all_favorite_lists";
    }

    public static class GetListReview {
        public static final String PATH = "/api/{" + PARAM_USER_TYPE +"}/reviews";
        public static final String PARAMS_USER_ID = "review[recipient_id]";
    }

    public static class GetSearchUser {
        public static final String PATH = "/api/users";
        public static final String PARAMS_SEARCH = "user[search]";
    }

    public class DeleteUserFavoriteList {
        public static final String PARAM_USER_TYPE = "user_type";
        public static final String PARAM_ID = "id";

        public static final String PATH = "/api/{" + PARAM_USER_TYPE + "}/favorite_lists/{" + PARAM_ID + "}";
    }

    public class DeleteUserBlackList {
        public static final String PARAM_USER_TYPE = "user_type";
        public static final String PARAM_ID = "id";

        public static final String PATH = "/api/{" + PARAM_USER_TYPE + "}/black_lists/{" + PARAM_ID + "}";
    }

    public static class PutFCMId {
        public static final String TAG = "Put Update Notification Registration Id";
        public static final String PATH = "/api/user_token";

        public static final String PARAM_REGISTRATION_ID = "user_token[registration_id]";
    }

    public class SwitchNotification {
        public static final String PATH = "";
        public static final String PARAM_SWITCH = "";
    }

    public class UpdateReadNotification {
        public static final String PARAM_USER_TYPE = "user_type";
        public static final String PARAM_NOTIFICATION_READ = "notification[read]";
        public static final String PARAM_NOTIFICATION_ID = "noti_id";
        public static final String PATH = "/api/{" + PARAM_USER_TYPE +
                "}/notifications/{" + PARAM_NOTIFICATION_ID + "}";
    }

    public class GetAllNotification {
        public static final String PARAM_USER_TYPE = "user_type";
        public static final String PARAM_NOTIFICATION_PAGE = "notification[page]";
        public static final String PARAM_PER_PAGE = "notification[per_page]";
        public static final String PATH = "/api/{" + PARAM_USER_TYPE + "}/notifications";
    }

    public class WebSockets {
        public static final String WEBSOCKETS_URL = SOCKET_URL + "cable";
        public static final String PARAM_PHONE_NUMBER = "PHONE_NUMBER";
        public static final String PARAM_AUTHORIZATION = "AUTHORIZATION";
        public static final int INTERVAL_DEFAULT = 15 * 1000;
        public static final int TIMEOUT_DEFAULT = 60 * 1000;
    }

    public class GetListRoutes {
        public static final String PATH = "/maps/api/directions/json";
        public static final String PARAM_ORIGIN = "origin";
        public static final String PARAM_DESTINATION = "destination";
        public static final String PARAM_KEY = "key";
    }

    public class PutShipperCancelReceiveInvoice {
        public static final String PARAM_USER_INVOICE_ID = "id";
        public static final String PATH = "/api/shipper/user_invoices/{" + PARAM_USER_INVOICE_ID + "}";
    }

    public class UserSetting {
        public static final String PATH = "/api/user_setting";
        public static final String PARAM_USER_ID = "user_setting[user_id]";
        public static final String PARAM_RECEIVE_NOTIFICATION = "user_setting[receive_notification]";
        public static final String PARAM_FAVORITE_LOCATION = "user_setting[favorite_location]";
        public static final String PARAM_ADDRESS = "user_setting[favorite_address]";
        public static final String PARAM_FAVORITE_LATITUDE = "user_setting[favorite_latitude]";
        public static final String PARAM_FAVORITE_LONGITUDE = "user_setting[favorite_longitude]";
        public static final String PARAM_LATITUDE = "user_setting[latitude]";
        public static final String PARAM_LONGITUDE = "user_setting[longitude]";
        public static final String PARAM_RADIUS = "user_setting[radius_display]";
    }

}
