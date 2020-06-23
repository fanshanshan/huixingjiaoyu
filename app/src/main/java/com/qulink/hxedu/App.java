package com.qulink.hxedu;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.text.LineBreaker;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.multidex.MultiDex;

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
import com.qulink.hxedu.entity.SystemSettingBean;
import com.qulink.hxedu.entity.TokenInfo;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.jpush.ExampleUtil;
import com.qulink.hxedu.jpush.TagAliasOperatorHelper;
import com.qulink.hxedu.ui.LoginActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.PrefUtils;
import com.qulink.hxedu.util.SystemUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshInitializer;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;


public class App extends Application {
    public static int sequence = 1;
    /**增加*/
    public static final int ACTION_ADD = 1;
    /**覆盖*/
    public static final int ACTION_SET = 2;
    /**删除部分*/
    public static final int ACTION_DELETE = 3;
    /**删除所有*/
    public static final int ACTION_CLEAN = 4;
    /**查询*/
    public static final int ACTION_GET = 5;

    public static final int ACTION_CHECK = 6;

    public static final int DELAY_SEND_ACTION = 1;

    public static final int DELAY_SET_MOBILE_NUMBER_ACTION = 2;
    private TokenInfo tokenInfo;
    private  Activity mCurrentActivity;
    private DefaultSetting defaultSetting;

    private SystemSettingBean systemSettingBean;

    public SystemSettingBean getSystemSettingBean(Context context) {
        if(systemSettingBean==null){
            systemSettingBean = PrefUtils.getSystemSetting(context);
        }
        return systemSettingBean;
    }

    public void setSystemSettingBean(SystemSettingBean systemSettingBean) {
        this.systemSettingBean = systemSettingBean;
    }

    public  Activity getCurrentActivity() {

        return mCurrentActivity;
    }

    public  void setCurrentActivity(Activity currentActivity) {
        mCurrentActivity = currentActivity;
    }

    public synchronized void getDefaultSetting(Context context, DefaultSettingCallback defaultSettingCallback) {
        if (defaultSetting == null) {
            getSettingInfo(context, defaultSettingCallback);
        } else {

            defaultSettingCallback.getDefaultSetting(defaultSetting);
        }
    }

    public void setDefaultSetting(DefaultSetting defaultSetting) {
        this.defaultSetting = defaultSetting;
    }

    public synchronized void getUserInfo(Context context, UserInfoCallback userInfoCallback) {
        if (userInfo == null) {
            if (getTokenInfo(context) != null) {
                getUserinfoByServer(context, userInfoCallback);
            }
        } else {
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
            // 加锁目的，防止多线程同时进入造成对象多次实例化
            synchronized (App.class) {
                // 为了在null的情况下创建实例，防止不必要的损耗
                if (instance == null) {
                    instance = new App();
                }
            }
        }
        return instance;
    }

