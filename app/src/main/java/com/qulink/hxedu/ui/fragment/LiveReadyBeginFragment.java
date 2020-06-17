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

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.R;
import com.qulink.hxedu.adapter.FragmentViewPagerAdapter;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.LiveClassfyBean;
import com.qulink.hxedu.util.DialogUtil;
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

            initLiveClassfy();
        }
        return rootView;
    }

    private List<Fragment> fragmentList;
    private List<String> titleList;

    void initAdapter() {
        titleList = new ArrayList<>();
        fragmentList = new ArrayList<>();
        ReadyBeginSubFragment liveRecordSubFragment;
        Bundle bundle;
        for(LiveClassfyBean liveClassfyBean:data){
            titleList.add(liveClassfyBean.getTitle());
            liveRecordSubFragment = new ReadyBeginSubFragment();
            bundle = new Bundle();
            bundle.putInt("id", liveClassfyBean.getId());//全部
            liveRecordSubFragment.setArguments(bundle);
            fragmentList.add(liveRecordSubFragment);
        }
        viewPagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager(), titleList, fragmentList);
        vp.setAdapter(viewPagerAdapter);
        tabParent.setViewPager(vp);
    }

    private List<LiveClassfyBean> data;

    private void initLiveClassfy(){
        ApiUtils.getInstance().getLiveClassify(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<LiveClassfyBean> hotArticalList =new Gson().fromJson(GsonUtil.GsonString(t.getData()),new TypeToken<List<LiveClassfyBean>>() {}.getType());
                data = new ArrayList<>();

                data.addAll(hotArticalList);
                initAdapter();
            }

            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        });
    }
}
