package com.qulink.hxedu.ui.zone;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.qulink.hxedu.R;
import com.qulink.hxedu.adapter.FragmentViewPagerAdapter;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.fragment.LiveReadyBeginFragment;
import com.qulink.hxedu.ui.fragment.LiveRecordFragment;
import com.qulink.hxedu.view.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyJoinTopicActivity extends BaseActivity {

    @BindView(R.id.tab_parent)
    SlidingTabLayout tabParent;
    @BindView(R.id.vp)
    ViewPager vp;

    private int commentNum;
    private int likeNum;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_my_join_topic;
    }

    @Override
    protected void init() {
        commentNum = getIntent().getIntExtra("commentNum",0);
        likeNum = getIntent().getIntExtra("likeNum",0);
        setTitle("我参与的");
        initFragment();
        initAdapter();
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    FragmentViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentList;
    private List<String> titleList;

    void initFragment() {

        fragmentList = new ArrayList<>();
        MyLikeArticalFragment liveSubFragment = new MyLikeArticalFragment();
        fragmentList.add(liveSubFragment);
        MyCommentsFragment liveReadyBeginFragment = new MyCommentsFragment();
        fragmentList.add(liveReadyBeginFragment);
    }

    void initAdapter() {
        titleList = new ArrayList<>();
        if(likeNum>100){
            titleList.add("我点赞的(99+)");
        }else{
            titleList.add("我点赞的("+(likeNum)+")");

        }if(commentNum>100){
            titleList.add("我评论的(99+)");

        }else{
            titleList.add("我评论的("+(commentNum)+")");
        }
        viewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        vp.setAdapter(viewPagerAdapter);
        String[] s = new String[]{};
        tabParent.setViewPager(vp);
    }
}
