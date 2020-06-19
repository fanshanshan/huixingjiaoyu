package com.qulink.hxedu.ui.sign;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.StudyPlan;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class StudyPlanActivity extends BaseActivity {

    @BindView(R.id.ll_un_finish_Plan)
    LinearLayout llNoPlan;
    @BindView(R.id.iv_plan_status)
    ImageView ivPlanStatus;
    @BindView(R.id.tv_finish_plan)
    TextView tvFinishPlan;
    @BindView(R.id.tv_total_plan)
    TextView tvTotalPlan;
    @BindView(R.id.tv_finish_plan_desc)
    TextView tvFinishPlanDesc;
    @BindView(R.id.recycle_un_finish_plan)
    RecyclerView recycleUnFinishPlan;
    @BindView(R.id.ll_finish_plan)
    LinearLayout llFinishPlan;
    @BindView(R.id.recycle_finish_plan)
    RecyclerView recycleFinishPlan;


    private   StudyPlan studyPlan;
    CommonRcvAdapter finishAdapter;
    CommonRcvAdapter unFinishAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_sudy_plan;
    }

    @Override
    protected void init() {
        setRightTitle(getString(R.string.add_plan));
        setTitle(getString(R.string.study_plan));
        setBackImg(R.drawable.back_white2);
        setBarBg(ContextCompat.getColor(this,R.color.white_transparent));
        setBarTtxtColors(ContextCompat.getColor(this,R.color.white));
        getStudyPlanDetail();
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick({R.id.tv_bar_right})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_bar_right:
                if(studyPlan==null){
                    return;
                }
                if(studyPlan.getTotal()> FinalValue.maxStudyLimit||studyPlan.getTotal()== FinalValue.maxStudyLimit){
                      ToastUtils.show(StudyPlanActivity.this,"每天最多添加"+FinalValue.maxStudyLimit+"个学习计划");

                }else{
                    Intent intent = new Intent(StudyPlanActivity.this, AddStudyPlanActivity.class);
                    intent.putExtra("length",studyPlan.getTotal());
                    RouteUtil.startNewActivityAndResult(StudyPlanActivity.this,intent , 1);

                }

                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 2) {
            //刷新学习计划
          //  getStudyPlanDetail();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getStudyPlanDetail();

    }

    void getStudyPlanDetail() {
        DialogUtil.showLoading(this, false);
        ApiUtils.getInstance().getStudyPlanSetail(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(StudyPlanActivity.this);
                studyPlan = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), StudyPlan.class);
                dealData(studyPlan);
            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(StudyPlanActivity.this, msg);
                DialogUtil.hideLoading(StudyPlanActivity.this);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(StudyPlanActivity.this);
                ToastUtils.show(StudyPlanActivity.this, expectionMsg);
            }
        });
    }

    void dealData(StudyPlan studyPlan) {
        if (studyPlan.getTotal() == 0) {
            ivPlanStatus.setImageResource(R.drawable.red_paln);
            tvFinishPlan.setText("0");
            tvTotalPlan.setText("/0");
            tvFinishPlanDesc.setText("未创建");
            llNoPlan.setVisibility(View.VISIBLE);
        }else{
            llFinishPlan.setVisibility(View.VISIBLE);
            tvFinishPlanDesc.setText("已完成"+studyPlan.getFinishedTotal()+"个");

            ivPlanStatus.setImageResource(R.drawable.blue_plan);
            tvFinishPlan.setText(studyPlan.getFinishedTotal()+"");
            tvTotalPlan.setText("/"+studyPlan.getTotal());
            llNoPlan.setVisibility(View.GONE);
            recycleFinishPlan.setVisibility(View.VISIBLE);
            recycleUnFinishPlan.setVisibility(View.VISIBLE);
            unFinishAdapter = new CommonRcvAdapter<StudyPlan.UnfinishedBean>(studyPlan.getUnfinished()) {

                @NonNull
                @Override
                public AdapterItem createItem(Object type) {
                    return new Item();
                }
            };
            recycleUnFinishPlan.setAdapter(unFinishAdapter);
            recycleUnFinishPlan.setLayoutManager(new LinearLayoutManager(this));
        }
        initFinishRecycle();
    }

    class Item implements AdapterItem<StudyPlan.UnfinishedBean>{

        TextView tvContent;
        TextView tvComplete;
        LinearLayout llRoot;
        @Override
        public int getLayoutResId() {
            return R.layout.un_finished_plan_item;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tvComplete = root.findViewById(R.id.tv_complete);
            tvContent = root.findViewById(R.id.tv_content);
            llRoot = root.findViewById(R.id.ll_root);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(StudyPlan.UnfinishedBean unfinishedBean, int position) {
            tvContent.setText(unfinishedBean.getContent());
            tvComplete.setText(getString(R.string.complete));
            tvComplete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finishStudyPlan(unfinishedBean);
                }
            });
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogUtil.showAlertDialog(StudyPlanActivity.this, "我的计划", unfinishedBean.getContent(), "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                }
            });
        }
    }
    void initFinishRecycle(){
        finishAdapter = new CommonRcvAdapter<StudyPlan.UnfinishedBean>(studyPlan.getFinished()) {

            TextView tvContent;
            TextView tvComplete;
            LinearLayout llRoot;
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AdapterItem() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.un_finished_plan_item;
                    }

                    @Override
                    public void bindViews(@NonNull View root) {
                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        tvComplete = root.findViewById(R.id.tv_complete);
                        tvContent = root.findViewById(R.id.tv_content);
                        llRoot = root.findViewById(R.id.ll_root);

                    }

                    @Override
                    public void setViews() {

                    }

                    @Override
                    public void handleData(Object o, int position) {
                        if(o instanceof StudyPlan.UnfinishedBean){
                            StudyPlan.UnfinishedBean unfinishedBean = (StudyPlan.UnfinishedBean)o;
                            tvContent.setText(unfinishedBean.getContent());
                            tvComplete.setBackgroundResource(R.drawable.btn_grey_bg_circle);
                            tvComplete.setText(getString(R.string.completed));
                            tvComplete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ToastUtils.show(StudyPlanActivity.this,getString(R.string.completed));
                                }
                            });
                            llRoot.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DialogUtil.showRuleDialog(StudyPlanActivity.this,"我的计划",unfinishedBean.getContent());
                                }
                            });
                        }
                    }
                };
            }
        };
        recycleFinishPlan.setAdapter(finishAdapter);
        recycleFinishPlan.setLayoutManager(new LinearLayoutManager(this));


    }

    void finishStudyPlan(StudyPlan.UnfinishedBean unfinishedBean){
        DialogUtil.showLoading(this,true);
        ApiUtils.getInstance().finishStudyPlanById(unfinishedBean.getId() + "", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(StudyPlanActivity.this);
                ToastUtils.show(StudyPlanActivity.this,getString(R.string.success_toast));
//                studyPlan.getUnfinished().remove(unfinishedBean);
//                studyPlan.getFinished().add(unfinishedBean);
//                studyPlan.setFinishedTotal(studyPlan.getFinishedTotal()+1);
//                tvFinishPlan.setText(studyPlan.getFinishedTotal()+"");
//
//                if(finishAdapter!=null){
//                    finishAdapter.notifyDataSetChanged();
//                }
//                if(unFinishAdapter!=null){
//                    unFinishAdapter.notifyDataSetChanged();
//                }
                getStudyPlanDetail();
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(StudyPlanActivity.this);

                ToastUtils.show(StudyPlanActivity.this,msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(StudyPlanActivity.this);

                ToastUtils.show(StudyPlanActivity.this,expectionMsg);

            }
        });
    }
}
