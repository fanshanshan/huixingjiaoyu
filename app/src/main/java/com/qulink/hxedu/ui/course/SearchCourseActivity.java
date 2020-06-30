package com.qulink.hxedu.ui.course;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
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

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.entity.CourseNameBean;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.SearchResultBean;
import com.qulink.hxedu.mvp.contract.SearchContract;
import com.qulink.hxedu.mvp.presenter.SearchPresenter;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.PrefUtils;
import com.qulink.hxedu.util.SystemUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.qulink.hxedu.view.FlowLayout;
import com.qulink.hxedu.view.SpacesItemDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class SearchCourseActivity extends BaseActivity implements SearchContract.View, OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.et_search)
    EditText etSearch;
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
    @BindView(R.id.recycle_filter_sub)
    RecyclerView recycleFilterSub;
    @BindView(R.id.recycle_search_result)
    EmptyRecyclerView recycleSearchResult;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.iv_zonghe)
    ImageView ivZonghe;
    @BindView(R.id.ll_zonghe)
    LinearLayout llZonghe;
    @BindView(R.id.iv_price)
    ImageView ivPrice;
    @BindView(R.id.ll_price)
    LinearLayout llPrice;
    @BindView(R.id.iv_filter)
    ImageView ivFilter;
    @BindView(R.id.ll_filter)
    LinearLayout llFilter;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.tv_fufei)
    TextView tvFufei;
    @BindView(R.id.tv_mianfei)
    TextView tvMianfei;
    @BindView(R.id.tv_vip)
    TextView tvVip;

    private int chargeType;
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
        smartLayout.setOnRefreshListener(this);
        smartLayout.setOnLoadMoreListener(this);
        initRecycle();
        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    search();
                }
                return false;
            }
        });
    }


    private int pageNo;
    private int pageSize = FinalValue.limit;
    private int zonghe = 0;
    private int price = 0;

    @Override
    protected boolean enableGestureBack() {
        return false;
    }

    private void resetFilter(){
        chargeType=0;
        tvVip.setBackgroundResource(R.drawable.filter_normal);
        tvFufei.setBackgroundResource(R.drawable.filter_normal);
        tvMianfei.setBackgroundResource(R.drawable.filter_normal);
        if(courseNameBeanList!=null){
            for(CourseNameBean c:courseNameBeanList){
                for(CourseNameBean.TagsBean t:c.getTags()){
                    if(t.isCheck()){
                        t.setCheck(false);
                    }
                }
            }
            recycleFilterSub.getAdapter().notifyDataSetChanged();
        }
    }
    @OnClick({R.id.tv_sure,R.id.tv_reset,R.id.tv_mianfei,R.id.tv_fufei,R.id.tv_vip,R.id.back, R.id.tv_cancel, R.id.iv_delete, R.id.ll_zonghe, R.id.ll_price, R.id.ll_filter})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_reset:
                resetFilter();
                break;
            case R.id.tv_sure:
                drawerLayout.closeDrawer(Gravity.RIGHT);
                search();
                break;
            case R.id.tv_mianfei:
                tvMianfei.setBackgroundResource(R.drawable.filter_check);
                tvFufei.setBackgroundResource(R.drawable.filter_normal);
                tvVip.setBackgroundResource(R.drawable.filter_normal);
                chargeType = 1;
                break;
            case R.id.tv_fufei:
                tvFufei.setBackgroundResource(R.drawable.filter_check);
                tvMianfei.setBackgroundResource(R.drawable.filter_normal);
                tvVip.setBackgroundResource(R.drawable.filter_normal);
                chargeType = 3;
                break;
            case R.id.tv_vip:
                tvVip.setBackgroundResource(R.drawable.filter_check);
                tvFufei.setBackgroundResource(R.drawable.filter_normal);
                tvMianfei.setBackgroundResource(R.drawable.filter_normal);
                chargeType = 2;
                break;
            case R.id.iv_back:
                if (llSearchResult.getVisibility() == View.VISIBLE) {
                    llSearchResult.setVisibility(View.GONE);
                } else {
                    finish();
                }
                break;
            case R.id.tv_cancel:
                finish();

                break;
            case R.id.iv_delete:
                clearHistory();
                break;
            case R.id.ll_zonghe:
                if (zonghe == 0) {
                    zonghe = 1;
                    ivZonghe.setImageResource(R.drawable.xia);
                } else {
                    zonghe = 0;
                    ivZonghe.setImageResource(R.drawable.shang);
                }
                search();
                break;
            case R.id.ll_price:
                if (price == 0) {
                    price = 1;
                    ivPrice.setImageResource(R.drawable.xia);
                } else {
                    price = 0;
                    ivPrice.setImageResource(R.drawable.shang);
                }
                search();

                break;
            case R.id.ll_filter:
                drawerLayout.openDrawer(Gravity.RIGHT);
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
            for (String s : searchHistoryArray) {
                searchHistoryList.add(s);
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
                etSearch.setText(text);
                search();
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

    private List<SearchResultBean.RecordsBean> data;

    private void initRecycle() {
        if (data == null) {
            data = new ArrayList<>();
        }
        recycleSearchResult.setAdapter(new CommonRcvAdapter<SearchResultBean.RecordsBean>(data) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycleSearchResult.setLayoutManager(new LinearLayoutManager(this));
        recycleSearchResult.setEmptyView(llEmpty);
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        search();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        loadmore();
    }

    class Item implements AdapterItem<SearchResultBean.RecordsBean> {
        @BindView(R.id.iv_img)
        RoundedImageView ivImg;
        @BindView(R.id.tv_title)
        TextView tvTitle;
        @BindView(R.id.tv_price)
        TextView tvPrice;
        @BindView(R.id.iv_sb_ui)
        ImageView ivSbUi;
        @BindView(R.id.tv_join_num)
        TextView tvJoinNum;
        @BindView(R.id.v_line)
        View vLine;

        @Override
        public int getLayoutResId() {
            return R.layout.search_course_item;
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
        public void handleData(SearchResultBean.RecordsBean recordsBean, int position) {
            App.getInstance().getDefaultSetting(SearchCourseActivity.this, new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(SearchCourseActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), recordsBean.getCurriculumImage())).into(ivImg);

                }
            });

            tvTitle.setText(recordsBean.getCurriculumName());
            tvJoinNum.setText(recordsBean.getParticipantNum() + "人加入了学习");
            if (recordsBean.getSpecialStatus() == 1) {
                ivSbUi.setImageResource(R.drawable.viptj);
                tvPrice.setText(recordsBean.getVipPrice() + "");

            } else {
                tvPrice.setText(recordsBean.getCurriculumPrice() + "");

            }
        }
    }

    private String classifyld;

    void search() {
        if (etSearch.getText().toString().equals("")) {
            ToastUtils.show(this, "请输入搜索内容");
            etSearch.requestFocus();
            return;
        }
        if (!arrayContailsItem(etSearch.getText().toString())) {
            addFlowItem(etSearch.getText().toString());
            searchHistoryList.add(etSearch.getText().toString());
            searchContent = searchContent + "&&" + etSearch.getText().toString();
            PrefUtils.putString(this, FinalValue.SEARCH_HISTORY_KEY, searchContent);
        }
        smartLayout.setNoMoreData(false);
        SystemUtil.closeKeybord(etSearch, this);
        DialogUtil.showLoading(this, false);
        pageNo = 1;
        ApiUtils.getInstance().searchCourse(pageNo, pageSize, etSearch.getText().toString()
                , chargeType, classifyld, getTagIds(), price, zonghe, new ApiCallback() {
                    @Override
                    public void success(ResponseData t) {
                        DialogUtil.hideLoading(SearchCourseActivity.this);
                        smartLayout.finishRefresh(true);

                        llSearchResult.setVisibility(View.VISIBLE);
                        SearchResultBean searchResultBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), SearchResultBean.class);
                        if (searchResultBean.getRecords() != null) {
                            data.clear();
                            data.addAll(searchResultBean.getRecords());
                            recycleSearchResult.getAdapter().notifyDataSetChanged();
                        } else {
                            smartLayout.setNoMoreData(true);
                        }
                    }

                    @Override
                    public void error(String code, String msg) {
                        DialogUtil.hideLoading(SearchCourseActivity.this);
                        ToastUtils.show(SearchCourseActivity.this, msg);
                        smartLayout.finishRefresh(true);
                    }

                    @Override
                    public void expcetion(String expectionMsg) {
                        DialogUtil.hideLoading(SearchCourseActivity.this);
                        ToastUtils.show(SearchCourseActivity.this, expectionMsg);
                        smartLayout.finishRefresh(true);

                    }
                });
    }
    void loadmore() {

        pageNo ++;
        ApiUtils.getInstance().searchCourse(pageNo, pageSize, etSearch.getText().toString()
                , chargeType, classifyld, getTagIds(), price, zonghe, new ApiCallback() {
                    @Override
                    public void success(ResponseData t) {
                        DialogUtil.hideLoading(SearchCourseActivity.this);
                        smartLayout.finishLoadMore(true);

                        llSearchResult.setVisibility(View.VISIBLE);
                        SearchResultBean searchResultBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), SearchResultBean.class);
                        if (searchResultBean.getRecords() != null) {
                            data.addAll(searchResultBean.getRecords());
                            recycleSearchResult.getAdapter().notifyDataSetChanged();
                        } else {
                            smartLayout.setNoMoreData(true);
                        }
                    }

                    @Override
                    public void error(String code, String msg) {
                        ToastUtils.show(SearchCourseActivity.this, msg);
                        smartLayout.finishLoadMore(true);
                    }

                    @Override
                    public void expcetion(String expectionMsg) {
                        ToastUtils.show(SearchCourseActivity.this, expectionMsg);
                        smartLayout.finishLoadMore(true);

                    }
                });
    }


    private void addDrawerlistener() {
        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                if (courseNameBeanList == null || courseNameBeanList.isEmpty()) {
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

    private List<CourseNameBean> courseNameBeanList;

    private void dealSearchFilterSuc() {
        if (courseNameBeanList == null) {

            return;
        }


        recycleFilterSub.setAdapter(new CommonRcvAdapter<CourseNameBean>(courseNameBeanList) {

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new SbUiItem();
            }
        });
        recycleFilterSub.setLayoutManager(new LinearLayoutManager(this));
        recycleFilterSub.addItemDecoration(new SpacesItemDecoration(0, 12, 0, 0));
    }
    class SbUiItem implements AdapterItem<CourseNameBean>{
        TextView tvName;
        RecyclerView recycle;

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

                @NonNull
                @Override
                public AdapterItem createItem(Object type) {
                    return new FilterItem();
                }
            });
            if (recycle.getItemDecorationCount() == 0) {
                recycle.addItemDecoration(new SpacesItemDecoration(12, 12, 0, 0));

            }
            recycle.setLayoutManager(new GridLayoutManager(SearchCourseActivity.this, 3));
        }
    }

    class FilterItem implements AdapterItem<CourseNameBean.TagsBean>{
        TextView tvContent;

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
            tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(courseNameBean.isCheck()){
                        courseNameBean.setCheck(false);
                    }else{
                        courseNameBean.setCheck(true);
                    }
                    recycleFilterSub.getAdapter().notifyDataSetChanged();
                }
            });
            if(courseNameBean.isCheck()){
                tvContent.setBackgroundResource(R.drawable.filter_check);
            }else{
                tvContent.setBackgroundResource(R.drawable.filter_normal);

            }
        }
    }

    @Override
    public void getFilterContentSuccess(List<CourseNameBean> list) {
        courseNameBeanList = list;
        dealSearchFilterSuc();
    }

    private String getTagIds() {
        if (courseNameBeanList == null) {
            return null;
        }
        String str = null;

        if (!courseNameBeanList.isEmpty()) {
            str = "";
            for (CourseNameBean c : courseNameBeanList) {
                for (CourseNameBean.TagsBean t : c.getTags()) {
                    if (t.isCheck()) {
                        str += t.getId() + ",";
                    }
                }
            }
        }

        if (!TextUtils.isEmpty(str)) {
            str = str.substring(0, str.length() - 1);
        }
        return str;
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
