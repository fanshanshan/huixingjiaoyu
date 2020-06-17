package com.qulink.hxedu.ui.zone;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.R;
import com.qulink.hxedu.adapter.TopicAdapter;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.mvp.contract.LikeArticalContract;
import com.qulink.hxedu.mvp.presenter.LikeArticalPresenter;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyLikeArticalFragment extends Fragment implements OnLoadMoreListener, OnRefreshListener, LikeArticalContract.View {


    @BindView(R.id.recycle)
    EmptyRecyclerView recycle;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    private Activity mActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    public MyLikeArticalFragment() {
        // Required empty public constructor
    }

    private View rootView;
    private LikeArticalPresenter presenter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_my_comments, container, false);
            ButterKnife.bind(this, rootView);
            presenter = new LikeArticalPresenter();
            presenter.attachView(this);
            initRecle();
            smartLayout.setEnableLoadMore(true);

            smartLayout.setOnRefreshListener(this);
            smartLayout.setOnLoadMoreListener(this);
            smartLayout.autoRefresh();
        }
        return rootView;
    }

    private TopicAdapter adapter;
    private List<PicBean> picBeanList;

    private void initRecle(){
        picBeanList = new ArrayList<>();
        adapter = new TopicAdapter(picBeanList,mActivity);
        recycle.setAdapter(adapter);
        adapter.setClickListener(new TopicAdapter.PicAdapterController() {
            @Override
            public void getPicMaster(int userId) {
                getPicMasterFromSever(userId);
            }

            @Override
            public void cancelLikeArtical(int id) {
                presenter.cancelLikeArtical(id);
            }

            @Override
            public void likeArtical(int id) {
                presenter.likeArtical(id);

            }

            @Override
            public void goArticalDetail(PicBean picBean) {
                Intent intent = new Intent(mActivity, ArticalDetailActivity.class);
                intent.putExtra("data",picBean);
                startActivityForResult(intent,0);
            }
        });
        recycle.setLayoutManager(new LinearLayoutManager(mActivity));
    }


    private int pageNo;
    private int pageSize = FinalValue.limit;

    void initData() {
        pageNo = 1;
        smartLayout.setNoMoreData(false);
        ApiUtils.getInstance().getMyLikeArtical(pageNo, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishRefresh(true);
                List<PicBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<PicBean>>() {
                }.getType());
                if (hotArticalList.size() < pageSize) {
                    smartLayout.setNoMoreData(true);
                }
                if (!hotArticalList.isEmpty()) {
                    picBeanList.clear();
                    picBeanList.addAll(hotArticalList);
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishRefresh(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishRefresh(false);

            }
        });
    }

    void loadmore() {
        pageNo++;

        ApiUtils.getInstance().getMyLikeArtical(pageNo, pageSize,  new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishLoadMore(true);
                List<PicBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<PicBean>>() {
                }.getType());

                if (!hotArticalList.isEmpty()) {
                    if (hotArticalList.size() < pageSize) {
                        smartLayout.setNoMoreData(true);
                    }
                    picBeanList.addAll(hotArticalList);
                    adapter.notifyItemRangeChanged(picBeanList.size()-hotArticalList.size(),hotArticalList.size());
                }
                smartLayout.finishLoadMore(true);
            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishLoadMore(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishLoadMore(false);

            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadmore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initData();
    }

    @Override
    public void likeArticalSuc(int articalId) {
        for(int i = 0;i<picBeanList.size();i++){
            if(picBeanList.get(i).getId()==articalId){
                picBeanList.get(i).setThumbStatus(1);
                picBeanList.get(i).setThumbs(picBeanList.get(i).getThumbs()+1);

                recycle.getAdapter().notifyItemChanged(i,picBeanList);
                break;
            }
        }
    }

    @Override
    public void cancelLikeArticalSuc(int articalId) {
        for(int i = 0;i<picBeanList.size();i++){
            if(picBeanList.get(i).getId()==articalId){
                picBeanList.get(i).setThumbStatus(0);
                picBeanList.get(i).setThumbs(picBeanList.get(i).getThumbs()-1);
                recycle.getAdapter().notifyItemChanged(i,picBeanList);
                break;
            }
        }
    }
    private void getPicMasterFromSever(int userId) {
        ApiUtils.getInstance().getPicMasterById(userId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<PicMaster> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<PicMaster>>() {
                }.getType());
                if (hotArticalList.size() > 0) {
                    dealGetMasterSuc(hotArticalList.get(0));
                }
            }

            @Override
            public void error(String code, String msg) {
                dealGetMasterFail(userId);
            }

            @Override
            public void expcetion(String expectionMsg) {
                dealGetMasterFail(userId);

            }
        });
    }

    private void dealGetMasterSuc(PicMaster picMaster) {
        for(int i = 0;i<picBeanList.size();i++){
            if(picBeanList.get(i).getUserId()==picMaster.getUserId()&&picBeanList.get(i).getPicMaster()==null){
                picBeanList.get(i).setPicMaster(picMaster);
                picBeanList.get(i).setInitMaster(false);
                recycle.getAdapter().notifyItemChanged(i,picBeanList);
                break;
            }
        }
    }


    private void dealGetMasterFail(int userId){
        for(int i = 0;i<picBeanList.size();i++){
            if(picBeanList.get(i).getUserId()==userId&&picBeanList.get(i).getPicMaster()==null){
                picBeanList.get(i).setPicMaster(new PicMaster());
                picBeanList.get(i).setInitMaster(false);
                recycle.getAdapter().notifyItemChanged(i,picBeanList);
                break;
            }
        }
    }
    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

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
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==-1){
            PicBean picBean = (PicBean) data.getSerializableExtra("data");
            if(picBean==null){
                return;
            }
            for(int i = 0;i<picBeanList.size();i++){
                if(picBeanList.get(i).getId()==picBean.getId()){
                    picBeanList.get(i).setThumbStatus(picBean.getThumbStatus());
                    picBeanList.get(i).setThumbs(picBean.getThumbs());
                    picBeanList.get(i).setComments(picBean.getComments());
                    adapter.notifyItemChanged(i,picBeanList);
                    break;
                }
            }
        }
    }
}
