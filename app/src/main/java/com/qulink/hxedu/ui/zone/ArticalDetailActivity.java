package com.qulink.hxedu.ui.zone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.ArticalDetailBean;
import com.qulink.hxedu.entity.CommentsBean;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.mvp.contract.ArticalContract;
import com.qulink.hxedu.mvp.contract.LikeArticalContract;
import com.qulink.hxedu.mvp.presenter.ArticalPresenter;
import com.qulink.hxedu.mvp.presenter.LikeArticalPresenter;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.ImagesPreviewActivity;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FastClick;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.SystemUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.qulink.hxedu.view.SoftKeyboardStateHelper;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class ArticalDetailActivity extends BaseActivity implements ArticalContract.View, LikeArticalContract.View, OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.iv_headimg)
    CircleImageView ivHeadimg;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.tv_bar_right)
    TextView tvBarRight;
    @BindView(R.id.iv_artical_headimg)
    CircleImageView ivArticalHeadimg;
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
    @BindView(R.id.tv_new_comments)
    TextView tvNewComments;
    @BindView(R.id.recycle_comments)
    EmptyRecyclerView recycleComments;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;

    private String etHint = "有爱评论~说点好听的";
    private PicBean shareContentBean;
    private ArticalPresenter articalPresenter;

    private LikeArticalPresenter likeArticalPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_artical_detail;
    }

    //监听软键盘的打开和关闭
    private void setListenerFotEditText(View view) {
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(view);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                //键盘打开
                etComment.requestFocus();
            }

            @Override
            public void onSoftKeyboardClosed() {
                //键盘关闭
                resetCommentType();


            }
        });
    }

    private void resetCommentType() {
        commentType = 1;
        etComment.setHint(etHint);
        etComment.setText("");
    }

    private int pageNo;
    private int pageSize;
    private int sortType = 0;

    private int commentType = 1;//1评论贴子 2回复评论
    private int commentId = 0;
    private String toUserName = "";
    private int rootComentId = 0;
    private int rootCommetnUserId = 0;
    private int toUserId = 0;
    private int articalId;
    private String title;
    @Override
    protected void init() {
        title = getIntent().getStringExtra("title");
        articalId = getIntent().getIntExtra("id",0);
        if(title==null){
            title = "贴子详情";
        }
        setTitle(title);
        setListenerFotEditText(rlRoot);
        smartLayout.setOnLoadMoreListener(this);
        smartLayout.setOnRefreshListener(this);
        articalPresenter = new ArticalPresenter();
        likeArticalPresenter = new LikeArticalPresenter();
        articalPresenter.attachView(this);
        likeArticalPresenter.attachView(this);
        shareContentBean = (PicBean) getIntent().getSerializableExtra("data");
        pageNo = 1;
        pageSize = FinalValue.limit;
        if (shareContentBean != null) {
            articalPresenter.getComments(pageNo, pageSize, shareContentBean.getId(), sortType);
            dealData();
            if(shareContentBean.getPicMaster()==null){
                articalPresenter.getPicMaster(shareContentBean.getUserId());
            }
//            if(getIntent().getBooleanExtra("getDetail",false)){
//                getDetail(shareContentBean.getId());
//            }
        }else{
            articalPresenter.getArticalDetail(articalId);
        }




        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                App.getInstance().getDefaultSetting(ArticalDetailActivity.this, new DefaultSettingCallback() {
                    @Override
                    public void getDefaultSetting(DefaultSetting defaultSetting) {
                        Glide.with(ArticalDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), userInfo.getHeadImg())).error(R.drawable.user_default).into(ivHeadimg);
                    }
                });
            }
        });
        //处理数据 并初始化界面



        //监听键盘返回键
        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {//这里可以做具体的操作
                    commentArtical(etComment.getText().toString());
                    return true;
                }
                return false;
            }
        });
        initCommentRecycle();
    }

