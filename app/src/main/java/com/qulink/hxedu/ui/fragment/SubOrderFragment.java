package com.qulink.hxedu.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.entity.CourseNameBean;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.HotCourseBean;
import com.qulink.hxedu.entity.OrderCourseBean;
import com.qulink.hxedu.ui.course.CourseDetailActivity;
import com.qulink.hxedu.ui.course.MoreCourseActivity;
import com.qulink.hxedu.ui.score.ShopCourseDetailActivity;
import com.qulink.hxedu.ui.sign.ScoreDetailActivity;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.qulink.hxedu.view.SpacesItemDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubOrderFragment extends Fragment implements OnLoadMoreListener, OnRefreshListener {
        //直播订单

    @BindView(R.id.recycle_course)
    EmptyRecyclerView recycleCourse;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    private View rootView;


    public SubOrderFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_sub_order, container, false);
            ButterKnife.bind(this, rootView);
            // initData();
                smartLayout.setOnLoadMoreListener(this);
                smartLayout.setOnRefreshListener(this);
            initRecycle();
            getData();
        }
        return rootView;
    }

    private int pageNo;
    private int pagesize = FinalValue.limit;

    private void getData() {
        pageNo = 1;
        smartLayout.setNoMoreData(false);
        ApiUtils.getInstance().getMyOrder(pageNo, pagesize, 2, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishRefresh(true);

                List<OrderCourseBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<OrderCourseBean>>() {
                }.getType());
                if(list==null){
                    smartLayout.setNoMoreData(true);
                }else{
                    if(list.size()<pagesize){
                        smartLayout.setNoMoreData(true);
                    }
                    data.clear();
                    data.addAll(list);
                    recycleCourse.getAdapter().notifyDataSetChanged();
                }
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
    private void loadmore() {
        pageNo ++;
        ApiUtils.getInstance().getMyOrder(pageNo, pagesize, 2, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishLoadMore(true);

                List<OrderCourseBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<OrderCourseBean>>() {
                }.getType());
                if(list==null){
                    smartLayout.setNoMoreData(true);
                }else{
                    if(list.size()<pagesize){
                        smartLayout.setNoMoreData(true);

                    }
                    data.addAll(list);
                    recycleCourse.getAdapter().notifyDataSetChanged();
                }
            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishLoadMore(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishLoadMore(false);

            }
        });
    }


    private List<OrderCourseBean> data;
    private void initRecycle() {
        data = new ArrayList<>();
        recycleCourse.setAdapter(new CommonRcvAdapter<OrderCourseBean>(data) {


            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycleCourse.addItemDecoration(new SpacesItemDecoration(0, 4, 0, 0));
        recycleCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleCourse.setEmptyView(rootView.findViewById(R.id.ll_empty));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            loadmore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
getData();
    }


    class Item implements AdapterItem<OrderCourseBean>{
        ImageView ivImg;
        TextView tvTitle;
        TextView tvNomey;
        TextView tvStatus;
        ImageView ivVip;
        LinearLayout llRoot;
        @Override
        public int getLayoutResId() {
            return R.layout.course_order_item;
        }

        @Override
        public void bindViews(@NonNull View root) {

            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tvTitle = root.findViewById(R.id.tv_title);
            llRoot = root.findViewById(R.id.ll_root);
            tvNomey = root.findViewById(R.id.tv_money);
            ivVip = root.findViewById(R.id.iv_vip);
            ivImg = root.findViewById(R.id.iv_img);
            tvStatus = root.findViewById(R.id.tv_status);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(OrderCourseBean data, int position) {

            tvTitle.setText(data.getCurriculumName());
            if(data.getOrderType()==1){
                tvNomey.setText("￥"+data.getAmount());
            }else{
                tvNomey.setText(data.getIntegration()+"积分+￥"+data.getAmount());

            }
            if(data.getExpired()==1){
                tvStatus.setVisibility(View.VISIBLE);
            }else{
                tvStatus.setVisibility(View.GONE);

            }
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(data.getOrderType()==2){
                        Intent intent = new Intent(getActivity(), ShopCourseDetailActivity.class);
                        intent.putExtra("courseId",data.getCurriculumId());
                        RouteUtil.startNewActivity(getActivity(), intent);
                    }else{
                        Intent intent = new Intent(getActivity(), CourseDetailActivity.class);
                        intent.putExtra("courseId",data.getCurriculumId());
                        RouteUtil.startNewActivity(getActivity(), intent);
                    }

                }
            });
            App.getInstance().getDefaultSetting(getActivity(), new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(getActivity()).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(),data.getCurriculumImage())).into(ivImg);

                }
            });

        }
    }

}
