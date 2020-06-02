package com.qulink.hxedu.ui.sign;

import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.SignDetailEntity;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.util.FastClick;
import com.qulink.hxedu.view.MyScrollView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class SignActivity extends BaseActivity {

    @BindView(R.id.sb_ui)
    View sbUi;
    @BindView(R.id.tv_sign_days)
    TextView tvSignDays;
    @BindView(R.id.ll_sign_result)
    LinearLayout llSignResult;

    @BindView(R.id.sc)
    MyScrollView sc;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.tv_bar_right)
    TextView tvBarRight;
    @BindView(R.id.ll_bar)
    LinearLayout llBar;
    @BindView(R.id.tv_sign)
    Button tvSign;
    @BindView(R.id.tv_my_score)
    TextView tvMyScore;
    @BindView(R.id.tv_today_score)
    TextView tvTodayScore;
    @BindView(R.id.tv_task1)
    TextView tvTask1;
    @BindView(R.id.tv_task2)
    TextView tvTask2;
    @BindView(R.id.tv_task3)
    TextView tvTask3;
    @BindView(R.id.tv_task4)
    TextView tvTask4;
    @BindView(R.id.recycle_plan)
    RecyclerView recyclePlan;
    @BindView(R.id.ll_un_finish_Plan)
    LinearLayout llUnFinishPlan;
    private List<String> signDayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_sign;
    }

    @Override
    protected void init() {
        setBarBg(0x00000000);
        setBarTtxtColors(0xffffffff);
        addScrollviewListener();
        setTitle(getString(R.string.earn_score));
        setRightTitle(getString(R.string.score_rule));
        setBackImg(R.drawable.back_white2);
        getScoreDetail();
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                if (userInfo.isSign()) {
                    tvSign.setVisibility(View.GONE);
                } else {
                    tvSign.setVisibility(View.VISIBLE);
                    tvSign.setText(getString(R.string.sign_get_score));
                }
            }
        });
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    void addScrollviewListener() {
        sc.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                final float ratio = (float) Math.min(Math.max(scrollY, 0), llBar.getHeight()) / llBar.getHeight();
                final int newAlpha = (int) (ratio * 255);
                llBar.setBackgroundColor(Color.argb((int) newAlpha, 53, 134, 249));
            }
        });
    }

    void getScoreDetail() {
        DialogUtil.showLoading(this, false);
        ApiUtils.getInstance().getScoreDetail(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(SignActivity.this);
                SignDetailEntity signDetailEntity = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), SignDetailEntity.class);
                dealData(signDetailEntity);

            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(SignActivity.this, msg);
                DialogUtil.hideLoading(SignActivity.this);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(SignActivity.this);
                ToastUtils.show(SignActivity.this, expectionMsg);

            }
        });
    }

    void dealData(SignDetailEntity signDetailEntity) {
        initsignDays(signDetailEntity.getSignRecord());
        tvSignDays.setText(signDetailEntity.getSignDays() + "天");
        tvMyScore.setText(signDetailEntity.getCredit() + "");
        if (signDetailEntity.watchVideoComplete()) {
            tvTask1.setBackgroundResource(R.drawable.btn_grey_bg_circle);
            tvTask1.setText(getString(R.string.complete));
        } else {
            tvTask1.setBackgroundResource(R.drawable.btn_theme_bg_circle);
            tvTask1.setText(getString(R.string.no_completed));
        }
        if (signDetailEntity.commonComplete()) {
            tvTask2.setBackgroundResource(R.drawable.btn_grey_bg_circle);
            tvTask2.setText(getString(R.string.complete));
        } else {
            tvTask2.setBackgroundResource(R.drawable.btn_theme_bg_circle);
            tvTask2.setText(getString(R.string.no_completed));
        }
        if (signDetailEntity.greatComplete()) {
            tvTask3.setBackgroundResource(R.drawable.btn_grey_bg_circle);
            tvTask3.setText(getString(R.string.complete));
        } else {
            tvTask3.setBackgroundResource(R.drawable.btn_theme_bg_circle);
            tvTask3.setText(getString(R.string.no_completed));

        }
        if (signDetailEntity.shareComplete()) {
            tvTask4.setBackgroundResource(R.drawable.btn_grey_bg_circle);
            tvTask4.setText(getString(R.string.complete));
        } else {
            tvTask4.setBackgroundResource(R.drawable.btn_theme_bg_circle);
            tvTask4.setText(getString(R.string.no_completed));
        }

        if (signDetailEntity.getLearningPlan().size() == 0) {
            llUnFinishPlan.setVisibility(View.VISIBLE);
            recyclePlan.setVisibility(View.GONE);
        } else {
            recyclePlan.setVisibility(View.VISIBLE);
            llUnFinishPlan.setVisibility(View.GONE);
            recyclePlan.setAdapter(new CommonRcvAdapter<SignDetailEntity.LearningPlanBean>(signDetailEntity.getLearningPlan()) {

                TextView tvContent;
                TextView tvComplete;
                TextView tvAddsore;

                @NonNull
                @Override
                public AdapterItem createItem(Object type) {
                    return new AdapterItem() {
                        @Override
                        public int getLayoutResId() {
                            return R.layout.sign_study_plan_item;
                        }

                        @Override
                        public void bindViews(@NonNull View root) {
                            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                            layoutParams.height= ViewGroup.LayoutParams.WRAP_CONTENT;
                            tvContent = root.findViewById(R.id.tv_content);
                            tvAddsore = root.findViewById(R.id.tv_add_score);
                            tvComplete = root.findViewById(R.id.tv_complete);

                        }

                        @Override
                        public void setViews() {

                        }

                        @Override
                        public void handleData(Object o, int position) {

                            if(o instanceof SignDetailEntity.LearningPlanBean){
                                SignDetailEntity.LearningPlanBean learningPlanBean = (SignDetailEntity.LearningPlanBean)o;
                                tvContent.setText(learningPlanBean.getContent());
                                tvAddsore.setText("+"+ FinalValue.completePlanAddScore+"积分");
                                if(learningPlanBean.isComplete()){
                                    tvComplete.setText(getString(R.string.completed));
                                    tvComplete.setBackgroundResource(R.drawable.btn_theme_bg_circle);
                                }else{
                                    tvComplete.setText(getString(R.string.no_completed));
                                    tvComplete.setBackgroundResource(R.drawable.btn_grey_bg_circle);
                                }
                            }
                        }
                    };
                }
            });
            recyclePlan.setLayoutManager(new LinearLayoutManager(this));
        }
    }

    void initsignDays(List<String> signRecord) {
        signDayList = new ArrayList<>();
        signDayList.add(getmoutianMD(-3));
        signDayList.add(getmoutianMD(-2));
        signDayList.add(getmoutianMD(-1));
        signDayList.add(getmoutianMD(0));
        signDayList.add(getmoutianMD(1));
        signDayList.add(getmoutianMD(2));
        signDayList.add(getmoutianMD(3));
        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;
        llSignResult.removeAllViews();
        for (int i = 0; i < signDayList.size(); i++) {
            imageView = new ImageView(this);
            linearLayout = new LinearLayout(this);
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            if (signRecord.contains(signDayList.get(i))) {
                if (i == 0) {
                    imageView.setImageResource(R.drawable.sign1);
                } else if (i == 1) {
                    imageView.setImageResource(R.drawable.sign2);
                } else if (i == 2) {
                    imageView.setImageResource(R.drawable.sign3);
                }
            } else {
                imageView.setImageResource(R.drawable.five_grey);
            }
            if (i == 3) {
                imageView.setImageResource(R.drawable.five);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            linearLayout.setLayoutParams(layoutParams);
            linearLayout.setGravity(Gravity.CENTER);
            layoutParams.weight = 1;
            textView = new TextView(this);
            textView.setText(signDayList.get(i).substring(5));
            textView.setTextColor(0xff999999);
            textView.setTextSize(12);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            linearLayout.addView(imageView);
            linearLayout.addView(textView);
            llSignResult.addView(linearLayout);
        }

    }

    public String getmoutianMD(int i) {
        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        String dateString = "";
        try {
            calendar.add(calendar.DATE, i);//把日期往后增加一天.整数往后推,负数往前移动
            date = calendar.getTime(); //这个时间就是日期往后推一天的结果
            dateString = formatter.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateString;


    }


    @OnClick({R.id.tv_sign_days, R.id.tv_sign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sign_days:
                break;
            case R.id.tv_sign:
                if(!FastClick.isFastClick()){
                    signServer();
                }
                break;
        }
    }


    void signServer() {
        DialogUtil.showLoading(SignActivity.this, true);
        ApiUtils.getInstance().sign(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(SignActivity.this);
                getScoreDetail();;
                App.getInstance().getUserInfo(SignActivity.this, new UserInfoCallback() {
                    @Override
                    public void getUserInfo(UserInfo userInfo) {
                        ToastUtils.show(SignActivity.this, getString(R.string.qd_desc));
                        userInfo.setSignStatus(1);
                        tvSign.setVisibility(View.GONE);
                        getScoreDetail();
                        showSignSucDialog();
                    }
                });
            }

            @Override
            public void error(String code, String msg) {

                DialogUtil.hideLoading(SignActivity.this);
                ToastUtils.show(SignActivity.this, msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(SignActivity.this);

                ToastUtils.show(SignActivity.this, expectionMsg);
            }
        });
    }


    void showSignSucDialog() {
        View diaView = View.inflate(this, R.layout.sign_suc, null);

        Dialog dialog = new Dialog(SignActivity.this, R.style.my_dialog);
        diaView.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView tvScore = diaView.findViewById(R.id.tv_score);
        tvScore.setText("+5");
        dialog.setContentView(diaView);
        dialog.show();
    }

}
