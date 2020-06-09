package com.qulink.hxedu.ui.zone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
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
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.ImagesPreviewActivity;
import com.qulink.hxedu.ui.fragment.ZoneFragment;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import kale.adapter.util.IAdapter;

public class TopTopicActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.tv_topic_name)
    TextView tvTopicName;
    @BindView(R.id.tv_topic_join_num)
    TextView tvTopicJoinNum;
    @BindView(R.id.recycle)
    EmptyRecyclerView recycle;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    private String topicName;
    private int topicJoinNum;
    private int topicId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_top_topic;
    }

    @Override
    protected void init() {
        smartLayout.setOnRefreshListener(this);
        smartLayout.setOnLoadMoreListener(this);
        setTitle(getString(R.string.top_topic_title));
        topicName = getIntent().getStringExtra("name");
        if (topicName == null) {
            topicName = "";
        }
        topicJoinNum = getIntent().getIntExtra("num", 0);
        topicId = getIntent().getIntExtra("id", 0);
        tvTopicName.setText(topicName);

        tvTopicJoinNum.setText("参与讨论：" + topicJoinNum);
        smartLayout.autoRefresh();
        initData();
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    void initPicRecycle(List<PicBean> list) {
        recycle.setAdapter(new CommonRcvAdapter<PicBean>(list) {


            @NonNull
            @Override
            public AdapterItem createItem(Object type) {


                return new TopicItem();
            }
        });
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setEmptyView(findViewById(R.id.ll_empty));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadmore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initData();
    }

    class TopicItem implements AdapterItem<PicBean> {
        TextView tvName;
        TextView tvCommentNum;
        TextView tvLikeNum;
        TextView tvItem;
        ExpandableTextView expandableTextView;
        RecyclerView imgRecycleview;
        ImageView ivHeadimg;
        LinearLayout levelContanier;

        @Override
        public int getLayoutResId() {
            return R.layout.subject_share_item;
        }

        @Override
        public void bindViews(@NonNull View root) {

            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            tvName = root.findViewById(R.id.tv_name);
            tvCommentNum = root.findViewById(R.id.tv_comment_num);
            tvLikeNum = root.findViewById(R.id.tv_like_num);
            expandableTextView = root.findViewById(R.id.content);
            tvItem = root.findViewById(R.id.tv_item);
            ivHeadimg = root.findViewById(R.id.iv_headimg);
            imgRecycleview = root.findViewById(R.id.recycle_img);
            levelContanier = root.findViewById(R.id.ll_level_contanier);

        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(final PicBean shareContentBean, int position) {
            if (!shareContentBean.isInitMaster()) {
                if (shareContentBean.getPicMaster() == null) {
                    shareContentBean.setInitMaster(true);
                    getPicMaster(shareContentBean.getUserId());
                    //   mPresenter.getPicMaster(shareContentBean.getUserId());
                } else {
                    tvName.setText(shareContentBean.getPicMaster().getNickname() + "");
                    App.getInstance().getDefaultSetting(TopTopicActivity.this, new DefaultSettingCallback() {
                        @Override
                        public void getDefaultSetting(DefaultSetting defaultSetting) {
                            Glide.with(TopTopicActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), shareContentBean.getPicMaster().getHeadImg())).into(ivHeadimg);
                        }
                    });
                    if (CourseUtil.isOk(shareContentBean.getPicMaster().getStatus())) {
                        ImageView imageView = new ImageView(TopTopicActivity.this);
                        imageView.setImageResource(R.drawable.hg);
                        levelContanier.addView(imageView);
                    }
                    if (!shareContentBean.getPicMaster().getBadge().isEmpty()) {

                        ImageView imageView = new ImageView(TopTopicActivity.this);
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.leftMargin = 6;
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageResource(R.drawable.xunzhang);
                        levelContanier.addView(imageView);
                    }
                }
            }
            tvLikeNum.setText(shareContentBean.getThumbs() + "");
            tvCommentNum.setText(shareContentBean.getComments() + "");
            expandableTextView.setContent(shareContentBean.getContent());
            levelContanier.removeAllViews();


            if (!TextUtils.isEmpty(shareContentBean.getTopicName())) {
                tvItem.setText(shareContentBean.getTopicName());
                tvItem.setVisibility(View.VISIBLE);
            } else {
                tvItem.setVisibility(View.GONE);

            }


            if (!TextUtils.isEmpty(shareContentBean.getImgPath())) {
                App.getInstance().getDefaultSetting(TopTopicActivity.this, new DefaultSettingCallback() {
                    @Override
                    public void getDefaultSetting(DefaultSetting defaultSetting) {
                        final ArrayList<String> imgLig = new ArrayList<>();
                        String[] strings = shareContentBean.getImgPath().split(",");
                        for (String s : strings) {
                            s = ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), s);
                            imgLig.add(s);
                        }

                        imgRecycleview.setAdapter(new CommonRcvAdapter<String>(imgLig) {

                            ImageView iv;

                            @NonNull
                            @Override
                            public AdapterItem createItem(Object type) {
                                return new AdapterItem<String>() {
                                    @Override
                                    public int getLayoutResId() {
                                        return R.layout.imageview_item;
                                    }

                                    @Override
                                    public void bindViews(@NonNull View root) {
                                        iv = root.findViewById(R.id.iv);
                                    }

                                    @Override
                                    public void setViews() {


                                    }

                                    @Override
                                    public void handleData(String o, int position) {
                                        iv.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(TopTopicActivity.this, ImagesPreviewActivity.class);
                                                intent.putExtra("position", position);
                                                intent.putStringArrayListExtra("data", imgLig);
                                                startActivity(intent);
                                            }
                                        });
                                        try {
                                            Glide.with(TopTopicActivity.this).load(o).into(iv);

                                        } catch (Exception e) {
                                        }
                                    }
                                };
                            }
                        });
                        imgRecycleview.setLayoutManager(new GridLayoutManager(TopTopicActivity.this, 3));

                    }
                });
            }


        }
    }

    private int pageNo;
    private int pageSize = FinalValue.limit;

    void initData() {
        pageNo = 1;
        smartLayout.setNoMoreData(false);
        ApiUtils.getInstance().getTopPic(pageNo, pageSize, topicName, topicId + "", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishRefresh(true);
                List<PicBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<PicBean>>() {
                }.getType());

                if (hotArticalList.size() < pageSize) {
                    smartLayout.setNoMoreData(true);
                }
                initPicRecycle(hotArticalList);

            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishRefresh(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishRefresh(false);

            }
        });
    }

    void loadmore() {
        pageNo++;

        ApiUtils.getInstance().getTopPic(pageNo, pageSize, topicName, topicId + "", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishLoadMore(true);
                List<PicBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<PicBean>>() {
                }.getType());
                if (hotArticalList.size() < pageSize) {
                    smartLayout.setNoMoreData(true);
                }

                if (!hotArticalList.isEmpty()) {
                    List<PicBean> list = ((IAdapter<PicBean>) recycle.getAdapter()).getData();
                    list.addAll(hotArticalList);
                    ((IAdapter<PicBean>) recycle.getAdapter()).setData(list);
                    recycle.getAdapter().notifyDataSetChanged();
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


    private void getPicMaster(int userId){
        ApiUtils.getInstance().getPicMasterById(userId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<PicMaster> hotArticalList =new Gson().fromJson(GsonUtil.GsonString(t.getData()),new TypeToken<List<PicMaster>>() {}.getType());
                if(hotArticalList.size()>0){
                    dealGetMasterSuc(hotArticalList.get(0));
                }
            }

            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        });
    }

    private void dealGetMasterSuc(PicMaster picMaster){
        List<PicBean> list = ((IAdapter<PicBean>) recycle.getAdapter()).getData();
        for (PicBean p : list) {
            if (p.getUserId() == picMaster.getUserId()) {
                p.setPicMaster(picMaster);
                p.setInitMaster(false);
            }
        }
        recycle.getAdapter().notifyDataSetChanged();
    }
}
