package com.qulink.hxedu.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.qulink.hxedu.App;
import com.qulink.hxedu.MainActivity;
import com.qulink.hxedu.MyActivityManager;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.NetUtil;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.PrefUtils;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.view.UniversalDialog;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.tv_logout)
    TextView tvLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void init() {
        setTitle(getString(R.string.setting));
        if(App.getInstance().isLogin(this)){
            tvLogout.setText("退出登录");
        }else{
            tvLogout.setText("立即登录");
        }
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick(R.id.tv_logout)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_logout:
                if(App.getInstance().isLogin(SettingActivity.this)){
                    logout();
                }else{
                    MyActivityManager.getInstance().pushActivity(SettingActivity.this);
                    RouteUtil.startNewActivity(SettingActivity.this,new Intent(SettingActivity.this, LoginActivity.class));
                }
                break;
        }
    }
    void logout(){
        DialogUtil.showAlertDialog(this, "提示", "确定要退出登陆吗？", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PrefUtils.clearToken(SettingActivity.this);
                App.getInstance().setTokenInfo(null);
                NetUtil.getInstance().setToken("");
                EventBus.getDefault().post(new MessageEvent(FinalValue.LOGOUT));
                RouteUtil.startNewActivity(SettingActivity.this,new Intent(SettingActivity.this, MainActivity.class));

            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }
}
