package com.qulink.hxedu.ui.score;

import android.app.Dialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alipay.sdk.app.PayTask;
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
import com.qulink.hxedu.entity.CatalogBean;
import com.qulink.hxedu.entity.CourseDetailBean;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.entity.PayWxBean;
import com.qulink.hxedu.entity.PersonNalCourseDetailBean;
import com.qulink.hxedu.entity.ShopCourseDetailBean;
import com.qulink.hxedu.mvp.contract.CourseDetailContract;
import com.qulink.hxedu.mvp.presenter.CourseDetailPresenter;
import com.qulink.hxedu.pay.PayResult;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.video.MyJzvdStd;
import com.qulink.hxedu.video.VideoStatuListener;
import com.qulink.hxedu.view.MyScrollView;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.apache.commons.lang3.StringEscapeUtils;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jzvd.Jzvd;
import de.hdodenhof.circleimageview.CircleImageView;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

//0:免费,1:vip,2,已购,3,收费
public class ShopCourseDetailActivity extends BaseActivity implements CourseDetailContract.View, VideoStatuListener {

    @BindView(R.id.ll_like)
    LinearLayout llLike;
    @BindView(R.id.jz_video)
    MyJzvdStd jzVideo;
    @BindView(R.id.tv_next)
    TextView tvNext;
    @BindView(R.id.tv_new_price)
    TextView tvNewPrice;
    @BindView(R.id.ll_new_price)
    LinearLayout llNewPrice;
    @BindView(R.id.tv_old_price)
    TextView tvOldPrice;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_shop_course_detail;
    }


    private void setCorverImg(CourseDetailBean courseDetailBean) {
        App.getInstance().getDefaultSetting(this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                // jzvdStd.posterImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
                Glide.with(ShopCourseDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), courseDetailBean.getDetail().getCurriculumImage())).into(jzVideo.posterImageView);
            }
        });
    }

    @Override
    protected void init() {
        courseId = getIntent().getIntExtra("courseId", 0);
        mPresenter = new CourseDetailPresenter();
        mPresenter.attachView(this);
        mPresenter.getCourseDetail(courseId);
        mPresenter.getCourseChapter(courseId);
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
                tvTitle.setTextColor(ContextCompat.getColor(ShopCourseDetailActivity.this, R.color.theme_color));
                tvNumber.setTextColor(ContextCompat.getColor(ShopCourseDetailActivity.this, R.color.theme_color));
            } else {
                tvTitle.setTextColor(ContextCompat.getColor(ShopCourseDetailActivity.this, R.color.black));
                tvNumber.setTextColor(ContextCompat.getColor(ShopCourseDetailActivity.this, R.color.black));

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

    @OnClick({R.id.ll_like, R.id.ll_course_catalog, R.id.ll_course_detail, R.id.tv_next, R.id.tv_share, R.id.iv_like, R.id.tv_download})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_course_catalog:
                chooseCourseCatalog();
                break;
            case R.id.ll_course_detail:
                chooseCourseDetail();
                break;
            case R.id.tv_next:
                if (shopCourseDetailBean == null) {
                    return;
                }
                if (shopCourseDetailBean.getIsExchanged() == 0) {
                    showChooseWayDialog();
                } else {
                    startPlay();
                }
                break;
            case R.id.tv_share:
                break;
            case R.id.ll_like:
                if (App.getInstance().isLogin(ShopCourseDetailActivity.this, true)) {
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
            recycleCourseCatalog.getAdapter().notifyDataSetChanged();
            sc.smoothScrollTo(0,0);
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
                DialogUtil.hideLoading(ShopCourseDetailActivity.this);
                List<CatalogBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<CatalogBean>>() {
                }.getType());
                if (hotArticalList != null && !hotArticalList.isEmpty()) {
                    catalogList = new ArrayList<>();
                    catalogList.addAll(hotArticalList);
                    jzVideo.setCatalogList(catalogList);
                    jzVideo.setPlayCurrentIndex(0);
                    jzVideo.mStartPlay();
                    sc.smoothScrollTo(0,0);

                    setCatalogStatus(0);
                    if (recycleCourseCatalog.getAdapter() != null) {
                        recycleCourseCatalog.getAdapter().notifyDataSetChanged();
                    }
                    initCatalog();
                }

            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(ShopCourseDetailActivity.this, msg);
                DialogUtil.hideLoading(ShopCourseDetailActivity.this);

            }

            @Override
            public void expcetion(String expectionMsg) {
                ToastUtils.show(ShopCourseDetailActivity.this, expectionMsg);
                DialogUtil.hideLoading(ShopCourseDetailActivity.this);


            }
        });
    }


    void collectionVideo() {
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().collectionCourse(courseId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(ShopCourseDetailActivity.this);
                if (courseDetailBean != null) {
                    courseDetailBean.getPersonal().setCollectStatus(1);

                    ivLike.setImageResource(R.drawable.likeed);

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

    void cancelCollection() {
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().cancelCollectionCourse(courseId + "", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(ShopCourseDetailActivity.this);
                if (courseDetailBean != null) {
                    courseDetailBean.getPersonal().setCollectStatus(0);
                    ivLike.setImageResource(R.drawable.course_like);
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
        if(courseDetailBean!=null&&courseDetailBean.getPersonal()!=null){
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
        EventBus.getDefault().unregister(this);
        jzVideo.destroy();

        mPresenter.detachView();
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
                Glide.with(ShopCourseDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), courseDetailBean.getDetail().getHeadImg())).into(ivTeacherHeadimg);
                Glide.with(ShopCourseDetailActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), courseDetailBean.getDetail().getCurriculumImage())).into(ivCourseCorver);

            }
        });

        if (courseDetailBean.getPersonal().getPurchaseStatus() == 1) {
            ivVip.setImageResource(R.drawable.vipzx);
        }

        jzVideo.setVideoStatuListener(this);
        jzVideo.setCourseId(courseId);
        tvCourseDesc.setContent(courseDetailBean.getDetail().getCurriculumDetail());
        tvTeacherDesc.setContent(courseDetailBean.getDetail().getIntro());

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
        showChooseWayDialog();
    }

    @Override
    public void buyLesson() {
    }

    @Override
    public void startClick() {
        getCourseCatalog();
    }


    private ShopCourseDetailBean shopCourseDetailBean;

    private void getCourseDetail() {
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().getShopCourseDetail(courseId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(ShopCourseDetailActivity.this);
                shopCourseDetailBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), ShopCourseDetailBean.class);
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
                if (shopCourseDetailBean.getIsExchanged() == 0) {
                    tvNext.setText("立即兑换");
                    jzVideo.setVideoType(4);
                } else {
                    tvNext.setText("开始学习");
                    jzVideo.setVideoType(1);

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

    private void exchange(int payType) {
        DialogUtil.showLoading(this, true, "请稍候");
        ApiUtils.getInstance().exchangeCourse(courseId, payType, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(ShopCourseDetailActivity.this);
                if (payType == 0) {
                    ToastUtils.show(ShopCourseDetailActivity.this, "兑换成功");
                    dealExchangeSuc();
                } else {
                    if (payType == 1) {
                        payAli(t.getData().toString());
                    } else {
                        String str = StringEscapeUtils.unescapeJava(t.getData().toString());
                        str = str.replace("\"{", "{");
                        str = str.replace("}\"", "}");
                        PayWxBean payWxBean = GsonUtil.GsonToBean(str, PayWxBean.class);

                        payWx(payWxBean);
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


    private void showChooseWayDialog() {
        Dialog bottomDialog;

        //创建dialog,同时设置dialog主题
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        //绘制dialog  UI视图
        View contentView = LayoutInflater.from(this).inflate(R.layout.pay_activity_way, null);

        RelativeLayout rlAli = contentView.findViewById(R.id.rl_ali);
        RelativeLayout rlwx = contentView.findViewById(R.id.rl_wx);
        RelativeLayout rlBanl = contentView.findViewById(R.id.rl_bank);
        LinearLayout llAdd = contentView.findViewById(R.id.ll_add_bank);
        ImageView ivClose = contentView.findViewById(R.id.iv_close);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
            }
        });

        //给dialog添加view
        bottomDialog.setContentView(contentView);
        //为绘制的view设置参数
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        //设置为全屏的宽
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        //设置dialog位置
        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        //添加进出场动画
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        //允许点击外部退出dialog
        bottomDialog.setCanceledOnTouchOutside(true);
        //show  dialog
        bottomDialog.show();

        rlAli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();
                exchange(1);
            }
        });
        rlwx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bottomDialog.dismiss();

                exchange(2);
            }
        });
        rlBanl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(ShopCourseDetailActivity.this, "暂未开放");
            }
        });
        llAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(ShopCourseDetailActivity.this, "暂未开放");
            }
        });

    }

    private void payAli(String orderInfo) {
        DialogUtil.hideLoading(ShopCourseDetailActivity.this);
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayTask alipay = new PayTask(ShopCourseDetailActivity.this);
                Map<String, String> result = alipay.payV2(orderInfo, true);

                Message msg = new Message();
                msg.what = ALIPAY_SUCCESS_CODE;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private IWXAPI api;

    private void regToWx() {
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, FinalValue.WECHAT_APP_ID, true);

        // 将应用的appId注册到微信
        api.registerApp(FinalValue.WECHAT_APP_ID);
//        //建议动态监听微信启动广播进行注册到微信
//        broadcastReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//
//                // 将该app注册到微信
//                api.registerApp(FinalValue.WECHAT_APP_ID);
//            }
//        };
//        registerReceiver(broadcastReceiver, new IntentFilter(ConstantsAPI.ACTION_REFRESH_WXAPP));


    }

    private void payWx(PayWxBean payWxBean) {
        if (api == null) {
            regToWx();
        }
        PayReq request = new PayReq();

        request.appId = payWxBean.getAppid();//子商户appid

        request.partnerId = payWxBean.getPartnerid();//子商户号

        request.prepayId = payWxBean.getPrepayid();

        request.packageValue = "Sign=WXPay";

        request.nonceStr = payWxBean.getNoncestr();

        request.timeStamp = payWxBean.getTimestamp();

        request.sign = payWxBean.getTimestamp();

        api.sendReq(request);
    }


    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case ALIPAY_SUCCESS_CODE:
                    PayResult payResult = new PayResult((Map<String, String>) msg.obj);
                    /**
                     * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
                     */
                    String resultInfo = payResult.getResult();// 同步返回需要验证的信息
                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为9000则代表支付成功
                    if (TextUtils.equals(resultStatus, "9000")) {
                        // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                        // PayResultDetail payResultDetail = GsonUtil.GsonToBean(resultInfo, PayResultDetail.class);
                        dealExchangeSuc();
                        ToastUtils.show(ShopCourseDetailActivity.this, "兑换成功");

                    } else {
                        // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                        ToastUtils.show(ShopCourseDetailActivity.this, resultInfo);
                    }
                    break;
                case WX_SUCCESS_CODE:
                    ToastUtils.show(ShopCourseDetailActivity.this, "兑换成功");
                    dealExchangeSuc();
                    break;
            }

        }

        ;
    };


    private void dealExchangeSuc() {
        jzVideo.dealCourseAndResume();
        if (shopCourseDetailBean != null) {
            shopCourseDetailBean.setExchangeType(1);
            tvNext.setText("开始学习");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void loginSuccess(MessageEvent messageEvent) {
        if (messageEvent.getCode() == FinalValue.WECHAT_PAYEOK) {
            mHandler.sendEmptyMessage(WX_SUCCESS_CODE);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    private final int ALIPAY_SUCCESS_CODE = 111;
    private final int WX_SUCCESS_CODE = 222;
}
