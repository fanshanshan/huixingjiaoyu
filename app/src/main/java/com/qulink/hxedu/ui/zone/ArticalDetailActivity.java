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
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.qulink.hxedu.util.FastClick;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class ArticalDetailActivity extends BaseActivity implements ArticalContract.View, LikeArticalContract.View {

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
    RecyclerView recycleComments;

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

    private int pageNo;
    private int pageSize;
    private int sortType = 0;

    @Override
    protected void init() {
        articalPresenter = new ArticalPresenter();
        likeArticalPresenter = new LikeArticalPresenter();
        articalPresenter.attachView(this);
        likeArticalPresenter.attachView(this);
        shareContentBean = (PicBean) getIntent().getSerializableExtra("data");
        if (shareContentBean != null) {
            pageNo = 1;
            pageSize = FinalValue.limit;
            articalPresenter.getComments(pageNo, pageSize, shareContentBean.getId(), sortType);
            dealData();

        }

        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                App.getInstance().getDefaultSetting(ArticalDetailActivity.this, new DefaultSettingCallback() {
                    @Override
                    public void getDefaultSetting(DefaultSetting defaultSetting) {
                        Glide.with(ArticalDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), userInfo.getHeadImg())).into(ivHeadimg);
                    }
                });
            }
        });
        //处理数据 并初始化界面

        //待结果返回
        Intent intent = new Intent();
        intent.putExtra("data", shareContentBean);
        setResult(RESULT_OK, intent);

        //监听键盘返回键
        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {//这里可以做具体的操作
                    comment(etComment.getText().toString());
                    return true;
                }
                return false;
            }
        });
        initCommentRecycle();
    }

    private void comment(String content) {
        if (TextUtils.isEmpty(content)) {
            ToastUtils.show(this, "请输入评论内容");
            return;
        }
        if (content.length() > FinalValue.maxCommentLimit) {
            ToastUtils.show(this, "评论长度不能超过" + FinalValue.maxCommentLimit);
            return;
        }
        articalPresenter.comment(shareContentBean.getId(), content);
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
            tvReply.setText("回复");
            if (!commentsBean.getReplyList().isEmpty()) {
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
            tvSlave.setText(replyListBean.getToUserName()+"：");
            tvContent.setText(replyListBean.getContent());
        }
    }

    @Override
    public void likeArticalSuc(int articalId) {
        shareContentBean.setThumbs(shareContentBean.getThumbs() + 1);
        shareContentBean.setThumbStatus(1);
        ivLike.setImageResource(R.drawable.liked);
        tvLikeNum.setText(shareContentBean.getThumbs() + "");

    }

    @Override
    public void cancelLikeArticalSuc(int articalId) {
        shareContentBean.setThumbs(shareContentBean.getThumbs() - 1);
        shareContentBean.setThumbStatus(0);
        ivLike.setImageResource(R.drawable.like);
        tvLikeNum.setText(shareContentBean.getThumbs() + "");

    }

    @Override
    public void getPicMasterSuc(PicMaster picMaster) {
        shareContentBean.setPicMaster(picMaster);
        dealPicMaster(picMaster);
    }

    @Override
    public void getCommentsSuc(List<CommentsBean> list) {
        commentsBeanList.clear();
        commentsBeanList.addAll(list);
        recycleImg.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void loadMoreCommentsSuc(List<CommentsBean> list) {

    }

    @Override
    public void getCommentsError(String msg) {

    }

    @Override
    public void loadMoreCommentError(String msg) {

    }

    @Override
    public void commentSuc(String content) {
        ToastUtils.show(this, content);
    }

    @Override
    public void commentFail(String content) {
        ToastUtils.show(this, content);

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
