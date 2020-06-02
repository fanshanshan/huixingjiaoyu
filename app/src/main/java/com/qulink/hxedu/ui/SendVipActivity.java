package com.qulink.hxedu.ui;

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
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.pay.PayResult;
import com.qulink.hxedu.pay.PayResultDetail;
import com.qulink.hxedu.pay.PayWay;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.RegexUtil;
import com.qulink.hxedu.util.ToastUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class SendVipActivity extends BaseActivity {

    @BindView(R.id.status)
    TextView status;
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

    private PayWay payWay = PayWay.ALI;


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
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
               if(isBuy()){
                   buyVipCheck(payResultDetail.getAlipay_trade_app_pay_response().getOut_trade_no());
               }else{
                   sendVipCheck(payResultDetail.getAlipay_trade_app_pay_response().getOut_trade_no());
               }
             } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                ToastUtils.show(SendVipActivity.this, resultInfo);
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
            }
        });

        type = getIntent().getStringExtra("type");
        if (type == null) {
            type = "buy";//默认为buy
        }
        setTitle(isBuy() ? getString(R.string.open_vip) : getString(R.string.send_vip2));
        llSend.setVisibility(isBuy()?View.GONE:View.VISIBLE);
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }


    private boolean isBuy(){
        if(type.equals("buy")){
            return true;
        }
        return  false;
    }
    @OnClick({R.id.rl_ali, R.id.rl_wx, R.id.rl_bank, R.id.tv_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_ali:
                payWay = PayWay.ALI;
                ivAli.setImageResource(R.drawable.pay_checked);
                ivWx.setImageResource(R.drawable.pay_check);
                ivBank.setImageResource(R.drawable.pay_check);

                break;
            case R.id.rl_wx:
                payWay = PayWay.WX;
                ivWx.setImageResource(R.drawable.pay_checked);
                ivAli.setImageResource(R.drawable.pay_check);
                ivBank.setImageResource(R.drawable.pay_check);
                break;
            case R.id.rl_bank:
                payWay = PayWay.BANK;
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
        switch (payWay) {
            case WX:
                ToastUtils.show(SendVipActivity.this, "暂未开放");
                break;
            case ALI:
                if(isBuy()){
                    buyVip();
                }else{
                    sendVip();
                }
                break;
            case BANK:
                ToastUtils.show(SendVipActivity.this, "暂未开放");

                break;
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

        ApiUtils.getInstance().sendVip(etPhone.getText().toString(), FinalValue.PAY_ALI, new ApiCallback() {
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
                        msg.what = 1111;
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

    void buyVip() {

        ApiUtils.getInstance().buyVip( FinalValue.PAY_ALI, new ApiCallback() {
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
                        msg.what = 1111;
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
        ApiUtils.getInstance().buyVipCheck( new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(SendVipActivity.this);

                ToastUtils.show(SendVipActivity.this, "购买成功");
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(SendVipActivity.this);

                ToastUtils.show(SendVipActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        },orderNo);
    }
    void sendVipCheck(String orderNo) {
        DialogUtil.showLoading(this, true, "正在查询支付状态");
        ApiUtils.getInstance().sendVipCheck(orderNo, etPhone.getText().toString(), new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(SendVipActivity.this);

                ToastUtils.show(SendVipActivity.this, "赠送成功");
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

}
