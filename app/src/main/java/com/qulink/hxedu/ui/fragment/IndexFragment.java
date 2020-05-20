package com.qulink.hxedu.ui.fragment;


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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qulink.hxedu.R;
import com.qulink.hxedu.entity.CustomData;
import com.qulink.hxedu.ui.CourseActivity;
import com.qulink.hxedu.ui.CourseDetailActivity;
import com.qulink.hxedu.ui.SearchActivity;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.view.MyScrollView;
import com.qulink.hxedu.view.SpacesItemDecoration;

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
public class IndexFragment extends Fragment {


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


    int scrollPosition;
    @BindView(R.id.iv_top_bg)
    ImageView ivTopBg;
    @BindView(R.id.tv1)
    TextView tv1;

    @BindView(R.id.ll_temp)
    LinearLayout llTemp;

    List<Integer> autoScrollImgs;
    @BindView(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @BindView(R.id.iv_search)
    ImageView ivSearch;
    @BindView(R.id.tv_mfkc)
    TextView tvMfkc;
    @BindView(R.id.tv_jkxl)
    TextView tvJkxl;
    @BindView(R.id.tv_wmbl)
    TextView tvWmbl;
    @BindView(R.id.tv_qzjy)
    TextView tvQzjy;
    @BindView(R.id.tv_zhxl)
    TextView tvZhxl;
    @BindView(R.id.tv_rsgs)
    TextView tvRsgs;
    @BindView(R.id.tv_slwd)
    TextView tvSlwd;
    @BindView(R.id.tv_ydsh)
    TextView tvYdsh;

    public IndexFragment() {
        // Required empty public constructor
    }


    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_index, container, false);
            ButterKnife.bind(this, rootView);

            initHotCource();
            initMoneyCource();
            addScrollviewListener();

            initAutoScrollViewPager();

        }
        return rootView;
    }


    //处理滑动bar颜色变化

    /**
     * 自动播放viewpager
     */
    void initAutoScrollViewPager() {
        autoScrollImgs = new ArrayList<>();
        autoScrollImgs.add(R.drawable.emba);
        autoScrollImgs.add(R.drawable.emba);
        autoScrollImgs.add(R.drawable.emba);
        autoScrollImgs.add(R.drawable.emba);
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
                                imageView.setBackgroundResource((int) data);
                            }
                        };
                    }

                    @Override
                    public int getLayoutId() {
                        return R.layout.imageview_item;
                    }
                }, autoScrollImgs).setPageIndicator(new int[]{R.drawable.indicator, R.drawable.indicator_select}).startTurning();
        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
//                .setPageIndicator(new int[]{R.drawable.ic_page_indicator, R.drawable.ic_page_indicator_focused})
        //设置指示器的方向
