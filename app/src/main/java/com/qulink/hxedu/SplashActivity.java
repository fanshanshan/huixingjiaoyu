package com.qulink.hxedu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qulink.hxedu.R;
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
import com.qulink.hxedu.service.LoadingService;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.sign.SignActivity;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.PrefUtils;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.SystemUtil;
import com.qulink.hxedu.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;

public class SplashActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_splash;
    }

    @Override
    protected void init() {
        TokenInfo tokenInfo = PrefUtils.getTokenBean(this);
        if(tokenInfo!=null&&tokenInfo.getToken()!=null){
           NetUtil.getInstance().setToken(tokenInfo.getToken());
           App.getInstance().setTokenInfo(tokenInfo);
           App.getInstance().getUserInfo(this, new UserInfoCallback() {
               @Override
               public void getUserInfo(UserInfo userInfo) {

               }
           });
       }
       App.getInstance().getDefaultSetting(this, new DefaultSettingCallback() {
           @Override
           public void getDefaultSetting(DefaultSetting defaultSetting) {

           }
       });

        RouteUtil.startNewActivity(SplashActivity.this,new Intent(SplashActivity.this,MainActivity.class));

        finish();

    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }




}
