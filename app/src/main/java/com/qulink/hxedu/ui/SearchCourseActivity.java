package com.qulink.hxedu.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.CourseNameBean;
import com.qulink.hxedu.mvp.contract.SearchContract;
import com.qulink.hxedu.mvp.presenter.SearchPresenter;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.PrefUtils;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.FlowLayout;
import com.qulink.hxedu.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class SearchCourseActivity extends BaseActivity implements SearchContract.View {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.flowLayout_search_record)
    FlowLayout flowLayoutSearchRecord;
    @BindView(R.id.iv_delete)
    ImageView ivDelete;
    @BindView(R.id.ll_search_history)
    LinearLayout llSearchHistory;
    @BindView(R.id.ll_search_result)
    LinearLayout llSearchResult;
    @BindView(R.id.drawer_Layout)
    DrawerLayout drawerLayout;
//    @BindView(R.id.recycle_hot_subject)
//    RecyclerView recycleHotSubject;

    SearchPresenter mPresenter;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.recycle_filter_top)
    RecyclerView recycleFilterTop;
    @BindView(R.id.recycle_filter_sub)
    RecyclerView recycleFilterSub;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_search;
    }

    @Override
    protected void init() {
        mPresenter = new SearchPresenter();
        mPresenter.attachView(this);
        getSearchHistory();
        addDrawerlistener();
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search(etSearch.getText().toString());
                }
                return false;
            }
        });
    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }

    @OnClick({R.id.iv_back, R.id.tv_search, R.id.iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_search:
                search(etSearch.getText().toString());
                break;
            case R.id.iv_delete:
                clearHistory();
                break;
        }
    }

    private List<String> searchHistoryList;
    private String searchContent;

    void getSearchHistory() {
        searchContent = PrefUtils.getString(this, FinalValue.SEARCH_HISTORY_KEY, "");
        String[] searchHistoryArray = new String[]{};
        searchHistoryList = new ArrayList<>();
        if (searchContent != "") {
            searchHistoryArray = searchContent.split("&&");
            searchHistoryList = Arrays.asList(searchHistoryArray);
            for (String s : searchHistoryList) {
                addFlowItem(s);
            }
        }

    }

    void clearHistory() {
        PrefUtils.putString(this, FinalValue.SEARCH_HISTORY_KEY, "");
        flowLayoutSearchRecord.removeAllViews();
        searchHistoryList.clear();
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
                search(text);
            }
        });
        flowLayoutSearchRecord.addView(tv);
    }

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

    void search(String text) {
        if (text.equals("")) {
            ToastUtils.show(this, "请输入搜索内容");
            etSearch.requestFocus();
            return;
        }
        if (!arrayContailsItem(text)) {
            addFlowItem(text);
            searchHistoryList.add(text);
            searchContent = searchContent + "&&" + text;
            PrefUtils.putString(this, FinalValue.SEARCH_HISTORY_KEY, searchContent);
        }
    }



    private List<CourseNameBean> filterList;
    private void addDrawerlistener() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                if (filterList==null||filterList.isEmpty()) {
                    mPresenter.getFilterContent(-1);//-1默认搜索全部
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }


    private void dealSearchFilterSuc(List<CourseNameBean> list) {
        if(list==null){
            return;
        }
        CourseNameBean courseNameBean =null;
        for(int i = 0;i < list.size();i++){
            if(list.get(i).getClassify().getId()==0){
                list.remove(i);
                courseNameBean = list.get(i);
                break;
            }
        }
        if(courseNameBean!=null){
            recycleFilterTop.setAdapter(new CommonRcvAdapter<CourseNameBean.TagsBean>(courseNameBean.getTags()) {
                TextView tvContent;
                @NonNull
                @Override
                public AdapterItem createItem(Object type) {
                    return new AdapterItem<CourseNameBean.TagsBean>() {
                        @Override
                        public int getLayoutResId() {
                            return R.layout.search_filter_item;
                        }

                        @Override
                        public void bindViews(@NonNull View root) {
                            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                            tvContent = root.findViewById(R.id.tv_content);
                        }

                        @Override
                        public void setViews() {

                        }

                        @Override
                        public void handleData(CourseNameBean.TagsBean courseNameBean, int position) {
                            tvContent.setText(courseNameBean.getValue());
                            tvContent.setBackgroundResource(R.drawable.filter_normal);
                        }
                    };
                }
            });

            recycleFilterTop.addItemDecoration(new SpacesItemDecoration(16,16,0,0));
            recycleFilterTop.setLayoutManager(new GridLayoutManager(this,3));

        }


        recycleFilterSub.setAdapter(new CommonRcvAdapter<CourseNameBean>(list) {
            TextView tvName;
            RecyclerView recycle;
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AdapterItem<CourseNameBean>() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.filter_item;
                    }

                    @Override
                    public void bindViews(@NonNull View root) {
                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        tvName = root.findViewById(R.id.tv_name);
                        recycle = root.findViewById(R.id.recycle);
                    }

                    @Override
                    public void setViews() {

                    }

                    @Override
                    public void handleData(CourseNameBean courseNameBean, int position) {
                        tvName.setText(courseNameBean.getClassify().getValue());
                        recycle.setAdapter(new CommonRcvAdapter<CourseNameBean.TagsBean>(courseNameBean.getTags()) {
                            TextView tvContent;
                            @NonNull
                            @Override
                            public AdapterItem createItem(Object type) {
                                return new AdapterItem<CourseNameBean.TagsBean>() {
                                    @Override
                                    public int getLayoutResId() {
                                        return R.layout.search_filter_item;
                                    }

                                    @Override
                                    public void bindViews(@NonNull View root) {
                                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                                        tvContent = root.findViewById(R.id.tv_content);
                                    }

                                    @Override
                                    public void setViews() {

                                    }

                                    @Override
                                    public void handleData(CourseNameBean.TagsBean courseNameBean, int position) {
                                        tvContent.setText(courseNameBean.getValue());
                                        tvContent.setBackgroundResource(R.drawable.filter_normal);
                                    }
                                };
                            }
                        });

                        recycle.addItemDecoration(new SpacesItemDecoration(12,12,0,0));
                        recycle.setLayoutManager(new GridLayoutManager(SearchCourseActivity.this,3));
                    }
                };
            }
        });
        recycleFilterSub.setLayoutManager(new LinearLayoutManager(this));
        recycleFilterSub.addItemDecoration(new SpacesItemDecoration(0,12,0,0));
    }

    @Override
    public void getFilterContentSuccess(List<CourseNameBean> list) {
        filterList = list;
        dealSearchFilterSuc(list);
    }

    @Override
    public void showLoading() {

        DialogUtil.showLoading(this, false);
    }

    @Override
    public void hideLoading() {
        DialogUtil.hideLoading(this);
    }

    @Override
    public void onError(String msg) {
        ToastUtils.show(this, msg);
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
        mPresenter.detachView();
    }

}
