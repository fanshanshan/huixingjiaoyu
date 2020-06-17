package com.qulink.hxedu.ui.setting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.WithdrawActivity;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class SetPayPwdActivity extends BaseActivity implements PayPwdView.InputCallBack{

    @BindView(R.id.et_verify)
    EditText etVerify;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.iv_pwd_visible)
    ImageView ivPwdVisible;
    @BindView(R.id.et_sure_pwd)
    EditText etSurePwd;
    @BindView(R.id.iv_sure_pwd_visible)
    ImageView ivSurePwdVisible;
    @BindView(R.id.tv_complete)
    TextView tvComplete;
    @BindView(R.id.tv_desc)
    TextView tvDesc;
    private String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_set_pay_pwd;
    }

    @Override
    protected void init() {
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                phone = userInfo.getPhone();
                sendSmsCode();

            }
        });
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private void showpayPwdDialog(){
        Bundle bundle;

        bundle = new Bundle();
        bundle.putString(PayFragment.EXTRA_CONTENT, "");
        bundle.putString(PayFragment.SETTING_TITLE, "");

        fragment  = new PayFragment();
        fragment.setArguments(bundle);
        fragment.setPaySuccessCallBack(SetPayPwdActivity.this);
        fragment.show(getSupportFragmentManager(), "Pay");
    }
    boolean showPwd = false;
    boolean showSurePwd = false;

    PayFragment fragment;
    @OnClick({R.id.iv_pwd_visible, R.id.iv_sure_pwd_visible, R.id.tv_complete,R.id.et_pwd,R.id.et_sure_pwd})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_pwd:
                setType = 1;
                showpayPwdDialog();
                break;case

                    R.id.et_sure_pwd:
                setType = 2;
                showpayPwdDialog();
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
            case R.id.iv_sure_pwd_visible:
                if (showSurePwd) {
                    showSurePwd = false;
                    etSurePwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    ivSurePwdVisible.setImageResource(R.drawable.eye_close);
                    if (!etSurePwd.getText().equals("")) {
                        etSurePwd.setSelection(etSurePwd.getText().toString().length());
                    }
                } else {
                    showSurePwd = true;
                    etSurePwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    ivSurePwdVisible.setImageResource(R.drawable.eye);
                    if (!etSurePwd.getText().equals("")) {
                        etSurePwd.setSelection(etSurePwd.getText().toString().length());
                    }

                }
                break;
            case R.id.tv_complete:
                complete();
                break;
        }
    }

    private void sendSmsCode() {
        DialogUtil.showLoading(this,true);

        ApiUtils.getInstance().sendSmsCode(phone, "4", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(SetPayPwdActivity.this);
                tvDesc.setText("短信验证码已发送至"+phone);
            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(SetPayPwdActivity.this, msg);
                DialogUtil.hideLoading(SetPayPwdActivity.this);
                tvDesc.setText(msg);

            }

            @Override
            public void expcetion(String e) {
                DialogUtil.hideLoading(SetPayPwdActivity.this);

            }
        });
    }
    private void complete(){
        if(TextUtils.isEmpty(etVerify.getText().toString())){
            ToastUtils.show(this,"请输入验证码");
            return;
        }  if(TextUtils.isEmpty(etPwd.getText().toString())){
            ToastUtils.show(this,"请输入支付密码");
            return;
        }  if(TextUtils.isEmpty(etPwd.getText().toString())){
            ToastUtils.show(this,"请再次输入验证码");
            return;
        }if(!etPwd.getText().toString().equals(etSurePwd.getText().toString())){
            ToastUtils.show(this,"两次输入的密码不一致");
            return;
        }
        DialogUtil.showLoading(this,true,"请稍候");
        ApiUtils.getInstance().resetPayPwd(phone, etVerify.getText().toString(), etSurePwd.getText().toString(), new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(SetPayPwdActivity.this);
                DialogUtil.showAlertDialog(SetPayPwdActivity.this, "提示", "支付密码设置成功", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(SetPayPwdActivity.this);
                ToastUtils.show(SetPayPwdActivity.this,msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(SetPayPwdActivity.this);
                ToastUtils.show(SetPayPwdActivity.this,expectionMsg);

            }
        });
    }

    private int setType;
    @Override
    public void onInputFinish(String result) {
        if(fragment!=null){
            fragment.dismiss();
        }
        if(setType==1){
            etPwd.setText(result);
        }else if(setType==2){
            etSurePwd.setText(result);
        }
    }

    @Override
    public void onSettingCallBack(String title) {

    }
}
