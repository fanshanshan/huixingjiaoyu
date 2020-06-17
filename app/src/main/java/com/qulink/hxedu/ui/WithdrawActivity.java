package com.qulink.hxedu.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.pay.PayFragment;
import com.qulink.hxedu.pay.PayPwdView;
import com.qulink.hxedu.ui.setting.SetPayPwdActivity;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class WithdrawActivity extends BaseActivity implements PayPwdView.InputCallBack {

    @BindView(R.id.et_mount)
    EditText etMount;
    @BindView(R.id.tv_can_withdraw_money)
    TextView tvCanWithdrawMoney;
    @BindView(R.id.tv_withdraw)
    TextView tvWithdraw;
    @BindView(R.id.iv_way)
    ImageView ivWay;
    @BindView(R.id.tv_way)
    TextView tvWay;
    @BindView(R.id.tv_show_dialog)
    LinearLayout tvShowDialog;

    private int selectWayType = 1;
    private String canWithdrawMoney = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_withdraw;
    }

    @Override
    protected void init() {
        tvWithdraw.setEnabled(false);

        setTitle("提现");
        canWithdrawMoney = getIntent().getStringExtra("money");
        if (TextUtils.isEmpty(canWithdrawMoney)) {
            canWithdrawMoney = "0.0";
        }
        tvCanWithdrawMoney.setText(canWithdrawMoney + "");
        etMount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(etMount.getText().toString())) {
                    tvWithdraw.setBackgroundResource(R.drawable.btn_theme_bg_circle);
                    tvWithdraw.setEnabled(true);

                } else {
                    tvWithdraw.setEnabled(false);

                    tvWithdraw.setBackgroundResource(R.drawable.btn_grey_bg_circle);
                }
            }
        });
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private void getBankCard() {
        ApiUtils.getInstance().getBankard(new ApiCallback() {
            @Override
            public void success(ResponseData t) {

            }

            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        });
    }

    @OnClick({R.id.tv_way, R.id.tv_show_dialog,R.id.tv_withdraw})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_way:
                break;
            case R.id.tv_withdraw:
               withdraw();
                break;
            case R.id.tv_show_dialog:
                showChooseWayDialog();
                break;
        }
    }

    private List<WithdrawWayBean> withdrawWayList;

    @Override
    public void onInputFinish(String result) {
        if(fragment!=null){
            fragment.dismiss();
        }
        DialogUtil.showLoading(this,true);
        ApiUtils.getInstance().withdraw(selectWayType, etMount.getText().toString(), result, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(WithdrawActivity.this);
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(WithdrawActivity.this);
                ToastUtils.show(WithdrawActivity.this,msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(WithdrawActivity.this);
                ToastUtils.show(WithdrawActivity.this,expectionMsg);

            }
        });

    }

    private  PayFragment fragment;
    private void withdraw(){
        if(TextUtils.isEmpty(etMount.getText().toString())){
            ToastUtils.show(this,"请输入提现金额");
            return;
        }
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                Bundle bundle = new Bundle();
                bundle.putString(PayFragment.EXTRA_CONTENT, "提现：¥ " + 100.00);
                if(CourseUtil.isOk(userInfo.getPayPasswordStatus())){
                    bundle.putString(PayFragment.SETTING_TITLE, "忘记密码");
                }else{
                    bundle.putString(PayFragment.SETTING_TITLE, "设置密码");
                }
                fragment  = new PayFragment();
                fragment.setArguments(bundle);
                fragment.setPaySuccessCallBack(WithdrawActivity.this);
                fragment.show(getSupportFragmentManager(), "Pay");
            }
        });
    }
    @Override
    public void onSettingCallBack(String title) {
        Intent intent = new Intent(this, SetPayPwdActivity.class);
        intent.putExtra("title",title);
        startActivity(intent);
    }

    class WithdrawWayBean {
        int img;
        String title;
        boolean isCheck;
        int type;

        public WithdrawWayBean(int img, String title, boolean isCheck, int type) {
            this.img = img;
            this.title = title;
            this.isCheck = isCheck;
            this.type = type;
        }
    }

    private void showChooseWayDialog() {
        Dialog bottomDialog;

        //创建dialog,同时设置dialog主题
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        //绘制dialog  UI视图
        View contentView = LayoutInflater.from(this).inflate(R.layout.withdraw_way, null);

        RelativeLayout rlAli = contentView.findViewById(R.id.rl_ali);
        RelativeLayout rlwx = contentView.findViewById(R.id.rl_wx);
        RelativeLayout rlBanl = contentView.findViewById(R.id.rl_bank);
        LinearLayout llAdd = contentView.findViewById(R.id.ll_add_bank);
        ImageView ivAli = contentView.findViewById(R.id.iv_ali);
        ImageView ivwx = contentView.findViewById(R.id.iv_wx);
        ImageView ivBank = contentView.findViewById(R.id.iv_bank);
        ImageView ivClose = contentView.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });
        if(selectWayType == 1){
            ivAli.setImageResource(R.drawable.checked);
            ivwx.setImageResource(R.drawable.check);

        }else if(selectWayType==2){
            ivwx.setImageResource(R.drawable.checked);
            ivAli.setImageResource(R.drawable.check);
        }
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
                selectWayType = 1;
                ivWay.setImageResource(R.drawable.pay_ali);
                tvWay.setText("支付宝");
                ivAli.setImageResource(R.drawable.checked);
                ivwx.setImageResource(R.drawable.check);
                ivBank.setImageResource(R.drawable.check);
                bottomDialog.dismiss();

            }
        }); rlwx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectWayType = 2;
                ivWay.setImageResource(R.drawable.pay_wx);
                tvWay.setText("微信");
                ivwx.setImageResource(R.drawable.checked);
                ivAli.setImageResource(R.drawable.check);
                ivBank.setImageResource(R.drawable.check);
                bottomDialog.dismiss();
            }
        });
        rlBanl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(WithdrawActivity.this,"暂未开放");
            }
        });llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(WithdrawActivity.this,"暂未开放");
            }
        });

    }



}
