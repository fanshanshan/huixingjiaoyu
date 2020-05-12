package com.qulink.hxedu.ui.fragment;


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

import com.qulink.hxedu.R;
import com.qulink.hxedu.entity.CustomData;
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
        }
        return rootView;
    }

    @OnClick(R.id.iv_qiandao)
    public void onViewClicked() {
    }

    //处理滑动bar颜色变化


    //scorllview添加滑动监听
    void addScrollviewListener() {
        sc.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                scrollPosition = scrollY;
                final float ratio = (float) Math.min(Math.max(scrollY, 0), llTopSearchBg.getHeight()) /  llTopSearchBg.getHeight();
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
                    }

                    @Override
                    public void setViews() {

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

        recycleHotCourse.addItemDecoration(new SpacesItemDecoration(12));
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
                    }

                    @Override
                    public void setViews() {

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

        recycleMoneyCourse.addItemDecoration(new SpacesItemDecoration(12));
        recycleMoneyCourse.setLayoutManager(layoutManager);
        recycleMoneyCourse.setAdapter(commonMoneyCourRcvAdapter);
    }
}


