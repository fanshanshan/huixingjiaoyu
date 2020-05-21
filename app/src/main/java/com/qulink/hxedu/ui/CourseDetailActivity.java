package com.qulink.hxedu.ui;

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
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.qulink.hxedu.R;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.view.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class CourseDetailActivity extends BaseActivity {

    @BindView(R.id.iv_course_corver)
    ImageView ivCourseCorver;
    @BindView(R.id.tv_course_name)
    TextView tvCourseName;
    @BindView(R.id.tv_course_time)
    TextView tvCourseTime;
    @BindView(R.id.tv_study_num)
    TextView tvStudyNum;
    @BindView(R.id.tv_course_detail)
    TextView tvCourseDetail;
    @BindView(R.id.course_detail_indicator)
    View courseDetailIndicator;
    @BindView(R.id.tv_course_catalog)
    TextView tvCourseCatalog;
    @BindView(R.id.course_catalog_indicator)
    View courseCatalogIndicator;
    @BindView(R.id.ll_course_catalog)
    LinearLayout llCourseCatalog;
    @BindView(R.id.ll_course_detail)
    LinearLayout llCourseDetail;
    @BindView(R.id.iv_teacher_headimg)
    CircleImageView ivTeacherHeadimg;
    @BindView(R.id.tv_teacher_name)
    TextView tvTeacherName;
    @BindView(R.id.tv_teacher_desc)
    ExpandableTextView tvTeacherDesc;
    @BindView(R.id.tv_course_desc)
    ExpandableTextView tvCourseDesc;
    @BindView(R.id.ll_course_desc)
    LinearLayout llCourseDesc;
    @BindView(R.id.recycle_course_catalog)
    RecyclerView recycleCourseCatalog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_course_detail;
    }

    @Override
    protected void init() {

        Glide.with(this).load("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2438335972,2815985515&fm=26&gp=0.jpg").into(ivCourseCorver);
        Glide.with(this).load("https://dss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=2438335972,2815985515&fm=26&gp=0.jpg").into(ivTeacherHeadimg);
        tvCourseName.setText("演员是怎样炼成的");
        tvTeacherName.setText("杨幂");
        tvCourseTime.setText("2020-02-12 17:30");
        tvStudyNum.setText("99999人点赞");
        tvCourseDesc.setContent("杨幂，1986年9月12日出生于北京市，中国内地影视女演员、流行乐歌手、影视制片人。2005年，杨幂进入北京电影学院表演系本科班就读。2006年，因出演金庸武侠剧《神雕侠侣》而崭露头角。2008年，凭借古装剧《王昭君》获得了第24届中国电视金鹰奖观众喜爱的电视剧女演员奖提名。2009年，杨幂在“80后新生代娱乐大明星”评选活动中与其她三位女演员共同被评为“四小花旦”。2011年，凭借穿越剧《宫锁心 ... >>>");
        tvTeacherDesc.setContent("2005年，杨幂进入北京电影学院表演系本科班就读。2006年，因出演金庸武侠剧《神雕侠侣》而崭露头角。2008年，凭借古装剧《王昭君》获得了第24届中国电视金鹰奖观众喜爱的电视剧女演员奖提名 [1]  。2009年，杨幂在“80后新生代娱乐大明星”评选活动中与其她三位女演员共同被评为“四小花旦” [2]  。2011年，凭借穿越剧《宫锁心玉》赢得广泛关注 [3]  ，并获得第17届上海电视节白玉兰奖观众票选最具人气女演员奖 [4]");
        chooseCourseDetail();

    }


    void chooseCourseDetail(){
        courseDetailIndicator.setVisibility(View.VISIBLE);
        courseCatalogIndicator.setVisibility(View.GONE);
        courseDetailIndicator.setBackgroundColor(0Xff5DA0FF);
        tvCourseDetail.setTextColor(0Xff5DA0FF);
        tvCourseCatalog.setTextColor(0Xff999999);
        recycleCourseCatalog.setVisibility(View.GONE);
        llCourseDesc.setVisibility(View.VISIBLE);
    } void chooseCourseCatalog(){
        if(catalogList==null){
            initCatalog();
        }
        courseCatalogIndicator.setVisibility(View.VISIBLE);
        courseDetailIndicator.setVisibility(View.GONE);
        courseCatalogIndicator.setBackgroundColor(0Xff5DA0FF);
        tvCourseCatalog.setTextColor(0Xff5DA0FF);
        tvCourseDetail.setTextColor(0Xff999999);
        llCourseDesc.setVisibility(View.GONE);
        recycleCourseCatalog.setVisibility(View.VISIBLE);
    }


    private List<String> catalogList;
    private CommonRcvAdapter<String> commonRcvAdapter;
    void initCatalog(){
        catalogList = new ArrayList<>();
        catalogList.add("今天你吃饭了吗");
        catalogList.add("中午没吃");
        catalogList.add("为啥不吃");
        catalogList.add("减肥呢");
        catalogList.add("然后呢");
        catalogList.add("晚上要撸串");
        commonRcvAdapter = new CommonRcvAdapter<String>(catalogList) {
            TextView tvNumber;
            TextView tvTitle;
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AdapterItem() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.course_catalog_item;
                    }

                    @Override
                    public void bindViews(@NonNull View root) {
                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        tvNumber = root.findViewById(R.id.tv_number);
                        tvTitle = root.findViewById(R.id.tv_title);

                    }

                    @Override
                    public void setViews() {

                    }

                    @Override
                    public void handleData(Object o, int position) {

                        tvTitle.setText(o.toString());
                        tvNumber.setText("第"+(position+1)+"讲");
                    }
                };
            }
        };
        recycleCourseCatalog.setAdapter(commonRcvAdapter);
        recycleCourseCatalog.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }

    @OnClick({R.id.ll_course_catalog, R.id.ll_course_detail,R.id.tv_buy})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_course_catalog:
                chooseCourseCatalog();
                break;
            case R.id.ll_course_detail:
                chooseCourseDetail();
                break;
            case R.id.tv_buy:
                RouteUtil.startNewActivity(CourseDetailActivity.this,new Intent(CourseDetailActivity.this,BuyLessonActivity.class));
                break;
        }
    }
}
