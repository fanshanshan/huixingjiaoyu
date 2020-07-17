package com.qulink.hxedu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.qulink.hxedu.api.NetUtil;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.TokenInfo;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.PrefUtils;
import com.qulink.hxedu.util.RouteUtil;

import butterknife.BindView;

public class SplashActivity extends BaseActivity {

    @BindView(R.id.iv)
    ImageView iv;

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
        Glide.with(this).load(R.drawable.splash).into(iv);
        TokenInfo tokenInfo = PrefUtils.getTokenBean(this);
        if (tokenInfo != null && tokenInfo.getToken() != null) {
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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RouteUtil.startNewActivity(SplashActivity.this, new Intent(SplashActivity.this, MainActivity.class));

                finish();
            }
        },2000);


    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {


        }
        return true;
    }

}
