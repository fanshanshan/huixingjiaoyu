package com.qulink.hxedu.ui.fragment;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.LiveDetailBean;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.ToastUtils;
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
public class LiveRecordSubFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.recycle_view)
    EmptyRecyclerView recycleView;

    CommonRcvAdapter adapter;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_empty)
    TextView llEmpty;

    private int classFyId;

    public LiveRecordSubFragment() {
        // Required empty public constructor
    }


    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_live_sub_record, container, false);
            ButterKnife.bind(this, rootView);
            classFyId = getArguments().getInt("id");
            getData();
            refreshLayout.setOnLoadMoreListener(this);
            refreshLayout.setOnRefreshListener(this);
            initData();
        }
        return rootView;
    }

    private List<LiveDetailBean.RecordsBean> data;


    void getData() {
        data = new ArrayList<>();

        adapter = new CommonRcvAdapter<LiveDetailBean.RecordsBean>(data) {


            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        };
        recycleView.setAdapter(adapter);
        recycleView.addItemDecoration(new SpacesItemDecoration(0, 16, 0, 0));
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadmore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initData();
    }

    class Item implements AdapterItem<LiveDetailBean.RecordsBean> {
        ImageView ivHead;
        ImageView ivImg;
        TextView tvTitle;
        TextView tvName;
        TextView tvDesc;
        TextView tvStatus;

        @Override
        public int getLayoutResId() {
            return R.layout.live_record_item;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            ivHead = root.findViewById(R.id.iv_head);
            ivImg = root.findViewById(R.id.iv_img);
            tvTitle = root.findViewById(R.id.tv_title);
            tvName = root.findViewById(R.id.tv_name);
            tvStatus = root.findViewById(R.id.tv_status);
            tvDesc = root.findViewById(R.id.tv_desc);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(LiveDetailBean.RecordsBean o, int position) {
            tvName.setText(o.getTeacherName());
            tvTitle.setText(o.getTitle());
            App.getInstance().getDefaultSetting(getActivity(), new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(getContext()).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), o.getHeadImage())).into(ivHead);
                    Glide.with(getContext()).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), o.getCoverUrl())).into(ivImg);

                }
            });
            String text = "观看回放";


            tvDesc.setText(text);

            tvStatus.setText("查看回放");
            tvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show(getActivity(), "视频正在制作中");
                }
            });
        }
    }

    private int pageNo;
    private int pageSize = FinalValue.limit;

    private void initData() {
        pageNo = 1;
        refreshLayout.setNoMoreData(false);
        llEmpty.setVisibility(View.GONE);

        ApiUtils.getInstance().getLiveList(pageNo, pageSize, 1, classFyId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                LiveDetailBean liveDetailBean = new Gson().fromJson(GsonUtil.GsonString(t.getData()), LiveDetailBean.class);
                if (liveDetailBean.getRecords() != null && !liveDetailBean.getRecords().isEmpty()) {
                    data.clear();
                    data.addAll(liveDetailBean.getRecords());
                    if (liveDetailBean.getRecords().size() < pageSize) {
                        refreshLayout.setNoMoreData(true);

                    }
                    llEmpty.setVisibility(View.GONE);

                } else {
                    llEmpty.setVisibility(View.VISIBLE);
                    refreshLayout.setNoMoreData(true);
                }
                refreshLayout.finishRefresh(true);
            }

            @Override
            public void error(String code, String msg) {
                refreshLayout.finishRefresh(true);

            }

            @Override
            public void expcetion(String expectionMsg) {
                refreshLayout.finishRefresh(true);

            }
        });

    }

    private void loadmore() {
        pageNo++;
        ApiUtils.getInstance().getLiveList(pageNo, pageSize, 1, classFyId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                LiveDetailBean liveDetailBean = new Gson().fromJson(GsonUtil.GsonString(t.getData()), LiveDetailBean.class);
                if (liveDetailBean.getRecords() != null && !liveDetailBean.getRecords().isEmpty()) {
                    data.addAll(liveDetailBean.getRecords());
                    if (liveDetailBean.getRecords().size() < pageSize) {
                        refreshLayout.setNoMoreData(true);

                    }
                } else {
                    refreshLayout.setNoMoreData(true);
                }
                refreshLayout.finishLoadMore(true);
            }

            @Override
            public void error(String code, String msg) {
                refreshLayout.finishLoadMore(true);

            }

            @Override
            public void expcetion(String expectionMsg) {
                refreshLayout.finishLoadMore(true);

            }
        });

    }
}

