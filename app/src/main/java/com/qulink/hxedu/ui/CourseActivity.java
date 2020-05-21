package com.qulink.hxedu.ui;

import android.os.Bundle;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.qulink.hxedu.R;
import com.qulink.hxedu.adapter.FragmentViewPagerAdapter;
import com.qulink.hxedu.ui.fragment.LiveReadyBeginFragment;
import com.qulink.hxedu.ui.fragment.LiveRecordFragment;
import com.qulink.hxedu.ui.fragment.SubCourceFragment;
import com.qulink.hxedu.view.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class CourseActivity extends BaseActivity {

    FragmentViewPagerAdapter viewPagerAdapter;
    @BindView(R.id.tab_parent)
    SlidingTabLayout tabParent;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private List<Fragment> fragmentList;
    private List<String> titleList;

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

        title = getIntent().getStringExtra("title");

        tvTitle.setText(title);
        initFragment();
        initAdapter();
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    void initFragment() {

        fragmentList = new ArrayList<>();
        SubCourceFragment liveSubFragment = new SubCourceFragment();
        fragmentList.add(liveSubFragment);
        liveSubFragment = new SubCourceFragment();
        fragmentList.add(liveSubFragment); liveSubFragment = new SubCourceFragment();
        fragmentList.add(liveSubFragment); liveSubFragment = new SubCourceFragment();
        fragmentList.add(liveSubFragment); liveSubFragment = new SubCourceFragment();
        fragmentList.add(liveSubFragment); liveSubFragment = new SubCourceFragment();
        fragmentList.add(liveSubFragment);
    }

    void initAdapter() {
        titleList = new ArrayList<>();
        titleList.add("标签一");
        titleList.add("标签二");
        titleList.add("标签三");
        titleList.add("标签四");
        titleList.add("标签五");
        titleList.add("标签六");
        viewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        vp.setAdapter(viewPagerAdapter);
        tabParent.setViewPager(vp);
    }
}
