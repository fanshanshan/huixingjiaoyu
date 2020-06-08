package com.qulink.hxedu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
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

import butterknife.BindView;
import butterknife.OnClick;

public class BindPhoneActivity extends BaseActivity {


    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verify)
    EditText etVerify;
    @BindView(R.id.tv_get_smscode)
    TextView tvGetSmscode;
    @BindView(R.id.et_invite_code)
    EditText etInviteCode;
    private int smsCodeEndTime = 60;

    String token;
    private String endTimeFormat = "重新发送 %d s";

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    smsCodeEndTime--;
                    if (smsCodeEndTime == 0) {
                        smsCodeEndTime = 60;
                        tvGetSmscode.setText(getString(R.string.get_smscode));
                        tvGetSmscode.setTextColor(ContextCompat.getColor(BindPhoneActivity.this, R.color.theme_color));
                    } else {
                        tvGetSmscode.setText(String.format(endTimeFormat, smsCodeEndTime));
                        handler.sendEmptyMessageDelayed(0, 1000);
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
        return R.layout.activity_bind_phone;
    }

    @Override
    protected void init() {
        token = getIntent().getStringExtra("token");

    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick(R.id.tv_login)
    public void onViewClicked() {
    }

    @OnClick({R.id.tv_get_smscode, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_smscode:
                sendSmscode();
                break;
            case R.id.tv_login:
                bindPhone();
                break;
        }
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


        ApiUtils.getInstance().sendSmsCode(etPhone.getText().toString(),"7", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                ToastUtils.show(BindPhoneActivity.this,getString(R.string.send_success));
                DialogUtil.hideLoading(BindPhoneActivity.this);
                etVerify.setText("");
                tvGetSmscode.setText( String.format(endTimeFormat, smsCodeEndTime));
                tvGetSmscode.setTextColor(0xff666666);
                handler.sendEmptyMessageDelayed(0,1000);
            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(BindPhoneActivity.this, msg);
                DialogUtil.hideLoading(BindPhoneActivity.this);

            }

            @Override
            public void expcetion(String e) {
                DialogUtil.hideLoading(BindPhoneActivity.this);

            }
        });
    }


    void bindPhone(){
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

        DialogUtil.showLoading(this, true);

        ApiUtils.getInstance().bindPhone(etPhone.getText().toString(), etVerify.getText().toString(), etInviteCode.getText().toString(), "7", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(BindPhoneActivity.this);
                ToastUtils.show(BindPhoneActivity.this,getString(R.string.bind_success));
                Intent intent = new Intent();
                intent.putExtra("token",token);
                setResult(RESULT_OK,intent);
                finish();
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(BindPhoneActivity.this);

                ToastUtils.show(BindPhoneActivity.this,msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(BindPhoneActivity.this);
                ToastUtils.show(BindPhoneActivity.this,expectionMsg);

            }
        });
    }
}
