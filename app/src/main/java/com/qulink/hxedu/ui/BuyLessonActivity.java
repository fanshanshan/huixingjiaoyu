package com.qulink.hxedu.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.pay.PayResult;
import com.qulink.hxedu.pay.PayResultDetail;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ToastUtils;

import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;

public class BuyLessonActivity extends BaseActivity {

    @BindView(R.id.iv_img)
    RoundedImageView ivImg;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_lesson_title)
    TextView tvLessonTitle;

    private boolean withResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_buy_lesson;
    }




    @Override
    protected void init() {
        withResult = getIntent().getBooleanExtra("withResult",false);
        setTitle(getString(R.string.sure_order));
        tvLessonTitle.setText("先定个小目标比方说整他一个亿");
        Glide.with(this).load("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1774576164,138926049&fm=26&gp=0.jpg").into(ivImg);
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick(R.id.tv_buy)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.tv_buy:
                break;
        }
    }

}
