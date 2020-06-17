package com.qulink.hxedu.ui.msg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveMsgFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.recycle)
    EmptyRecyclerView recycle;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;

    public LiveMsgFragment() {
        // Required empty public constructor
    }


    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_live_msg, container, false);
            ButterKnife.bind(this, rootView);
            smartLayout.setOnRefreshListener(this);
            initData();
        }
        return rootView;
    }


    private int pageNo;
    private int pageSize = FinalValue.limit;

    private void initData(){
        pageNo=1;
        smartLayout.setNoMoreData(false);
        ApiUtils.getInstance().getLiveMsgList(pageNo, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishRefresh(true);
            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishRefresh(true);
            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishRefresh(true);
            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        initData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }
}
