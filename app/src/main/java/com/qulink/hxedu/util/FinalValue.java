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
            +File.separator+"Camera"+File.separator;


    public static int completePlanAddScore =1;
    public static int limit =8;
    public static int maxStudyLimit = 6;
    public static int startpageNo =1;


    public static int hotCourseCurriculumType = 1;//热门课程
    public static int moneyCourseCurriculumType = 1;//付费课程
    public static int WECHAT_PAYEOK = 999;//微信支付成功返回码
    public static int WECHAT_LOGINEOK = 888;//微信登录成功返回码
    public static int WECHAT_SHAREOK = 777;//微信分享成功返回码
    public static String WECHAT_APP_ID = "wx9c863272e5ed7596";//微信appid
}
