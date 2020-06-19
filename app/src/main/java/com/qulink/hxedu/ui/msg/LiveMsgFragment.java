package com.qulink.hxedu.ui.msg;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.LiveMsgItem;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.qulink.hxedu.view.SpacesItemDecoration;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveMsgFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {


    private List<LiveMsgItem.RecordsBean> data;
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
            initRecycle();
            initData();
        }
        return rootView;
    }

    private void initRecycle() {

        data = new ArrayList<>();
        recycle.setAdapter(new CommonRcvAdapter<LiveMsgItem.RecordsBean>(data) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycle.setEmptyView(llEmpty);
        recycle.addItemDecoration(new SpacesItemDecoration(0,32,0,0));
    }

    class Item implements AdapterItem<LiveMsgItem.RecordsBean> {
        @BindView(R.id.tv_open_time)
        TextView tvOpenTime;
        @BindView(R.id.v_line)
        View vLine;
        @BindView(R.id.iv_img)
        RoundedImageView ivImg;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_join_num)
        TextView tvJoinNum;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        @Override
        public int getLayoutResId() {
            return R.layout.live_msg_item;
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
        public void handleData(LiveMsgItem.RecordsBean liveMsgItem, int position) {
            tvOpenTime.setText("开播时间："+liveMsgItem.getStartTime());
            tvTitle.setText(liveMsgItem.getTitle());
            tvOpenTime.setText("直播提醒："+liveMsgItem.getNoticeTime());
            tvJoinNum.setText(liveMsgItem.getSourceId()+"人预约");
            App.getInstance().getDefaultSetting(getActivity(), new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(getActivity()).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(),liveMsgItem.getCoverUrl())).into(ivImg);
                }
            });
        }
    }

    private int pageNo;
    private int pageSize = FinalValue.limit;

    private boolean isBatch;
    private void batchLiveMsg(){
        ApiUtils.getInstance().batchLiveMsg(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                isBatch = true;
            }

            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        });
    }
    private void initData() {
        pageNo = 1;
        smartLayout.setNoMoreData(false);
        ApiUtils.getInstance().getLiveMsgList(pageNo, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                if(!isBatch){
                    batchLiveMsg();
                    isBatch=true;
                }
                smartLayout.finishRefresh(true);

                LiveMsgItem hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), LiveMsgItem.class);

                if (hotArticalList .getRecords()!= null && !hotArticalList.getRecords().isEmpty()) {
                    data.clear();
                    data.addAll(hotArticalList.getRecords());
                    recycle.getAdapter().notifyDataSetChanged();
                    if (hotArticalList.getRecords().size() < pageSize) {
                        smartLayout.setNoMoreData(true);
                    }
                } else {
                    smartLayout.setNoMoreData(true);
                }
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

    private void loadmore() {
        pageNo++;
        ApiUtils.getInstance().getLiveMsgList(pageNo, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishLoadMore(true);
                LiveMsgItem hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), LiveMsgItem.class);

                if (hotArticalList .getRecords()!= null && !hotArticalList.getRecords().isEmpty()) {
                    data.addAll(hotArticalList.getRecords());
                    recycle.getAdapter().notifyDataSetChanged();
                    if (hotArticalList.getRecords().size() < pageSize) {
                        smartLayout.setNoMoreData(true);
                    }
                } else {
                    smartLayout.setNoMoreData(true);
                }
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
}
