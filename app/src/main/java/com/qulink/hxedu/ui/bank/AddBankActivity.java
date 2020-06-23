package com.qulink.hxedu.ui.bank;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qulink.hxedu.R;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class AddBankActivity extends BaseActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_number)
    EditText etNumber;
    @BindView(R.id.et_bank_name)
    EditText etBankName;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.tv_user_rule)
    TextView tvUserRule;
    @BindView(R.id.ll_user_rule)
    LinearLayout llUserRule;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.iv_rule)
    ImageView ivRule;

    private boolean agreeRule = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_add_bank;
    }

    @Override
    protected void init() {
        setTitle("添加银行卡");
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private void next() {

        if (TextUtils.isEmpty(etName.getText().toString())) {
            ToastUtils.show(this, "请输入持卡人姓名");
            return;
        }
        if (TextUtils.isEmpty(etNumber.getText().toString())) {
            ToastUtils.show(this, "请输入银行卡号");
            return;
        }
        if (TextUtils.isEmpty(etBankName.getText().toString())) {
            ToastUtils.show(this, "请输入开户行");
            return;
        }
        if (TextUtils.isEmpty(etPhone.getText().toString())) {
            ToastUtils.show(this, "请输入银行卡绑定的手机号");
            return;
        }
        if(!agreeRule){
            ToastUtils.show(this, "请阅读并勾选用户协议");
         return;
        }

    }

    @OnClick({R.id.ll_user_rule, R.id.tv_next,R.id.iv_rule, R.id.tv_user_rule})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_user_rule:
                break;
            case R.id.tv_next:
                next();
                break;
            case R.id.iv_rule:
                if(agreeRule){
                    agreeRule = false;
                    ivRule.setImageResource(R.drawable.check);
                }else {
                    agreeRule = true;
                    ivRule.setImageResource(R.drawable.checked);
                }
                break;
            case R.id.tv_user_rule:
                DialogUtil.showRuleDialog(this,"用户协议","谁他妈知道是啥");
                break;
        }
    }


}