    public TokenInfo getTokenInfo(Context context) {
        if (tokenInfo == null) {
            TokenInfo tokenInfo = PrefUtils.getTokenBean(context);
            if (tokenInfo == null) {
                return null;
            } else {
                //NetUtil.getInstance().setToken(tokenInfo.getToken());
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

    public boolean isLogin(Context context, boolean withIntent) {
        if (getTokenInfo(context) == null) {
            showLoginDialog(context);
            return false;
        }
        return true;
    }

    public void setTokenInfo(TokenInfo tokenInfo) {
        this.tokenInfo = tokenInfo;

    }

    public void loginSuccess(Context context,TokenInfo tokenInfo) {
        setTokenInfo(tokenInfo);
        NetUtil.getInstance().setToken(tokenInfo.getToken());
        refreshUserInfo(context);
        PrefUtils.saveToken(this, tokenInfo);
    }

    public void logout() {
        setTokenInfo(null);
        PrefUtils.saveToken(this, tokenInfo);
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
        SmartRefreshLayout.setDefaultRefreshInitializer(new DefaultRefreshInitializer() {
            @Override
            public void initialize(@NonNull Context context, @NonNull RefreshLayout layout) {
                layout.getLayout().setTag("close egg");
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //initJpush(this);
//        CrashHanlder handler = CrashHanlder.getInstance();
//        handler.init(getApplicationContext());
//        Thread.setDefaultUncaughtExceptionHandler(handler);

        MultiDex.install(this);
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

           //     currentActivity = activity;
            }

            @Override
            public void onActivityResumed(Activity activity) {
                setCurrentActivity(activity);

            }

            @Override
            public void onActivityPaused(Activity activity) {
              //  currentActivity = null;
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
//初始化sdk
      JPushInterface.init(this);
    }
    void initTxLive() {
        String licenceURL = "http://license.vod2.myqcloud.com/license/v1/c0eb7b670075d3f4316a5f8deeeddc54/TXLiveSDK.licence"; // 获取到的 licence url
        String licenceKey = "5b90ec4d6ca6b7511633306647073db0"; // 获取到的 licence key
       // TXLiveBase.getInstance().setLicence(this, licenceURL, licenceKey);
    }


    void getSettingInfo(Context context, DefaultSettingCallback defaultSettingCallback) {

        DialogUtil.showLoading((Activity) context, true);
        ApiUtils.getInstance().getQiniuUrl(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading((Activity) context);
                DefaultSetting defaultSetting = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), DefaultSetting.class);
                defaultSettingCallback.getDefaultSetting(defaultSetting);
                setDefaultSetting(defaultSetting);
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading((Activity) context);
                ToastUtils.show(context, msg);

                String str = SystemUtil.readAssetsTxt(context,"setting");
                DefaultSetting defaultSetting = GsonUtil.GsonToBean(str, DefaultSetting.class);

                defaultSettingCallback.getDefaultSetting(defaultSetting);
                setDefaultSetting(defaultSetting);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading((Activity) context);
                String str = SystemUtil.readAssetsTxt(context,"setting");
                DefaultSetting defaultSetting = GsonUtil.GsonToBean(str, DefaultSetting.class);

                defaultSettingCallback.getDefaultSetting(defaultSetting);
                setDefaultSetting(defaultSetting);
            }


        });
    }

    void getUserinfoByServer(Context context, UserInfoCallback userInfoCallback) {
        DialogUtil.showLoading((Activity) context, false);
        ApiUtils.getInstance().getUserInfo(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading((Activity) context);
                UserInfo userInfo = GsonUtil.GsonToBean(t.getData().toString(), UserInfo.class);
                App.getInstance().setUserInfo(userInfo);
                userInfoCallback.getUserInfo(userInfo);
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading((Activity) context);

                if (isLogin(context)) {
                    EventBus.getDefault().post(new MessageEvent(FinalValue.TOKEN_ERROR));
                } else {
                    ToastUtils.show(context, msg);
                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading((Activity) context);

            }


        });
    }
    void refreshUserInfo(Context context) {
        ApiUtils.getInstance().getUserInfo(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                UserInfo userInfo = GsonUtil.GsonToBean(t.getData().toString(), UserInfo.class);
                App.getInstance().setUserInfo(userInfo);
                EventBus.getDefault().post(new MessageEvent(FinalValue.LOGIN_SUCCESS, 0));
                deleteAlias(context);
             setAlias(context,userInfo.getId()+"");
            }

            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void expcetion(String expectionMsg) {

            }


        });
    }


    void showLoginDialog(Context context) {
        DialogUtil.showAlertDialog(context, "提示", "登录后才能进行操作", "立即登录", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                context.startActivity(new Intent(context, LoginActivity.class));
            }
        }, "一会再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    public void setAlias(Context context,String alias) {

        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = 2;
        sequence++;
        tagAliasBean.alias = "5233";

        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(context,sequence,tagAliasBean);

    }

    public void deleteAlias(Context context) {

        TagAliasOperatorHelper.TagAliasBean tagAliasBean = new TagAliasOperatorHelper.TagAliasBean();
        tagAliasBean.action = 3;
        sequence++;

        tagAliasBean.isAliasAction = true;
        TagAliasOperatorHelper.getInstance().handleAction(context,sequence,tagAliasBean);

    }


}
