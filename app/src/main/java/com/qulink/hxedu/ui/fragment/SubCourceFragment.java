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
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.entity.CourseBean;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.ui.course.CourseDetailActivity;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.view.SpacesItemDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import kale.adapter.util.IAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class SubCourceFragment extends Fragment implements OnLoadMoreListener, OnRefreshListener {


    @BindView(R.id.recycle_course)
    RecyclerView recycleCourse;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;

    public SubCourceFragment() {
        // Required empty public constructor
    }


    int classifyId;
    int tagId;




    class  Item implements AdapterItem<CourseBean.RecordsBean>{
        ImageView ivImg;
        TextView tvTitle;
        TextView tvNomey;
        ImageView ivVip;
        TextView tvJoinNum;
        LinearLayout llRoot;
        @Override
        public int getLayoutResId() {
            return R.layout.course_item;
        }

        @Override
        public void bindViews(@NonNull View root) {

            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tvTitle = root.findViewById(R.id.tv_title);
            llRoot = root.findViewById(R.id.ll_root);
            tvNomey = root.findViewById(R.id.tv_money);
            ivVip = root.findViewById(R.id.iv_vip);
            tvJoinNum = root.findViewById(R.id.tv_join_num);
            ivImg = root.findViewById(R.id.iv_img);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(CourseBean.RecordsBean data, int position) {

            tvTitle.setText(data.getCurriculumName());
            tvNomey.setText(data.getParticipantNum() + "");
            tvJoinNum.setText(data.getParticipantNum() + "人加入了学习");
            App.getInstance().getDefaultSetting(getActivity(), new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(getActivity()).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(),data.getCurriculumImage())).into(ivImg);

                }
            });
            if(data.isVipFree()){
                ivVip.setImageResource(R.drawable.vipmf);
            }
            if(data.isVipSpecial()){
                ivVip.setImageResource(R.drawable.vipzx);
            }
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getActivity(),CourseDetailActivity.class);
                    intent.putExtra("courseId",data.getId());
                    RouteUtil.startNewActivity(getActivity(), intent);
                }
            });
        }
    }
    void initData(List<CourseBean.RecordsBean> records) {


        recycleCourse.setAdapter(new CommonRcvAdapter<CourseBean.RecordsBean>(records) {


            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycleCourse.addItemDecoration(new SpacesItemDecoration(0, 4, 0, 0));
        recycleCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    private int pageNo;
    private int pageSize = FinalValue.limit;

    void getData() {
        pageNo = 1;
        smartLayout.setNoMoreData(false);
        hideEmpty();
        ApiUtils.getInstance().getCourseList(classifyId, tagId, pageNo, pageSize, 0, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                CourseBean courseBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), CourseBean.class);
                initData(courseBean.getRecords());
                smartLayout.finishRefresh(true);
                if(courseBean.getTotal()==0){
                    showEmpty();
                }
                if(courseBean.getRecords().size()<pageSize){
                    smartLayout.finishLoadMoreWithNoMoreData();
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

    void loadMore(){
        pageNo++;
        ApiUtils.getInstance().getCourseList(classifyId, tagId, pageNo, pageSize, 0, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                CourseBean courseBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), CourseBean.class);
              if(courseBean.getRecords().size()<pageSize){
                  smartLayout.setNoMoreData(true);
              }
                dealLoadMore(courseBean.getRecords());
                smartLayout.finishLoadMore(true);
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
    void dealLoadMore(List<CourseBean.RecordsBean> records){
        List<CourseBean.RecordsBean> list =   ((IAdapter<CourseBean.RecordsBean>) recycleCourse.getAdapter()).getData();
        list.addAll(records);
        ((IAdapter<CourseBean.RecordsBean>) recycleCourse.getAdapter()).setData(list);
        recycleCourse.getAdapter().notifyDataSetChanged();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }



    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }



    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_sub_cource, container, false);
            ButterKnife.bind(this, rootView);
            ButterKnife.bind(this, rootView);
            smartLayout.setOnLoadMoreListener(this);
            smartLayout.setOnRefreshListener(this);
            smartLayout.autoRefresh();
            classifyId = getArguments().getInt("classId");
            tagId = getArguments().getInt("tagId");
            smartLayout.autoRefresh();
            getData();
        }
        return rootView;
    }

    protected void hideEmpty(){
        View view = rootView.findViewById(R.id.ll_empty);
        if(view!=null){
            view.setVisibility(View.GONE);
        }
    }
    protected void showEmpty(){
        View view = rootView.findViewById(R.id.ll_empty);
        if(view!=null){
            view.setVisibility(View.VISIBLE);
        }
    }
}
