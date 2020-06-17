package com.qulink.hxedu.ui.course;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.CatalogBean;
import com.qulink.hxedu.entity.CourseDetailBean;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.PersonNalCourseDetailBean;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.mvp.contract.CourseDetailContract;
import com.qulink.hxedu.mvp.presenter.CourseDetailPresenter;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.BuyLessonActivity;
import com.qulink.hxedu.ui.LoginActivity;
import com.qulink.hxedu.ui.VipDetailActivity;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class CourseDetailActivity extends BaseActivity implements CourseDetailContract.View {

    @BindView(R.id.ll_like)
    LinearLayout llLike;
    @BindView(R.id.videoView)
    PlayerView videoView;
    private String TAG = "CourseDetailActivity";
    @BindView(R.id.iv_course_corver)
    ImageView ivCourseCorver;
    @BindView(R.id.tv_course_name)
    TextView tvCourseName;
    @BindView(R.id.tv_course_time)
    TextView tvCourseTime;
    @BindView(R.id.tv_study_num)
    TextView tvStudyNum;
    @BindView(R.id.tv_course_detail)
    TextView tvCourseDetail;
    @BindView(R.id.course_detail_indicator)
    View courseDetailIndicator;
    @BindView(R.id.tv_course_catalog)
    TextView tvCourseCatalog;
    @BindView(R.id.course_catalog_indicator)
    View courseCatalogIndicator;
    @BindView(R.id.ll_course_catalog)
    LinearLayout llCourseCatalog;
    @BindView(R.id.ll_course_detail)
    LinearLayout llCourseDetail;
    @BindView(R.id.iv_teacher_headimg)
    CircleImageView ivTeacherHeadimg;
    @BindView(R.id.tv_teacher_name)
    TextView tvTeacherName;
    @BindView(R.id.tv_teacher_desc)
    ExpandableTextView tvTeacherDesc;
    @BindView(R.id.tv_course_desc)
    ExpandableTextView tvCourseDesc;
    @BindView(R.id.ll_course_desc)
    LinearLayout llCourseDesc;
    @BindView(R.id.recycle_course_catalog)
    RecyclerView recycleCourseCatalog;

    CourseDetailPresenter mPresenter;
    int courseId;
    @BindView(R.id.tv_price)
    TextView tvPrice;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_vip)
    ImageView ivVip;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.tv_like)
    TextView tvLike;
    @BindView(R.id.tv_download)
    TextView tvDownload;
    @BindView(R.id.iv_like)
    ImageView ivLike;

    private PersonNalCourseDetailBean personNalCourseDetailBean;
    private int LOGIN_CODE = 1;
    private int BUY_LESSON_CODE = 2;
    private int OPEN_VIP_CODE = 3;

    private boolean exChange;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_course_detail;
    }

    private void initPlayer(){
        SimpleExoPlayer player = new SimpleExoPlayer.Builder(this).build();
        videoView.setPlayer(player);
        // Produces DataSource instances through which media data is loaded.
        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                Util.getUserAgent(this, "App"));
// This is the MediaSource representing the media to be played.

        // Measures bandwidth during playback. Can be null if not required.
        DefaultBandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
// Produces Extractor instances for parsing the media data.
        ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();
        MediaSource source = new ExtractorMediaSource(Uri.parse("http://clips.vorwaerts-gmbh.de/big_buck_bunny.mp4"),dataSourceFactory, extractorsFactory, null, null);
// Loops the video indefinitely.
        LoopingMediaSource loopingSource = new LoopingMediaSource(source);


