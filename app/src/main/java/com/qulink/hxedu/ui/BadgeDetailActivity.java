package com.qulink.hxedu.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.BadgeBean;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.MyScrollView;
import com.qulink.hxedu.view.SpacesItemDecoration;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class BadgeDetailActivity extends BaseActivity {

    @BindView(R.id.iv_headimg)
    ImageView ivHeadimg;
    @BindView(R.id.tv_badge_num)
    TextView tvBadgeNum;
    @BindView(R.id.tv_badge_range)
    TextView tvBadgeRange;
    @BindView(R.id.tv_registe_days)
    TextView tvRegisteDays;
    @BindView(R.id.tv_badge_num2)
    TextView tvBadgeNum2;
    @BindView(R.id.recycle)
    RecyclerView recycle;
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
    @BindView(R.id.sc)
    MyScrollView sc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_badge_detail;
    }

    @Override
    protected void init() {
        setTitle("我的勋章");
        setBarBg(ContextCompat.getColor(this, R.color.white_transparent));
        setBackImg(R.drawable.back_white2);
        setBarTtxtColors(ContextCompat.getColor(this, R.color.white));
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {

            }
        });
        initData();
        initBadgeLayoutData();
        addScrollviewListener();
    }

    //scorllview添加滑动监听
    void addScrollviewListener() {
        sc.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                final float ratio = (float) Math.min(Math.max(scrollY, 0), llBar.getHeight()) / llBar.getHeight();
                final int newAlpha = (int) (ratio * 255);
                llBar.setBackgroundColor(Color.argb((int) newAlpha, 53, 134, 249));
            }
        });
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private BadgeBean badgeBean;

    private void dealData() {
        if (badgeBean == null) {
            return;
        }
        App.getInstance().getDefaultSetting(BadgeDetailActivity.this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                Glide.with(BadgeDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), badgeBean.getHeadImg())).error(R.drawable.user_default).into(ivHeadimg);
            }
        });
        tvBadgeNum.setText(badgeBean.getTotal() + "");
        tvBadgeNum2.setText("已获得" + badgeBean.getTotal() + "枚");
        tvBadgeRange.setText(badgeBean.getPercentage() + "%");
        tvRegisteDays.setText("已加入慧行" + badgeBean.getRegisterDays() + "天");
        if (badgeBean.getBadges() != null && !badgeBean.getBadges().isEmpty()) {
            for (BadgeBean.BadgesBean b : badgeBean.getBadges()) {
                for (BadgeLayoutBean badgeLayoutBean : badgeBeanList) {
                    if (badgeLayoutBean.type == b.getType()) {
                        badgeLayoutBean.status = 1;
                        break;
                    }
                }
            }
            recycle.getAdapter().notifyDataSetChanged();
        }
    }

    private void initData() {
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().getBadgeInfo(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(BadgeDetailActivity.this);
                badgeBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), BadgeBean.class);
                dealData();
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(BadgeDetailActivity.this);
                ToastUtils.show(BadgeDetailActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(BadgeDetailActivity.this);
                ToastUtils.show(BadgeDetailActivity.this, expectionMsg);
            }
        });
    }

    private List<BadgeLayoutBean> badgeBeanList;

    private void initBadgeLayoutData() {
        badgeBeanList = new ArrayList<>();
        badgeBeanList.add(new BadgeLayoutBean(R.drawable.hz1h, R.drawable.hz1, "我爱学习", "连续打开30天，获得\"我爱学习\"勋章", 1, 0,"连续打卡30天",30,0,"天"));
        badgeBeanList.add(new BadgeLayoutBean(R.drawable.hz2h, R.drawable.hz2, "学习如风常随我动", "连续打卡90天，获得\"学习如风，常随我动\"勋章", 2, 0,"连续打卡90天",90,0,"天"));
        badgeBeanList.add(new BadgeLayoutBean(R.drawable.hz3h, R.drawable.hz3, "我就是学习本人", "连续打卡180天，获得\"我就是学习本人\"勋章", 3, 0,"连续打卡90天",90,0,"天"));
        badgeBeanList.add(new BadgeLayoutBean(R.drawable.hz4h, R.drawable.hz4, "评论员", "累计发布评论200条，获得\"评论员\"勋章", 4, 0,"发布评论200条",200,0,"条"));
        badgeBeanList.add(new BadgeLayoutBean(R.drawable.hz5h, R.drawable.hz5, "特邀评论员", "累计发布评论1000条，获得\"特邀评论员\"勋章", 5, 0,"发布评论1000条",1000,0,"条"));
        badgeBeanList.add(new BadgeLayoutBean(R.drawable.hz6h, R.drawable.hz6, "特技特邀评论员", "累计发布评论5000条，获得\"特技特邀评论员\"勋章", 6, 0,"发布评论1500条",1500,0,"条"));
        badgeBeanList.add(new BadgeLayoutBean(R.drawable.hz7h, R.drawable.hz7, "冉冉升起的分享之星", "话题被评为热门话题一次，获得\"冉冉升起的分享之星\"勋章", 7, 0,"热门话题一次",1,0,"次"));
        badgeBeanList.add(new BadgeLayoutBean(R.drawable.hz8h, R.drawable.hz8, "分享界的扛把子", "话题被评为热门话题十次，获得\"分享界的扛把子\"勋章", 8, 0,"热门话题十次",10,0,"次"));
        badgeBeanList.add(new BadgeLayoutBean(R.drawable.hz9h, R.drawable.hz9, "分享界的创造之神", "话题被评为热门话题五十次，获得\"分享界的创造之神\"勋章", 9, 0,"热门话题五十次",50,0,"次"));
        recycle.setAdapter(new CommonRcvAdapter<BadgeLayoutBean>(badgeBeanList) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.addItemDecoration(new SpacesItemDecoration(0, 32, 0, 0));
    }

    class Item implements AdapterItem<BadgeLayoutBean> {
        @BindView(R.id.iv_hz)
        ImageView ivHz;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        @Override
        public int getLayoutResId() {
            return R.layout.badge_item;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            ButterKnife.bind(this, root);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(BadgeLayoutBean badgeLayoutBean, int position) {
            if (badgeLayoutBean.status == 1) {
                ivHz.setImageResource(badgeLayoutBean.selectLogo);
            } else {
                ivHz.setImageResource(badgeLayoutBean.normalogo);
            }
            tvTitle.setText(badgeLayoutBean.title);
            tvDesc.setText(badgeLayoutBean.desc);
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getBadgeDetailByType(badgeLayoutBean);
                }
            });
        }
    }

    private void getBadgeDetailByType(BadgeLayoutBean badgeLayoutBean){
        DialogUtil.showLoading(this,true);
        ApiUtils.getInstance().getBadgeInfoByType(badgeLayoutBean.type, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(BadgeDetailActivity.this);
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.GsonString(t.getData()));
                    badgeLayoutBean.current = jsonObject.getInt("currentProg");
                    badgeLayoutBean.getNum = jsonObject.getInt("count");
                    showSignSucDialog(badgeLayoutBean);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(BadgeDetailActivity.this);
                ToastUtils.show(BadgeDetailActivity.this,msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(BadgeDetailActivity.this);
                ToastUtils.show(BadgeDetailActivity.this,expectionMsg);

            }
        });
    }

    void showSignSucDialog(BadgeLayoutBean badgeLayoutBean) {
        View diaView = View.inflate(this, R.layout.badge_dialog, null);
        TextView tv_status_desc = diaView.findViewById(R.id.tv_status_desc);
        TextView tv_get_nums = diaView.findViewById(R.id.tv_get_nums);
        ProgressBar progress_bar_h = diaView.findViewById(R.id.progress_bar_h);
        TextView tv_ok_desc = diaView.findViewById(R.id.tv_ok_desc);
        TextView tv_progress_desc = diaView.findViewById(R.id.tv_progress_desc);
        TextView tv_sb = diaView.findViewById(R.id.tv_sb);
        ImageView iv_logo = diaView.findViewById(R.id.iv_logo);
        tv_get_nums.setText("已有"+badgeLayoutBean.current+"人获得");
        tv_ok_desc.setText(badgeLayoutBean.okDesc);
        tv_status_desc.setText(badgeLayoutBean.status==1?"已获得":"暂未获得");
        if(badgeLayoutBean.status==1){
            iv_logo.setImageResource(badgeLayoutBean.selectLogo);
            tv_sb.setText("你可行啊");
        }else{
            iv_logo.setImageResource(badgeLayoutBean.normalogo);
            tv_sb.setText("请继续努力");
        }
        progress_bar_h.setMax(badgeLayoutBean.max);
        progress_bar_h.setProgress(badgeLayoutBean.current);
        tv_progress_desc.setText("进度："+badgeLayoutBean.current+badgeLayoutBean.unit);
        Dialog dialog = new Dialog(BadgeDetailActivity.this, R.style.my_dialog);
        diaView.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.setContentView(diaView);
        dialog.show();
    }

    class BadgeLayoutBean {
        int normalogo;
        int selectLogo;
        String title;
        String desc;
        int type;
        int status;
        String okDesc;
        int max;
        int current;
        int getNum;
        String unit;

        public BadgeLayoutBean(int normalogo, int selectLogo, String title, String desc, int type, int status, String okDesc, int max, int current,String unit) {
            this.normalogo = normalogo;
            this.selectLogo = selectLogo;
            this.title = title;
            this.desc = desc;
            this.type = type;
            this.status = status;
            this.okDesc = okDesc;
            this.max = max;
            this.current = current;
            this.unit = unit;
        }

        public BadgeLayoutBean(int normalogo, int selectLogo, String title, String desc, int type, int status) {
            this.normalogo = normalogo;
            this.selectLogo = selectLogo;
            this.title = title;
            this.desc = desc;
            this.status = status;
            this.type = type;
        }
    }


}
