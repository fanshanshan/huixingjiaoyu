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
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.SystemMsgBean;
import com.qulink.hxedu.ui.RichTextActivity;
import com.qulink.hxedu.util.CourseUtil;
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
public class SystemMsgFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.recycle)
    EmptyRecyclerView recycle;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;

    public SystemMsgFragment() {
        // Required empty public constructor
    }

    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_system_msg, container, false);
            ButterKnife.bind(this, rootView);
            smartLayout.setOnRefreshListener(this);
            smartLayout.setOnLoadMoreListener(this);
            initRecycle();
            smartLayout.autoRefresh();

            getData();
        }
        return rootView;
    }

    private int pageNo;
    private int pageSize = FinalValue.limit;

    private List<SystemMsgBean> data;

    private void initRecycle() {
        data = new ArrayList<>();
        recycle.setAdapter(new CommonRcvAdapter<SystemMsgBean>(data) {
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

    class Item implements AdapterItem<SystemMsgBean> {
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.iv_point)
        ImageView ivPoint;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        @Override
        public int getLayoutResId() {
            return R.layout.system_msg_item;
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
        public void handleData(SystemMsgBean systemMsgBean, int position) {
            tvTitle.setText(systemMsgBean.getTitle());
            tvContent.setText(systemMsgBean.getContent());
            tvTime.setText(systemMsgBean.getCreateTime());

            if (!CourseUtil.isOk(systemMsgBean.getReadStatus())) {
                ivPoint.setVisibility(View.VISIBLE);
            } else {
                ivPoint.setVisibility(View.GONE);
            }
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickPosition = position;
                    Intent intent = new Intent(mActivity, MsgDetailActivity.class);
                    intent.putExtra("content",systemMsgBean.getContent());
                    intent.putExtra("title",systemMsgBean.getTitle());
                    startActivityForResult(intent,0);
                }
            });
        }
    }

    private int clickPosition;
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(data!=null){
            data.get(clickPosition).setReadStatus(1);
            recycle.getAdapter().notifyItemChanged(clickPosition,data);
            ApiUtils.getInstance().updateMsgReadStatus(data.get(clickPosition).getId(), new ApiCallback() {
                @Override
                public void success(ResponseData t) {

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
        ApiUtils.getInstance().getSystemMsg(pageNo, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(mActivity);
                List<SystemMsgBean> systemMsgBeanList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<SystemMsgBean>>() {
                }.getType());

                if (systemMsgBeanList != null && !systemMsgBeanList.isEmpty()) {
                    data.clear();
                    data.addAll(systemMsgBeanList);
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
        ApiUtils.getInstance().getSystemMsg(pageNo, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<SystemMsgBean> systemMsgBeanList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<SystemMsgBean>>() {
                }.getType());

                if (systemMsgBeanList != null && !systemMsgBeanList.isEmpty()) {
                    data.addAll(systemMsgBeanList);
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
