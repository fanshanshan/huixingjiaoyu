package com.qulink.hxedu.ui.auth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.qulink.hxedu.MyActivityManager;
import com.qulink.hxedu.R;
import com.qulink.hxedu.ui.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

public class NoRealAuthActivity extends BaseActivity {

    @BindView(R.id.tv_complete)
    TextView tvComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_no_real_auth;
    }

    @Override
    protected void init() {
        setTitle("实名认证");
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick({R.id.ll_bar, R.id.tv_complete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_bar:
                break;
            case R.id.tv_complete:
                startActivity(new Intent(NoRealAuthActivity.this,RealAuthActivity.class));
                finish();
                break;
        }
    }
}
