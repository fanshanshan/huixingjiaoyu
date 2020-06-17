package com.qulink.hxedu.ui.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.qulink.hxedu.App;
import com.qulink.hxedu.MyActivityManager;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.RegexUtil;
import com.qulink.hxedu.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class EditPhoneActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_verify)
    EditText etVerify;
    @BindView(R.id.tv_get_smscode)
    TextView tvGetSmscode;
    @BindView(R.id.tv_login)
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
                        tvGetSmscode.setTextColor(ContextCompat.getColor(EditPhoneActivity.this, R.color.theme_color));
                    } else {
                        tvGetSmscode.setText(String.format(endTimeFormat, smsCodeEndTime));
                        handler.sendEmptyMessageDelayed(0, 1000);
                    }
                    break;
            }
        }
    };
    @Override
    protected int getLayout() {
        return R.layout.activity_edit_phone;
    }

    @Override
    protected void init() {
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                etPhone.setText(userInfo.getPhone());
            }
        });
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick({R.id.tv_get_smscode, R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_get_smscode:
                sendSmscode();
                break;
            case R.id.tv_login:
                next();
                break;
        }
    }
    private void next(){
        if(TextUtils.isEmpty(etVerify.getText().toString())){
            ToastUtils.show(this,"请输入验证码");
            return;
        }
        DialogUtil.showLoading(this,true);
        ApiUtils.getInstance().verifyOldPhoneCode(etPhone.getText().toString(), etVerify.getText().toString(), new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(EditPhoneActivity.this);
                Intent intent = new Intent(EditPhoneActivity.this,EditPhoneNextActivity.class);
                intent.putExtra("code",etVerify.getText().toString());
                MyActivityManager.getInstance().pushActivity(EditPhoneActivity.this);
                startActivity(intent);
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(EditPhoneActivity.this);
                ToastUtils.show(EditPhoneActivity.this,msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(EditPhoneActivity.this);
ToastUtils.show(EditPhoneActivity.this,expectionMsg);
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


        ApiUtils.getInstance().sendSmsCode(etPhone.getText().toString(),"5", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(EditPhoneActivity.this);
                etVerify.setText("");
                tvGetSmscode.setText( String.format(endTimeFormat, smsCodeEndTime));
                tvGetSmscode.setTextColor(0xff666666);
                handler.sendEmptyMessageDelayed(0,1000);
            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(EditPhoneActivity.this, msg);
                DialogUtil.hideLoading(EditPhoneActivity.this);

            }

            @Override
            public void expcetion(String e) {
                DialogUtil.hideLoading(EditPhoneActivity.this);

            }
        });
    }
}
