package com.qulink.hxedu.ui.setting;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.RegexUtil;
import com.qulink.hxedu.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetPwdActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verify)
    EditText etVerify;
    @BindView(R.id.tv_get_smscode)
    TextView tvGetSmscode;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.iv_pwd_visible)
    ImageView ivPwdVisible;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    String title;
    boolean showPwd = false;
    @BindView(R.id.tv_title)
    TextView tvTitle;
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
                        tvGetSmscode.setTextColor(ContextCompat.getColor(ForgetPwdActivity.this, R.color.theme_color));
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
        return R.layout.activity_forget_pwd;
    }

    @Override
    protected void init() {
        title = getIntent().getStringExtra("title");
        if (title == null) {
            title
                    = "忘记密码";
        }

        tvTitle.setText(title);
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick({R.id.tv_get_smscode, R.id.iv_pwd_visible, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_smscode:
                sendSmscode();
                break;
            case R.id.iv_pwd_visible:
                if (showPwd) {
                    showPwd = false;
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivPwdVisible.setImageResource(R.drawable.eye_close);
                    if (!etPwd.getText().equals("")) {
                        etPwd.setSelection(etPwd.getText().toString().length());
                    }
                } else {
                    showPwd = true;
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivPwdVisible.setImageResource(R.drawable.eye);
                    if (!etPwd.getText().equals("")) {
                        etPwd.setSelection(etPwd.getText().toString().length());
                    }

                }
                break;
            case R.id.tv_login:
                complete();
                break;
        }
    }

    void complete() {
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
        if (etPwd.getText().toString().equals("")) {
            ToastUtils.show(this, "请输入密码");
            return;
        }
        if (!RegexUtil.isPwd(etPwd.getText().toString())) {
            ToastUtils.show(this, getString(R.string.pwd_error_tip));
            return;
        }
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().resetPwd(etPhone.getText().toString(), etVerify.getText().toString(), etPwd.getText().toString(), new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(ForgetPwdActivity.this);
                ToastUtils.show(ForgetPwdActivity.this, "密码修改成功");
                finish();
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(ForgetPwdActivity.this);
                ToastUtils.show(ForgetPwdActivity.this, msg);

            }

            @Override
            public void expcetion(String e) {
                DialogUtil.hideLoading(ForgetPwdActivity.this);
                ToastUtils.show(ForgetPwdActivity.this, e);

            }
        });
    }

    void sendSmscode() {
        if (smsCodeEndTime != 60) {
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


        ApiUtils.getInstance().sendSmsCode(etPhone.getText().toString(), "3", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(ForgetPwdActivity.this);
                etVerify.setText("");
                tvGetSmscode.setText(String.format(endTimeFormat, smsCodeEndTime));
                tvGetSmscode.setTextColor(0xff666666);
                handler.sendEmptyMessageDelayed(0, 1000);
            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(ForgetPwdActivity.this, msg);
                DialogUtil.hideLoading(ForgetPwdActivity.this);

            }

            @Override
            public void expcetion(String e) {
                DialogUtil.hideLoading(ForgetPwdActivity.this);

            }
        });
    }

}
