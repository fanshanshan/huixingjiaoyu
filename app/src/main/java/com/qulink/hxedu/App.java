package com.qulink.hxedu;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.qulink.hxedu.api.NetUtil;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.entity.TokenInfo;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.PrefUtils;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;

import org.greenrobot.eventbus.EventBus;
import org.xutils.x;

import java.lang.ref.WeakReference;
import java.util.Stack;


public class App extends Application {
    private final Stack<WeakReference<Activity>> activitys = new Stack<WeakReference<Activity>>();

    private TokenInfo tokenInfo;

    public UserInfo getUserInfo() {
        if(userInfo==null){
            return new UserInfo();
        }
        return userInfo;
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

    public TokenInfo getTokenInfo() {

        return tokenInfo;
    }

    public boolean isLogin() {
        if (tokenInfo == null) {
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
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG); // 是否输出debug日志, 开启debug会影响性能.

    }



    /**
     * 将Activity压入Application栈
     *
     * @param task 将要压入栈的Activity对象
     */
    public void pushTask(WeakReference<Activity> task) {
        activitys.push(task);
    }

    /**
     * 将传入的Activity对象从栈中移除
     *
     * @param task
     */
    public void removeTask(WeakReference<Activity> task) {
        activitys.remove(task);
    }

    public void exit() {
        try {
            //关闭所有Activity
            removeAll();
            //退出进程
            //System.exit(0);
        } catch (Exception e) {
        }
    }

    public void removeAll() {
        //finish所有的Activity
        for (WeakReference<Activity> task : activitys) {
            Activity mActivity = task.get();
            if (null != mActivity && !mActivity.isFinishing()) {
                mActivity.finish();
            }
        }
    }

}
