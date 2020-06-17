package com.qulink.hxedu.ui.score;

import android.content.Intent;
import android.graphics.Paint;
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
import com.qulink.hxedu.entity.ShopCourseDetailBean;
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

public class ShopCourseDetailActivity extends BaseActivity implements CourseDetailContract.View {

    @BindView(R.id.tv_new_price)
    TextView tvNewPrice;
    @BindView(R.id.ll_new_price)
    LinearLayout llNewPrice;
    @BindView(R.id.tv_old_price)
    TextView tvOldPrice;
    @BindView(R.id.ll_like)
    LinearLayout llLike;
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
        return R.layout.activity_shop_course_detail;
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
        getCourseDetail();
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
                if(shopCourseDetailBean==null){
                    return;
                }
                if(shopCourseDetailBean.getExchangeType()==1){
                    //直接兑换
                    exchange();
                }else{
                    //去支付
                }
                break;
            case R.id.tv_share:
                break;
            case R.id.ll_like:
                if (App.getInstance().isLogin(ShopCourseDetailActivity.this, true)) {
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

    void collectionVideo() {

    }

    private void toBuy() {
        Intent intent = new Intent(ShopCourseDetailActivity.this, BuyLessonActivity.class);
        RouteUtil.startNewActivityAndResult(ShopCourseDetailActivity.this, intent, BUY_LESSON_CODE);
    }

    private void toOpenVip() {
        Intent intent = new Intent(ShopCourseDetailActivity.this, VipDetailActivity.class);
        RouteUtil.startNewActivityAndResult(ShopCourseDetailActivity.this, intent, OPEN_VIP_CODE);
    }

    private void toLogin() {
        Intent intent = new Intent(ShopCourseDetailActivity.this, LoginActivity.class);
        RouteUtil.startNewActivityAndResult(ShopCourseDetailActivity.this, intent, LOGIN_CODE);
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
        tvTeacherDesc.setText(courseDetailBean.getDetail().getCurriculumDetail());
        tvTeacherName.setText(courseDetailBean.getDetail().getNickname());
        App.getInstance().getDefaultSetting(this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                Glide.with(ShopCourseDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), courseDetailBean.getDetail().getHeadImg())).into(ivTeacherHeadimg);
                Glide.with(ShopCourseDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), courseDetailBean.getDetail().getCurriculumImage())).into(ivCourseCorver);

            }
        });

        if (CourseUtil.isVipSpecial(courseDetailBean.getDetail().getSpecialStatus())) {
            ivVip.setImageResource(R.drawable.vipzx);
        }

        tvCourseDesc.setContent(courseDetailBean.getDetail().getCurriculumDetail());
        tvTeacherDesc.setContent(courseDetailBean.getDetail().getIntro());
        dealBtnBuy(courseDetailBean.getPersonal());
        tvStudyNum.setText(courseDetailBean.getParticipant().getValue() + "人学过");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private  ShopCourseDetailBean shopCourseDetailBean;
    private void getCourseDetail() {
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().getShopCourseDetail(courseId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(ShopCourseDetailActivity.this);
                shopCourseDetailBean   = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), ShopCourseDetailBean.class);
                if (shopCourseDetailBean.getExchangeType() == 1) {
                    //积分
                    tvNewPrice.setText(shopCourseDetailBean.getIntegration() + "");
                } else if (shopCourseDetailBean.getExchangeType() == 2) {
                    if (shopCourseDetailBean.getPayAmount() != shopCourseDetailBean.getVipAmount()) {
                        ivVip.setImageResource(R.drawable.vip2);
                        tvOldPrice.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                        tvOldPrice.setText(shopCourseDetailBean.getIntegration() + "+￥" + shopCourseDetailBean.getPayAmount());
                        tvNewPrice.setText(shopCourseDetailBean.getIntegration() + "+￥" + shopCourseDetailBean.getVipAmount());
                    } else {
                        tvNewPrice.setText(shopCourseDetailBean.getIntegration() + "+￥" + shopCourseDetailBean.getPayAmount());

                    }
                }
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(ShopCourseDetailActivity.this);
                ToastUtils.show(ShopCourseDetailActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(ShopCourseDetailActivity.this);
                ToastUtils.show(ShopCourseDetailActivity.this, expectionMsg);

            }
        });
    }

    private void exchange(){
        DialogUtil.showLoading(this,true,"请稍候");
        ApiUtils.getInstance().exchangeCourse(courseId, 0, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(ShopCourseDetailActivity.this);
                ToastUtils.show(ShopCourseDetailActivity.this,"课程兑换成功");
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(ShopCourseDetailActivity.this);
                ToastUtils.show(ShopCourseDetailActivity.this,msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(ShopCourseDetailActivity.this);
                ToastUtils.show(ShopCourseDetailActivity.this,expectionMsg);
            }
        });
    }

}
