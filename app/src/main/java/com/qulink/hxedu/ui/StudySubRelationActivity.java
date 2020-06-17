package com.qulink.hxedu.ui;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.SubRelation;
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

public class StudySubRelationActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.recycle)
    EmptyRecyclerView recycle;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    @BindView(R.id.ll_relation_bar)
    LinearLayout llRelationBar;
    private int userId;
    private int num;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_study_sub_relation;
    }

    @Override
    protected void init() {
        setTitle("学习关系");
        smartLayout.setOnLoadMoreListener(this);
        smartLayout.setOnRefreshListener(this);
        userId = getIntent().getIntExtra("id", 0);
        num = getIntent().getIntExtra("num", 0);
        name = getIntent().getStringExtra("name");
        if (name == null) {
            name = "";
        }
        tvName.setText(name + "的学弟学妹");
        tvNumber.setText(num + "人");
        initRecycle();
        getData();
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private int page;
    private int pageSize = FinalValue.limit;
    private List<SubRelation> data;

    private void getData() {
        page = 1;
        smartLayout.setNoMoreData(false);
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().getStudySubRelation(userId, page, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(StudySubRelationActivity.this);
                List<SubRelation> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<SubRelation>>() {
                }.getType());
                if (list != null && !list.isEmpty()) {
                    llRelationBar.setVisibility(View.VISIBLE);

                    if (list.size() < pageSize) {
                        data.clear();
                        smartLayout.setNoMoreData(true);
                        data.addAll(list);
                        recycle.getAdapter().notifyDataSetChanged();
                    }
                } else {
                    llRelationBar.setVisibility(View.GONE);
                    smartLayout.setNoMoreData(true);
                }
                smartLayout.finishRefresh(true);

            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(StudySubRelationActivity.this);
                ToastUtils.show(StudySubRelationActivity.this, msg);
                smartLayout.finishRefresh(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishRefresh(false);
                DialogUtil.hideLoading(StudySubRelationActivity.this);
                ToastUtils.show(StudySubRelationActivity.this, expectionMsg);

            }
        });
    }

    private void loadMore() {
        page++;
        smartLayout.setNoMoreData(false);
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().getStudySubRelation(userId, page, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(StudySubRelationActivity.this);
                List<SubRelation> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<SubRelation>>() {
                }.getType());
                if (list != null && !list.isEmpty()) {
                    if (list.size() < pageSize) {
                        smartLayout.setNoMoreData(true);
                        data.addAll(list);
                        recycle.getAdapter().notifyDataSetChanged();
                    }
                } else {
                    smartLayout.setNoMoreData(true);
                }
                smartLayout.finishLoadMore(true);
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(StudySubRelationActivity.this);
                ToastUtils.show(StudySubRelationActivity.this, msg);
                smartLayout.finishLoadMore(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(StudySubRelationActivity.this);
                ToastUtils.show(StudySubRelationActivity.this, expectionMsg);
                smartLayout.finishLoadMore(false);


            }
        });
    }

    private void initRecycle() {
        data = new ArrayList<>();
        recycle.setAdapter(new CommonRcvAdapter<SubRelation>(data) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycle.setEmptyView(findViewById(R.id.empty));
        recycle.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getData();
    }

    class Item implements AdapterItem<SubRelation> {
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_sex)
        TextView tvSex;

        @Override
        public int getLayoutResId() {
            return R.layout.sub_relation_item;
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
        public void handleData(SubRelation subRelation, int position) {
            tvName.setText(subRelation.getName() == null ? FinalValue.defaultUserName : subRelation.getName());
            tvSex.setText(subRelation.getSex());
        }
    }
}
