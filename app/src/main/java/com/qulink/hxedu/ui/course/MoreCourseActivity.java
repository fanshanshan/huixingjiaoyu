package com.qulink.hxedu.ui.course;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.HotCourseBean;
import com.qulink.hxedu.ui.BaseActivity;
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
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import kale.adapter.util.IAdapter;

public class MoreCourseActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    String title;
    int curriculumType;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_more_course;
    }

    @Override
    protected void init() {
        title = getIntent().getStringExtra("title");
        curriculumType = getIntent().getIntExtra("curriculumType", 0);
        setTitle(title);
        smartLayout.setOnRefreshListener(this);
        smartLayout.setOnLoadMoreListener(this);
        smartLayout.autoRefresh();
        initData();
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }


    private int pageNo;
    private int pageSize = FinalValue.limit;

    private void initData() {
        pageNo = 1;
        ApiUtils.getInstance().getIndexSortCourse(curriculumType, pageNo, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                HotCourseBean hotCourseBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), HotCourseBean.class);
                if(hotCourseBean.getRecords().size()==0){
                    showEmpty();
                }
                dealInitData(hotCourseBean.getRecords());
                if(hotCourseBean.getRecords().size()<pageSize){
                    smartLayout.setNoMoreData(true);
                }
                smartLayout.finishRefresh(true);

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

    private void loadMore() {
        pageNo++;
        ApiUtils.getInstance().getIndexSortCourse(curriculumType, pageNo, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                HotCourseBean hotCourseBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), HotCourseBean.class);
                dealLoadMore(hotCourseBean.getRecords());
                if(hotCourseBean.getRecords().size()<pageSize){
                    smartLayout.setNoMoreData(true);
                }
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

    private void dealInitData(List<HotCourseBean.RecordsBean> list) {
        recycle.setAdapter(new CommonRcvAdapter<HotCourseBean.RecordsBean>(list) {


            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycle.addItemDecoration(new SpacesItemDecoration(0, 4, 0, 0));
        recycle.setLayoutManager(new LinearLayoutManager(this));

    }


    class Item implements AdapterItem<HotCourseBean.RecordsBean>{
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
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    RouteUtil.startNewActivity(MoreCourseActivity.this, new Intent(MoreCourseActivity.this, CourseDetailActivity.class));
                }
            });
        }

        @Override
        public void handleData(HotCourseBean.RecordsBean data, int position) {

            tvTitle.setText(data.getCurriculumName());
            tvNomey.setText(data.getParticipantNum() + "");
            tvJoinNum.setText(data.getParticipantNum() + "人加入了学习");
            App.getInstance().getDefaultSetting(MoreCourseActivity.this, new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(MoreCourseActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(),data.getCurriculumImage())).into(ivImg);

                }
            });
            if(data.isVipFree()){
                ivVip.setImageResource(R.drawable.vipmf);
            }
            if(data.isVipSpecial()){
                ivVip.setImageResource(R.drawable.vipzx);
            }
        }
    }
    private void dealLoadMore(List<HotCourseBean.RecordsBean> list) {
        List<HotCourseBean.RecordsBean> data =   ((IAdapter<HotCourseBean.RecordsBean>) recycle.getAdapter()).getData();
        data.addAll(list);
        ((IAdapter<HotCourseBean.RecordsBean>) recycle.getAdapter()).setData(list);
        recycle.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
            loadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initData();
    }
}
