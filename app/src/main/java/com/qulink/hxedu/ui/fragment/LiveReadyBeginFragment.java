package com.qulink.hxedu.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.qulink.hxedu.R;
import com.qulink.hxedu.adapter.FragmentViewPagerAdapter;
import com.qulink.hxedu.view.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveReadyBeginFragment extends Fragment {


    @BindView(R.id.tab_parent)
    SlidingTabLayout tabParent;
    @BindView(R.id.vp)
    ViewPager vp;
    private View rootView;
    FragmentViewPagerAdapter viewPagerAdapter;

    public LiveReadyBeginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_live_ready_begin, container, false);

            ButterKnife.bind(this, rootView);

            initFragment();
            initAdapter();
        }
        return rootView;
    }

    private List<Fragment> fragmentList;
    private List<String> titleList;

    void initFragment() {

        fragmentList = new ArrayList<>();
        ReadyBeginSubFragment liveSubFragment = new ReadyBeginSubFragment();
        Bundle bundle = new Bundle();
        bundle.putString("title", "标题1");//全部
        liveSubFragment.setArguments(bundle);
        fragmentList.add(liveSubFragment);
        liveSubFragment = new ReadyBeginSubFragment();
        bundle = new Bundle();
        bundle.putString("title", "标题2");//全部
        liveSubFragment.setArguments(bundle);
        fragmentList.add(liveSubFragment);
        liveSubFragment = new ReadyBeginSubFragment();
        bundle = new Bundle();
        bundle.putString("title", "标题2");//全部
        liveSubFragment.setArguments(bundle);
        fragmentList.add(liveSubFragment);
        liveSubFragment = new ReadyBeginSubFragment();
        bundle = new Bundle();
        bundle.putString("title", "标题2");//全部
        liveSubFragment.setArguments(bundle);
        fragmentList.add(liveSubFragment);
        liveSubFragment = new ReadyBeginSubFragment();
        bundle = new Bundle();
        bundle.putString("title", "标题2");//全部
        liveSubFragment.setArguments(bundle);
        fragmentList.add(liveSubFragment);
        liveSubFragment = new ReadyBeginSubFragment();
        bundle = new Bundle();
        bundle.putString("title", "标题2");//全部
        liveSubFragment.setArguments(bundle);
        fragmentList.add(liveSubFragment);
        liveSubFragment = new ReadyBeginSubFragment();
        bundle = new Bundle();
        bundle.putString("title", "标题2");//全部
        liveSubFragment.setArguments(bundle);
        fragmentList.add(liveSubFragment);
    }

    void initAdapter() {
        titleList = new ArrayList<>();
        titleList.add("分类名称");
        titleList.add("分类名称");
        titleList.add("分类名称");
        titleList.add("分类名称");
        titleList.add("分类名称");
        titleList.add("分类名称");
        titleList.add("分类名称");
        viewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        vp.setAdapter(viewPagerAdapter);
        String[] s = new String[]{};
        tabParent.setViewPager(vp);
    }
}
