package com.qulink.hxedu.ui.msg;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.qulink.hxedu.R;
import com.qulink.hxedu.adapter.FragmentViewPagerAdapter;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.SystemIndexBean;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MsgActivity extends BaseActivity {

    //    @BindView(R.id.tab_parent)
//    SlidingTabLayout tabParent;
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.tabLayout)
    TabLayout tabLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_msg;
    }

    @Override
    protected void init() {

        setTitle("消息");
        initFragment();

        getMsgStatus();
        //  initIndexMsgInfo();
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
        SystemMsgFragment liveSubFragment = new SystemMsgFragment();
        fragmentList.add(liveSubFragment);
        SystemNoticeFragment systemNoticeFragment = new SystemNoticeFragment();
        fragmentList.add(systemNoticeFragment);
        ZoneMsgFragment zoneMsgFragment = new ZoneMsgFragment();
        fragmentList.add(zoneMsgFragment);
        LiveMsgFragment liveMsgFragment = new LiveMsgFragment();
        fragmentList.add(liveMsgFragment);
    }

    private TextView tv_tab_title;


    private void initTab(SystemIndexBean systemIndexBean) {
        titleList = new ArrayList<>();
        titleList.add("系统消息");
        titleList.add("通告消息");
        titleList.add("社区消息");
        titleList.add("直播消息");
        //添加tabLayout选中监听
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                //设置选中时的文字颜色
                if (tab.getCustomView() != null) {
                    tv_tab_title = tab.getCustomView().findViewById(R.id.tv_tab_title);
                    tv_tab_title.setTextColor(getResources().getColor(R.color.colorAccent));
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                //设置未选中时的文字颜色
                if (tab.getCustomView() != null) {
                    tv_tab_title = tab.getCustomView().findViewById(R.id.tv_tab_title);
                    tv_tab_title.setTextColor(getResources().getColor(R.color.black));
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), titleList, fragmentList);
        vp.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(vp);
        if (tabLayout.getTabAt(0).getCustomView() != null) {
            tabLayout.getTabAt(0).getCustomView().setSelected(true);

        }

        if (systemIndexBean.isUmrStatus()) {
            TabLayout.Tab tab = tabLayout.getTabAt(0);
            // 更新Badge前,先remove原来的customView,否则Badge无法更新
            View customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }

            // 更新CustomView
            tab.setCustomView(R.layout.tab_msg_item);
            tv_tab_title = tab.getCustomView().findViewById(R.id.tv_tab_title);
            tv_tab_title.setText("系统消息");

        }
        if (systemIndexBean.isAmrStatus()) {
            TabLayout.Tab tab = tabLayout.getTabAt(1);
            // 更新Badge前,先remove原来的customView,否则Badge无法更新
            View customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }

            // 更新CustomView
            tab.setCustomView(R.layout.tab_msg_item);
            tv_tab_title = tab.getCustomView().findViewById(R.id.tv_tab_title);
            tv_tab_title.setText("通告消息");
        }
        if (systemIndexBean.isCmrStatus()) {
            TabLayout.Tab tab = tabLayout.getTabAt(2);
            // 更新Badge前,先remove原来的customView,否则Badge无法更新
            View customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }

            // 更新CustomView
            tab.setCustomView(R.layout.tab_msg_item);
            tv_tab_title = tab.getCustomView().findViewById(R.id.tv_tab_title);
            tv_tab_title.setText("社区消息");
        }
        if (systemIndexBean.isLmrStatus()) {
            TabLayout.Tab tab = tabLayout.getTabAt(3);
            // 更新Badge前,先remove原来的customView,否则Badge无法更新
            View customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }

            // 更新CustomView
            tab.setCustomView(R.layout.tab_msg_item);
            tv_tab_title = tab.getCustomView().findViewById(R.id.tv_tab_title);
            tv_tab_title.setText("直播消息");
        }


    }

    private void getMsgStatus() {
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().getMsgReadStatus(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(MsgActivity.this);
                SystemIndexBean systemIndexBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), SystemIndexBean.class);
                initTab(systemIndexBean);
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(MsgActivity.this);
                ToastUtils.show(MsgActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(MsgActivity.this);
                ToastUtils.show(MsgActivity.this, expectionMsg);
            }
        });
    }

    class TitleBean {
        boolean hasNoRead;
        String title;

        public TitleBean(String title) {
            this.title = title;
        }
    }


}
