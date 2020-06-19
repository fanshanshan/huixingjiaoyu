package com.qulink.hxedu.ui.course;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.qulink.hxedu.ui.SendVipActivity;
import com.qulink.hxedu.ui.VipDetailActivity;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.video.MyJzvdStd;
import com.qulink.hxedu.video.VideoStatuListener;
import com.qulink.hxedu.view.MyScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;
import de.hdodenhof.circleimageview.CircleImageView;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

//0:免费,1:vip,2,已购,3,收费
public class CourseDetailActivity extends BaseActivity implements CourseDetailContract.View, VideoStatuListener {

    @BindView(R.id.ll_like)
    LinearLayout llLike;
    @BindView(R.id.jz_video)
    MyJzvdStd jzVideo;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.sc)
    MyScrollView sc;
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


    private void setCorverImg(CourseDetailBean courseDetailBean) {
        App.getInstance().getDefaultSetting(this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                // jzvdStd.posterImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
                Glide.with(CourseDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), courseDetailBean.getDetail().getCurriculumImage())).into(jzVideo.posterImageView);
            }
        });
    }

    @Override
    protected void init() {
        courseId = getIntent().getIntExtra("courseId", 0);
        exChange = getIntent().getBooleanExtra("exChange", false);
        mPresenter = new CourseDetailPresenter();
        mPresenter.attachView(this);
        mPresenter.getCourseDetail(courseId);
      //  mPresenter.getCourseChapter(courseId);
//        mPresenter.getPersonnalCourseDetail(courseId);
//        mPresenter.getCourseLookNumberNameById(courseId);
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

    class CatalogItem implements AdapterItem<CatalogBean> {
        TextView tvNumber;
        TextView tvTitle;
        LinearLayout llRoot;

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
            llRoot = root.findViewById(R.id.ll_root);

        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(CatalogBean o, int position) {

            tvTitle.setText(o.getChapterName().toString());
            tvNumber.setText("第" + o.getChapterId() + "讲");
            if (o.isPlaying()) {
                tvTitle.setTextColor(ContextCompat.getColor(CourseDetailActivity.this, R.color.theme_color));
                tvNumber.setTextColor(ContextCompat.getColor(CourseDetailActivity.this, R.color.theme_color));
            } else {
                tvTitle.setTextColor(ContextCompat.getColor(CourseDetailActivity.this, R.color.black));
                tvNumber.setTextColor(ContextCompat.getColor(CourseDetailActivity.this, R.color.black));

            }
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (jzVideo.getCatalogList() == null) {
                        jzVideo.setCatalogList(catalogList);
                    }
                    jzVideo.changePlayIndex(position);
                    setCatalogStatus(position);
                    if (recycleCourseCatalog.getAdapter() != null) {
                        recycleCourseCatalog.getAdapter().notifyDataSetChanged();
                    }
                    sc.smoothScrollTo(0,0);

                }
            });
        }
    }

    ;

    void initCatalog() {

        commonRcvAdapter = new CommonRcvAdapter<CatalogBean>(catalogList) {

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new CatalogItem();
            }
        };
        recycleCourseCatalog.setAdapter(commonRcvAdapter);
        recycleCourseCatalog.setLayoutManager(new LinearLayoutManager(this));
    }


    @Override
    protected boolean enableGestureBack() {
        return false;
    }

    @OnClick({R.id.tv_price, R.id.ll_like, R.id.ll_course_catalog, R.id.ll_course_detail, R.id.tv_next, R.id.tv_share, R.id.iv_like, R.id.tv_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_price:
                ToastUtils.show(this, "wcaonima");
                break;
            case R.id.ll_course_catalog:
                chooseCourseCatalog();
                break;
            case R.id.ll_course_detail:
                chooseCourseDetail();
                break;
            case R.id.tv_next:
                if (courseDetailBean.getPersonal().getPurchaseStatus() == 0) {
                    startPlay();
                } else if (courseDetailBean.getPersonal().getPurchaseStatus() == 1) {
                    //不是vip 提示开通vip 已经是vip 开始播放
                    App.getInstance().getUserInfo(this, new UserInfoCallback() {
                        @Override
                        public void getUserInfo(UserInfo userInfo) {
                            if (userInfo.isVip()) {
                                startPlay();
                            } else {
                                showOpenVipDialog();
                            }
                        }
                    });
                } else if (courseDetailBean.getPersonal().getPurchaseStatus() == 2) {
                    startPlay();
                } else if (courseDetailBean.getPersonal().getPurchaseStatus() == 3) {
                    goToBuyLessonPage();
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
        try {
            if(courseDetailBean.getPersonal().getStudyStatus()==0){
                mPresenter.increaseVideoLookNumber(courseId);
            }
        }catch (Exception e){

        }
        if (catalogList == null) {
            getCourseCatalog();
        } else {
            jzVideo.setCatalogList(catalogList);
            jzVideo.setPlayCurrentIndex(0);
            jzVideo.mStartPlay();
            setCatalogStatus(0);
            if(recycleCourseCatalog.getAdapter()!=null){
                recycleCourseCatalog.getAdapter().notifyDataSetChanged();
            }
           // sc.smoothScrollTo(0,0);
        }
    }

    private void setCatalogStatus(int index) {
        for (CatalogBean c : catalogList) {

        }
        for (int i = 0; i < catalogList.size(); i++) {
            if (i == index) {
                catalogList.get(i).setPlaying(true);
            } else {
                catalogList.get(i).setPlaying(false);
            }
        }
    }

    private void getCourseCatalog() {
        DialogUtil.showLoading(this, false);
        ApiUtils.getInstance().getCourseChapter(courseId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(CourseDetailActivity.this);
                List<CatalogBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<CatalogBean>>() {
                }.getType());
                if (hotArticalList != null && !hotArticalList.isEmpty()) {
                    catalogList = new ArrayList<>();
                    catalogList.addAll(hotArticalList);
                    jzVideo.setCatalogList(catalogList);
                    jzVideo.setPlayCurrentIndex(0);
                    jzVideo.mStartPlay();

                    setCatalogStatus(0);
                    if (recycleCourseCatalog.getAdapter() != null) {
                        recycleCourseCatalog.getAdapter().notifyDataSetChanged();
                    }
                    initCatalog();
                }

            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(CourseDetailActivity.this, msg);
                DialogUtil.hideLoading(CourseDetailActivity.this);

            }

            @Override
            public void expcetion(String expectionMsg) {
                ToastUtils.show(CourseDetailActivity.this, expectionMsg);
                DialogUtil.hideLoading(CourseDetailActivity.this);


            }
        });
    }

    private void showOpenVipDialog() {
        DialogUtil.showAlertDialog(this, "提示", "该课程会员才能观看，是否开通会员？", "立即开通", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToVipPage();
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
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
                    ivLike.setImageResource(R.drawable.likeed);

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
                    ivLike.setImageResource(R.drawable.course_like);
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
        // this.personNalCourseDetailBean = personNalCourseDetailBean;
        //  dealBtnBuy(personNalCourseDetailBean);
    }

    @Override
    public void increaseVideoLookNumberSuc() {
        Log.e(TAG, "上报观看记录成功");
        if(courseDetailBean!=null){
            courseDetailBean.getPersonal().setStudyStatus(1);
        }
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
        if (courseDetailBean.getPersonal().getPurchaseStatus() == 0) {
            tvNext.setText("开始学习");
            jzVideo.setVideoType(1);

        } else if (courseDetailBean.getPersonal().getPurchaseStatus() == 1) {
            App.getInstance().getUserInfo(CourseDetailActivity.this, new UserInfoCallback() {
                @Override
                public void getUserInfo(UserInfo userInfo) {
                    if (userInfo.isVip()) {
                        jzVideo.setVideoType(1);
                        tvNext.setText("开始学习");
                    } else {
                        jzVideo.setVideoType(2);
                        tvNext.setText("开通会员");
                    }
                }
            });

        } else if (courseDetailBean.getPersonal().getPurchaseStatus() == 2) {
            jzVideo.setVideoType(1);
            tvNext.setText("开始学习");
        } else if (courseDetailBean.getPersonal().getPurchaseStatus() == 3) {
            jzVideo.setVideoType(3);
            tvNext.setText("立即购买");
        }
        tvNext.setVisibility(View.VISIBLE);
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
        jzVideo.destroy();
    }

    private CourseDetailBean courseDetailBean;

    private void dealCourseDetail(CourseDetailBean c) {
        courseDetailBean = c;

        if (courseDetailBean == null) {
            return;
        }

        setCorverImg(courseDetailBean);

        tvTeacherDesc.setText(courseDetailBean.getDetail().getCurriculumDetail());
        tvTeacherName.setText(courseDetailBean.getDetail().getNickname());
        App.getInstance().getDefaultSetting(this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                Glide.with(CourseDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), courseDetailBean.getDetail().getHeadImg())).into(ivTeacherHeadimg);
                Glide.with(CourseDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), courseDetailBean.getDetail().getCurriculumImage())).into(ivCourseCorver);

            }
        });

        if (courseDetailBean.getPersonal().getPurchaseStatus() == 1) {
            ivVip.setImageResource(R.drawable.vipzx);
        }

        jzVideo.setVideoStatuListener(this);
        jzVideo.setCourseId(courseId);
        tvPrice.setText(courseDetailBean.getDetail().getCurriculumPrice() + "");
        tvCourseDesc.setContent(courseDetailBean.getDetail().getCurriculumDetail());
        tvTeacherDesc.setContent(courseDetailBean.getDetail().getIntro());
        dealBtnBuy(courseDetailBean.getPersonal());
        tvStudyNum.setText(courseDetailBean.getParticipant().getValue() + "人学过");
        tvCourseTime.setText("视频时长:" + courseDetailBean.getPersonal().getVideoDuration());
        tvCourseName.setText(courseDetailBean.getPersonal().getVideoName());

        if (CourseUtil.isOk(courseDetailBean.getPersonal().getCollectStatus())) {
            ivLike.setImageResource(R.drawable.likeed);
        } else {
            ivLike.setImageResource(R.drawable.course_like);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == BUY_LESSON_CODE) {
                jzVideo.dealCourseAndResume();

            } else if (requestCode == OPEN_VIP_CODE) {
                jzVideo.dealCourseAndResume();
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (Jzvd.backPress()) {
                return true;
            }

        }
       return super.onKeyDown(keyCode,event);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void openVip() {
        goToVipPage();
    }

    @Override
    public void buyLesson() {
        goToBuyLessonPage();
    }

    @Override
    public void startClick() {
        getCourseCatalog();
    }

    private void goToVipPage() {
        Intent intent = new Intent(CourseDetailActivity.this, SendVipActivity.class);
        intent.putExtra("withResult", true);
        intent.putExtra("type", "buy");
        startActivityForResult(intent, OPEN_VIP_CODE);
    }

    private void goToBuyLessonPage() {
        Intent intent = new Intent(CourseDetailActivity.this, BuyLessonActivity.class);
        intent.putExtra("withResult", true);
        startActivityForResult(intent, OPEN_VIP_CODE);
    }

}
