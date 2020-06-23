package com.qulink.hxedu.ui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.LevelInfoBean;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.util.ViewUtils;
import com.qulink.hxedu.view.MyScrollView;
import com.qulink.hxedu.view.tablayout.CircularProgressView;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class LevelInfoActivity extends BaseActivity {

    @BindView(R.id.tv_registe_days)
    TextView tvRegisteDays;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.tv_badge_range)
    TextView tvBadgeRange;
    @BindView(R.id.iv_headimg)
    CircleImageView ivHeadimg;
    @BindView(R.id.sc)
    MyScrollView sc;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.tv_bar_right)
    TextView tvBarRight;
    @BindView(R.id.ll_bar)
    LinearLayout llBar;
    @BindView(R.id.tv_now_level)
    TextView tvNowLevel;
    @BindView(R.id.tv_need_credit)
    TextView tvNeedCredit;
    @BindView(R.id.progress)
    CircularProgressView progress;
    @BindView(R.id.tv_today_credit)
    TextView tvTodayCredit;
    @BindView(R.id.tv_today_credit2)
    TextView tvTodayCredit2;
    @BindView(R.id.tv_video_credit)
    TextView tvVideoCredit;
    @BindView(R.id.tv_topic_credit)
    TextView tvTopicCredit;
    private LevelInfoBean levelInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_level_info;
    }

    @Override
    protected void init() {
        setTitle("等级");
        setBackImg(R.drawable.back_white2);
        setBarBg(ContextCompat.getColor(this, R.color.white_transparent));
        setBarTtxtColors(ContextCompat.getColor(this, R.color.white));
        getLevelInfo();

    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private void dealData() {
        if (levelInfoBean == null) {
            return;
        }
        App.getInstance().getDefaultSetting(this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                Glide.with(LevelInfoActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), levelInfoBean.getHeadImg())).into(ivHeadimg);
            }
        });
        tvLevel.setBackgroundResource(ViewUtils.getLevelBgByLevel(levelInfoBean.getLevel()));
        tvLevel.setText("Lv" + levelInfoBean.getLevel());
        tvRegisteDays.setText("已加入慧行" + levelInfoBean.getRegisterDays() + "天");
        tvBadgeRange.setText(levelInfoBean.getPercentage() + "%");
        tvNowLevel.setText(levelInfoBean.getLevel() + "");
        tvNeedCredit.setText(levelInfoBean.getNeedExperience() + "");
        tvTodayCredit2.setText(levelInfoBean.getViewedVideoExperience()+levelInfoBean.getTopicsExperience()+"");
        tvTodayCredit.setText(levelInfoBean.getViewedVideoExperience()+levelInfoBean.getTopicsExperience()+"");
        tvVideoCredit.setText("+"+levelInfoBean.getViewedVideoExperience());
        tvTopicCredit.setText("+"+levelInfoBean.getTopicsExperience());
        progress.setProgress(Integer.parseInt(levelInfoBean.getPercentage()));

    }

    private void getLevelInfo() {
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().getLevelInfo(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(LevelInfoActivity.this);
                levelInfoBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), LevelInfoBean.class);
                dealData();
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(LevelInfoActivity.this);
                ToastUtils.show(LevelInfoActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(LevelInfoActivity.this);
                ToastUtils.show(LevelInfoActivity.this, expectionMsg);
            }
        });
    }
}