// Prepare the player with the source.
        player.prepare(loopingSource);
        player.setPlayWhenReady(true);
    }
    @Override
    protected void init() {
        courseId = getIntent().getIntExtra("courseId", 0);
        exChange = getIntent().getBooleanExtra("exChange", false);
        mPresenter = new CourseDetailPresenter();
        mPresenter.attachView(this);
        mPresenter.getCourseDetail(courseId);
//        mPresenter.getPersonnalCourseDetail(courseId);
//        mPresenter.getCourseLookNumberNameById(courseId);
        chooseCourseDetail();
        initPlayer();
    }


    void chooseCourseDetail() {
        courseDetailIndicator.setVisibility(View.VISIBLE);
        courseCatalogIndicator.setVisibility(View.GONE);
        courseDetailIndicator.setBackgroundColor(0Xff5DA0FF);
        tvCourseDetail.setTextColor(0Xff5DA0FF);
        tvCourseCatalog.setTextColor(0Xff999999);
        recycleCourseCatalog.setVisibility(View.GONE);
        llCourseDesc.setVisibility(View.VISIBLE);
    }

    void chooseCourseCatalog() {
        if (catalogList == null) {
            mPresenter.getCourseChapter(courseId);
        }
        courseCatalogIndicator.setVisibility(View.VISIBLE);
        courseDetailIndicator.setVisibility(View.GONE);
        courseCatalogIndicator.setBackgroundColor(0Xff5DA0FF);
        tvCourseCatalog.setTextColor(0Xff5DA0FF);
        tvCourseDetail.setTextColor(0Xff999999);
        llCourseDesc.setVisibility(View.GONE);
        recycleCourseCatalog.setVisibility(View.VISIBLE);
    }


    private List<CatalogBean> catalogList;
    private CommonRcvAdapter<CatalogBean> commonRcvAdapter;

    void initCatalog() {

        commonRcvAdapter = new CommonRcvAdapter<CatalogBean>(catalogList) {
            TextView tvNumber;
            TextView tvTitle;

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AdapterItem<CatalogBean>() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.course_catalog_item;
                    }

                    @Override
                    public void bindViews(@NonNull View root) {
                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        tvNumber = root.findViewById(R.id.tv_number);
                        tvTitle = root.findViewById(R.id.tv_title);

                    }

                    @Override
                    public void setViews() {

                    }

                    @Override
                    public void handleData(CatalogBean o, int position) {

                        tvTitle.setText(o.getChapterName().toString());
                        tvNumber.setText("第" + o.getChapterId() + "讲");
                    }
                };
            }
        };
        recycleCourseCatalog.setAdapter(commonRcvAdapter);
        recycleCourseCatalog.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }

    @OnClick({R.id.ll_like, R.id.ll_course_catalog, R.id.ll_course_detail, R.id.tv_buy, R.id.tv_share, R.id.iv_like, R.id.tv_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_course_catalog:
                chooseCourseCatalog();
                break;
            case R.id.ll_course_detail:
                chooseCourseDetail();
                break;
            case R.id.tv_buy:
                if (personNalCourseDetailBean == null) {
                    return;
                }
                if (App.getInstance().isLogin(this)) {
                    if (personNalCourseDetailBean.getPurchaseStatus() == 0) {
                        //开始播放
                        startPlay();
                    } else if (personNalCourseDetailBean.getPurchaseStatus() == 1) {
                        App.getInstance().getUserInfo(this, new UserInfoCallback() {
                            @Override
                            public void getUserInfo(UserInfo userInfo) {
                                if (userInfo.isVip()) {
                                    startPlay();
                                } else {
                                    toOpenVip();
                                }
                            }
                        });

                    } else if (personNalCourseDetailBean.getPurchaseStatus() == 2) {
                    } else if (personNalCourseDetailBean.getPurchaseStatus() == 3) {
                        toBuy();
                    }
                } else {
                    toLogin();
                }
                break;
            case R.id.tv_share:
                break;
            case R.id.ll_like:
                if (App.getInstance().isLogin(CourseDetailActivity.this, true)) {
                    if (courseDetailBean.getPersonal().getCollectStatus() == 1) {
                        cancelCollection();
                    } else {
                        collectionVideo();
                    }
                }
                break;
            case R.id.tv_download:
                break;
        }
    }

    private void startPlay() {
        mPresenter.increaseVideoLookNumber(courseId);
    }

    void collectionVideo() {
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().collectionCourse(courseId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(CourseDetailActivity.this);
                if (courseDetailBean != null) {
                    courseDetailBean.getPersonal().setCollectStatus(1);
                    dealBtnBuy(courseDetailBean.getPersonal());
                }
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(CourseDetailActivity.this);
                ToastUtils.show(CourseDetailActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(CourseDetailActivity.this);
                ToastUtils.show(CourseDetailActivity.this, expectionMsg);
            }
        });
    }

    void cancelCollection() {
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().cancelCollectionCourse(courseId + "", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(CourseDetailActivity.this);
                if (courseDetailBean != null) {
                    courseDetailBean.getPersonal().setCollectStatus(0);
                    dealBtnBuy(courseDetailBean.getPersonal());
                }
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(CourseDetailActivity.this);
                ToastUtils.show(CourseDetailActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(CourseDetailActivity.this);
                ToastUtils.show(CourseDetailActivity.this, expectionMsg);
            }
        });
    }

    private void toBuy() {
        Intent intent = new Intent(CourseDetailActivity.this, BuyLessonActivity.class);
        RouteUtil.startNewActivityAndResult(CourseDetailActivity.this, intent, BUY_LESSON_CODE);
    }

    private void toOpenVip() {
        Intent intent = new Intent(CourseDetailActivity.this, VipDetailActivity.class);
        RouteUtil.startNewActivityAndResult(CourseDetailActivity.this, intent, OPEN_VIP_CODE);
    }

    private void toLogin() {
        Intent intent = new Intent(CourseDetailActivity.this, LoginActivity.class);
        RouteUtil.startNewActivityAndResult(CourseDetailActivity.this, intent, LOGIN_CODE);
    }

    @Override
    public void getCourseDetailSuc(CourseDetailBean courseDetailBean) {

        dealCourseDetail(courseDetailBean);
    }

    @Override
    public void getCourseNumberSuc(String s) {
    }

    @Override
    public void getPersonnalCourseDetail(PersonNalCourseDetailBean personNalCourseDetailBean) {
        this.personNalCourseDetailBean = personNalCourseDetailBean;
        //  dealBtnBuy(personNalCourseDetailBean);
    }

    @Override
    public void increaseVideoLookNumberSuc() {
        Log.e(TAG, "上报观看记录成功");
    }

    @Override
    public void getCourseChapterSuc(List<CatalogBean> s) {
        if (s != null && !s.isEmpty()) {
            catalogList = new ArrayList<>();
            catalogList.addAll(s);
            initCatalog();
        }
    }

    void dealBtnBuy(CourseDetailBean.PersonalBean personNalCourseDetailBean) {
        if (personNalCourseDetailBean == null) {
            return;
        }
        tvCourseTime.setText("视频时长:" + personNalCourseDetailBean.getVideoDuration());
        tvCourseName.setText(personNalCourseDetailBean.getVideoName());

        tvBuy.setVisibility(View.VISIBLE);
        if (CourseUtil.isOk(personNalCourseDetailBean.getCollectStatus())) {
            ivLike.setImageResource(R.drawable.likeed);
        } else {
            ivLike.setImageResource(R.drawable.course_like);
        }
        if (App.getInstance().isLogin(this)) {
            if (personNalCourseDetailBean.getPurchaseStatus() == 0) {
                tvBuy.setText("开始学习");

            } else if (personNalCourseDetailBean.getPurchaseStatus() == 1) {
                App.getInstance().getUserInfo(this, new UserInfoCallback() {
                    @Override
                    public void getUserInfo(UserInfo userInfo) {
                        if (userInfo.isVip()) {
                            tvBuy.setText("开始学习");
                        } else {
                            tvBuy.setText("VIP免费");
                        }
                    }
                });

            } else if (personNalCourseDetailBean.getPurchaseStatus() == 2) {
                tvBuy.setText("开始学习");
            } else if (personNalCourseDetailBean.getPurchaseStatus() == 3) {
                tvBuy.setText("立即购买");
            }
        } else {
            tvBuy.setText("开始学习");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    private CourseDetailBean courseDetailBean;

    private void dealCourseDetail(CourseDetailBean c) {
        courseDetailBean = c;

        if (courseDetailBean == null) {
            return;
        }
        tvTeacherDesc.setText(courseDetailBean.getDetail().getCurriculumDetail());
        tvTeacherName.setText(courseDetailBean.getDetail().getNickname());
        App.getInstance().getDefaultSetting(this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                Glide.with(CourseDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), courseDetailBean.getDetail().getHeadImg())).into(ivTeacherHeadimg);
                Glide.with(CourseDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), courseDetailBean.getDetail().getCurriculumImage())).into(ivCourseCorver);

            }
        });

        if (CourseUtil.isVipSpecial(courseDetailBean.getDetail().getSpecialStatus())) {
            ivVip.setImageResource(R.drawable.vipzx);
        }

        tvPrice.setText(courseDetailBean.getDetail().getCurriculumPrice() + "");
        tvCourseDesc.setContent(courseDetailBean.getDetail().getCurriculumDetail());
        tvTeacherDesc.setContent(courseDetailBean.getDetail().getIntro());
        dealBtnBuy(courseDetailBean.getPersonal());
        tvStudyNum.setText(courseDetailBean.getParticipant().getValue() + "人学过");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
