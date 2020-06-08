package com.qulink.hxedu.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.qulink.hxedu.R;
import com.qulink.hxedu.adapter.FragmentViewPagerAdapter;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.CourseBean;
import com.qulink.hxedu.entity.CourseNameBean;
import com.qulink.hxedu.mvp.contract.CourseContract;
import com.qulink.hxedu.mvp.presenter.CoursePresenter;
import com.qulink.hxedu.ui.fragment.LiveReadyBeginFragment;
import com.qulink.hxedu.ui.fragment.LiveRecordFragment;
import com.qulink.hxedu.ui.fragment.SubCourceFragment;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.view.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

public class CourseActivity extends BaseActivity implements CourseContract.View {

    FragmentViewPagerAdapter viewPagerAdapter;
    @BindView(R.id.tab_parent)
    SlidingTabLayout tabParent;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private List<Fragment> fragmentList;
    private List<String> titleList;

    CoursePresenter mPresenter;
    String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_course;
    }

    @Override
    protected void init() {
        mPresenter = new CoursePresenter();
        mPresenter.attachView(this);
        mPresenter.getCourseNameById(getIntent().getIntExtra("id",0));
        title = getIntent().getStringExtra("title");

        tvTitle.setText(title);
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    void initFragment( List<CourseNameBean> list) {

        if(list==null){
            return;
        }
        fragmentList = new ArrayList<>();
        titleList = new ArrayList<>();

        if(!list.isEmpty()){
            CourseNameBean courseNameBean = list.get(0);
            SubCourceFragment liveSubFragment ;
            Bundle bundle ;
            for(CourseNameBean.TagsBean t:courseNameBean.getTags()){
                liveSubFragment = new SubCourceFragment();
                bundle = new Bundle();
                bundle.putInt("classId",getIntent().getIntExtra("id",0));
                bundle.putInt("tagId",t.getId());
                liveSubFragment.setArguments(bundle);
                titleList.add(t.getValue());
                fragmentList.add(liveSubFragment);
            }
            viewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
            vp.setAdapter(viewPagerAdapter);
            tabParent.setViewPager(vp);
        }

    }




    @Override
    public void showLoading() {

        DialogUtil.showLoading(this,true);
    }

    @Override
    public void hideLoading() {
        DialogUtil.hideLoading(this);
    }

    @Override
    public void onError(String msg) {

    }

    @Override
    public void onSuccess(ResponseData data) {

    }

    @Override
    public void onExpcetion(String msg) {

    }

    @Override
    public void noMore() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.detachView();
    }

    @Override
    public void getCourseNameSuc( List<CourseNameBean> list) {
        initFragment(list);
    }


}
