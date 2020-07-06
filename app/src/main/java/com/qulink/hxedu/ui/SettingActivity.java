package com.qulink.hxedu.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.qulink.hxedu.App;
import com.qulink.hxedu.MainActivity;
import com.qulink.hxedu.MyActivityManager;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.NetUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.entity.SystemSettingBean;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.ui.setting.EditPhoneActivity;
import com.qulink.hxedu.ui.setting.ForgetPwdActivity;
import com.qulink.hxedu.ui.setting.SetPayPwdActivity;
import com.qulink.hxedu.util.CacheUtil;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.PrefUtils;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.tv_logout)
    TextView tvLogout;
    @BindView(R.id.switch_wx)
    Switch switchWx;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.switch_net_play_video)
    Switch switchNetPlayVideo;
    @BindView(R.id.switch_net_download_video)
    Switch switchNetDownloadVideo;
    @BindView(R.id.tv_cache)
    TextView tvCache;

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
        if (App.getInstance().isLogin(this)) {
            tvLogout.setText("退出登录");
        } else {
            tvLogout.setText("立即登录");
        }
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                tvPhone.setText(userInfo.getPhone());
                if (userInfo.isBindWx()) {
                    switchWx.setChecked(true);
                } else {
                    switchWx.setChecked(false);
                }
            }
        });

        switchWx.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    startWxLogin();
                }
            }
        });
        switchNetPlayVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SystemSettingBean systemSettingBean;
                if (isChecked) {
                    systemSettingBean = App.getInstance().getSystemSettingBean(SettingActivity.this);
                    if (systemSettingBean == null) {
                        systemSettingBean = new SystemSettingBean();
                    }
                    systemSettingBean.setNetPlayVideo(1);
                } else {
                    systemSettingBean = App.getInstance().getSystemSettingBean(SettingActivity.this);
                    if (systemSettingBean == null) {
                        systemSettingBean = new SystemSettingBean();
                    }
                    systemSettingBean.setNetPlayVideo(0);
                }
                saveSystemSetting(systemSettingBean);
            }
        });
        switchNetDownloadVideo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SystemSettingBean systemSettingBean;
                if (isChecked) {
                    systemSettingBean = App.getInstance().getSystemSettingBean(SettingActivity.this);
                    if (systemSettingBean == null) {
                        systemSettingBean = new SystemSettingBean();
                    }
                    systemSettingBean.setNetdownVideo(1);
                } else {
                    systemSettingBean = App.getInstance().getSystemSettingBean(SettingActivity.this);
                    if (systemSettingBean == null) {
                        systemSettingBean = new SystemSettingBean();
                    }
                    systemSettingBean.setNetdownVideo(0);
                }
                saveSystemSetting(systemSettingBean);
            }
        });


        SystemSettingBean systemSettingBean = App.getInstance().getSystemSettingBean(this);
        if (systemSettingBean == null) {
            switchNetDownloadVideo.setChecked(false);
            switchNetPlayVideo.setChecked(false);
        } else {
            if (CourseUtil.isOk(systemSettingBean.getNetdownVideo())) {
                switchNetDownloadVideo.setChecked(true);
            } else {
                switchNetDownloadVideo.setChecked(false);
            }
            if (CourseUtil.isOk(systemSettingBean.getNetPlayVideo())) {
                switchNetPlayVideo.setChecked(true);
            } else {
                switchNetPlayVideo.setChecked(false);
            }
        }


        try {
            tvCache.setText(CacheUtil.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveSystemSetting(SystemSettingBean systemSettingBean) {
        PrefUtils.saveSystemSetting(this, systemSettingBean);
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick({R.id.rl_advice,R.id.tv_logout, R.id.ll_phone, R.id.ll_edit_pwd, R.id.rl_set_pay_pwd,R.id.rl_cache,R.id.tv_cache})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_logout:
                if (App.getInstance().isLogin(SettingActivity.this)) {
                    logout();
                } else {
                    MyActivityManager.getInstance().pushActivity(SettingActivity.this);
                    RouteUtil.startNewActivity(SettingActivity.this, new Intent(SettingActivity.this, LoginActivity.class));
                }
                break;
            case R.id.ll_phone:
                startActivityForResult(new Intent(SettingActivity.this, EditPhoneActivity.class), 0);
                break;
            case R.id.ll_edit_pwd:
                Intent intent = new Intent(SettingActivity.this, ForgetPwdActivity.class);
                intent.putExtra("title", "修改密码");
                startActivity(intent);
                break;
            case R.id.rl_set_pay_pwd:
                startActivity(new Intent(SettingActivity.this, SetPayPwdActivity.class));
                break;

                case R.id.rl_advice:
                startActivity(new Intent(SettingActivity.this, AdviceActivity.class));
                break;
            case R.id.rl_cache:
               boolean result =  CacheUtil.clearAllCache(this);
               if(result){
                   ToastUtils.show(this,"缓存已清除");

                   try {
                       tvCache.setText(CacheUtil.getTotalCacheSize(this));
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
                break; case R.id.tv_cache:
               boolean result1 =  CacheUtil.clearAllCache(this);
               if(result1){
                   ToastUtils.show(this,"缓存已清除");

                   try {
                       tvCache.setText(CacheUtil.getTotalCacheSize(this));
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
                break;
        }
    }

    void logout() {
        DialogUtil.showAlertDialog(this, "提示", "确定要退出登陆吗？", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                PrefUtils.clearToken(SettingActivity.this);
                App.getInstance().setTokenInfo(null);
                App.getInstance().deleteAlias(SettingActivity.this);
                NetUtil.getInstance().setToken("");
                App.getInstance().setLoginLive(false);
                EventBus.getDefault().post(new MessageEvent(FinalValue.LOGOUT));
                RouteUtil.startNewActivity(SettingActivity.this, new Intent(SettingActivity.this, MainActivity.class));
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

    }


    void startWxLogin() {
        regToWx();
        {
            // send oauth request
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "wechat_sdk_demo_test";
            api.sendReq(req);
        }
    }

    private IWXAPI api;

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, FinalValue.WECHAT_APP_ID, true);

        // 将应用的appId注册到微信
        api.registerApp(FinalValue.WECHAT_APP_ID);
//        //建议动态监听微信启动广播进行注册到微信
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                // 将该app注册到微信
//                api.registerApp(FinalValue.WECHAT_APP_ID);
//            }
//        };
//        registerReceiver(broadcastReceiver, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));


    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Success(MessageEvent messageEvent) {
        if (messageEvent.getCode() == FinalValue.WECHAT_LOGINEOK
        ) {
//            getUserInfo();
            wxLoginToServer(messageEvent.getMessage());
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    void wxLoginToServer(String code) {
        DialogUtil.showLoading(this, true, "请稍候");
        ApiUtils.getInstance().wxLogin(code, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(SettingActivity.this);
                switchWx.setChecked(true);
                App.getInstance().getUserInfo(SettingActivity.this, new UserInfoCallback() {
                    @Override
                    public void getUserInfo(UserInfo userInfo) {
                        userInfo.setWxBindStatus(1);
                    }
                });
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(SettingActivity.this);
                ToastUtils.show(SettingActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(SettingActivity.this);
                ToastUtils.show(SettingActivity.this, expectionMsg);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            App.getInstance().getUserInfo(SettingActivity.this, new UserInfoCallback() {
                @Override
                public void getUserInfo(UserInfo userInfo) {
                    tvPhone.setText(userInfo.getPhone());
                }
            });
        }
    }

    @OnClick(R.id.tv_cache)
    public void onViewClicked() {
    }
}
