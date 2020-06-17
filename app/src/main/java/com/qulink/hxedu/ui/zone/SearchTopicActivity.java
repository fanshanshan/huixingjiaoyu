package com.qulink.hxedu.ui.zone;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.qulink.hxedu.entity.SubjectBean;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.PrefUtils;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.SystemUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.qulink.hxedu.view.FlowLayout;
import com.qulink.hxedu.view.SoftKeyboardStateHelper;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class SearchTopicActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.recycle_hot_subject)
    RecyclerView recycleHotSubject;
    @BindView(R.id.recycle_result)
    EmptyRecyclerView recycleResult;
    @BindView(R.id.flowLayout_search_record)
    FlowLayout flowLayoutSearchRecord;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.drawer_Layout)
    LinearLayout drawerLayout;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    @BindView(R.id.ll_search_history)
    LinearLayout llSearchHistory;
    @BindView(R.id.ll_search_result)
    LinearLayout llSearchResult;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_search_topic;
    }

    @Override
    protected void init() {
        smartLayout.setOnLoadMoreListener(this);
        smartLayout.setOnRefreshListener(this);
        initRecycle();
        initSearchResult();
        initData();
        getSearchHistory();
        //监听键盘返回键
        setListenerFotEditText(drawerLayout);
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {//这里可以做具体的操作
                    searchSubject(etSearch.getText().toString());
                    return true;
                }
                return false;
            }
        });
    }

    private List<String> searchHistoryList;
    private String searchContent;

    void getSearchHistory() {
        searchContent = PrefUtils.getString(this, FinalValue.SEARCH_HISTORY_KEY, "");
        String[] searchHistoryArray = new String[]{};
        searchHistoryList = new ArrayList<>();
        if (searchContent != "") {
            searchHistoryArray = searchContent.split("&&");
            for (String s : searchHistoryArray) {
                searchHistoryList.add(s);
                addFlowItem(s);
            }
        }

    }

    void clearHistory() {
        PrefUtils.putString(this, FinalValue.SEARCH_HISTORY_KEY, "");
        flowLayoutSearchRecord.removeAllViews();
        if (searchHistoryList != null) {
            searchHistoryList.clear();

        }
    }

    void addFlowItem(String text) {

        TextView tv = (TextView) getLayoutInflater().inflate(
                R.layout.search_label_tv, flowLayoutSearchRecord, false);
        tv.setText(text);
        tv.setTextColor(0xff999999);
        tv.setBackgroundResource(R.drawable.search_txt_bg);
        //点击事件
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchSubject(text);
            }
        });
        flowLayoutSearchRecord.addView(tv);
    }


    //监听软键盘的打开和关闭
    private void setListenerFotEditText(View view) {
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(view);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                //键盘打开
                // showSearchResult();
                hideSearchResult();
            }

            @Override
            public void onSoftKeyboardClosed() {
                //键盘关闭
                // resetCommentType();

            }
        });
    }

    private void showSearchResult() {
        llSearchResult.setVisibility(View.VISIBLE);
        llSearchHistory.setVisibility(View.GONE);
    }

    private void hideSearchResult() {
        llSearchResult.setVisibility(View.GONE);
        llSearchHistory.setVisibility(View.VISIBLE);
    }


    @Override
    protected boolean enableGestureBack() {
        return false;
    }

    private List<SubjectBean> subjectBeanList;

    private void initData() {

        ApiUtils.getInstance().getTopPic(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<SubjectBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<SubjectBean>>() {
                }.getType());
                if (!hotArticalList.isEmpty()) {
                    subjectBeanList.clear();
                    subjectBeanList.addAll(hotArticalList);
                    recycleHotSubject.getAdapter().notifyDataSetChanged();

                }

            }

            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        });
    }

    private void initRecycle() {
        subjectBeanList = new ArrayList<>();
        CommonRcvAdapter<SubjectBean> adapter = new CommonRcvAdapter<SubjectBean>(subjectBeanList) {


            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AddSubjectItem();

            }
        };

        recycleHotSubject.setAdapter(adapter);
        recycleHotSubject.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initSearchResult() {
        searchResultList = new ArrayList<>();
        CommonRcvAdapter adapter = new CommonRcvAdapter(searchResultList) {


            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AddSubjectItem();

            }
        };

        recycleResult.setEmptyView(findViewById(R.id.ll_empty));
        recycleResult.setAdapter(adapter);
        recycleResult.setLayoutManager(new LinearLayoutManager(this));
    }

    @OnClick({R.id.tv_search, R.id.iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:
                searchSubject(etSearch.getText().toString());
                break;
            case R.id.iv_delete:
                clearHistory();
                break;
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadMore();

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        searchSubject(etSearch.getText().toString());

    }

    class AddSubjectItem implements AdapterItem<SubjectBean> {
        TextView tvContent;
        LinearLayout llRoot;
        ImageView ivImg;

        @Override
        public int getLayoutResId() {
            return R.layout.add_subject;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tvContent = root.findViewById(R.id.tv_content);
            ivImg = root.findViewById(R.id.iv_img);
            llRoot = root.findViewById(R.id.ll_root);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(SubjectBean subjectBean, int position) {
            tvContent.setText(subjectBean.getName());
            if (CourseUtil.isOk(subjectBean.getHotStatus())) {
                ivImg.setImageResource(R.drawable.hot);
            }
            if (CourseUtil.isOk(subjectBean.getRecStatus())) {
                ivImg.setImageResource(R.drawable.tj);
            }
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchTopicActivity.this, TopTopicActivity.class);
                    intent.putExtra("name", subjectBean.getName());
                    intent.putExtra("num", subjectBean.getNumbers());
                    intent.putExtra("id", subjectBean.getId());
                    RouteUtil.startNewActivity(SearchTopicActivity.this, intent);
                }
            });
        }
    }

    private int pageSize = FinalValue.limit * 2;
    private int pageNo = 1;

    private List<SubjectBean> searchResultList;

    boolean arrayContailsItem(String text) {
        boolean contain = false;
        for (int i = 0; i < searchHistoryList.size(); i++) {
            if (searchHistoryList.get(i).equals(text)) {
                contain = true;
                break;
            }
        }
        return contain;
    }

    private void searchSubject(String keyword) {
        if (TextUtils.isEmpty(keyword)) {
            ToastUtils.show(this, "请输入搜索内容");
            return;
        }
        if (!arrayContailsItem(keyword)) {
            addFlowItem(keyword);
            searchHistoryList.add(keyword);
            if (TextUtils.isEmpty(searchContent)) {
                searchContent = keyword;
            } else {
                searchContent = searchContent + "&&" + keyword;
            }
            PrefUtils.putString(this, FinalValue.SEARCH_HISTORY_KEY, searchContent);
        }
        SystemUtil.closeKeybord(etSearch, this);
        showSearchResult();
        DialogUtil.showLoading(this, false);
        smartLayout.setNoMoreData(false);
        pageNo = 1;
        ApiUtils.getInstance().searchSubject(pageSize, pageNo, keyword, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(SearchTopicActivity.this);
                List<SubjectBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<SubjectBean>>() {
                }.getType());

                smartLayout.finishRefresh(true);
                searchResultList.clear();
                if (!list.isEmpty()) {
                    searchResultList.addAll(list);
                    if (list.size() < pageSize) {
                        smartLayout.setNoMoreData(true);
                    }
                }
                recycleResult.getAdapter().notifyDataSetChanged();

            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(SearchTopicActivity.this);
                ToastUtils.show(SearchTopicActivity.this, msg);
                smartLayout.finishRefresh(true);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(SearchTopicActivity.this);
                ToastUtils.show(SearchTopicActivity.this, expectionMsg);
                smartLayout.finishRefresh(true);

            }
        });
    }

    void loadMore() {
        pageNo++;
        ApiUtils.getInstance().searchSubject(pageSize, pageNo, etSearch.getText().toString(), new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<SubjectBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<SubjectBean>>() {
                }.getType());
                smartLayout.finishLoadMore(true);
                if (!list.isEmpty()) {
                    searchResultList.clear();
                    searchResultList.addAll(list);
                    recycleResult.getAdapter().notifyDataSetChanged();
                    if (list.size() < pageSize) {
                        smartLayout.setNoMoreData(true);
                    }
                }
            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishLoadMore(false);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(SearchTopicActivity.this);


            }
        });
    }
}
