package com.qulink.hxedu.api;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.App;
import com.qulink.hxedu.MyActivityManager;
import com.qulink.hxedu.entity.HotCourseBean;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.ui.LoginActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.RouteUtil;

import org.greenrobot.eventbus.EventBus;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.lang.reflect.Type;
import java.util.Map;


public class NetUtil {
private     static String token = "";


    private static NetUtil instance;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static NetUtil getInstance() {
        if (instance == null) {
            instance = new NetUtil();
        }
        return instance;
    }


    public void post(String method, Map<String, String> params, ApiCallback apiCallback) {
        RequestParams requestParams = new RequestParams(method);
        // params.setSslSocketFactory(...); // 如果需要自定义SSL
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet())
                requestParams.addQueryStringParameter(entry.getKey(), entry.getValue());
        }
       // requestParams.addHeader("token", token);
        Log.e("请求ulr", requestParams.toString());
        Log.e("header", requestParams.getHeaders().toString());
        Log.e("body", requestParams.getBodyParams().toString());
        x.http().post(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", result);
                ResponseData<HotCourseBean> responseData = GsonUtil.GsonToBean(result, ResponseData.class);
                if (responseData.getCode().equals("200")) {
                    apiCallback.success(responseData);
                } else {
                    if (responseData.getCode().equals(FinalValue.TOKEN_ERROR)) {
                        showTokenErrorDialog();
                        // EventBus.getDefault().post(new MessageEvent(FinalValue.TOKEN_ERROR,0));
                    }
                    apiCallback.error(responseData.getCode(), responseData.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                apiCallback.expcetion(ex.getMessage());
                if(ex.getMessage()!=null){
                    Log.e("请求异常", ex.getMessage());
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    public void get(String method, Map<String, String> params, ApiCallback apiCallback) {
        RequestParams requestParams = new RequestParams(method);
        // params.setSslSocketFactory(...); // 如果需要自定义SSL
       //requestParams.addHeader("token", token);

        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                System.out.println(entry.getKey() + "：" + entry.getValue());
                requestParams.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        Log.e("请求ulr", requestParams.toString());
        Log.e("header", requestParams.getHeaders().toString());
        Log.e("body", requestParams.getBodyParams().toString());
        x.http().get(requestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                Log.e("请求成功", result);
                ResponseData responseData = GsonUtil.GsonToBean(result, ResponseData.class);
                if (responseData.getCode().equals("200")) {
                    apiCallback.success(responseData);
                } else {
                    if (responseData.getCode().equals(FinalValue.TOKEN_ERROR)) {
                        showTokenErrorDialog();
                        //EventBus.getDefault().post(new MessageEvent(FinalValue.TOKEN_ERROR,0));
                    }
                    apiCallback.error(responseData.getCode(), responseData.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                apiCallback.expcetion(ex.getMessage());
                if(ex.getMessage()!=null){
                    Log.e("请求异常", ex.getMessage());
                }

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }


   private boolean isShow = false;

   private void showTokenErrorDialog() {
        if (isShow) {
            return;
        }
        if (App.getInstance().getCurrentActivity() == null) {
            return;
        }
       isShow = true;

       DialogUtil.showAlertDialog(App.getInstance().getCurrentActivity(), "提示", "登陆状态已过期，请重新登陆", "确定", (dialog, which) -> {
           dialog.dismiss();
           App.getInstance().logout();
           EventBus.getDefault().post(new MessageEvent(FinalValue.LOGOUT, 0));
           RouteUtil.startNewActivity(App.getInstance().getCurrentActivity(), new Intent(MyActivityManager.getInstance().currentActivity(), LoginActivity.class));
       }, "一会再说", (dialog, which) -> {
           App.getInstance().logout();
           dialog.dismiss();
       });

    }
}
