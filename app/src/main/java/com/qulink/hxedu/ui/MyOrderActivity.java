package com.qulink.hxedu.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;

import com.qulink.hxedu.R;
import com.qulink.hxedu.adapter.FragmentViewPagerAdapter;
import com.qulink.hxedu.ui.fragment.ReadyBeginSubFragment;
import com.qulink.hxedu.ui.fragment.SubOrderFragment;
import com.qulink.hxedu.view.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyOrderActivity extends BaseActivity {



    @BindView(R.id.tab_parent)
    SlidingTabLayout tabParent;
    @BindView(R.id.vp)
    ViewPager vp;
    private View rootView;
    FragmentViewPagerAdapter viewPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_my_order;
    }

    @Override
    protected void init() {

        setTitle(getString(R.string.my_order));
        initFragment();
        initAdapter();
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }
    private List<Fragment> fragmentList;
    private List<String> titleList;

    void initFragment() {

        fragmentList = new ArrayList<>();
        SubOrderFragment liveSubFragment = new SubOrderFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", "标题1");//全部
        liveSubFragment.setArguments(bundle);
        fragmentList.add(liveSubFragment);
        liveSubFragment = new SubOrderFragment();
        bundle = new Bundle();
        bundle.putString("title", "标题2");//全部
        liveSubFragment.setArguments(bundle);
        fragmentList.add(liveSubFragment);

    }

    void initAdapter() {
        titleList = new ArrayList<>();
        titleList.add("直播");
        titleList.add("其他课程");
        viewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        vp.setAdapter(viewPagerAdapter);
        String[] s = new String[]{};
        tabParent.setViewPager(vp);
    }
}
