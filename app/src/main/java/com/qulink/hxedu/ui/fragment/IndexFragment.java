package com.qulink.hxedu.ui.fragment;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.CourseItemBean;
import com.qulink.hxedu.entity.CustomData;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.HotCourseBean;
import com.qulink.hxedu.entity.IndexBannerBean;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.mvp.contract.IndexInfoContract;
import com.qulink.hxedu.mvp.presenter.IndexInfoPresenter;
import com.qulink.hxedu.ui.CourseActivity;
import com.qulink.hxedu.ui.CourseDetailActivity;
import com.qulink.hxedu.ui.SearchActivity;
import com.qulink.hxedu.ui.sign.PlatformAccountSignActivity;
import com.qulink.hxedu.ui.sign.SignActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.MyScrollView;
import com.qulink.hxedu.view.SpacesItemDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment implements OnRefreshListener, IndexInfoContract.View {


    @BindView(R.id.iv_qiandao)
    ImageView ivQiandao;
    @BindView(R.id.ll_top_search_bg)
    LinearLayout llTopSearchBg;
    @BindView(R.id.recycle_hot_course)
    RecyclerView recycleHotCourse;
    @BindView(R.id.recycle_money_course)
    RecyclerView recycleMoneyCourse;
    @BindView(R.id.sc)
    MyScrollView sc;


    @BindView(R.id.iv_top_bg)
    ImageView ivTopBg;
    @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.ll_temp)
    LinearLayout llTemp;

    @BindView(R.id.convenientBanner)
    ConvenientBanner<IndexBannerBean> convenientBanner;
    @BindView(R.id.iv_search)
    ImageView ivSearch;

    @BindView(R.id.tv_more_hot_course)
    TextView tvMoreHotCourse;
    @BindView(R.id.tv_more_money_course)
    TextView tvMoreMoneyCourse;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    @BindView(R.id.recycle_course)
    RecyclerView recycleCourse;


    private IndexInfoPresenter presenter;

    public IndexFragment() {
        // Required empty public constructor
    }


    private View rootView;

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_index, container, false);
            ButterKnife.bind(this, rootView);
            presenter = new IndexInfoPresenter();
            presenter.attachView(this);
            presenter.getCourseItem();
            presenter.getBanner();
            presenter.getHotCourse();
            presenter.getMoneyCourse();
            smartLayout.setOnRefreshListener(this);
            smartLayout.setEnableLoadMore(false);
            addScrollviewListener();
            initView();

            getBannerFromServer();
        }
        return rootView;
    }


    private void initView() {
        App.getInstance().getUserInfo(getThisActivity(), new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                if (userInfo.isSign()) {
                    ivQiandao.setImageResource(R.drawable.qd);
                } else {
                    ivQiandao.setImageResource(R.drawable.wqd);
                }
            }
        });
    }


    /**
     * 为scrollview添加监听 滑动改变bar颜色
     */
    private void addScrollviewListener() {
        sc.setOnScrollListener(scrollY -> {
            final float ratio = (float) Math.min(Math.max(scrollY, 0), llTopSearchBg.getHeight()) / llTopSearchBg.getHeight();
            final int newAlpha = (int) (ratio * 255);
            llTopSearchBg.setBackgroundColor(Color.argb((int) newAlpha, 245, 245, 245));
        });
    }




    private void initMoneyCource() {
        //付费课程
        List<CustomData> moneyCourseData = new ArrayList<>();
        moneyCourseData.add(new CustomData("", 2345, 666, "VIP免费", "《管理3阶能力提升训练营》"));
        moneyCourseData.add(new CustomData("", 2345, 666, "VIP免费", "《管理3阶能力提升训练营》"));
        moneyCourseData.add(new CustomData("", 2345, 666, "VIP免费", "《管理3阶能力提升训练营》"));
        moneyCourseData.add(new CustomData("", 2345, 666, "VIP免费", "《管理3阶能力提升训练营》"));
        CommonRcvAdapter commonMoneyCourRcvAdapter = new CommonRcvAdapter<CustomData>(moneyCourseData) {

            TextView tvTitle;
            TextView tvJoinNum;
            TextView tvDesc;
            LinearLayout llRoot;
            TextView tvmonney;

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AdapterItem() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.index_course_item;
                    }

                    @Override
                    public void bindViews(@NonNull View root) {
                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        tvTitle = root.findViewById(R.id.tv_title);
                        tvDesc = root.findViewById(R.id.tv_desc);
                        tvmonney = root.findViewById(R.id.tv_money);
                        tvJoinNum = root.findViewById(R.id.tv_join_num);
                        llRoot = root.findViewById(R.id.ll_root);
                    }

                    @Override
                    public void setViews() {
                        llRoot.setOnClickListener(v -> RouteUtil.startNewActivity(getThisActivity(), new Intent(getThisActivity(), CourseDetailActivity.class)));
                    }

                    @SuppressLint("SetTextI18n")
                    @Override
                    public void handleData(Object o, int position) {
                        CustomData customData;
                        if (o instanceof CustomData) {
                            customData = (CustomData) o;
                            tvmonney.setText(customData.getPrice() + "");
                            tvDesc.setText(customData.getDesc());
                            tvTitle.setText(customData.getTitle());
                            tvJoinNum.setText(customData.getJoinNum() + "加入了学习");
                        }
                    }
                };
            }


        };
        GridLayoutManager layoutManager = new GridLayoutManager(getThisActivity(), 2);

        recycleMoneyCourse.addItemDecoration(new SpacesItemDecoration(12, 0, 0, 0));
        recycleMoneyCourse.setLayoutManager(layoutManager);
        recycleMoneyCourse.setAdapter(commonMoneyCourRcvAdapter);
    }


    @OnClick({R.id.iv_qiandao, R.id.tv_more_hot_course, R.id.tv_more_money_course, R.id.iv_top_bg, R.id.iv_search})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_top_bg:
                break;
            case R.id.iv_search:
                RouteUtil.startNewActivity(getThisActivity(), new Intent(getThisActivity(), SearchActivity.class));
                break;

            case R.id.tv_more_hot_course:
                intent = new Intent(getThisActivity(), CourseActivity.class);
                intent.putExtra("title", "热门课程");
                RouteUtil.startNewActivity(getThisActivity(), intent);
                break;
            case R.id.tv_more_money_course:
                intent = new Intent(getThisActivity(), CourseActivity.class);
                intent.putExtra("title", "付费课程");
                RouteUtil.startNewActivity(getThisActivity(), intent);
                break;
            case R.id.iv_qiandao:
                if (App.getInstance().isLogin(getThisActivity(), true)) {
                    App.getInstance().getUserInfo(getThisActivity(), new UserInfoCallback() {
                        @Override
                        public void getUserInfo(UserInfo userInfo) {
                            if (userInfo.isPlatformAccount()) {
                                RouteUtil.startNewActivity(getThisActivity(), new Intent(getThisActivity(), PlatformAccountSignActivity.class));
                            } else {
                                RouteUtil.startNewActivity(getThisActivity(), new Intent(getThisActivity(), SignActivity.class));
                            }
                        }
                    });
                }
                break;
        }
    }


    private List<IndexBannerBean> indexBannerBeanList;

    private void getBannerFromServer() {
        ApiUtils.getInstance().getIndexBanner(new ApiCallback() {
            @Override
            public void success(ResponseData t) {


            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishRefresh(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishRefresh(false);


            }
        });
    }

    //首页的一级分类item
    private void dealCourseItem(final List<CourseItemBean> list) {

        App.getInstance().getDefaultSetting(getActivity(), new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                recycleCourse.setAdapter(new CommonRcvAdapter<CourseItemBean>(list) {
                    TextView tvTitle;
                    ImageView ivImg;

                    @NonNull
                    @Override
                    public AdapterItem createItem(Object type) {
                        return new AdapterItem<CourseItemBean>() {
                            @Override
                            public int getLayoutResId() {
                                return R.layout.course_index_item;
                            }

                            @Override
                            public void bindViews(@NonNull View root) {
                                ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                                tvTitle = root.findViewById(R.id.tv_title);
                                ivImg = root.findViewById(R.id.iv_img);

                            }

                            @Override
                            public void setViews() {

                            }

                            @Override
                            public void handleData(CourseItemBean courseItemBean, int position) {
                                tvTitle.setText(courseItemBean.getClassifyName());
                                Glide.with(getThisActivity()).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), courseItemBean.getClassifyImage())).into(ivImg);

                            }
                        };
                    }
                });
            }
        });

        recycleCourse.setLayoutManager(new GridLayoutManager(getThisActivity(), 4));
    }

    //首页banner
    private void dealIndexBanner() {
        if (indexBannerBeanList == null) {
            return;
        }
        App.getInstance().getDefaultSetting(getThisActivity(), new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                convenientBanner.setPages(
                        new CBViewHolderCreator() {
                            @Override
                            public Holder createHolder(View itemView) {
                                return new Holder(itemView) {
                                    RoundedImageView imageView;

                                    @Override
                                    protected void initView(View itemView) {
                                        imageView = itemView.findViewById(R.id.iv);
                                    }

                                    @Override
                                    public void updateUI(Object data) {
                                        IndexBannerBean indexBannerBean = GsonUtil.GsonToBean(GsonUtil.GsonString(data), IndexBannerBean.class);
                                        Glide.with(getThisActivity()).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), indexBannerBean.getCarouselImage())).into(imageView);
                                    }
                                };
                            }

                            @Override
                            public int getLayoutId() {
                                return R.layout.index_banner_item;
                            }
                        }, indexBannerBeanList)
                        .setPageIndicator(new int[]{R.drawable.indicator, R.drawable.indicator_select}).startTurning();
            }
        });

    }

    //热门课程
    private void dealHotCourse(List<HotCourseBean.RecordsBean> list) {

        App.getInstance().getDefaultSetting(getThisActivity(), new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                recycleHotCourse.setAdapter(new CommonRcvAdapter<HotCourseBean.RecordsBean>(list) {

                    TextView tvTitle;
                    TextView tvJoinNum;
                    TextView tvDesc;
                    TextView tvmonney;
                    ImageView ivCorver;
                    LinearLayout llRoot;

                    @NonNull
                    @Override
                    public AdapterItem createItem(Object type) {
                        return new AdapterItem<HotCourseBean.RecordsBean>() {
                            @Override
                            public int getLayoutResId() {
                                return R.layout.index_course_item;
                            }

                            @Override
                            public void bindViews(@NonNull View root) {
                                ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                                tvTitle = root.findViewById(R.id.tv_title);
                                tvDesc = root.findViewById(R.id.tv_desc);
                                tvmonney = root.findViewById(R.id.tv_money);
                                tvJoinNum = root.findViewById(R.id.tv_join_num);
                                ivCorver = root.findViewById(R.id.iv_course_corver);
                                llRoot = root.findViewById(R.id.ll_root);
                            }

                            @Override
                            public void setViews() {

                            }

                            @Override
                            public void handleData(HotCourseBean.RecordsBean hotCourseBean, int position) {
                                tvmonney.setText(hotCourseBean.getCurriculumPrice() + "");
                                tvDesc.setText(hotCourseBean.getCurriculumIntro());
                                tvTitle.setText(hotCourseBean.getCurriculumName());
                                tvJoinNum.setText(hotCourseBean.getParticipantNum() + "加入了学习");
                                Glide.with(getThisActivity()).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), hotCourseBean.getCurriculumImage())).into(ivCorver);
                            }
                        };
                    }
                });


            }
        });
        GridLayoutManager layoutManager = new GridLayoutManager(getThisActivity(), 2);

        recycleHotCourse.addItemDecoration(new SpacesItemDecoration(12, 0, 0, 0));
        recycleHotCourse.setLayoutManager(layoutManager);

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initView();
        //presenter.getCourseItem();
        //presenter.getBanner();
        presenter.getHotCourse();
        // presenter.getMoneyCourse();
    }

    @Override
    public void showLoading() {
        DialogUtil.showLoading(getThisActivity(), true);
    }

    @Override
    public void hideLoading() {
        DialogUtil.hideLoading(getThisActivity());
    }

    @Override
    public void onError(String msg) {

        ToastUtils.show(getThisActivity(), msg);
    }

    @Override
    public void onSuccess(ResponseData data) {

    }

    @Override
    public void onExpcetion(String msg) {

    }


    @Override
    public void onBannerSuccess(ResponseData data) {
        smartLayout.finishRefresh(true);
        indexBannerBeanList = GsonUtil.GsonToList(GsonUtil.GsonString(data.getData()), IndexBannerBean.class);
        dealIndexBanner();
    }

    @Override
    public void onCourseSuccess(ResponseData data) {
        smartLayout.finishRefresh(true);
        List<CourseItemBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(data.getData()), new TypeToken<List<CourseItemBean>>() {
        }.getType());
        dealCourseItem(list);
    }

    @Override
    public void onHotCourseSuccess(HotCourseBean hotCourseBean) {
        dealHotCourse(hotCourseBean.getRecords());


    }

    @Override
    public void onMoneyCourseSuccess(HotCourseBean hotCourseBean) {

    }

    @Override
    public void onBannerError(String msg) {
        smartLayout.finishRefresh(false);
    }

    @Override
    public void onCourseError(String msg) {
        ToastUtils.show(getThisActivity(), msg);

    }


    private FragmentActivity getThisActivity() {
        if (getActivity() != null) {
            return getActivity();
        }
        return new FragmentActivity();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }
}


