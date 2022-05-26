package com.mvl.mvl_assignment.helper

import android.graphics.Color

class Constants {

    companion object Key {
        const val APPNAME = "MandapPartner"
        const val NOTIFICATION_CHANNEL_DEFAULT_ID = "ND1"
        const val NOTIFICATION_CHANNEL_DEFAULT_TITLE = "$APPNAME Default"
        const val NOTIFICATION_CHANNEL_DEFAULT_DESCRIPTION = "Important notifications from mandap.com team"
        const val API_STATUS_SUCCESS = "success"

        //Constant
        var WEBVIEW_GO_BACK_ON_RETRY = "WEBVIEW_GO_BACK_ON_RETRY"

        //Error Messages
        const val ERROR_SOMETHING_WENT_WRONG = "Something went wrong, Please try again later"
        const val ERROR_TRY_AGAIN_CLOSE_THE_APP = "Something went wrong, Please close the app & try again"
        const val ERROR_TIMEOUT_EXCEPTION = "It seems your internet connection is too slow"
        const val ERROR_SOCKET_TIMEOUT_EXCEPTION = "Check your internet connection and try again"
        const val ERROR_UNKNOWN_HOST_EXCEPTION = "Check your internet connection and try again"
        const val ERROR_AUTO_LOGIN_API_FAILURE = "Something went wrong, Please login again"

        const val ERROR_ENTER_VALID_MOBILE_NUMBER = "Enter valid mobile number"
        const val ERROR_ENTER_VALID_OTP = "Enter valid OTP"

        //Sharedpreferences
        const val SP_LOGIN_SUCCESS = "sp_login_succ"
        const val SP_FIREBASE_USER_TOKEN_REFRESHED = "SP_FIREBASE_USER_TOKEN_REFRESHED"
        const val SP_FIREBASE_USER_TOKEN_LOGGED = "SP_FIREBASE_USER_TOKEN_LOGGED"
        const val SP_LATEST_APP_VERSION_FROM_UPDATE_NOTIFCATION = "SP_LATEST_APP_VERSION_FROM_UPDATE_NOTIFCATION"

        const val SP_LOGGED_USER_ID = "SP_LOGGED_USER_ID"
        const val SP_LOGGED_USER_PHONE = "SP_LOGGED_USER_PHONE"
        const val SP_LOGGED_DATE = "SP_LOGGED_DATE"
        const val SP_BASE_URL_TO_LOAD = "SP_BASE_URL_TO_LOAD"
        const val SP_LOGGED_USER_NAME = "SP_LOGGED_USER_NAME"
        const val SP_LOGGED_USER_KEY = "SP_LOGGED_USER_KEY"
        const val SP_DEVICE_ID = "SP_DEIVCE_ID"

        //Share this app & Send feedback
        var INTENT_TYPE_TEXT_PLAIN = "text/plain"
        const val SHARE_THIS_APP_MESSAGE = "Hey, check out this app \n"
        const val SHARE_THIS_APP_CREATOR_TITLE = "Share this app via"
        const val SEND_FEEDBACK_CREATOR_TITLE = "Send feedback"
        const val PLAY_STORE_PREFIX_LINK = "https://play.google.com/store/apps/details?id="

        //RateThisApp Dialog
        const val RATE_THIS_APP_TITLE = "Rate this app"
        const val RATE_THIS_APP_MESSAGE = "If you enjoy using this app, would you mind taking a moment to rate it?"

        //Firebase
        var TOPIC_MMV_INSTALLED_USER = "TOPIC_MMV_INSTALLED_USER"
        var TOPIC_MMV_INSTALLED_USER_TEST = "TOPIC_MMV_INSTALLED_USER_TEST"

        // App update alert dialog
        var APP_UPDATE_TITLE = "New app version available"
        var APP_UPDATE_MESSAGE = "An update version for MandapPartner app is available on Play Store"
        var APP_UPDATE_POSITIVE_BUTTON = "Update"

        var APP_UPDATE_NEGATIVE_BUTTON = "Exit"

        //Notification Type
        const val NOTIFICATION_TAG_OPEN_APP = 1
        const val NOTIFICATION_TAG_LOAD_URL = 2
        const val NOTIFICATION_TAG_UPDATE_APP = 3
        const val NOTIFICATION_TAG_GOTO_PLAYSTORE = 4

        //Notification Strings
        const val NOTIFICATION_TYPE = "notification_type"
        const val NOTIFICATION_IMAGE_URL = "image_url"
        const val NOTIFICATION_DETAIL_URL = "detail_url"
        const val NOTIFICATION_TITLE = "title"
        const val NOTIFICATION_BODY = "body"
        const val NOTIFICATION_NEW_APPVERSION = "new_version_code"
        const val NOTIFICATION_PLAYSTORE_PACKAGE_NAME = "playstore_package_name"

        //Intent Keys
        var INTENT_LOAD_NOTIFICATION_URL = "INTENT_LOAD_NOTIFICATION_URL"

        //Preferences
        var PREF_LOAD_NOTIFICATION_URL_IF_AVAILABLE = "PREF_LOAD_NOTIFICATION_URL_IF_AVAILABLE"
        var PREF_ENABLE_NOTIFICATIONS_FOR_MI_VIVO = "PREF_ENABLE_NOTIFICATIONS_FOR_MI_VIVO"

        //EnableNotifications
        const val ENABLE_NOTIFICATION_NOT = 0
        const val ENABLE_NOTIFICATION_LATER = 1
        const val ENABLE_NOTIFICATION_OK = 2



        const val REQUEST_CODE_LOCATION_PERMISSION = 0

        const val ACTION_START_OR_RESUME_SERVICE = "ACTION_START_OR_RESUME_SERVICE"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
        const val ACTION_PAUSE_SERVICE = "ACTION_PAUSE_SERVICE"

        const val ACTION_SHOW_TRACKING_ACTIVITY = "ACTION_SHOW_TRACKING_ACTIVITY"

        const val LOCATION_UPDATE_INTERVAL = 5000L
        const val FASTEST_LOCATION_INTERVAL = 2000L

        const val POLYLINE_COLOR = Color.RED
        const val POLYLINE_WIDTH = 8f

        const val NOTIFICATION_CHANNEL_ID = "tracking_channel"
        const val NOTIFICATION_CHANNEL_NAME = "Tracking"
        const val NOTIFICATION_ID = 1



        const val ERROR_ENTER_VALID_BUSINESS_NAME = "Enter valid business name"
        const val ERROR_ENTER_VALID_NAME = "Enter valid name"
        const val ERROR_ENTER_VALID_EMAIL = "Please enter a valid email address"
        const val ERROR_SELECT_LATLONG = "Please select your location in Map"
        const val ERROR_ENTER_VALID_ADDRESS = "Enter valid address"
        const val ERROR_ENTER_VALID_PINCODE = "Enter valid pincode"
        const val ERROR_SELECT_CITY = "Please select city"
        const val ERROR_SELECT_LOCALITY = "Please select locality"
        const val REQUEST_CODE_TO_START_MAP_ADDRESS_ACTIVITY = 267
        const val REQUEST_CODE_AUTOCOMPLETE_MAP_ACTIVITY = 280
        const val REQUEST_PERMISSIONS_LOCATION_SETTINGS_REQUEST_CODE = 288
        const val REQUEST_CODE_TO_START_LEAD_DETAIL_PAGE = 301
        const val REQUEST_CODE_TO_START_VIEW_GALLERY = 101
        const val REQUEST_CODE_TO_START_KYC_ACTIVITY = 268
        const val REQUEST_CODE_TO_ADD_PROFILE = 269
        const val REQUEST_CODE_TO_EDIT_PROFILE = 270
        const val REQUEST_CODE_TO_START_CHANGE_COVER_ACTIVITY = 271
        const val ADDRESS_ID: String="address_id"
        const val NICK_NAME: String="nick_name"
        const val PREF_LOCATION: String="pref_loc"
        const val COUNTRY = "country"
        const val CITY = "city"
        const val STATE = "state"
        const val PINCODE = "pincode"
        const val ADDRESS = "address"
    }

}