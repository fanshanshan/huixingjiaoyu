package com.qulink.hxedu.ui.zone;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.qulink.hxedu.adapter.MyTopicAdapter;
import com.qulink.hxedu.adapter.TopicAdapter;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.PersonZoneIndexBean;
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.mvp.contract.LikeArticalContract;
import com.qulink.hxedu.mvp.contract.PersonZoneIndexContract;
import com.qulink.hxedu.mvp.presenter.LikeArticalPresenter;
import com.qulink.hxedu.mvp.presenter.PersonZoneIndexPresenter;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.ImagesPreviewActivity;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.qulink.hxedu.view.MyScrollView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.glide.transformations.BlurTransformation;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import kale.adapter.util.IAdapter;

public class PersonZoneIndexActivity extends BaseActivity implements PersonZoneIndexContract.View, OnLoadMoreListener, OnRefreshListener, LikeArticalContract.View {


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
    EmptyRecyclerView recycleMyTopic;
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
    private LikeArticalPresenter likeArticalPresenter;

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
        likeArticalPresenter = new LikeArticalPresenter();
        mPresenter.attachView(this);
        likeArticalPresenter.attachView(this);
        mPresenter.getPersonIndex();
        mPresenter.getMyToPic();
        addScrollviewListener();
        initPicRecycle();
    }

    //scorllview添加滑动监听  根据滑动距离改变顶部bar的颜色
    private void addScrollviewListener() {
        sc.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                if (scrollY > (rlTopBg.getHeight() - llTab.getHeight() - status.getHeight())) {
                    if (llFloat.getVisibility() == View.GONE) {
                        llFloat.setVisibility(View.VISIBLE);
                    }
                } else {
                    if (llFloat.getVisibility() == View.VISIBLE) {
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
    private List<PicBean> picBeanList;
    private MyTopicAdapter topicAdapter;
    private void initPicRecycle() {
        picBeanList = new ArrayList<>();
        topicAdapter = new MyTopicAdapter(picBeanList,this);
        topicAdapter.setClickListener(new MyTopicAdapter.PicAdapterController() {
            @Override
            public void getPicMaster(int userId) {
                mPresenter.getPicMaster(userId);
            }

            @Override
            public void cancelLikeArtical(int id) {
                likeArticalPresenter.cancelLikeArtical(id);

            }

            @Override
            public void likeArtical(int id) {
                likeArticalPresenter.likeArtical(id);


            }
        });
        recycleMyTopic.setAdapter(topicAdapter);
        recycleMyTopic.setEmptyView(findViewById(R.id.ll_empty));
        recycleMyTopic.setLayoutManager(new LinearLayoutManager(PersonZoneIndexActivity.this));
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    //获取我的页面信息成功
   private PersonZoneIndexBean personZoneIndexBean;
    @Override
    public void getPersonIndexSuc(PersonZoneIndexBean s) {
        personZoneIndexBean = s;
        dealData();
    }

    //获取我的话题成功
    @Override
    public void getMyTopicSuc(List<PicBean> hotArticalList) {
        smartLayout.setNoMoreData(false);

        smartLayout.finishRefresh(true);
        if(hotArticalList!=null&&!hotArticalList.isEmpty()){
            picBeanList.clear();
            picBeanList.addAll(hotArticalList);
            topicAdapter.notifyDataSetChanged();
        }

    }

    //加载更多话题成功 刷新界面
    @Override
    public void loadmoreTopicSuc(List<PicBean> hotArticalList) {
        smartLayout.finishLoadMore(true);
        if(hotArticalList!=null&&!hotArticalList.isEmpty()){
            picBeanList.addAll(hotArticalList);
            topicAdapter.notifyItemRangeChanged(picBeanList.size()-hotArticalList.size(),hotArticalList.size());
        }
    }

    //根据用户id获取用户信息
    @Override
    public void getPicMasterSuc(PicMaster picMaster) {
        for(int i = 0;i<picBeanList.size();i++){
            if(picBeanList.get(i).getUserId()==picMaster.getUserId()&&picBeanList.get(i).getPicMaster()==null){
                picBeanList.get(i).setPicMaster(picMaster);
                picBeanList.get(i).setInitMaster(false);
                topicAdapter.notifyItemChanged(i,picBeanList);
                break;
            }
        }
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

    private void dealData() {
        if (personZoneIndexBean == null) {
            return;
        }

        App.getInstance().getDefaultSetting(this, new
                DefaultSettingCallback() {
                    @Override
                    public void getDefaultSetting(DefaultSetting defaultSetting) {
                        String url = ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(),
                                personZoneIndexBean.getHeadImg());
                        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ivTopImg.getLayoutParams();
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
        if(personZoneIndexBean.getArticles()>100){
            tvMyTopic.setText("我的话题(99+)");
            tvMyTopicFloat.setText("我的话题(99+)");
        }else{
            tvMyTopic.setText("我的话题(" + personZoneIndexBean.getArticles() + ")");
            tvMyTopicFloat.setText("我的话题(" + personZoneIndexBean.getArticles() + ")");

        }if((personZoneIndexBean.getThumbs() + personZoneIndexBean.getComments())>100){
            tvJoinTopic.setText("我参与的(99+)");
            tvJoinTopicFloat.setText("我参与的(99+)");

        }else{
            tvJoinTopic.setText("我参与的(" + (personZoneIndexBean.getThumbs() + personZoneIndexBean.getComments()) + ")");
            tvJoinTopicFloat.setText("我参与的(" + (personZoneIndexBean.getThumbs() + personZoneIndexBean.getComments()) + ")");

        }

         if (CourseUtil.isOk(personZoneIndexBean.getStatus())) {
            ivHg.setVisibility(View.VISIBLE);
        }
        if (!personZoneIndexBean.getBadge().isEmpty()) {
            ivXz.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.tv_bar_title, R.id.tv_bar_right,R.id.tv_join_topic,R.id.tv_join_topic_float})
    public void onViewClicked(View view) {
        Intent intent = null;

        switch (view.getId()) {
            case R.id.tv_bar_title:
                break;
            case R.id.tv_bar_right:
                //相册
                App.getInstance().getUserInfo(this, new UserInfoCallback() {
                    @Override
                    public void getUserInfo(UserInfo userInfo) {
                        if(userInfo.isRealAuth(PersonZoneIndexActivity.this)){
                            RouteUtil.startNewActivityAndResult(PersonZoneIndexActivity.this, new Intent(PersonZoneIndexActivity.this, PublishTopicActivity.class), 0);
                        }
                    }
                });
                break;
            case R.id.tv_join_topic:
                 intent = new Intent(PersonZoneIndexActivity.this,MyJoinTopicActivity.class);
                intent.putExtra("commentNum",personZoneIndexBean==null?0:personZoneIndexBean.getComments());
                intent.putExtra("likeNum",personZoneIndexBean==null?0:personZoneIndexBean.getThumbs());
                RouteUtil.startNewActivity(this,intent);
                break;
                case R.id.tv_join_topic_float:
                 intent = new Intent(PersonZoneIndexActivity.this,MyJoinTopicActivity.class);
                intent.putExtra("commentNum",personZoneIndexBean==null?0:personZoneIndexBean.getComments());
                intent.putExtra("likeNum",personZoneIndexBean==null?0:personZoneIndexBean.getThumbs());
                RouteUtil.startNewActivity(this,intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if(requestCode == 0 ){
                //刷新一下我的贴子

                mPresenter.getMyToPic();
                mPresenter.getPersonIndex();
            }else if(requestCode==1){
                PicBean picBean = (PicBean) data.getSerializableExtra("data");
                if(picBean==null){
                    return;
                }
                for(int i = 0;i<picBeanList.size();i++){
                    if(picBeanList.get(i).getId()==picBean.getId()){
                        picBeanList.get(i).setThumbStatus(picBean.getThumbStatus());
                        picBeanList.get(i).setThumbs(picBean.getThumbs());
                        picBeanList.get(i).setComments(picBean.getComments());
                        topicAdapter.notifyItemChanged(i,picBeanList);
                        break;
                    }
                }
            }
        }

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPresenter.loadMoreMyTopic();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        smartLayout.setNoMoreData(false);
        mPresenter.getMyToPic();
        mPresenter.getPersonIndex();
    }

    @Override
    public void likeArticalSuc(int articalId) {
        for(int i = 0;i<picBeanList.size();i++){
            if(picBeanList.get(i).getId()==articalId){
                picBeanList.get(i).setThumbStatus(1);
                picBeanList.get(i).setThumbs(picBeanList.get(i).getThumbs()+1);

                recycleMyTopic.getAdapter().notifyItemChanged(i,picBeanList);
                break;
            }
        }
    }

    @Override
    public void cancelLikeArticalSuc(int articalId) {
        for(int i = 0;i<picBeanList.size();i++){
            if(picBeanList.get(i).getId()==articalId){
                picBeanList.get(i).setThumbStatus(0);
                picBeanList.get(i).setThumbs(picBeanList.get(i).getThumbs()-1);
                recycleMyTopic.getAdapter().notifyItemChanged(i,picBeanList);
                break;
            }
        }
    }

    class TopicItem implements AdapterItem<PicBean> {

        @BindView(R.id.iv_headimg)
        CircleImageView ivHeadimg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.ll_level_contanier)
        LinearLayout llLevelContanier;
        @BindView(R.id.content)
        ExpandableTextView content;
        @BindView(R.id.recycle_img)
        RecyclerView recycleImg;
        @BindView(R.id.tv_item)
        TextView tvItem;
        @BindView(R.id.iv_comment)
        ImageView ivComment;
        @BindView(R.id.tv_comment_num)
        TextView tvCommentNum;
        @BindView(R.id.iv_like)
        ImageView ivLike;
        @BindView(R.id.tv_like_num)
        TextView tvLikeNum;
        @BindView(R.id.ll_like)
        LinearLayout llLike;
        @BindView(R.id.iv_share)
        ImageView ivShare;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        @Override
        public int getLayoutResId() {
            return R.layout.subject_share_item;
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
                        llLevelContanier.addView(imageView);
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

            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(PersonZoneIndexActivity.this, ArticalDetailActivity.class);
                    intent.putExtra("data", shareContentBean);
                    startActivityForResult(intent, 1);
                }
            });
            tvLikeNum.setText(shareContentBean.getThumbs() + "");
            tvCommentNum.setText(shareContentBean.getComments() + "");
            content.setContent(shareContentBean.getContent());
            llLevelContanier.removeAllViews();

            if (!TextUtils.isEmpty(shareContentBean.getTopicName())) {
                tvItem.setText(shareContentBean.getTopicName());
                tvItem.setVisibility(View.VISIBLE);
                tvItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        RouteUtil.goSubjectPage(PersonZoneIndexActivity.this,shareContentBean.getTopicName(),shareContentBean.getTopicId(),0);
                    }
                });
            } else {
                tvItem.setVisibility(View.GONE);

            }

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

                        recycleImg.setAdapter(new CommonRcvAdapter<String>(imgLig) {

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
                        recycleImg.setLayoutManager(new GridLayoutManager(PersonZoneIndexActivity.this, 3));

                    }
                });
            }


        }

    }

}
