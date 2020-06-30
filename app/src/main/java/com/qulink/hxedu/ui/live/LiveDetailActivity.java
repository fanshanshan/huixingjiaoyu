package com.qulink.hxedu.ui.live;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import com.qulink.hxedu.entity.LiveInfoBean;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.mvp.contract.LiveContract;
import com.qulink.hxedu.mvp.presenter.LivePresenter;
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
public class LiveDetailActivity extends BaseActivity implements LiveContract.View, VideoStatuListener {

    @BindView(R.id.jz_video)
    MyJzvdStd jzVideo;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.sc)
    MyScrollView sc;
    @BindView(R.id.tv_like_num)
    TextView tvLikeNum;
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

    LivePresenter mPresenter;
    int courseId;
    @BindView(R.id.tv_price)
    TextView tvPrice;

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.iv_vip)
    ImageView ivVip;

    private int LOGIN_CODE = 1;
    private int BUY_LESSON_CODE = 2;
    private int OPEN_VIP_CODE = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_live_detail;
    }


    private void setCorverImg(CourseDetailBean courseDetailBean) {
        App.getInstance().getDefaultSetting(this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                // jzvdStd.posterImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
                Glide.with(LiveDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), courseDetailBean.getDetail().getCurriculumImage())).into(jzVideo.posterImageView);
            }
        });
    }

    @Override
    protected void init() {
        courseId = getIntent().getIntExtra("liveId", 0);
        mPresenter = new LivePresenter();
        mPresenter.attachView(this);
        mPresenter.getLiveDetail(courseId);
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
            mPresenter.getLiveCatalog(courseId);
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

    @Override
    public void getLiveDetailSuc(LiveInfoBean liveInfoBean) {
        dealCourseDetail(liveInfoBean);
    }

    @Override
    public void getCatalogSuc(LiveInfoBean liveInfoBean) {

    }

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
                tvTitle.setTextColor(ContextCompat.getColor(LiveDetailActivity.this, R.color.theme_color));
                tvNumber.setTextColor(ContextCompat.getColor(LiveDetailActivity.this, R.color.theme_color));
            } else {
                tvTitle.setTextColor(ContextCompat.getColor(LiveDetailActivity.this, R.color.black));
                tvNumber.setTextColor(ContextCompat.getColor(LiveDetailActivity.this, R.color.black));

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
                    sc.smoothScrollTo(0, 0);

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

    @OnClick({R.id.tv_price,  R.id.ll_course_catalog, R.id.ll_course_detail, R.id.tv_next})
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
                break;
            case R.id.tv_share:
                break;

            case R.id.tv_download:
                break;
        }
    }

    private void startPlay() {

        if (catalogList == null) {
            getCourseCatalog();
        } else {
            jzVideo.setCatalogList(catalogList);
            jzVideo.setPlayCurrentIndex(0);
            jzVideo.mStartPlay();
            setCatalogStatus(0);
            if (recycleCourseCatalog.getAdapter() != null) {
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
                DialogUtil.hideLoading(LiveDetailActivity.this);
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
                ToastUtils.show(LiveDetailActivity.this, msg);
                DialogUtil.hideLoading(LiveDetailActivity.this);

            }

            @Override
            public void expcetion(String expectionMsg) {
                ToastUtils.show(LiveDetailActivity.this, expectionMsg);
                DialogUtil.hideLoading(LiveDetailActivity.this);


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


    private void toBuy() {
        Intent intent = new Intent(LiveDetailActivity.this, BuyLessonActivity.class);
        RouteUtil.startNewActivityAndResult(LiveDetailActivity.this, intent, BUY_LESSON_CODE);
    }

    private void toOpenVip() {
        Intent intent = new Intent(LiveDetailActivity.this, VipDetailActivity.class);
        RouteUtil.startNewActivityAndResult(LiveDetailActivity.this, intent, OPEN_VIP_CODE);
    }

    private void toLogin() {
        Intent intent = new Intent(LiveDetailActivity.this, LoginActivity.class);
        RouteUtil.startNewActivityAndResult(LiveDetailActivity.this, intent, LOGIN_CODE);
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

    private LiveInfoBean liveInfoBean;

    private void dealCourseDetail(LiveInfoBean c) {
        liveInfoBean = c;

        if (liveInfoBean == null) {
            return;
        }


        tvTeacherDesc.setText(liveInfoBean.getTeacherIntro());
        tvTeacherName.setText(liveInfoBean.getTeacherName());
        App.getInstance().getDefaultSetting(this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                Glide.with(LiveDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), liveInfoBean.getCoverUrl())).into(ivTeacherHeadimg);
                Glide.with(LiveDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), liveInfoBean.getCoverUrl())).into(ivCourseCorver);

            }
        });

        if (liveInfoBean.getType() == 1) {
            ivVip.setImageResource(R.drawable.vipzx);
        }

        jzVideo.setVideoStatuListener(this);
        jzVideo.setCourseId(courseId);
        tvPrice.setText(liveInfoBean.getPayAmount() + "");
        tvCourseDesc.setContent(liveInfoBean.getIntro());
        tvStudyNum.setText(liveInfoBean.getParticipantTotal() + "人观看");
        tvLikeNum.setText(liveInfoBean.getParticipantTotal() + "人点赞");
        tvCourseTime.setText(liveInfoBean.getStartTime());
        tvCourseName.setText(liveInfoBean.getTeacherName());


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
        return super.onKeyDown(keyCode, event);
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
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
            handler.sendEmptyMessageDelayed(HIDE_BACK_CODE, 2000);
        }
    }

    @Override
    public void clickSurface() {
        showBackBtn();

        if (handler != null) {
            handler.sendEmptyMessageDelayed(HIDE_BACK_CODE, 2000);

        }
    }

    @Override
    public void onVideoPause() {
        showBackBtn();
    }

    private final int HIDE_BACK_CODE = 222;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case HIDE_BACK_CODE:
                    hideBackBtn();
                    break;
            }
        }
    };

    private void showBackBtn() {
        ivBack.setVisibility(View.VISIBLE);
    }

    private void hideBackBtn() {


        ivBack.setVisibility(View.GONE);

    }

    @Override
    public void playNext(int position) {

        setCatalogStatus(position);
        if (recycleCourseCatalog.getAdapter() != null) {
            recycleCourseCatalog.getAdapter().notifyDataSetChanged();
        }
    }

    private void goToVipPage() {
        Intent intent = new Intent(LiveDetailActivity.this, SendVipActivity.class);
        intent.putExtra("withResult", true);
        intent.putExtra("type", "buy");
        startActivityForResult(intent, OPEN_VIP_CODE);
    }

    private void goToBuyLessonPage() {
        Intent intent = new Intent(LiveDetailActivity.this, BuyLessonActivity.class);
        intent.putExtra("withResult", true);
        startActivityForResult(intent, OPEN_VIP_CODE);
    }

}