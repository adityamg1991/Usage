package com.coffeemug.usage.Utilities;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by aditya on 01/07/15.
 */
public class Constants {

    public static final String APP_THEME_HASH_CODE = "#4682B4";
    public static final String TAG_FUCK = "FUCK";

    public static final String PCK_NAME = "package_name_extra";
    public static final String APP_LABEL = "app_label_extra";

    public static final SimpleDateFormat APP_DATE_FORMAT = new SimpleDateFormat("MM/dd/yyyy");

    public static final ArrayList<String> PHONE_LAUNCHER_PACKAGE_LIST = new ArrayList<String>();

    public static final String INTENT_KEY = "intent_key";
    public static final String NOTIFICATION_FUTILE_APPS = "notification_futile_apps";

    static {

        PHONE_LAUNCHER_PACKAGE_LIST.add("com.android.systemui");
    }


    public static final String KEY_SHOW_FUTILE_APPS = "key_show_futile_apps";
    public static final String VALUE_SHOW_FUTILE_APPS = "value_show_futile_apps";
}
