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
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.HotCourseBean;
import com.qulink.hxedu.entity.RecentLearnBean;
import com.qulink.hxedu.ui.course.CourseDetailActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import kale.adapter.util.IAdapter;

public class RecentLearnActivity extends BaseActivity implements OnRefreshListener {

    @BindView(R.id.recycle_today)
    RecyclerView recycleToday;
    @BindView(R.id.ll_today)
    LinearLayout llToday;
    @BindView(R.id.recycle_seven)
    RecyclerView recycleSeven;
    @BindView(R.id.ll_seven)
    LinearLayout llSeven;
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
        setTitle("最近学习");
        setRightTitle("编辑");
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_recent_learn;
    }

    @Override
    protected void init() {
        smartLayout.setOnRefreshListener(this);
        smartLayout.setEnableLoadMore(false);
        findViewById(R.id.tv_bar_right).setVisibility(View.GONE);
        getRecentLearn();
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private void initTodayRecycle(List<RecentLearnBean.RecentSevenDaysBean> list) {
        if (list == null || list.isEmpty()) {
           list = new ArrayList<>();
        }
        llToday.setVisibility(View.VISIBLE);
        recycleToday.setAdapter(new CommonRcvAdapter<RecentLearnBean.RecentSevenDaysBean>(list) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycleToday.setLayoutManager(new LinearLayoutManager(this));

    }

    private void initSevenRecycle(List<RecentLearnBean.RecentSevenDaysBean> list) {
        if (list == null || list.isEmpty()) {
            list = new ArrayList<>();
        }
        llSeven.setVisibility(View.VISIBLE);
        recycleSeven.setAdapter(new CommonRcvAdapter<RecentLearnBean.RecentSevenDaysBean>(list) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycleSeven.setLayoutManager(new LinearLayoutManager(this));
    }

    private boolean isEditing;

    @OnClick({R.id.tv_bar_right,R.id.tv_all,R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bar_right:
                if (isEditing) {
                    smartLayout.setEnableRefresh(true);
                    isEditing = false;
                    setRightTitle("编辑");
                    llControl.setVisibility(View.GONE);
                    clearStatus();
                } else {
                    isEditing = true;
                    smartLayout.setEnableRefresh(false);

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
        List<RecentLearnBean.RecentSevenDaysBean> today = getTodayList();
        if(!today.isEmpty()){
            for(RecentLearnBean.RecentSevenDaysBean r:today){
                r.setCheck(false);
            }
        } List<RecentLearnBean.RecentSevenDaysBean> seven = getSevenList();
        if(!seven.isEmpty()){
            for(RecentLearnBean.RecentSevenDaysBean r:seven){
                if(r.isCheck()){
                    r.setCheck(false);
                }
            }
        }
    }
    private int getSelectNum(){
        int selectNum = 0;
        List<RecentLearnBean.RecentSevenDaysBean> today = getTodayList();
        if(!today.isEmpty()){
            for(RecentLearnBean.RecentSevenDaysBean r:today){
               if(r.isCheck()){
                   selectNum++;
               }
            }
        } List<RecentLearnBean.RecentSevenDaysBean> seven = getSevenList();
        if(!seven.isEmpty()){
            for(RecentLearnBean.RecentSevenDaysBean r:seven){
                if(r.isCheck()){
                    selectNum++;
                }
            }
        }
        return selectNum;
    }

    private void delete(){
        String ids = "";
        List<RecentLearnBean.RecentSevenDaysBean> today = getTodayList();
        if(!today.isEmpty()){
            for(RecentLearnBean.RecentSevenDaysBean r:today){
                if(r.isCheck()){
                    ids = ids+","+r.getScheduleId();
                }
            }
        } List<RecentLearnBean.RecentSevenDaysBean> seven = getSevenList();
        if(!seven.isEmpty()){
            for(RecentLearnBean.RecentSevenDaysBean r:seven){
                if(r.isCheck()){
                    ids = ids+","+r.getScheduleId();
                }
            }
        }
        if(!TextUtils.isEmpty(ids)){
           ids = ids.substring(1,ids.length());
           DialogUtil.showLoading(this,true,"请稍候");
           ApiUtils.getInstance().deleteRecentLearnByIds(ids, new ApiCallback() {
               @Override
               public void success(ResponseData t) {
                   DialogUtil.hideLoading(RecentLearnActivity.this);
                   setResult(RESULT_OK);
                   getRecentLearn();
               }

               @Override
               public void error(String code, String msg) {
                   DialogUtil.hideLoading(RecentLearnActivity.this);
                   ToastUtils.show(RecentLearnActivity.this,msg);
               }

               @Override
               public void expcetion(String expectionMsg) {
                   DialogUtil.hideLoading(RecentLearnActivity.this);
                    ToastUtils.show(RecentLearnActivity.this,expectionMsg);
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
        if (recycleSeven.getAdapter() != null) {
            recycleSeven.getAdapter().notifyDataSetChanged();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getRecentLearn();
    }

    private List<RecentLearnBean.RecentSevenDaysBean> getTodayList(){
        if(recycleToday.getAdapter()==null){
            return new ArrayList<>();
        }
       return   ((IAdapter<RecentLearnBean.RecentSevenDaysBean>) recycleToday.getAdapter()).getData();

    } private List<RecentLearnBean.RecentSevenDaysBean> getSevenList(){
        if(recycleSeven.getAdapter()==null){
            return new ArrayList<>();
        }
       return   ((IAdapter<RecentLearnBean.RecentSevenDaysBean>) recycleSeven.getAdapter()).getData();

    }
    private void allChoose(){
        List<RecentLearnBean.RecentSevenDaysBean> today = getTodayList();
        if(!today.isEmpty()){
            for(RecentLearnBean.RecentSevenDaysBean r:today){
                r.setCheck(true);
            }
        } List<RecentLearnBean.RecentSevenDaysBean> seven = getSevenList();
        if(!seven.isEmpty()){
            for(RecentLearnBean.RecentSevenDaysBean r:seven){
                r.setCheck(true);
            }
        }
        tvDelete.setText("删除("+getSelectNum()+")");
        refreshAdapter();

    }
    class Item implements AdapterItem<RecentLearnBean.RecentSevenDaysBean> {
        @BindView(R.id.iv_img)
        RoundedImageView ivImg;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
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
        public void handleData(RecentLearnBean.RecentSevenDaysBean recentSevenDaysBean, int position) {
            App.getInstance().getDefaultSetting(RecentLearnActivity.this, new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(RecentLearnActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), recentSevenDaysBean.getCurriculumImage())).into(ivImg);
                }
            });
            tvTitle.setText(recentSevenDaysBean.getCurriculumName());
            tvTime.setText("已观看：" + recentSevenDaysBean.getWatchedTime());
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
                        Intent intent = new Intent(RecentLearnActivity.this, CourseDetailActivity.class);
                        intent.putExtra("courseId",recentSevenDaysBean.getVideoId());
                        startActivity(intent);
                    }
                }
            });
        }
    }

    private void getRecentLearn() {
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().recentLean(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishRefresh(true);
                isEditing = false;
                setRightTitle("编辑");
                llControl.setVisibility(View.GONE);
                clearStatus();
                RecentLearnBean recentLearnBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), RecentLearnBean.class);
                if (recentLearnBean != null) {
                    initTodayRecycle(recentLearnBean.getToday());
                    initSevenRecycle(recentLearnBean.getRecentSevenDays());
                    if((getTodayList().size()+getSevenList().size())>0){
                        findViewById(R.id.tv_bar_right).setVisibility(View.VISIBLE);
                    }else{
                        findViewById(R.id.tv_bar_right).setVisibility(View.GONE);
                    }
                    DialogUtil.hideLoading(RecentLearnActivity.this);
                }
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(RecentLearnActivity.this);
                ToastUtils.show(RecentLearnActivity.this, msg);
                smartLayout.finishRefresh(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(RecentLearnActivity.this);
                ToastUtils.show(RecentLearnActivity.this, expectionMsg);
                smartLayout.finishRefresh(false);

            }
        });
    }
}
