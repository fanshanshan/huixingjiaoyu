package com.qulink.hxedu.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.pay.PayResult;
import com.qulink.hxedu.pay.PayResultDetail;
import com.qulink.hxedu.pay.PayWay;
import com.qulink.hxedu.ui.sign.SignActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.RegexUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SendVipActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.tv_bar_right)
    TextView tvBarRight;
    @BindView(R.id.ll_bar)
    LinearLayout llBar;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.rl_ali)
    RelativeLayout rlAli;
    @BindView(R.id.rl_wx)
    RelativeLayout rlWx;
    @BindView(R.id.rl_bank)
    RelativeLayout rlBank;
    @BindView(R.id.iv_ali)
    ImageView ivAli;
    @BindView(R.id.iv_wx)
    ImageView ivWx;
    @BindView(R.id.iv_bank)
    ImageView ivBank;
    @BindView(R.id.tv_vip_price_desc)
    TextView tvVipPriceDesc;

    String type;//
    @BindView(R.id.ll_send)
    LinearLayout llSend;
    @BindView(R.id.tv_price)
    TextView tvPrice;

    private int payWay =1;

    private final int ALIPAY_SUCCESS_CODE=111;
    private final int WX_SUCCESS_CODE=222;
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
         switch (msg.what){
             case ALIPAY_SUCCESS_CODE:
                 PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                 /**
                  * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                  */
                 String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                 String resultStatus = payResult.getResultStatus();
                 // 判断resultStatus 为9000则代表支付成功
                 if (TextUtils.equals(resultStatus, "9000")) {
                     // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                     PayResultDetail payResultDetail = GsonUtil.GsonToBean(resultInfo, PayResultDetail.class);
                     if (isBuy()) {
                         buyVipCheck(payResultDetail.getAlipay_trade_app_pay_response().getOut_trade_no());
                     } else {
                         sendVipCheck(payResultDetail.getAlipay_trade_app_pay_response().getOut_trade_no());
                     }
                 } else {
                     // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                     ToastUtils.show(SendVipActivity.this, resultInfo);
                 }
                 break;
             case WX_SUCCESS_CODE:

                 break;
         }
        }

        ;
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_send_vip;
    }

    @Override
    protected void init() {
        setBarBg(0x00000000);
        setBarTtxtColors(0xffffffff);
        setBackImg(R.drawable.back_white2);
        App.getInstance().getDefaultSetting(this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                tvVipPriceDesc.setText(defaultSetting.getVip_price().getValue() + "元/年");
                tvPrice.setText("￥"+defaultSetting.getVip_price().getValue() );
            }
        });

        type = getIntent().getStringExtra("type");
        if (type == null) {
            type = "buy";//默认为buy
        }
        setTitle(isBuy() ? getString(R.string.open_vip) : getString(R.string.send_vip2));
        llSend.setVisibility(isBuy() ? View.GONE : View.VISIBLE);
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }


    private boolean isBuy() {
        if (type.equals("buy")) {
            return true;
        }
        return false;
    }

    @OnClick({R.id.rl_ali, R.id.rl_wx, R.id.rl_bank, R.id.tv_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_ali:
                payWay = 1;
                ivAli.setImageResource(R.drawable.pay_checked);
                ivWx.setImageResource(R.drawable.pay_check);
                ivBank.setImageResource(R.drawable.pay_check);

                break;
            case R.id.rl_wx:
                payWay = 2;
                ivWx.setImageResource(R.drawable.pay_checked);
                ivAli.setImageResource(R.drawable.pay_check);
                ivBank.setImageResource(R.drawable.pay_check);
                break;
            case R.id.rl_bank:
                payWay = 3;
                ivBank.setImageResource(R.drawable.pay_checked);
                ivWx.setImageResource(R.drawable.pay_check);
                ivAli.setImageResource(R.drawable.pay_check);
                break;
            case R.id.tv_buy:
                pay();
                break;
        }
    }

    void pay() {
        if (isBuy()) {
            buyVip();
        } else {
            sendVip();
        }
    }

    void sendVip() {
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            ToastUtils.show(this, "请输入好友手机号");
            return;
        }
        if (RegexUtil.isPhoneNumber(etPhone.getText().toString())) {
            ToastUtils.show(this, "请输入正确的手机号");
            return;
        }

        DialogUtil.showLoading(this,true);
        ApiUtils.getInstance().sendVip(etPhone.getText().toString(), payWay+"", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(SendVipActivity.this);
                if(payWay==1){
                    final String orderInfo = t.getData().toString();
                    Runnable payRunnable = new Runnable() {
                        @Override
                        public void run() {
                            PayTask alipay = new PayTask(SendVipActivity.this);
                            Map<String, String> result = alipay.payV2(orderInfo, true);

                            Message msg = new Message();
                            msg.what = 1111;
                            msg.obj = result;
                            mHandler.sendMessage(msg);
                        }
                    };
                    // 必须异步调用
                    Thread payThread = new Thread(payRunnable);
                    payThread.start();
                }else{
                    payWx();
                }

            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(SendVipActivity.this);

                ToastUtils.show(SendVipActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(SendVipActivity.this);
                ToastUtils.show(SendVipActivity.this, expectionMsg);

            }
        });
    }

    void buyVip() {

        ApiUtils.getInstance().buyVip(FinalValue.PAY_ALI, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(SendVipActivity.this);
                final String orderInfo = t.getData().toString();
                Runnable payRunnable = new Runnable() {
                    @Override
                    public void run() {
                        PayTask alipay = new PayTask(SendVipActivity.this);
                        Map<String, String> result = alipay.payV2(orderInfo, true);

                        Message msg = new Message();
                        msg.what = ALIPAY_SUCCESS_CODE;
                        msg.obj = result;
                        mHandler.sendMessage(msg);
                    }
                };
                // 必须异步调用
                Thread payThread = new Thread(payRunnable);
                payThread.start();
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(SendVipActivity.this);

                ToastUtils.show(SendVipActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(SendVipActivity.this);
                ToastUtils.show(SendVipActivity.this, expectionMsg);

            }
        });
    }

    void buyVipCheck(String orderNo) {
        DialogUtil.showLoading(this, true, "正在查询支付状态");
        ApiUtils.getInstance().buyVipCheck(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(SendVipActivity.this);

                App.getInstance().getUserInfo(SendVipActivity.this, new UserInfoCallback() {
                    @Override
                    public void getUserInfo(UserInfo userInfo) {
                        userInfo.setStatus(1);
                    }
                });
                DialogUtil.showAlertDialog(SendVipActivity.this, "提示", "会员购买成功", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(SendVipActivity.this);

                ToastUtils.show(SendVipActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        }, orderNo);
    }

    void sendVipCheck(String orderNo) {
        DialogUtil.showLoading(this, true, "正在查询支付状态");
        ApiUtils.getInstance().sendVipCheck(orderNo, etPhone.getText().toString(), new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(SendVipActivity.this);

                DialogUtil.showAlertDialog(SendVipActivity.this, "提示", "赠送会员成功", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(SendVipActivity.this);

                ToastUtils.show(SendVipActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        });
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
    private void payWx(){
        if(api==null){
            regToWx();
        }
        PayReq request = new PayReq();

        request.appId = "wxd930ea5d5a258f4f";//子商户appid

        request.partnerId = "1900000109";//子商户号

        request.prepayId= "1101000000140415649af9fc314aa427";

        request.packageValue = "Sign=WXPay";

        request.nonceStr= "1101000000140429eb40476f8896f4c9";

        request.timeStamp= "1398746574";

        request.sign= "7FFECB600D7157C5AA49810D2D8F28BC2811827B";

        api.sendReq(request);
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccess(MessageEvent messageEvent) {
        if (messageEvent.getCode() == FinalValue.WECHAT_PAYEOK) {
            mHandler.sendEmptyMessage(WX_SUCCESS_CODE);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
