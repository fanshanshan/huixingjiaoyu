package com.qulink.hxedu;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.os.Bundle;

import com.alipay.sdk.app.EnvUtils;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.NetUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.entity.TokenInfo;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.ui.LoginActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.PrefUtils;
import com.qulink.hxedu.util.ToastUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.tencent.rtmp.TXLiveBase;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.Stack;


public class App extends Application {

    private TokenInfo tokenInfo;

    private Activity currentActivity;
    private DefaultSetting defaultSetting;

    public Activity getCurrentActivity() {

        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public void getDefaultSetting(Context context, DefaultSettingCallback defaultSettingCallback) {
        if(defaultSetting==null){
            getSettingInfo(context,defaultSettingCallback);
        }else{
            defaultSettingCallback.getDefaultSetting(defaultSetting);
        }
    }

    public void setDefaultSetting(DefaultSetting defaultSetting) {
        this.defaultSetting = defaultSetting;
    }

    public void getUserInfo(Context context,UserInfoCallback userInfoCallback) {
        if(userInfo==null){
            if(getTokenInfo(context)!=null){
                getUserinfoByServer(context,userInfoCallback);
            }
        }else{
            userInfoCallback.getUserInfo(userInfo);
        }

    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    private UserInfo userInfo;

    private static App instance;

    public static App getInstance() {
        if (instance == null) {
            instance = new App();
        }
        return instance;
    }

    public TokenInfo getTokenInfo(Context context) {
        if(tokenInfo==null){
            TokenInfo tokenInfo = PrefUtils.getTokenBean(context);
            if(tokenInfo==null){
                return null;
            }else{
                NetUtil.getInstance().setToken(tokenInfo.getToken());
                setTokenInfo(tokenInfo);
            }

        }
        return tokenInfo;
    }

    public boolean isLogin(Context context) {
        if (getTokenInfo(context) == null) {
           // context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        }
        return true;
    }
    public boolean isLogin(Context context,boolean withIntent) {
        if (getTokenInfo(context) == null) {
            context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        }
        return true;
    }
    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;

    }

    public void loginSuccess(TokenInfo tokenInfo){
        setTokenInfo(tokenInfo);
        NetUtil.getInstance().setToken(tokenInfo.getToken());
        EventBus.getDefault().post(new MessageEvent(FinalValue.LOGIN_SUCCESS, 0));
        PrefUtils.saveToken(this,tokenInfo);
    }

    public void logout(){
        setTokenInfo(null);
        PrefUtils.saveToken(this,tokenInfo);
        EventBus.getDefault().post(new MessageEvent(FinalValue.LOGOUT, 0));

    }
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();


//        CrashHanlder handler = CrashHanlder.getInstance();
//        handler.init(getApplicationContext());
//        Thread.setDefaultUncaughtExceptionHandler(handler);


        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

        //支付宝沙箱环境
        EnvUtils.setEnv(EnvUtils.EnvEnum.SANDBOX);

        //腾讯云直播
        initTxLive();


        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {

            }

            @Override
            public void onActivityStarted(Activity activity) {

                currentActivity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {
               // MyActivityManager.getInstance().popActivity(activity);

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {
            }
        });





    }

    void initTxLive(){
        String licenceURL = "http://license.vod2.myqcloud.com/license/v1/c0eb7b670075d3f4316a5f8deeeddc54/TXLiveSDK.licence"; // 获取到的 licence url
        String licenceKey = "5b90ec4d6ca6b7511633306647073db0"; // 获取到的 licence key
        TXLiveBase.getInstance().setLicence(this, licenceURL, licenceKey);
    }





    void getSettingInfo(Context context, DefaultSettingCallback defaultSettingCallback) {

        DialogUtil.showLoading((Activity) context,true);
        ApiUtils.getInstance().getQiniuUrl(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading((Activity)context);
                DefaultSetting defaultSetting = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()),DefaultSetting.class);
                defaultSettingCallback.getDefaultSetting(defaultSetting);
                setDefaultSetting(defaultSetting);
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading((Activity)context);
                ToastUtils.show(context,msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading((Activity)context);
            }


        });
    }
    void getUserinfoByServer(Context context, UserInfoCallback userInfoCallback) {
        DialogUtil.showLoading((Activity) context,false);
        ApiUtils.getInstance().getUserInfo(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading((Activity)context);

                UserInfo userInfo = GsonUtil.GsonToBean(t.getData().toString(),UserInfo.class);
                App.getInstance().setUserInfo(userInfo);
                userInfoCallback.getUserInfo(userInfo);
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading((Activity)context);

                if(isLogin(context)){
                    EventBus.getDefault().post(new MessageEvent(FinalValue.TOKEN_ERROR));
                }else{
                    ToastUtils.show(context,msg);
                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading((Activity)context);

            }


        });
    }



}
