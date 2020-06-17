package com.qulink.hxedu.ui.msg;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.NoticeDetail;
import com.qulink.hxedu.entity.SystemNoticeBean;
import com.qulink.hxedu.util.DialogUtil;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class SystemNoticeFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.recycle)
    EmptyRecyclerView recycle;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;

    public SystemNoticeFragment() {
        // Required empty public constructor
    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_system_notice, container, false);
            ButterKnife.bind(this, rootView);
            smartLayout.setOnRefreshListener(this);
            smartLayout.autoRefresh();
            smartLayout.setOnLoadMoreListener(this);
            initRecycle();
            getData();
        }
        return rootView;
    }

    private int pageNo;
    private int pageSize = FinalValue.limit;

    private List<SystemNoticeBean.RecordsBean> data;

    private void initRecycle() {
        data = new ArrayList<>();
        recycle.setAdapter(new CommonRcvAdapter<SystemNoticeBean.RecordsBean>(data) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycle.setLayoutManager(new LinearLayoutManager(mActivity));
        recycle.setEmptyView(llEmpty);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadmore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

    class Item implements AdapterItem<SystemNoticeBean.RecordsBean> {


        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;
        @BindView(R.id.iv_point)
        ImageView ivPoint;

        @Override
        public int getLayoutResId() {
            return R.layout.system_notice_item;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            ButterKnife.bind(this, root);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(SystemNoticeBean.RecordsBean systemMsgBean, int position) {
            tvTitle.setText(systemMsgBean.getTitle());
            if (systemMsgBean.getType() == 1) {
                tvType.setBackgroundResource(R.drawable.gonggao);
                tvType.setText("公告");
            } else {
                tvType.setBackgroundResource(R.drawable.huodong);
                tvType.setText("活动");
            }
            if (systemMsgBean.getUnread()==1){
                ivPoint.setVisibility(View.GONE);
            }else{
                ivPoint.setVisibility(View.VISIBLE);

            }

                llRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickPosition = position;

                        if(systemMsgBean.getType()==1){
                            getNoticeDetail(systemMsgBean.getId());
                        }else{
                            Intent intent = new Intent(mActivity, ActivityDetailActivity.class);
                            intent.putExtra("id", systemMsgBean.getId());
                            intent.putExtra("title", systemMsgBean.getTitle());
                            startActivityForResult(intent, 0);
                        }

                    }
                });
        }
    }

    private int clickPosition;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (data != null) {
            data.get(clickPosition).setUnread(1);
            recycle.getAdapter().notifyItemChanged(clickPosition, data);
        }
    }

    private void getNoticeDetail(int id){
        ApiUtils.getInstance().getNoticeDetail(id, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                NoticeDetail noticeDetail = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()),NoticeDetail.class);
                Intent intent = new Intent(mActivity, NoticeDetailActivity.class);
                intent.putExtra("data", noticeDetail);

                startActivityForResult(intent, 0);
            }

            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        });
    }
    private Activity mActivity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    private void getData() {
        smartLayout.setNoMoreData(false);
        pageNo = 1;
        DialogUtil.showLoading(mActivity, false);
        ApiUtils.getInstance().getSystemNotice(pageNo, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(mActivity);
                SystemNoticeBean systemMsgBeanList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), SystemNoticeBean.class);

                if (systemMsgBeanList.getRecords() != null && !systemMsgBeanList.getRecords().isEmpty()) {
                    data.clear();
                    data.addAll(systemMsgBeanList.getRecords());
                    recycle.getAdapter().notifyDataSetChanged();
                } else {
                    smartLayout.setNoMoreData(true);
                }
                smartLayout.finishRefresh(true);
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(mActivity);
                ToastUtils.show(mActivity, msg);
                smartLayout.finishRefresh(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(mActivity);
                smartLayout.finishRefresh(false);

                ToastUtils.show(mActivity, expectionMsg);
            }
        });
    }

    private void loadmore() {
        pageNo++;
        ApiUtils.getInstance().getSystemNotice(pageNo, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                SystemNoticeBean systemMsgBeanList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), SystemNoticeBean.class);

                if (systemMsgBeanList.getRecords() != null && !systemMsgBeanList.getRecords().isEmpty()) {
                    data.addAll(systemMsgBeanList.getRecords());
                    recycle.getAdapter().notifyDataSetChanged();
                } else {
                    smartLayout.setNoMoreData(true);
                }
                smartLayout.finishLoadMore(true);
            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(mActivity, msg);
                smartLayout.finishLoadMore(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishLoadMore(false);

                ToastUtils.show(mActivity, expectionMsg);
            }
        });
    }


}
