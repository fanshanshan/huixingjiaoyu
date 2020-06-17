package com.qulink.hxedu.ui.zone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.adapter.TopicAdapter;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.mvp.contract.LikeArticalContract;
import com.qulink.hxedu.mvp.presenter.LikeArticalPresenter;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.ImagesPreviewActivity;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import kale.adapter.util.IAdapter;

public class TopTopicActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, LikeArticalContract.View {

    @BindView(R.id.tv_topic_name)
    TextView tvTopicName;
    @BindView(R.id.tv_topic_join_num)
    TextView tvTopicJoinNum;
    @BindView(R.id.recycle)
    EmptyRecyclerView recycle;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    private String topicName;
    private int topicJoinNum;
    private int topicId;

    private LikeArticalPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_top_topic;
    }

    @Override
    protected void init() {
        smartLayout.setOnRefreshListener(this);
        smartLayout.setOnLoadMoreListener(this);
        topicName = getIntent().getStringExtra("name");
        if (topicName == null) {
            topicName = "";
        }
        topicJoinNum = getIntent().getIntExtra("num", 0);
        topicId = getIntent().getIntExtra("id", 0);
        tvTopicName.setText(topicName);
        if(getIntent().getBooleanExtra("isPlatform",false)){
            setTitle(topicName);
        }else{
            setTitle(getString(R.string.top_topic_title));

        }

        presenter = new LikeArticalPresenter();
        presenter.attachView(this);

        tvTopicJoinNum.setText("参与讨论：" + topicJoinNum);
        smartLayout.autoRefresh();
        initData();
        initPicRecycle();

    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private TopicAdapter adapter;
    private List<PicBean> picBeanList;
    void initPicRecycle() {
        picBeanList = new ArrayList<>();
        adapter = new TopicAdapter(picBeanList,this);
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
                Intent intent = new Intent(TopTopicActivity.this, ArticalDetailActivity.class);
                intent.putExtra("data",picBean);
                startActivityForResult(intent,0);
            }
        });
        recycle.setAdapter(adapter);
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setEmptyView(findViewById(R.id.ll_empty));


    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadmore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initData();
    }


    private int pageNo;
    private int pageSize = FinalValue.limit;

    void initData() {
        pageNo = 1;
        smartLayout.setNoMoreData(false);
        ApiUtils.getInstance().getTopPic(pageNo, pageSize, topicId==0?topicName:"", topicId == 0 ? "" : topicId + "", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishRefresh(true);
                List<PicBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<PicBean>>() {
                }.getType());
                if (hotArticalList.size() < pageSize) {
                    smartLayout.setNoMoreData(true);
                }
                if (!hotArticalList.isEmpty()) {
                    topicJoinNum = hotArticalList.get(0).getNumbers();
                    topicName = hotArticalList.get(0).getTopicName();
                    tvTopicJoinNum.setText("参与讨论：" + topicJoinNum);
                    tvTopicName.setText(topicName);
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

        ApiUtils.getInstance().getTopPic(pageNo, pageSize, topicName, topicId == 0 ? "" : topicId + "", new ApiCallback() {
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
                adapter.notifyItemChanged(i,picBeanList);
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
        ToastUtils.show(this,msg);
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
        presenter.detachView();
    }
}
