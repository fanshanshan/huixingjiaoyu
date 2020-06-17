package com.qulink.hxedu.ui.sign;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.entity.ScoreDetailBean;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.FinalValue;
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
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class ScoreDetailActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.recycle)
    EmptyRecyclerView recycle;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_score_detail;
    }

    @Override
    protected void init() {

        setTitle("积分明细");
        smartLayout.setOnRefreshListener(this);
        smartLayout.setOnLoadMoreListener(this);
        initRecycle();
        initData();
        smartLayout.autoRefresh();
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }


    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initData();
    }

    private void initRecycle() {
        data = new ArrayList<>();
        recycle.setAdapter(new CommonRcvAdapter<ScoreDetailBean>(data) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycle.setLayoutManager(new LinearLayoutManager(this));
    }

    private int page;
    private int pageSize = FinalValue.limit*2;
    private void initData(){
        page = 1;
        ApiUtils.getInstance().scoreDetail(page,pageSize,new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishRefresh(true);
                List<ScoreDetailBean> hotArticalList =new Gson().fromJson(GsonUtil.GsonString(t.getData()),new TypeToken<List<ScoreDetailBean>>() {}.getType());
                if(!hotArticalList.isEmpty()){
                    data.clear();
                    data.addAll(hotArticalList);
                    if(hotArticalList.size()<pageSize){
                        smartLayout.setNoMoreData(true);
                    }
                }
                recycle.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishRefresh(false);
                ToastUtils.show(ScoreDetailActivity.this,msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishRefresh(false);
                ToastUtils.show(ScoreDetailActivity.this,expectionMsg);


            }
        });
    }
    private void loadMore(){
        page ++;
        ApiUtils.getInstance().scoreDetail(page,pageSize,new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishLoadMore(true);
                List<ScoreDetailBean> hotArticalList =new Gson().fromJson(GsonUtil.GsonString(t.getData()),new TypeToken<List<ScoreDetailBean>>() {}.getType());
                if(!hotArticalList.isEmpty()){
                    data.addAll(hotArticalList);
                    if(hotArticalList.size()<pageSize){
                        smartLayout.setNoMoreData(true);
                    }
                }
                recycle.getAdapter().notifyDataSetChanged();
            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishLoadMore(false);
                ToastUtils.show(ScoreDetailActivity.this,msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishLoadMore(false);
                ToastUtils.show(ScoreDetailActivity.this,expectionMsg);


            }
        });
    }
    private List<ScoreDetailBean> data;

    class Item implements AdapterItem<ScoreDetailBean> {
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_score)
        TextView tvScore;

        @Override
        public int getLayoutResId() {
            return R.layout.score_detail_item;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ButterKnife.bind(this,root);
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(ScoreDetailBean scoreDetailBean, int position) {
            tvContent.setText(scoreDetailBean.getRemark());
            tvTime.setText(scoreDetailBean.getCreateTime());
            tvScore.setText(scoreDetailBean.getCredit()+"");
        }
    }
}
