package com.qulink.hxedu.ui.score;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.ShopBean;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.course.CourseDetailActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.qulink.hxedu.view.SpacesItemDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class ScoreShopActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.tv_score)
    TextView tvScore;
    @BindView(R.id.recycle)
    EmptyRecyclerView recycle;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_score_shop;
    }

    @Override
    protected void init() {

        setTitle("积分商城");
        setBarBg(ContextCompat.getColor(this, R.color.white_transparent));
        setBackImg(R.drawable.back_white2);
        setBarTtxtColors(ContextCompat.getColor(this, R.color.white));
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                tvScore.setText(userInfo.getCredit() + "");

            }
        });
        smartLayout.setOnRefreshListener(this);
        smartLayout.setOnLoadMoreListener(this);
        initRecycle();
        getData();
    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }

    private int pageNo;
    private int pageSize = FinalValue.limit;
    private List<ShopBean.RecordsBean> data;

    private void initRecycle() {
        data = new ArrayList<>();
        recycle.setAdapter(new CommonRcvAdapter<ShopBean.RecordsBean>(data) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycle.addItemDecoration(new SpacesItemDecoration(32, 32, 0, 0));
        recycle.setLayoutManager(new GridLayoutManager(this, 2));
        recycle.setEmptyView(findViewById(R.id.ll_empty));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        loadMore();
    }

    class Item implements AdapterItem<ShopBean.RecordsBean> {
        @BindView(R.id.iv_img)
        RoundedImageView ivImg;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_new_price)
        TextView tvNewPrice;
        @BindView(R.id.ll_new_price)
        LinearLayout llNewPrice;
        @BindView(R.id.tv_old_price)
        TextView tvOldPrice;
        @BindView(R.id.iv_vip)
        ImageView ivVip;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        @Override
        public int getLayoutResId() {
            return R.layout.shop_item;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ButterKnife.bind(this, root);
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(ShopBean.RecordsBean shopBean, int position) {
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ScoreShopActivity.this, ShopCourseDetailActivity.class);
                    intent.putExtra("courseId",shopBean.getId());
                    RouteUtil.startNewActivity(ScoreShopActivity.this, intent);

                }
            });
            App.getInstance().getDefaultSetting(ScoreShopActivity.this, new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(ScoreShopActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), shopBean.getCurriculumImage())).into(ivImg);

                    tvTitle.setText(shopBean.getCurriculumName());

                    if (shopBean.getExchangeType() == 1) {
                        //积分
                        tvNewPrice.setText(shopBean.getIntegration() + "");
                    } else if (shopBean.getExchangeType() == 2) {
                        if (shopBean.getPayAmount() != shopBean.getVipAmount()) {
                            ivVip.setImageResource(R.drawable.vip2);
                            tvOldPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG );
                            tvOldPrice.setText(shopBean.getIntegration() + "+￥" + shopBean.getPayAmount());
                            tvNewPrice.setText(shopBean.getIntegration() + "+￥" + shopBean.getVipAmount());
                        } else {
                            tvNewPrice.setText(shopBean.getIntegration() + "+￥" + shopBean.getPayAmount());

                        }
                    }

                }
            });
        }
    }

    private void getData() {
        DialogUtil.showLoading(this, true);
        pageNo = 1;
        smartLayout.setNoMoreData(false);
        ApiUtils.getInstance().scoreShopList(pageNo, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(ScoreShopActivity.this);
                smartLayout.finishRefresh(true);
                ShopBean shopBean = new Gson().fromJson(GsonUtil.GsonString(t.getData()), ShopBean.class);
                if (shopBean.getRecords() != null && !shopBean.getRecords().isEmpty()) {
                    data.clear();
                    data.addAll(shopBean.getRecords());
                    recycle.getAdapter().notifyDataSetChanged();
                    if (shopBean.getRecords().size() < pageSize) {
                        smartLayout.setNoMoreData(true);
                    }
                } else {
                    smartLayout.setNoMoreData(true);

                }
            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishRefresh(true);
                DialogUtil.hideLoading(ScoreShopActivity.this);
                ToastUtils.show(ScoreShopActivity.this, msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishRefresh(true);
                DialogUtil.hideLoading(ScoreShopActivity.this);
                ToastUtils.show(ScoreShopActivity.this, expectionMsg);
            }
        });
    }

    private void loadMore() {
        pageNo++;
        ApiUtils.getInstance().scoreShopList(pageNo, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(ScoreShopActivity.this);
                smartLayout.finishLoadMore(true);
                ShopBean shopBean = new Gson().fromJson(GsonUtil.GsonString(t.getData()), ShopBean.class);
                if (shopBean.getRecords() != null && !shopBean.getRecords().isEmpty()) {
                    data.addAll(shopBean.getRecords());
                    recycle.getAdapter().notifyDataSetChanged();
                    if (shopBean.getRecords().size() < pageSize) {
                        smartLayout.setNoMoreData(true);
                    }
                } else {
                    smartLayout.setNoMoreData(true);

                }
            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishLoadMore(true);
                DialogUtil.hideLoading(ScoreShopActivity.this);
                ToastUtils.show(ScoreShopActivity.this, msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishLoadMore(true);
                DialogUtil.hideLoading(ScoreShopActivity.this);
                ToastUtils.show(ScoreShopActivity.this, expectionMsg);
            }
        });
    }

}
