package com.qulink.hxedu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.qulink.hxedu.App;
import com.qulink.hxedu.MainActivity;
import com.qulink.hxedu.MyActivityManager;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.entity.TokenInfo;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.ui.setting.ForgetPwdActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.RegexUtil;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

public class PwdLoginActivity extends BaseActivity {

    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.tv_forget_pwd)
    TextView tvForgetPwd;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.iv_pwd_visible)
    ImageView ivPwdVisible;


    private boolean showPwd = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_pwd_login;
    }

    @Override
    protected void init() {

    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick({R.id.tv_forget_pwd, R.id.tv_login, R.id.iv_pwd_visible})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_forget_pwd:
                RouteUtil.startNewActivity(this, new Intent(this, ForgetPwdActivity.class));
                break;
            case R.id.tv_login:

                login();
                break;
            case R.id.iv_pwd_visible:
                if (showPwd) {
                    showPwd = false;
                    if (!etPwd.getText().equals("")) {
                        etPwd.setSelection(etPwd.getText().toString().length());
                    }
                    etPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());

                    ivPwdVisible.setImageResource(R.drawable.eye_close);
                } else {
                    showPwd = true;
                    if (!etPwd.getText().equals("")) {
                        etPwd.setSelection(etPwd.getText().toString().length());
                    }
                    etPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());


                    ivPwdVisible.setImageResource(R.drawable.eye);

                }
                break;
        }
    }


    void login(){

        if(etPhone.getText().toString().equals("")){
            ToastUtils.show(this,"请输入手机号");
            return;
        }
        if(RegexUtil.isPhoneNumber(etPhone.getText().toString())){
            ToastUtils.show(this,"请输入正确的手机号");
            return;
        }

        if(etPwd.getText().toString().equals("")){
            ToastUtils.show(this,"请输入密码");
            return;
        }
        if(!RegexUtil.isPwd(etPwd.getText().toString())){
            ToastUtils.show(this,getString(R.string.pwd_error_tip));
            return;
        }
        DialogUtil.showLoading(this,true);
        ApiUtils.getInstance().loginByPwd(etPhone.getText().toString(), etPwd.getText().toString(),  new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(PwdLoginActivity.this);

                TokenInfo tokenInfo = GsonUtil.GsonToBean(t.getData().toString(),TokenInfo.class);
                App.getInstance().loginSuccess(PwdLoginActivity.this,tokenInfo);

                MyActivityManager.getInstance().pushActivity(PwdLoginActivity.this);
                RouteUtil.startNewActivity(PwdLoginActivity.this,new Intent(PwdLoginActivity.this, MainActivity.class));
                MyActivityManager.getInstance().popAllActivitys();

                setJpushAlias();
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(PwdLoginActivity.this);
                ToastUtils.show(PwdLoginActivity.this,msg);

            }

            @Override
            public void expcetion(String e) {
                DialogUtil.hideLoading(PwdLoginActivity.this);
                ToastUtils.show(PwdLoginActivity.this,e);
            }
        });
    }


    private void setJpushAlias(){
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                EventBus.getDefault().post(new MessageEvent(FinalValue.SET_ALIS, userInfo.getId()));
            }
        });
    }
}
