package com.qulink.hxedu.ui;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qulink.hxedu.R;

import butterknife.BindView;
import butterknife.OnClick;

public class BuyLessonActivity extends BaseActivity {

    @BindView(R.id.iv_img)
    RoundedImageView ivImg;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.tv_lesson_title)
    TextView tvLessonTitle;

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
        setTitle(getString(R.string.sure_order));
        tvLessonTitle.setText("先定个小目标比方说整他一个亿");
        Glide.with(this).load("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1774576164,138926049&fm=26&gp=0.jpg").into(ivImg);
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick(R.id.tv_buy)
    public void onViewClicked() {
    }
}
