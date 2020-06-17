package com.qulink.hxedu.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.qulink.hxedu.R;
import com.qulink.hxedu.adapter.FragmentViewPagerAdapter;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.view.tablayout.SlidingTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends Fragment {


    View rootView;
    @BindView(R.id.tab_parent)
    SlidingTabLayout tabParent;
    @BindView(R.id.vp)
    ViewPager vp;
    private Activity mActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity)context;
    }

    FragmentViewPagerAdapter viewPagerAdapter;
    private List<Fragment> fragmentList;
    private List<String> titleList;
    public LiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_live, container, false);
            ButterKnife.bind(this, rootView);
            initFragment();
            initAdapter();
            //initLiveClassfy();
        }
        return rootView;
    }

    void initFragment(){

        fragmentList = new ArrayList<>();
        LiveRecordFragment liveSubFragment =  new LiveRecordFragment();
        Bundle  bundle = new Bundle();
        bundle.putString("title","精彩回放");//全部
        liveSubFragment.setArguments(bundle);
        fragmentList.add(liveSubFragment);
        LiveReadyBeginFragment liveReadyBeginFragment =  new LiveReadyBeginFragment();
          bundle = new Bundle();
        bundle.putString("title","-1");//全部
        liveSubFragment.setArguments(bundle);
        fragmentList.add(liveReadyBeginFragment);
    }

   void initAdapter(){
       titleList = new ArrayList<>();
       titleList.add("精彩回放");
       titleList.add("即将开始");
        viewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(),titleList,fragmentList);
        vp.setAdapter(viewPagerAdapter);
        String[] s = new String[]{};
        tabParent.setViewPager(vp);
    }


}
