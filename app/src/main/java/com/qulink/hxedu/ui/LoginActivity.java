package com.qulink.hxedu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.qulink.hxedu.App;
import com.qulink.hxedu.MainActivity;
import com.qulink.hxedu.MyActivityManager;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.TokenInfo;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.PrefUtils;
import com.qulink.hxedu.util.RegexUtil;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.tv_get_smscode)
    TextView tvGetSmscode;
    @BindView(R.id.tv_pwd_login)
    TextView tvPwdLogin;

    @BindView(R.id.tv_registe)
    TextView tvRegiste;
    @BindView(R.id.status)
    TextView status;
    private int REGISTE_RESULT_CODE = 5233;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verify)
    EditText etVerify;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.iv_wx_login)
    ImageView ivWxLogin;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private int smsCodeEndTime = 60;

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
                        tvGetSmscode.setTextColor(ContextCompat.getColor(LoginActivity.this, R.color.theme_color));
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
        return R.layout.activity_login;
    }

    @Override
    protected void init() {

    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick({R.id.btn_login, R.id.iv_wx_login, R.id.tv_registe, R.id.tv_pwd_login, R.id.tv_get_smscode})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
            case R.id.iv_wx_login:
                break;
            case R.id.tv_registe:
                RouteUtil.startNewActivityAndResult(this, new Intent(this, RegisteActivity.class), REGISTE_RESULT_CODE);
                break;
            case R.id.tv_get_smscode:
                sendSmscode();
                break;
            case R.id.tv_pwd_login:
                MyActivityManager.getInstance().pushActivity(this);
                RouteUtil.startNewActivity(this,new Intent(this,PwdLoginActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REGISTE_RESULT_CODE) {
            if (data != null) {
                etPhone.setText(data.getStringExtra("phone"));
                etPhone.setSelection(data.getStringExtra("phone").length());
            }
        }
    }

    void login(){
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
        ApiUtils.getInstance().loginBySmsCode(etPhone.getText().toString(), etVerify.getText().toString(), new ApiCallback() {
            @Override
            public void success(ResponseData data) {
                DialogUtil.hideLoading(LoginActivity.this);
                ToastUtils.show(LoginActivity.this,getString(R.string.login_success));
                TokenInfo tokenInfo = GsonUtil.GsonToBean(data.getData().toString(),TokenInfo.class);
                App.getInstance().setTokenInfo(tokenInfo);
                PrefUtils.saveToken(LoginActivity.this,tokenInfo);
                MyActivityManager.getInstance().pushActivity(LoginActivity.this);
                RouteUtil.startNewActivity(LoginActivity.this,new Intent(LoginActivity.this, MainActivity.class));
                MyActivityManager.getInstance().popAllActivitys();
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(LoginActivity.this);
                ToastUtils.show(LoginActivity.this,msg);
            }

            @Override
            public void expcetion(String e) {
                DialogUtil.hideLoading(LoginActivity.this);
                ToastUtils.show(LoginActivity.this,e);
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


        ApiUtils.getInstance().sendSmsCode(etPhone.getText().toString(),"2",  new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(LoginActivity.this);
                etVerify.setText("");
                tvGetSmscode.setText( String.format(endTimeFormat, smsCodeEndTime));
                tvGetSmscode.setTextColor(0xff666666);
                handler.sendEmptyMessageDelayed(0,1000);
            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(LoginActivity.this, msg);
                DialogUtil.hideLoading(LoginActivity.this);

            }

            @Override
            public void expcetion(String e) {
                DialogUtil.hideLoading(LoginActivity.this);

            }
        });
    }


}
