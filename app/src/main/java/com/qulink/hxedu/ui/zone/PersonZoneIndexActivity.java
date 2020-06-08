package com.qulink.hxedu.ui.zone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.PersonZoneIndexBean;
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.mvp.contract.PersonZoneIndexContract;
import com.qulink.hxedu.mvp.presenter.PersonZoneIndexPresenter;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.ImagesPreviewActivity;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.MyScrollView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import kale.adapter.util.IAdapter;

public class PersonZoneIndexActivity extends BaseActivity implements PersonZoneIndexContract.View, OnLoadMoreListener, OnRefreshListener {


    @BindView(R.id.iv_top_img)
    ImageView ivTopImg;
    @BindView(R.id.iv_headimg)
    CircleImageView ivHeadimg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.iv_hg)
    ImageView ivHg;
    @BindView(R.id.iv_xz)
    ImageView ivXz;
    @BindView(R.id.tv_my_topic)
    TextView tvMyTopic;
    @BindView(R.id.tv_join_topic)
    TextView tvJoinTopic;
    @BindView(R.id.recycle_my_topic)
    RecyclerView recycleMyTopic;
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
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    @BindView(R.id.sc)
    MyScrollView sc;
    @BindView(R.id.tv_my_topic_float)
    TextView tvMyTopicFloat;
    @BindView(R.id.tv_join_topic_float)
    TextView tvJoinTopicFloat;
    @BindView(R.id.ll_float)
    LinearLayout llFloat;
    @BindView(R.id.rl_top_bg)
    RelativeLayout rlTopBg;
    @BindView(R.id.ll_tab)
    LinearLayout llTab;
    private PersonZoneIndexPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_person_zone_index;
    }

    @Override
    protected void init() {

        setTitle(getString(R.string.my_topic));
        setRightTitle(getString(R.string.publis));
        setBarTtxtColors(ContextCompat.getColor(this, R.color.white));
        setBarBg(ContextCompat.getColor(this, R.color.white_transparent));
        setBackImg(R.drawable.back_white2);
        smartLayout.setOnLoadMoreListener(this);
        smartLayout.setOnRefreshListener(this);
        mPresenter = new PersonZoneIndexPresenter();
        mPresenter.attachView(this);
        mPresenter.getPersonIndex();
        mPresenter.getMyToPic();
        addScrollviewListener();

    }

    //scorllview添加滑动监听  根据滑动距离改变顶部bar的颜色
    private void addScrollviewListener() {
        sc.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY >(rlTopBg.getHeight()-llTab.getHeight()-status.getHeight())) {
                    if(llFloat.getVisibility()==View.GONE){
                        llFloat.setVisibility(View.VISIBLE);
                    }
                } else {
                    if(llFloat.getVisibility()==View.VISIBLE){
                        llFloat.setVisibility(View.GONE);
                    }
                }
                final float ratio = (float) Math.min(Math.max(scrollY, 0), llBar.getHeight()) / llBar.getHeight();
                final int newAlpha = (int) (ratio * 255);
                llBar.setBackgroundColor(Color.argb((int) newAlpha, 53, 134, 249));

            }
        });
    }

    //初始化我的帖子
    private void initPicRecycle(List<PicBean> list) {
        recycleMyTopic.setAdapter(new CommonRcvAdapter<PicBean>(list) {


            @Override
            public Object getItemType(PicBean picBean) {
                return super.getItemType(picBean);
            }

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {


                return new TopicItem();
            }
        });
        recycleMyTopic.setLayoutManager(new LinearLayoutManager(PersonZoneIndexActivity.this));
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    //获取我的页面信息陈宫
    @Override
    public void getPersonIndexSuc(PersonZoneIndexBean s) {
        dealData(s);
    }

    //获取我的话题成功
    @Override
    public void getMyTopicSuc(List<PicBean> hotArticalList) {
        smartLayout.setNoMoreData(false);

        smartLayout.finishRefresh(true);
        if(hotArticalList.isEmpty()){
            showEmpty();
        }else{
            initPicRecycle(hotArticalList);
        }
    }

    //加载更多话题成功 刷新界面
    @Override
    public void loadmoreTopicSuc(List<PicBean> hotArticalList) {
        List<PicBean> list = ((IAdapter<PicBean>) recycleMyTopic.getAdapter()).getData();
        list.addAll(hotArticalList);
        ((IAdapter<PicBean>) recycleMyTopic.getAdapter()).setData(list);
        recycleMyTopic.getAdapter().notifyDataSetChanged();
        smartLayout.finishLoadMore(true);
    }

    //根据用户id获取用户信息
    @Override
    public void getPicMasterSuc(PicMaster picMaster) {
        List<PicBean> list = ((IAdapter<PicBean>) recycleMyTopic.getAdapter()).getData();
        for (PicBean p : list) {
            if (p.getUserId() == picMaster.getUserId()) {
                p.setPicMaster(picMaster);
                p.setInitMaster(false);
            }
        }
        recycleMyTopic.getAdapter().notifyDataSetChanged();
    }


    @Override
    public void showLoading() {
        DialogUtil.showLoading(this, true);
    }

    @Override
    public void hideLoading() {
        DialogUtil.hideLoading(this);
    }

    @Override
    public void onError(String msg) {
        smartLayout.finishRefresh(false);
        smartLayout.finishLoadMore(false);
        ToastUtils.show(this, msg);
    }

    @Override
    public void onSuccess(ResponseData data) {

    }

    @Override
    public void onExpcetion(String msg) {

    }

    @Override
    public void noMore() {
        smartLayout.setNoMoreData(true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private void dealData(PersonZoneIndexBean personZoneIndexBean) {
        if (personZoneIndexBean == null) {
            return;
        }

        App.getInstance().getDefaultSetting(this, new
                DefaultSettingCallback() {
                    @Override
                    public void getDefaultSetting(DefaultSetting defaultSetting) {
                        String url = ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(),
                                personZoneIndexBean.getHeadImg());
                        RelativeLayout.LayoutParams layoutParams = ( RelativeLayout.LayoutParams )ivTopImg.getLayoutParams();
                        layoutParams.height = rlTopBg.getHeight();
                        ivTopImg.setLayoutParams(layoutParams);
                        Glide.with(PersonZoneIndexActivity.this)
                                .load(url)
                                .error(R.drawable.user_default)
                                .bitmapTransform(new BlurTransformation(PersonZoneIndexActivity.this, 20))
                                .into(ivTopImg);
                        Glide.with(PersonZoneIndexActivity.this)
                                .load(url)
                                .error(R.drawable.user_default)

                                .into(ivHeadimg);
                    }
                });

        tvName.setText(personZoneIndexBean.getNickname());
        tvMyTopic.setText("我的话题(" + personZoneIndexBean.getArticles() + ")");
        tvMyTopicFloat.setText("我的话题(" + personZoneIndexBean.getArticles() + ")");
        tvJoinTopic.setText("我参与的(" + (personZoneIndexBean.getThumbs() + personZoneIndexBean.getComments()) + ")");
        tvJoinTopicFloat.setText("我参与的(" + (personZoneIndexBean.getThumbs() + personZoneIndexBean.getComments()) + ")");
        if (CourseUtil.isOk(personZoneIndexBean.getStatus())) {
            ivHg.setVisibility(View.VISIBLE);
        }
        if (!personZoneIndexBean.getBadge().isEmpty()) {
            ivXz.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.tv_bar_title, R.id.tv_bar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bar_title:
                break;
            case R.id.tv_bar_right:
                //相册
                RouteUtil.startNewActivityAndResult(PersonZoneIndexActivity.this, new Intent(this, PublishTopicActivity.class), 0);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            //刷新一下我的贴子
            mPresenter.getMyToPic();
            mPresenter.getPersonIndex();
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPresenter.loadMoreMyTopic();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        hideEmpty();
        mPresenter.getMyToPic();
        mPresenter.getPersonIndex();
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
            App.getInstance().getUserInfo(PersonZoneIndexActivity.this, new UserInfoCallback() {
                @Override
                public void getUserInfo(UserInfo userInfo) {
                    tvName.setText(userInfo.getNickname() + "");
                    App.getInstance().getDefaultSetting(PersonZoneIndexActivity.this, new DefaultSettingCallback() {
                        @Override
                        public void getDefaultSetting(DefaultSetting defaultSetting) {
                            Glide.with(PersonZoneIndexActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), userInfo.getHeadImg())).into(ivHeadimg);
                        }
                    });

                    if (userInfo.isVip()) {
                        ImageView imageView = new ImageView(PersonZoneIndexActivity.this);
                        imageView.setImageResource(R.drawable.hg);
                        levelContanier.addView(imageView);
                    }
//                    if (!userInfo.getBadge().isEmpty()) {
//                        ImageView imageView = new ImageView(PersonZoneIndexActivity.this);
//                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//                        layoutParams.leftMargin = 6;
//                        imageView.setLayoutParams(layoutParams);
//                        imageView.setImageResource(R.drawable.xunzhang);
//                        levelContanier.addView(imageView);
//                    }
                }
            });

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

//                            if (shareContentBean.item != "") {
//                                tvItem.setText(shareContentBean.item);
//                                tvItem.setVisibility(View.VISIBLE);
//                            } else {
//                                tvItem.setVisibility(View.GONE);
//
//                            }


            if (!TextUtils.isEmpty(shareContentBean.getImgPath())) {
                App.getInstance().getDefaultSetting(PersonZoneIndexActivity.this, new DefaultSettingCallback() {
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
                                                Intent intent = new Intent(PersonZoneIndexActivity.this, ImagesPreviewActivity.class);
                                                intent.putExtra("position", position);
                                                intent.putStringArrayListExtra("data", imgLig);
                                                startActivity(intent);
                                            }
                                        });
                                        try {
                                            Glide.with(PersonZoneIndexActivity.this).load(o).into(iv);

                                        } catch (Exception e) {
                                        }
                                    }
                                };
                            }
                        });
                        imgRecycleview.setLayoutManager(new GridLayoutManager(PersonZoneIndexActivity.this, 3));

                    }
                });
            }


        }
        protected void hideEmpty(){
            View view = findViewById(R.id.ll_empty);
            if(view!=null){
                view.setVisibility(View.GONE);
            }
        }
        protected void showEmpty(){
            View view = findViewById(R.id.ll_empty);
            if(view!=null){
                view.setVisibility(View.VISIBLE);
            }
        }
    }
}
