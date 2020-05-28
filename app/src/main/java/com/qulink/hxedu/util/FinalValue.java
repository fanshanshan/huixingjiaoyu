package com.qulink.hxedu.util;

import android.os.Environment;

import java.io.File;

public class FinalValue {
    public static int barHeight = 40;
    public static String SEARCH_HISTORY_KEY = "searchHistory";
    public static String API_ERROR = "5233";
    public static String TOKEN_ERROR = "102";
    public static String LOGIN_SUCCESS = "LOGIN_SUCCESS";
    public static String LOGOUT = "LOGOUT";
    public static String GET_USERINFO = "GET_USERINFO";

    public static String PAY_ALI="1";
    public static String PAY_WX="2";
    public static String PAY_BANK="3";

    public static String IMG_SAVE_PATH = Environment.getExternalStorageDirectory()
            + File.separator + Environment.DIRECTORY_DCIM
            +File.separator+"Camera"+File.separator; ;
}
