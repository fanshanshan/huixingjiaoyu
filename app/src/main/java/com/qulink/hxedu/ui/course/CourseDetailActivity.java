package com.qulink.hxedu.ui.course;

import android.content.Intent;
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
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_course_detail;
    }

    @Override
    protected void init() {
        courseId = getIntent().getIntExtra("courseId", 0);
        mPresenter = new CourseDetailPresenter();
        mPresenter.attachView(this);
        mPresenter.getCourseDetail(courseId);
        mPresenter.getPersonnalCourseDetail(courseId);
        mPresenter.getCourseLookNumberNameById(courseId);
        chooseCourseDetail();

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
            initCatalog();
        }
        courseCatalogIndicator.setVisibility(View.VISIBLE);
        courseDetailIndicator.setVisibility(View.GONE);
        courseCatalogIndicator.setBackgroundColor(0Xff5DA0FF);
        tvCourseCatalog.setTextColor(0Xff5DA0FF);
        tvCourseDetail.setTextColor(0Xff999999);
        llCourseDesc.setVisibility(View.GONE);
        recycleCourseCatalog.setVisibility(View.VISIBLE);
    }


    private List<String> catalogList;
    private CommonRcvAdapter<String> commonRcvAdapter;

    void initCatalog() {
        catalogList = new ArrayList<>();
        catalogList.add("今天你吃饭了吗");
        catalogList.add("中午没吃");
        catalogList.add("为啥不吃");
        catalogList.add("减肥呢");
        catalogList.add("然后呢");
        catalogList.add("晚上要撸串");
        commonRcvAdapter = new CommonRcvAdapter<String>(catalogList) {
            TextView tvNumber;
            TextView tvTitle;

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AdapterItem() {
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
                    public void handleData(Object o, int position) {

                        tvTitle.setText(o.toString());
                        tvNumber.setText("第" + (position + 1) + "讲");
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

    @OnClick({R.id.ll_like,R.id.ll_course_catalog, R.id.ll_course_detail, R.id.tv_buy, R.id.tv_share, R.id.iv_like, R.id.tv_download})
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
                if(App.getInstance().isLogin(CourseDetailActivity.this,true)){
                    collectionVideo();
                }
                break;
            case R.id.tv_download:
                break;
        }
    }

    private void startPlay() {
        mPresenter.increaseVideoLookNumber(courseId);
    }

    void collectionVideo(){

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
        tvStudyNum.setText(s + "人学过");
    }

    @Override
    public void getPersonnalCourseDetail(PersonNalCourseDetailBean personNalCourseDetailBean) {
        this.personNalCourseDetailBean = personNalCourseDetailBean;
        dealBtnBuy(personNalCourseDetailBean);
    }

    @Override
    public void increaseVideoLookNumberSuc() {
        Log.e(TAG,"上报观看记录成功");
    }

    void dealBtnBuy(PersonNalCourseDetailBean personNalCourseDetailBean) {
        if (personNalCourseDetailBean == null) {
            return;
        }
        tvCourseTime.setText("视频时长:" + personNalCourseDetailBean.getVideoDuration());
        tvCourseName.setText(personNalCourseDetailBean.getVideoName());

        tvBuy.setVisibility(View.VISIBLE);
        if (CourseUtil.isOk(personNalCourseDetailBean.getCollectStatus())) {
            ivLike.setImageResource(R.drawable.likeed);
        }else{
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

    private void dealCourseDetail(CourseDetailBean courseDetailBean) {
        if (courseDetailBean == null) {
            return;
        }
        tvTeacherDesc.setText(courseDetailBean.getCurriculumDetail());
        tvTeacherName.setText(courseDetailBean.getNickname());
        App.getInstance().getDefaultSetting(this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                Glide.with(CourseDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), courseDetailBean.getHeadImg())).into(ivTeacherHeadimg);
                Glide.with(CourseDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), courseDetailBean.getCurriculumImage())).into(ivCourseCorver);

            }
        });

        if (CourseUtil.isVipSpecial(courseDetailBean.getSpecialStatus())) {
            ivVip.setImageResource(R.drawable.vipzx);
        }

        tvPrice.setText(courseDetailBean.getCurriculumPrice() + "");
        tvCourseDesc.setContent(courseDetailBean.getCurriculumDetail());
        tvTeacherDesc.setContent(courseDetailBean.getIntro());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


}
