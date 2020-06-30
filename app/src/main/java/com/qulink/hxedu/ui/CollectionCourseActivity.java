package com.qulink.hxedu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.entity.CollectionBean;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.HotCourseBean;
import com.qulink.hxedu.entity.RecentLearnBean;
import com.qulink.hxedu.ui.course.CourseDetailActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import kale.adapter.util.IAdapter;

public class CollectionCourseActivity extends BaseActivity implements OnRefreshListener , OnLoadMoreListener {

    @BindView(R.id.recycle_today)
    EmptyRecyclerView recycleToday;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    @BindView(R.id.tv_all)
    TextView tvAll;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.ll_control)
    LinearLayout llControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("我的收藏");
        setRightTitle("编辑");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_collection_course;
    }

    @Override
    protected void init() {
        smartLayout.setOnRefreshListener(this);
        smartLayout.setOnLoadMoreListener(this);
        findViewById(R.id.tv_bar_right).setVisibility(View.GONE);
        initTodayRecycle();
        getRecentLearn();
    }
    private List<CollectionBean.RecordsBean> data;

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private void initTodayRecycle() {
        data = new ArrayList<>();
        recycleToday.setAdapter(new CommonRcvAdapter<CollectionBean.RecordsBean>(data) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycleToday.setLayoutManager(new LinearLayoutManager(this));
        recycleToday.setEmptyView(findViewById(R.id.ll_empty));
    }



    private boolean isEditing;

    @OnClick({R.id.tv_bar_right,R.id.tv_all,R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bar_right:
                if (isEditing) {
                    smartLayout.setEnableRefresh(true);
                    smartLayout.setEnableLoadMore(true);
                    isEditing = false;
                    setRightTitle("编辑");
                    llControl.setVisibility(View.GONE);
                    clearStatus();
                } else {
                    isEditing = true;
                    smartLayout.setEnableRefresh(false);
                    smartLayout.setEnableLoadMore(false);

                    setRightTitle("取消");
                    llControl.setVisibility(View.VISIBLE);

                }
                refreshAdapter();
                break;
            case R.id.tv_all:
                allChoose();
                break;
            case R.id.tv_delete:
                delete();
                break;
        }
    }

    private void clearStatus(){
        List<CollectionBean.RecordsBean> today = getTodayList();
        if(!today.isEmpty()){
            for(CollectionBean.RecordsBean r:today){
                r.setCheck(false);
            }
        }
    }
    private int getSelectNum(){
        int selectNum = 0;
        List<CollectionBean.RecordsBean> today = getTodayList();
        if(!today.isEmpty()){
            for(CollectionBean.RecordsBean r:today){
                if(r.isCheck()){
                    selectNum++;
                }
            }
        }
        return selectNum;
    }

    private void delete(){
        String ids = "";
        List<CollectionBean.RecordsBean> today = getTodayList();
        if(!today.isEmpty()){
            for(CollectionBean.RecordsBean r:today){
                if(r.isCheck()){
                    ids = ids+","+r.getCollectId();
                }
            }
        }
        if(!TextUtils.isEmpty(ids)){
            ids = ids.substring(1,ids.length());
            DialogUtil.showLoading(this,true,"请稍候");
            ApiUtils.getInstance().deleteColletion(ids, new ApiCallback() {
                @Override
                public void success(ResponseData t) {
                    DialogUtil.hideLoading(CollectionCourseActivity.this);
                    setResult(RESULT_OK);
                    getRecentLearn();
                }

                @Override
                public void error(String code, String msg) {
                    DialogUtil.hideLoading(CollectionCourseActivity.this);
                    ToastUtils.show(CollectionCourseActivity.this,msg);
                }

                @Override
                public void expcetion(String expectionMsg) {
                    DialogUtil.hideLoading(CollectionCourseActivity.this);
                    ToastUtils.show(CollectionCourseActivity.this,expectionMsg);
                }
            });
        }else{
            ToastUtils.show(this,"请选择要删除的课程");
        }
    }

    private void refreshAdapter() {
        if (recycleToday.getAdapter() != null) {
            recycleToday.getAdapter().notifyDataSetChanged();
        }

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getRecentLearn();
    }

    private List<CollectionBean.RecordsBean> getTodayList(){
        if(recycleToday.getAdapter()==null){
            return new ArrayList<>();
        }
        return   ((IAdapter<CollectionBean.RecordsBean>) recycleToday.getAdapter()).getData();

    }
    private void allChoose(){
        List<CollectionBean.RecordsBean> today = getTodayList();
        if(!today.isEmpty()){
            for(CollectionBean.RecordsBean r:today){
                r.setCheck(true);
            }
        }
        tvDelete.setText("删除("+getSelectNum()+")");
        refreshAdapter();

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadmore();
    }

    class Item implements AdapterItem<CollectionBean.RecordsBean> {
        @BindView(R.id.iv_img)
        RoundedImageView ivImg;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.v_line)
        View vLine;
        @BindView(R.id.iv_check)
        ImageView ivCheck;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        @Override
        public int getLayoutResId() {
            return R.layout.recent_learn_item;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ButterKnife.bind(this, root);
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(CollectionBean.RecordsBean recentSevenDaysBean, int position) {
            App.getInstance().getDefaultSetting(CollectionCourseActivity.this, new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(CollectionCourseActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), recentSevenDaysBean.getCurriculumImage())).into(ivImg);
                }
            });
            tvTitle.setText(recentSevenDaysBean.getCurriculumName());
          //  tvTime.setText("已观看：" + recentSevenDaysBean.getWatchedTime());
            if (recentSevenDaysBean.isCheck()) {
                ivCheck.setImageResource(R.drawable.checked);
            } else {
                ivCheck.setImageResource(R.drawable.check);
            }
            if (isEditing) {
                ivCheck.setVisibility(View.VISIBLE);
            } else {
                ivCheck.setVisibility(View.GONE);
            }
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isEditing) {
                        if (recentSevenDaysBean.isCheck()) {
                            recentSevenDaysBean.setCheck(false);
                        } else {
                            recentSevenDaysBean.setCheck(true);
                        }
                        tvDelete.setText("删除("+getSelectNum()+")");
                        refreshAdapter();
                    }else{
                        Intent intent = new Intent(CollectionCourseActivity.this, CourseDetailActivity.class);
                        intent.putExtra("courseId",recentSevenDaysBean.getCollectId());
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private int pageNo;
    private int pageSize = FinalValue.limit;
    private void getRecentLearn() {
        pageNo = 1;
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().collectCourseList(pageNo,pageSize,new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishRefresh(true);
                isEditing = false;
                setRightTitle("编辑");
                llControl.setVisibility(View.GONE);
                clearStatus();
                CollectionBean recentLearnBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), CollectionBean.class);
                if (recentLearnBean != null) {
                    if((recentLearnBean.getRecords().size())>0){
                        findViewById(R.id.tv_bar_right).setVisibility(View.VISIBLE);
                    }else{
                        findViewById(R.id.tv_bar_right).setVisibility(View.GONE);
                    }
                    if(recentLearnBean.getRecords().size()<pageSize){
                        smartLayout.setNoMoreData(true);
                    }
                    data.clear();
                    data.addAll(recentLearnBean.getRecords());
                    refreshAdapter();
                    DialogUtil.hideLoading(CollectionCourseActivity.this);
                }
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(CollectionCourseActivity.this);
                ToastUtils.show(CollectionCourseActivity.this, msg);
                smartLayout.finishRefresh(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(CollectionCourseActivity.this);
                ToastUtils.show(CollectionCourseActivity.this, expectionMsg);
                smartLayout.finishRefresh(false);

            }
        });
    }
    private void loadmore() {
        pageNo ++;
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().collectCourseList(pageNo,pageSize,new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishLoadMore(true);
                isEditing = false;
                setRightTitle("编辑");
                llControl.setVisibility(View.GONE);
                clearStatus();
                CollectionBean recentLearnBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), CollectionBean.class);
                if (recentLearnBean != null&&recentLearnBean.getRecords()!=null) {
                    data.addAll(recentLearnBean.getRecords());
                    if(recentLearnBean.getRecords().size()<pageSize){
                        smartLayout.setNoMoreData(true);
                    }
                    refreshAdapter();

                }
            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(CollectionCourseActivity.this, msg);
                smartLayout.finishLoadMore(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                ToastUtils.show(CollectionCourseActivity.this, expectionMsg);
                smartLayout.finishLoadMore(false);

            }
        });
    }
}
