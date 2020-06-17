package com.qulink.hxedu.ui.msg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.ZongMsgSubBean;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.zone.ArticalDetailActivity;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class ZoneMsgSubPageActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.recycle)
    EmptyRecyclerView recycle;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;

    private String title;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_zone_msg_sub_page;
    }

    @Override
    protected void init() {
        title = getIntent().getStringExtra("title");
        type = getIntent().getIntExtra("type", 0);
        if (title == null) {
            title = "";
        }
        smartLayout.setOnRefreshListener(this);
        smartLayout.setOnLoadMoreListener(this);
        setTitle(title);
        smartLayout.autoRefresh();
        initRecycle();
        getData();
    }

    private void initRecycle() {
        data = new ArrayList<>();
        recycle.setAdapter(new CommonRcvAdapter<ZongMsgSubBean>(data) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setEmptyView(llEmpty);
    }

    class Item implements AdapterItem<ZongMsgSubBean> {
        @BindView(R.id.iv_img)
        CircleImageView ivImg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.v_line)
        View vLine;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        @Override
        public int getLayoutResId() {
            return R.layout.sub_zong_msg_item;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            ButterKnife.bind(this,root);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(ZongMsgSubBean zongMsgSubBean, int position) {
            App.getInstance().getDefaultSetting(ZoneMsgSubPageActivity.this, new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(ZoneMsgSubPageActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(),zongMsgSubBean.getHeadImg())).into(ivImg);

                }
            });

            tvName.setText(zongMsgSubBean.getNickname());
            tvTime.setText(zongMsgSubBean.getCreateTime());
            tvContent.setText(zongMsgSubBean.getRemark());
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ZoneMsgSubPageActivity.this, ArticalDetailActivity.class);
                    intent.putExtra("id",zongMsgSubBean.getArticleId());
                    startActivity(intent);
                }
            });
        }


    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private List<ZongMsgSubBean> data;

    private int pageNo;
    private int pageSize = FinalValue.limit;

    private void getData() {
        smartLayout.setNoMoreData(false);
        pageNo = 1;
        ApiUtils.getInstance().getZoneMsgByType(pageNo, pageSize, type, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishRefresh(true);

                List<ZongMsgSubBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<ZongMsgSubBean>>() {
                }.getType());
                if (hotArticalList != null && !hotArticalList.isEmpty()) {
                    if (hotArticalList.size() < pageSize) {
                        smartLayout.setNoMoreData(true);
                    }
                    data.clear();
                    data.addAll(hotArticalList);
                    recycle.getAdapter().notifyDataSetChanged();
                } else {
                    smartLayout.setNoMoreData(true);
                }
            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishRefresh(false);
                ToastUtils.show(ZoneMsgSubPageActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishRefresh(false);
                ToastUtils.show(ZoneMsgSubPageActivity.this, expectionMsg);

            }
        });

    }

    private void loadmore() {
        pageNo++;
        ApiUtils.getInstance().getZoneMsgByType(pageNo, pageSize, type, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<ZongMsgSubBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<ZongMsgSubBean>>() {
                }.getType());
                if (hotArticalList != null && !hotArticalList.isEmpty()) {
                    if (hotArticalList.size() < pageSize) {
                        smartLayout.setNoMoreData(true);
                    }
                    data.addAll(hotArticalList);
                    recycle.getAdapter().notifyDataSetChanged();
                } else {
                    smartLayout.setNoMoreData(true);
                }
                smartLayout.finishLoadMore(true);
            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishLoadMore(false);
            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishLoadMore(false);

            }
        });

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadmore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }
}
