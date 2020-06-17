package com.qulink.hxedu.ui.auth;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;

import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ToastUtils;

import butterknife.OnClick;

public class RealAuthActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_real_auth;
    }

    @Override
    protected void init() {

    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick(R.id.tv_submit)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_submit:
                realAuth();
                break;
        }
    }
    private void realAuth(){
        DialogUtil.showLoading(this,true);
        ApiUtils.getInstance().realAuth("刘宁山", "610502199503246016", "1", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(RealAuthActivity.this);
                DialogUtil.showAlertDialog(RealAuthActivity.this, "提示", "恭喜您，实名认证成功", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getInstance().getUserInfo(RealAuthActivity.this, new UserInfoCallback() {
                            @Override
                            public void getUserInfo(UserInfo userInfo) {
                                userInfo.setRealAuthStatus(1);
                            }
                        });
                        dialog.dismiss();
                        finish();
                    }
                });
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(RealAuthActivity.this);
                ToastUtils.show(RealAuthActivity.this,msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(RealAuthActivity.this);
                ToastUtils.show(RealAuthActivity.this,expectionMsg);

            }
        });
    }
}