//                .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.ALIGN_PARENT_RIGHT)
//                .setOnPageChangeListener(this)//监听翻页事件
        ;
    }

    /**
     * 为scrollview添加监听 滑动改变bar颜色
     */
    void addScrollviewListener() {
        sc.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                scrollPosition = scrollY;
                final float ratio = (float) Math.min(Math.max(scrollY, 0), llTopSearchBg.getHeight()) / llTopSearchBg.getHeight();
                final int newAlpha = (int) (ratio * 255);
                llTopSearchBg.setBackgroundColor(Color.argb((int) newAlpha, 245, 245, 245));
            }
        });
    }

    //热门课程
    List<CustomData> hotCourseData;
    CommonRcvAdapter commonHotCourRcvAdapter;

    void initHotCource() {
        hotCourseData = new ArrayList<>();
        hotCourseData.add(new CustomData("", 2345, 666, "VIP免费", "《管理3阶能力提升训练营》"));
        hotCourseData.add(new CustomData("", 2345, 666, "VIP免费", "《管理3阶能力提升训练营》"));
        hotCourseData.add(new CustomData("", 2345, 666, "VIP免费", "《管理3阶能力提升训练营》"));
        hotCourseData.add(new CustomData("", 2345, 666, "VIP免费", "《管理3阶能力提升训练营》"));
        commonHotCourRcvAdapter = new CommonRcvAdapter<CustomData>(hotCourseData) {

            TextView tvTitle;
            TextView tvJoinNum;
            TextView tvDesc;
            TextView tvmonney;

            LinearLayout llRoot;
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
                        tvTitle = (TextView) root.findViewById(R.id.tv_title);
                        tvDesc = (TextView) root.findViewById(R.id.tv_desc);
                        tvmonney = (TextView) root.findViewById(R.id.tv_money);
                        tvJoinNum = (TextView) root.findViewById(R.id.tv_join_num);
                        llRoot = (LinearLayout) root.findViewById(R.id.ll_root);
                    }

                    @Override
                    public void setViews() {
                        llRoot.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RouteUtil.startNewActivity(getActivity(),new Intent(getActivity(), CourseDetailActivity.class));
                            }
                        });
                    }

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
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        recycleHotCourse.addItemDecoration(new SpacesItemDecoration(12, 0, 0, 0));
        recycleHotCourse.setLayoutManager(layoutManager);
        recycleHotCourse.setAdapter(commonHotCourRcvAdapter);
    }


    //付费课程
    List<CustomData> moneyCourseData;
    CommonRcvAdapter commonMoneyCourRcvAdapter;

    void initMoneyCource() {
        moneyCourseData = new ArrayList<>();
        moneyCourseData.add(new CustomData("", 2345, 666, "VIP免费", "《管理3阶能力提升训练营》"));
        moneyCourseData.add(new CustomData("", 2345, 666, "VIP免费", "《管理3阶能力提升训练营》"));
        moneyCourseData.add(new CustomData("", 2345, 666, "VIP免费", "《管理3阶能力提升训练营》"));
        moneyCourseData.add(new CustomData("", 2345, 666, "VIP免费", "《管理3阶能力提升训练营》"));
        commonMoneyCourRcvAdapter = new CommonRcvAdapter<CustomData>(moneyCourseData) {

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
                        tvTitle = (TextView) root.findViewById(R.id.tv_title);
                        tvDesc = (TextView) root.findViewById(R.id.tv_desc);
                        tvmonney = (TextView) root.findViewById(R.id.tv_money);
                        tvJoinNum = (TextView) root.findViewById(R.id.tv_join_num);
                        llRoot = (LinearLayout) root.findViewById(R.id.ll_root);
                    }

                    @Override
                    public void setViews() {
                        llRoot.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RouteUtil.startNewActivity(getActivity(),new Intent(getActivity(), CourseDetailActivity.class));
                            }
                        });
                    }

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
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);

        recycleMoneyCourse.addItemDecoration(new SpacesItemDecoration(12, 0, 0, 0));
        recycleMoneyCourse.setLayoutManager(layoutManager);
        recycleMoneyCourse.setAdapter(commonMoneyCourRcvAdapter);
    }


    @OnClick({R.id.tv_more_hot_course, R.id.tv_more_money_course, R.id.iv_top_bg, R.id.iv_search, R.id.tv_mfkc, R.id.tv_jkxl, R.id.tv_wmbl, R.id.tv_qzjy, R.id.tv_zhxl, R.id.tv_rsgs, R.id.tv_slwd, R.id.tv_ydsh})
    public void onViewClicked(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.iv_top_bg:
                break;
            case R.id.iv_search:
                RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), SearchActivity.class));
                break;
            case R.id.tv_mfkc:
                intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("title", "免费课程");
                RouteUtil.startNewActivity(getActivity(), intent);
                break;
            case R.id.tv_jkxl:
                intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("title", "健康系列");
                RouteUtil.startNewActivity(getActivity(), intent);
                break;
            case R.id.tv_wmbl:
                intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("title", "完美伴侣");
                RouteUtil.startNewActivity(getActivity(), intent);
                break;
            case R.id.tv_qzjy:
                intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("title", "亲子教育");
                RouteUtil.startNewActivity(getActivity(), intent);
                break;
            case R.id.tv_zhxl:
                intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("title", "智慧系列");
                RouteUtil.startNewActivity(getActivity(), intent);
                break;
            case R.id.tv_rsgs:
                break;
            case R.id.tv_slwd:
                break;
            case R.id.tv_ydsh:
                break;
            case R.id.tv_more_hot_course:
                intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("title", "热门课程");
                RouteUtil.startNewActivity(getActivity(), intent);
                break;
            case R.id.tv_more_money_course:
                intent = new Intent(getActivity(), CourseActivity.class);
                intent.putExtra("title", "付费课程");
                RouteUtil.startNewActivity(getActivity(), intent);
                break;
        }
    }
}


