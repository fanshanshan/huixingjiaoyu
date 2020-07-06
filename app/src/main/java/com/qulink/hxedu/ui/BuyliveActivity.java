package com.qulink.hxedu.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.CourseDetailBean;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.LiveInfoBean;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.entity.PayWxBean;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.pay.PayResult;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class BuyliveActivity extends BaseActivity {

    @BindView(R.id.iv_img)
    RoundedImageView ivImg;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_lesson_title)
    TextView tvLessonTitle;
    @BindView(R.id.tv_price1)
    TextView tvPrice1;
    @BindView(R.id.iv_vip)
    ImageView ivVip;
    @BindView(R.id.tv_price2)
    TextView tvPrice2;
    @BindView(R.id.iv_ali)
    ImageView ivAli;
    @BindView(R.id.rl_ali)
    RelativeLayout rlAli;
    @BindView(R.id.iv_wx)
    ImageView ivWx;
    @BindView(R.id.rl_wx)
    RelativeLayout rlWx;
    @BindView(R.id.iv_bank)
    ImageView ivBank;
    @BindView(R.id.rl_bank)
    RelativeLayout rlBank;

    private boolean withResult;
    private int liveId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    protected int getLayout() {
        return R.layout.activity_buy_lesson;
    }


    @Override
    protected void init() {
        withResult = getIntent().getBooleanExtra("withResult", false);
        liveId = getIntent().getIntExtra("liveId", 0);
        setTitle(getString(R.string.sure_order));
        detdetail();
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private int payWay = 1;

    @OnClick({R.id.tv_buy,R.id.rl_ali,R.id.rl_wx,R.id.rl_bank})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_buy:
                exchange(payWay);
                break;

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
                ToastUtils.show(this,"暂未开放");
//                payWay = 3;
//                ivBank.setImageResource(R.drawable.pay_checked);
//                ivWx.setImageResource(R.drawable.pay_check);
//                ivAli.setImageResource(R.drawable.pay_check);
                break;
        }
    }



    private LiveInfoBean liveInfoBean;
    private void exchange(int payType) {
        DialogUtil.showLoading(this, true, "请稍候");
        ApiUtils.getInstance().buylive(liveId, payType, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(BuyliveActivity.this);
                if (payType == 1) {
                    payAli(t.getData().toString());
                } else {
                    String str = StringEscapeUtils.unescapeJava(t.getData().toString());
                    str = str.replace("\"{", "{");
                    str = str.replace("}\"", "}");
                    PayWxBean payWxBean = GsonUtil.GsonToBean(str, PayWxBean.class);

                    payWx(payWxBean);
                }
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(BuyliveActivity.this);
                ToastUtils.show(BuyliveActivity.this, msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(BuyliveActivity.this);
                ToastUtils.show(BuyliveActivity.this, expectionMsg);
            }
        });
    }

    private void payAli(String orderInfo) {
        DialogUtil.hideLoading(BuyliveActivity.this);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(BuyliveActivity.this);
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

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
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
                        // PayResultDetail payResultDetail = GsonUtil.GsonToBean(resultInfo, PayResultDetail.class);
                        buyLessonSuc();

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.show(BuyliveActivity.this, resultInfo);
                    }
                    break;
                case WX_SUCCESS_CODE:
                    buyLessonSuc();
                    break;
            }

        }

        ;
    };

    private void buyLessonSuc(){

        DialogUtil.showAlertDialog(this, "提示", "课程已成功购买，快去学习吧", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                setResult(RESULT_OK);
                finish();
            }
        });

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
    private final int ALIPAY_SUCCESS_CODE = 111;
    private final int WX_SUCCESS_CODE = 222;
    private void payWx(PayWxBean payWxBean) {
        if (api == null) {
            regToWx();
        }
        PayReq request = new PayReq();

        request.appId = payWxBean.getAppid();//子商户appid

        request.partnerId = payWxBean.getPartnerid();//子商户号

        request.prepayId = payWxBean.getPrepayid();

        request.packageValue = "Sign=WXPay";

        request.nonceStr = payWxBean.getNoncestr();

        request.timeStamp = payWxBean.getTimestamp();

        request.sign = payWxBean.getTimestamp();

        api.sendReq(request);
    }


    private void detdetail(){
        DialogUtil.showLoading(this,true);
        ApiUtils.getInstance().getLiveDetail(liveId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(BuyliveActivity.this);
                LiveInfoBean liveInfoBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()),LiveInfoBean.class);
                tvLessonTitle.setText(liveInfoBean.getTitle());
                App.getInstance().getUserInfo(BuyliveActivity.this, new UserInfoCallback() {
                    @Override
                    public void getUserInfo(UserInfo userInfo) {
                        if(userInfo.isVip()){
                            tvPrice1.setText("￥"+liveInfoBean.getVipAmount() + "");
                            tvPrice2.setText("￥"+liveInfoBean.getVipAmount() + "");
                        }else{
                            tvPrice1.setText("￥"+liveInfoBean.getPayAmount() + "");
                            tvPrice2.setText("￥"+liveInfoBean.getPayAmount() + "");
                        }
                    }
                });


                App.getInstance().getDefaultSetting(BuyliveActivity.this, new DefaultSettingCallback() {
                    @Override
                    public void getDefaultSetting(DefaultSetting defaultSetting) {
                        Glide.with(BuyliveActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(),liveInfoBean.getCoverUrl())).into(ivImg);

                    }
                });
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(BuyliveActivity.this);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(BuyliveActivity.this);

            }
        });
    }
}
