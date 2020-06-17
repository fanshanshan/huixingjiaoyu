package com.qulink.hxedu.ui.msg;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.app.PayTask;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.ActivityDetailBean;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.pay.PayResult;
import com.qulink.hxedu.pay.PayResultDetail;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.SendVipActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ToastUtils;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.zzhoujay.richtext.RichText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class ActivityDetailActivity extends BaseActivity {

    private final int ALIPAY_SUCCESS_CODE=111;
    private final int WX_SUCCESS_CODE=222;

    int activityId;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_join_num)
    TextView tvJoinNum;
    @BindView(R.id.tv_need_pay)
    TextView tvNeedPay;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_wx)
    EditText etWx;
    @BindView(R.id.tv_money)
    TextView tvMoney;
    @BindView(R.id.ll_money)
    LinearLayout llMoney;
    @BindView(R.id.tv_complete)
    TextView tvComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_detail;
    }

    @Override
    protected void init() {
        setTitle("活动详情");
        activityId = getIntent().getIntExtra("id", 0);
        getActivityDetail();
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private void getActivityDetail() {
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().getActivityDetail(activityId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(ActivityDetailActivity.this);
                activityDetailBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), ActivityDetailBean.class);
                dealData();
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(ActivityDetailActivity.this);
                ToastUtils.show(ActivityDetailActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(ActivityDetailActivity.this);
                ToastUtils.show(ActivityDetailActivity.this, expectionMsg);
            }
        });
    }

    ActivityDetailBean activityDetailBean;

    private void dealData() {
        if (activityDetailBean == null) {
            return;
        }
        RichText.from(activityDetailBean.getIntro()).into(tvContent);
        tvTime.setText(activityDetailBean.getCreateTime());
        tvTitle.setText(activityDetailBean.getTitle());
        tvJoinNum.setText(activityDetailBean.getParticipateTotal() + "人已报名");
        if (activityDetailBean.getPayRequired()==1) {
            llMoney.setVisibility(View.VISIBLE);
            tvNeedPay.setVisibility(View.VISIBLE);
            tvMoney.setText(activityDetailBean.getPayAmount() + "");
        } else {
            tvNeedPay.setVisibility(View.VISIBLE);
            llMoney.setVisibility(View.GONE);

        }
        if (activityDetailBean.getParticipated() == 1) {
            etPhone.setText(activityDetailBean.getLastRecord().getMobileNumber());
            etWx.setText(activityDetailBean.getLastRecord().getWxNumber());
        }

        if (!activityIsOver()) {
            if (activityDetailBean.getParticipated() == 0) {
                tvComplete.setEnabled(true);
                tvComplete.setText("报名预约");
                etWx.setEnabled(true);
                etPhone.setEnabled(true);
                tvComplete.setBackgroundResource(R.drawable.btn_theme_bg_circle);
            } else {
                tvComplete.setText("已报名");
                tvComplete.setEnabled(false);
                etWx.setEnabled(false);
                etPhone.setEnabled(false);
                tvComplete.setBackgroundResource(R.drawable.btn_grey_bg_circle);
            }
        } else {
            tvComplete.setEnabled(false);
            etWx.setEnabled(false);
            etPhone.setEnabled(false);

            tvComplete.setText("活动已结束");
            tvComplete.setBackgroundResource(R.drawable.btn_grey_bg_circle);
        }
    }

    private boolean activityIsOver(){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        boolean isOver = false;
        try {
            isOver = date.after(dateFormat.parse(activityDetailBean.getEndDate()));
        } catch (ParseException e) {
            e.printStackTrace();
            isOver = true;
        }
        return  isOver;
    }
    private void showChooseWayDialog() {
        Dialog bottomDialog;

        //创建dialog,同时设置dialog主题
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        //绘制dialog  UI视图
        View contentView = LayoutInflater.from(this).inflate(R.layout.pay_activity_way, null);

        RelativeLayout rlAli = contentView.findViewById(R.id.rl_ali);
        RelativeLayout rlwx = contentView.findViewById(R.id.rl_wx);
        RelativeLayout rlBanl = contentView.findViewById(R.id.rl_bank);
        LinearLayout llAdd = contentView.findViewById(R.id.ll_add_bank);
        ImageView ivClose = contentView.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });

        //给dialog添加view
        bottomDialog.setContentView(contentView);
        //为绘制的view设置参数
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        //设置为全屏的宽
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        //设置dialog位置
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        //添加进出场动画
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        //允许点击外部退出dialog
        bottomDialog.setCanceledOnTouchOutside(true);
        //show  dialog
        bottomDialog.show();

        rlAli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
                join(1);
            }
        });
        rlwx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();

                join(2);
            }
        });
        rlBanl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(ActivityDetailActivity.this, "暂未开放");
            }
        });
        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(ActivityDetailActivity.this, "暂未开放");
            }
        });

    }


    @OnClick(R.id.tv_complete)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_complete:
                if(TextUtils.isEmpty(etPhone.getText().toString())){
                    ToastUtils.show(this,"请输入手机号");
                    return;
                } if(TextUtils.isEmpty(etWx.getText().toString())){
                ToastUtils.show(this,"请输入微信号");
                return;
            }
                if(activityDetailBean==null){
                    return;
                }
                if (!activityIsOver()) {
                    if (activityDetailBean.getParticipated() == 0) {
                        if(activityDetailBean.getPayRequired()==1){
                            DialogUtil.showAlertDialog(ActivityDetailActivity.this, "提示", "本次活动需要支付" + activityDetailBean.getPayAmount() + "费用,是否继续报名", "继续", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    showChooseWayDialog();
                                }
                            }, "取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }else{
                            join(0);
                        }
                    }
                }
                break;
        }
    }

    private void join(int payType){
        if(TextUtils.isEmpty(etPhone.getText().toString())){
            ToastUtils.show(this,"请输入手机号");
            return;
        } if(TextUtils.isEmpty(etWx.getText().toString())){
            ToastUtils.show(this,"请输入微信号");
            return;
        }
        ApiUtils.getInstance().joinActivity(activityId, etPhone.getText().toString(), etWx.getText().toString(), payType, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                if(payType==0){
                    ToastUtils.show(ActivityDetailActivity.this,"报名成功");
                    getActivityDetail();
                }else{
                    if(payType==1){
                        payAli("");
                    }else{
                        payWx();
                    }
                }
            }

            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        });
    }


    private void payAli(String orderInfo){
        DialogUtil.hideLoading(ActivityDetailActivity.this);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(ActivityDetailActivity.this);
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
                       // PayResultDetail payResultDetail = GsonUtil.GsonToBean(resultInfo, PayResultDetail.class);
                        ToastUtils.show(ActivityDetailActivity.this,"报名成功");
                        getActivityDetail();
                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.show(ActivityDetailActivity.this, resultInfo);
                    }
                    break;
                case WX_SUCCESS_CODE:

                    ToastUtils.show(ActivityDetailActivity.this,"报名成功");
                    getActivityDetail();
                    break;
            }

        }

        ;
    };


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
