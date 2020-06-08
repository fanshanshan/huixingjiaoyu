package com.qulink.hxedu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.RegexUtil;
import com.qulink.hxedu.util.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisteActivity extends BaseActivity {

    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.iv_check)
    ImageView ivCheck;
    @BindView(R.id.tv_xieyi)
    TextView tvXieyi;
    @BindView(R.id.ll_xieyi)
    LinearLayout llXieyi;
    @BindView(R.id.iv_wx_login)
    ImageView ivWxLogin;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verify)
    EditText etVerify;
    @BindView(R.id.et_invite_code)
    EditText etInviteCode;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_send_smd)
    TextView tvSendSmd;

    private boolean agreeXieyi = false;
    private int smsCodeEndTime = 60;

    private String endTimeFormat = "重新发送 %d s";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    smsCodeEndTime--;
                    if(smsCodeEndTime==0){
                        smsCodeEndTime = 60;
                        tvSendSmd.setText(getString(R.string.get_smscode));
                        tvSendSmd.setTextColor(ContextCompat.getColor(RegisteActivity.this,R.color.theme_color));
                    }else{
                        tvSendSmd.setText( String.format(endTimeFormat, smsCodeEndTime));
                        handler.sendEmptyMessageDelayed(0,1000);
                    }
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_registe;
    }

    @Override
    protected void init() {

    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick({R.id.btn_login, R.id.iv_check, R.id.tv_xieyi, R.id.ll_xieyi, R.id.iv_wx_login, R.id.tv_send_smd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                registe();
                break;
            case R.id.tv_xieyi:
                ToastUtils.show(this, "用户协议");
                break;
            case R.id.ll_xieyi:
                if (agreeXieyi) {
                    agreeXieyi = false;
                    ivCheck.setImageResource(R.drawable.check);
                } else {
                    agreeXieyi = true;
                    ivCheck.setImageResource(R.drawable.checked);
                }

                break;
            case R.id.iv_wx_login:
                break;
            case R.id.tv_send_smd:
                sendSmscode();
                break;
        }
    }


    void registe() {

        if(!agreeXieyi){
            ToastUtils.show(this, "请先勾选注册协议");
           return;
        }
        if (etPhone.getText().toString().equals("")) {
            ToastUtils.show(this, "请输入手机号");
            return;
        }
        if (RegexUtil.isPhoneNumber(etPhone.getText().toString())) {
            ToastUtils.show(this, "请输入正确的手机号");
            return;
        }
        if (etVerify.getText().toString().equals("")) {
            ToastUtils.show(this, "请输入验证码");
            return;
        }

        DialogUtil.showLoading(this,true);
        ApiUtils.getInstance().registe(etPhone.getText().toString(), etVerify.getText().toString(),
                etInviteCode.getText().toString(), "1",  new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(RegisteActivity.this);
                ToastUtils.show(RegisteActivity.this,"注册成功");
                Intent intent = new Intent(RegisteActivity.this,LoginActivity.class);
                intent.putExtra("phone",etPhone.getText().toString());
                setResult(RESULT_OK,intent);
                finish();

            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(RegisteActivity.this);

                ToastUtils.show(RegisteActivity.this,msg);
            }

            @Override
            public void expcetion(String e) {
                DialogUtil.hideLoading(RegisteActivity.this);

            }
        });
    }

    void sendSmscode() {
        if(smsCodeEndTime!=60){
            return;
        }
        if (etPhone.getText().toString().equals("")) {
            ToastUtils.show(this, "请输入手机号");
            return;
        }
        if (RegexUtil.isPhoneNumber(etPhone.getText().toString())) {
            ToastUtils.show(this, "请输入正确的手机号");
            return;
        }
        DialogUtil.showLoading(this, true);


        ApiUtils.getInstance().sendSmsCode(etPhone.getText().toString(),"1", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(RegisteActivity.this);
                etVerify.setText("");
                tvSendSmd.setText( String.format(endTimeFormat, smsCodeEndTime));
                tvSendSmd.setTextColor(0xff666666);
                handler.sendEmptyMessageDelayed(0,1000);
            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(RegisteActivity.this, msg);
                DialogUtil.hideLoading(RegisteActivity.this);

            }

            @Override
            public void expcetion(String e) {
                DialogUtil.hideLoading(RegisteActivity.this);

            }
        });
    }
}