//    private void getDetail(int id){
//        DialogUtil.showLoading(this,true);
//        ApiUtils.getInstance().getTopicDetail(1, 1, id, 0, new ApiCallback() {
//            @Override
//            public void success(ResponseData t) {
//                DialogUtil.hideLoading(ArticalDetailActivity.this);
//                ArticalDetailBean articalDetailBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()),ArticalDetailBean.class);
//                shareContentBean.setContent(articalDetailBean.getArticelDetails().getContent());
//
//                dealData();
//            }
//
//            @Override
//            public void error(String code, String msg) {
//                DialogUtil.hideLoading(ArticalDetailActivity.this);
//
//            }
//
//            @Override
//            public void expcetion(String expectionMsg) {
//                DialogUtil.hideLoading(ArticalDetailActivity.this);
//
//            }
//        });
//    }

    private void commentArtical(String content) {

        if(!App.getInstance().isLogin(this,true)){
          return;
        }
        if (TextUtils.isEmpty(content)) {
            ToastUtils.show(this, "请输入评论内容");
            return;
        }
        if (content.length() > FinalValue.maxCommentLimit) {
            ToastUtils.show(this, "评论长度不能超过" + FinalValue.maxCommentLimit);
            return;
        }
        SystemUtil.closeKeybord(etComment, this);
        if (commentType == 1) {
            articalPresenter.comment(shareContentBean.getId(), content);
        } else {
            articalPresenter.replyComments(shareContentBean.getId(), commentId, toUserId, rootComentId, content, toUserName,rootCommetnUserId);
        }
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private void dealPicMaster(PicMaster picMaster) {
        tvName.setText(picMaster.getNickname() + "");
        App.getInstance().getDefaultSetting(ArticalDetailActivity.this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                Glide.with(ArticalDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), picMaster.getHeadImg())).into(ivArticalHeadimg);
            }
        });
        if (CourseUtil.isOk(picMaster.getStatus())) {
            ImageView imageView = new ImageView(ArticalDetailActivity.this);
            imageView.setImageResource(R.drawable.hg);
            llLevelContanier.addView(imageView);
        }
        if (!picMaster.getBadge().isEmpty()) {

            ImageView imageView = new ImageView(ArticalDetailActivity.this);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.leftMargin = 6;
            imageView.setLayoutParams(layoutParams);
            imageView.setImageResource(R.drawable.xunzhang);
            llLevelContanier.addView(imageView);
        }
    }

    private void dealData() {
        if (shareContentBean == null) {
            return;
        }
        if (shareContentBean.getPicMaster() == null) {
            articalPresenter.getPicMaster(shareContentBean.getUserId());
        } else {
            dealPicMaster(shareContentBean.getPicMaster());

        }
        if (CourseUtil.isOk(shareContentBean.getThumbStatus())) {
            ivLike.setImageResource(R.drawable.liked);
        } else {
            ivLike.setImageResource(R.drawable.like);
        }
        //点赞 和取消点赞
        ivLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClick.isFastClick()) {
                    if (CourseUtil.isOk(shareContentBean.getThumbStatus())) {
                        likeArticalPresenter.cancelLikeArtical(shareContentBean.getId());
                    } else {
                        likeArticalPresenter.likeArtical(shareContentBean.getId());
                    }
                }
            }
        });


        tvLikeNum.setText(shareContentBean.getThumbs() + "");
        tvCommentNum.setText(shareContentBean.getComments() + "");
        content.setContent(shareContentBean.getContent());
        llLevelContanier.removeAllViews();

        if (!TextUtils.isEmpty(shareContentBean.getTopicName())) {
            tvItem.setText(shareContentBean.getTopicName());
            tvItem.setVisibility(View.VISIBLE);
        } else {
            tvItem.setVisibility(View.GONE);
        }


        if (!TextUtils.isEmpty(shareContentBean.getImgPath())) {
            App.getInstance().getDefaultSetting(ArticalDetailActivity.this, new DefaultSettingCallback() {
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
                                            Intent intent = new Intent(ArticalDetailActivity.this, ImagesPreviewActivity.class);
                                            intent.putExtra("position", position);
                                            intent.putStringArrayListExtra("data", imgLig);
                                            startActivity(intent);
                                        }
                                    });
                                    try {
                                        Glide.with(ArticalDetailActivity.this).load(o).into(iv);

                                    } catch (Exception e) {
                                    }
                                }
                            };
                        }
                    });
                    recycleImg.setLayoutManager(new GridLayoutManager(ArticalDetailActivity.this, 3));

                }
            });
        }


    }

    private List<CommentsBean> commentsBeanList;

    private void initCommentRecycle() {
        commentsBeanList = new ArrayList<>();
        recycleComments.setAdapter(new CommonRcvAdapter<CommentsBean>(commentsBeanList) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new CommentItem();
            }
        });
        recycleComments.setLayoutManager(new LinearLayoutManager(this));
        recycleComments.setEmptyView(findViewById(R.id.ll_empty));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        pageNo++;
        articalPresenter.loadMoreComments(pageNo, pageSize, shareContentBean.getId(), 0);
    }

    @OnClick({R.id.ll_like, R.id.tv_new_comments})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_like:
                if(App.getInstance().isLogin(this,true)){
                    if (CourseUtil.isOk(shareContentBean.getThumbStatus())) {
                        likeArticalPresenter.cancelLikeArtical(shareContentBean.getId());
                    } else {
                        likeArticalPresenter.likeArtical(shareContentBean.getId());
                    }
                }

                break;
            case R.id.tv_new_comments:
                pageNo = 1;
                articalPresenter.getComments(pageNo, pageSize, shareContentBean.getId(), 1);
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        pageNo = 1;
        smartLayout.setNoMoreData(false);
        articalPresenter.getComments(pageNo, pageSize, shareContentBean.getId(), 0);
    }

    class CommentItem implements AdapterItem<CommentsBean> {

        @BindView(R.id.iv_headimg)
        CircleImageView ivHeadimg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.iv_like)
        ImageView ivLike;
        @BindView(R.id.tv_like_num)
        TextView tvLikeNum;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_reply)
        TextView tvReply;
        @BindView(R.id.recycle)
        RecyclerView recycle;
        @BindView(R.id.ll_like)
        LinearLayout llLike;
        @BindView(R.id.tv_more_reply)
        TextView tvMoreReply;

        @Override
        public int getLayoutResId() {
            return R.layout.comments_item;
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
        public void handleData(CommentsBean commentsBean, int position) {
            App.getInstance().getDefaultSetting(ArticalDetailActivity.this, new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(ArticalDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), commentsBean.getHeadImg())).into(ivHeadimg);
                }
            });
            tvName.setText(commentsBean.getNickname());
            tvLikeNum.setText(commentsBean.getThumbs() + "");
            tvContent.setText(commentsBean.getContent());
            tvTime.setText(commentsBean.getCreateTime());
            tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentType = 2;
                    commentId = commentsBean.getId();
                    rootComentId = commentsBean.getId();
                    toUserName = commentsBean.getNickname();
                    rootCommetnUserId = commentsBean.getUserId();
                    toUserId = commentsBean.getUserId();
                    etComment.setHint("回复 @" + commentsBean.getNickname() + ":");
                    SystemUtil.openKeybord(etComment, ArticalDetailActivity.this);
                }
            });
            tvReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentType = 2;
                    commentId = commentsBean.getId();
                    toUserId = commentsBean.getUserId();
                    rootComentId = commentsBean.getId();
                    toUserName = commentsBean.getNickname();
                    rootCommetnUserId = commentsBean.getUserId();

                    etComment.setHint("回复 @" + commentsBean.getNickname() + ":");
                    SystemUtil.openKeybord(etComment, ArticalDetailActivity.this);
                }
            });
            llLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (CourseUtil.isOk(commentsBean.getStatus())) {
                        articalPresenter.cancelLikeComments(shareContentBean.getId(), commentsBean.getId());
                    } else {
                        articalPresenter.likeComments(shareContentBean.getId(), commentsBean.getId());
                    }
                }
            });
            if (CourseUtil.isOk(commentsBean.getStatus())) {
                ivLike.setImageResource(R.drawable.liked);
            } else {
                ivLike.setImageResource(R.drawable.like);

            }
            if (commentsBean.isNoMoreReply()) {
                tvMoreReply.setVisibility(View.GONE);
            } else {
                tvMoreReply.setVisibility(View.VISIBLE);
            }
            tvMoreReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //加载更多
                    commentsBean.setReplyPageNo(commentsBean.getReplyPageNo() + 1);
                    articalPresenter.getReplyByCommentsId(commentsBean.getReplyPageNo(), commentsBean.getReplyPageSize(), commentsBean.getId());
                }
            });
            tvReply.setText("回复");
            if (commentsBean.getReplyList()!=null) {
                recycle.setAdapter(new CommonRcvAdapter<CommentsBean.ReplyListBean>(commentsBean.getReplyList()) {
                    @NonNull
                    @Override
                    public AdapterItem createItem(Object type) {
                        return new ReplyItem();
                    }
                });
                recycle.setLayoutManager(new LinearLayoutManager(ArticalDetailActivity.this));
            }
        }
    }

    class ReplyItem implements AdapterItem<CommentsBean.ReplyListBean> {
        @BindView(R.id.tv_master)
        TextView tvMaster;
        @BindView(R.id.tv_slave)
        TextView tvSlave;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        @Override
        public int getLayoutResId() {
            return R.layout.reply_item;
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
        public void handleData(CommentsBean.ReplyListBean replyListBean, int position) {
            tvMaster.setText(replyListBean.getFromUserName());
            tvSlave.setText(replyListBean.getToUserName() + "：");
            tvContent.setText(replyListBean.getContent());
            if (replyListBean.getRootCommentUserId() == replyListBean.getFromUserId()) {
                tvMaster.setTextColor(ContextCompat.getColor(ArticalDetailActivity.this, R.color.yellow));
            }
            if (replyListBean.getRootCommentUserId() == replyListBean.getToUserId()) {
                tvSlave.setTextColor(ContextCompat.getColor(ArticalDetailActivity.this, R.color.yellow));
            }
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    commentType = 2;
                    toUserName = replyListBean.getToUserName();
                    //  commentId = replyListBean.getId();
                    rootComentId = replyListBean.getRootCommentId();
                    commentId = replyListBean.getRootCommentId();
                    toUserId = replyListBean.getToUserId();
                    rootCommetnUserId = replyListBean.getRootCommentUserId();

                    etComment.setHint("回复 @" + replyListBean.getToUserName() + ":");
                    SystemUtil.openKeybord(etComment, ArticalDetailActivity.this);
                }
            });
        }
    }

    @Override
    public void likeArticalSuc(int articalId) {
        shareContentBean.setThumbs(shareContentBean.getThumbs() + 1);
        shareContentBean.setThumbStatus(1);
        ivLike.setImageResource(R.drawable.liked);
        tvLikeNum.setText(shareContentBean.getThumbs() + "");
        setBackResult();
    }

    private boolean setResult  = false;
    private void setBackResult(){
        //待结果返回
        if(!setResult){
            Intent intent = new Intent();
            intent.putExtra("data", shareContentBean);
            setResult(RESULT_OK, intent);
            setResult = true;
        }

    }
    @Override
    public void cancelLikeArticalSuc(int articalId) {
        shareContentBean.setThumbs(shareContentBean.getThumbs() - 1);
        shareContentBean.setThumbStatus(0);
        ivLike.setImageResource(R.drawable.like);
        tvLikeNum.setText(shareContentBean.getThumbs() + "");
        setBackResult();

    }

    @Override
    public void getPicMasterSuc(PicMaster picMaster) {
        shareContentBean.setPicMaster(picMaster);
        dealPicMaster(picMaster);
    }

    @Override
    public void getPicMasterFail(int userId) {
        shareContentBean.setPicMaster(new PicMaster());
    }

    @Override
    public void getCommentsSuc(List<CommentsBean> list) {
        smartLayout.finishRefresh(true);
        smartLayout.setNoMoreData(false);
        if(!list.isEmpty()){
            commentsBeanList.clear();
            commentsBeanList.addAll(list);
            recycleComments.getAdapter().notifyDataSetChanged();
            if(list.size()<pageSize){
                smartLayout.setNoMoreData(true);
            }
        }

    }

    @Override
    public void loadMoreCommentsSuc(List<CommentsBean> list) {
        smartLayout.finishLoadMore(true);

        if (!list.isEmpty()) {
            commentsBeanList.addAll(list);
            recycleComments.getAdapter().notifyDataSetChanged();
            if (list.size() < pageSize) {
                smartLayout.setNoMoreData(true);
            }
        }

    }

    @Override
    public void loadMoreReplySuc(List<CommentsBean.ReplyListBean> list, int commentId) {


        for (CommentsBean commentsBean : commentsBeanList) {
            if (commentsBean.getId() == commentId) {
                if (list.size() < commentsBean.getReplyPageSize()) {
                    commentsBean.setNoMoreReply(true);
                }
                commentsBean.getReplyList().addAll(list);
                break;
            }
        }
        recycleComments.getAdapter().notifyDataSetChanged();

    }

    @Override
    public void getCommentsError(String msg) {
        smartLayout.finishRefresh(false);

    }

    @Override
    public void loadMoreCommentError(String msg) {
        smartLayout.finishLoadMore(false);

    }

    @Override
    public void commentSuc(String content, int commentId, int userId) {
        setBackResult();

        shareContentBean.setComments(shareContentBean.getComments() + 1);
        tvCommentNum.setText(shareContentBean.getComments() + "");
        CommentsBean commentsBean = new CommentsBean();
        commentsBean.setId(commentId);
        commentsBean.setNoMoreReply(true);
        commentsBean.setUserId(userId);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        commentsBean.setCreateTime(dateFormat.format(new Date()));
        commentsBean.setContent(content);
        commentsBean.setReplyList(new ArrayList<>());
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                commentsBean.setNickname(userInfo.getNickname());
                commentsBean.setHeadImg(userInfo.getHeadImg());
                commentsBeanList.add(0, commentsBean);
                recycleComments.getAdapter().notifyDataSetChanged();
            }
        });

    }

    @Override
    public void replyCommentSuc(String content, String toUserName, int commentId, int fromUserId, int rootCommentId, int toUserId,int rootCommentUserId) {
        for (CommentsBean commentsBean : commentsBeanList) {
            if (commentsBean.getId() == rootCommentId) {
                CommentsBean.ReplyListBean replyListBean = new CommentsBean.ReplyListBean();
                replyListBean.setRootCommentId(rootCommentId);
                replyListBean.setRootCommentUserId(rootCommentUserId);
                replyListBean.setContent(content);
                replyListBean.setFromUserId(fromUserId);
                replyListBean.setToUserId(toUserId);
                replyListBean.setId(commentId);
                replyListBean.setToUserName(toUserName);

                App.getInstance().getUserInfo(this, new UserInfoCallback() {
                    @Override
                    public void getUserInfo(UserInfo userInfo) {
                        replyListBean.setFromUserName(userInfo.getNickname());
                        commentsBean.getReplyList().add(0, replyListBean);
                        recycleComments.getAdapter().notifyDataSetChanged();

                    }
                });
                break;
            }
        }
    }

    @Override
    public void commentFail(String content) {
        ToastUtils.show(this, content);

    }

    @Override
    public void likeCommentSuc(int commentId) {
        for (CommentsBean c : commentsBeanList) {
            if (c.getId() == commentId) {
                c.setThumbs(c.getThumbs() + 1);
                c.setStatus(1);
            }
        }
        recycleComments.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void cancelLikeCommentSuc(int commentId) {
        for (CommentsBean c : commentsBeanList) {
            if (c.getId() == commentId) {
                c.setThumbs(c.getThumbs() - 1);
                c.setStatus(0);
            }
        }
        recycleComments.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void getArticalDetailSuc(PicBean picBean) {
        shareContentBean = picBean;
        if(shareContentBean!=null){
            articalPresenter.getComments(pageNo, pageSize, shareContentBean.getId(), sortType);
            dealData();
            if(shareContentBean.getPicMaster()==null){
                articalPresenter.getPicMaster(shareContentBean.getUserId());
            }
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        articalPresenter.detachView();
        likeArticalPresenter.detachView();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(String msg) {
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

    }


}
