package com.qulink.hxedu;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.NetUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.entity.TokenInfo;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.PrefUtils;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

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

        RouteUtil.startNewActivity(SplashActivity.this,new Intent(SplashActivity.this,MainActivity.class));

        finish();

    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }




}
