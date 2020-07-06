package com.qulink.hxedu.ui.live;

import android.content.Intent;
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
import com.qulink.hxedu.entity.CourseNameBean;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.LiveClassfyBean;
import com.qulink.hxedu.entity.LiveDetailBean;
import com.qulink.hxedu.entity.SearchLiveBean;
import com.qulink.hxedu.entity.SearchResultBean;
import com.qulink.hxedu.mvp.contract.SearchContract;
import com.qulink.hxedu.mvp.presenter.SearchPresenter;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.course.SearchCourseActivity;
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

public class SearchLiveActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {

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
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.tv_fufei)
    TextView tvFufei;
    @BindView(R.id.tv_mianfei)
    TextView tvMianfei;

    private int chargeType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_search_live;
    }

    @Override
    protected void init() {
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

    @Override
    protected boolean enableGestureBack() {
        return false;
    }

    private void resetFilter(){
        chargeType=0;
        tvFufei.setBackgroundResource(R.drawable.filter_normal);
        tvMianfei.setBackgroundResource(R.drawable.filter_normal);
        if(classFyList!=null){
            for(LiveClassfyBean c:classFyList){
                if(c.isCheck()){
                    c.setCheck(false);
                }
            }
            recycleFilterSub.getAdapter().notifyDataSetChanged();
        }
    }
    @OnClick({R.id.tv_sure,R.id.tv_reset,R.id.tv_mianfei,R.id.tv_fufei, R.id.tv_cancel, R.id.iv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_reset:
                resetFilter();
                drawerLayout.closeDrawer(Gravity.RIGHT);

                search();
                break;
            case R.id.tv_sure:
                drawerLayout.closeDrawer(Gravity.RIGHT);
                search();
                break;
            case R.id.tv_mianfei:
                tvMianfei.setBackgroundResource(R.drawable.filter_check);
                tvFufei.setBackgroundResource(R.drawable.filter_normal);
                liveStatus = 1;

                break;
            case R.id.tv_fufei:
                tvFufei.setBackgroundResource(R.drawable.filter_check);
                tvMianfei.setBackgroundResource(R.drawable.filter_normal);
                liveStatus = 2;
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

    private List<SearchLiveBean.RecordsBean> data;

    private void initRecycle() {
        if (data == null) {
            data = new ArrayList<>();
        }
        recycleSearchResult.setAdapter(new CommonRcvAdapter<SearchLiveBean.RecordsBean>(data) {
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
              loadmore();

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        search();
    }

    class Item implements AdapterItem<SearchLiveBean.RecordsBean> {
        ImageView ivHead;
        ImageView ivImg;
        TextView tvTitle;
        TextView tvName;
        TextView tvDesc;
        TextView tvStatus;
        LinearLayout llRoot;
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
            llRoot = root.findViewById(R.id.ll_root);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(SearchLiveBean.RecordsBean o, int position) {
            tvName.setText(o.getTeacherName());
            tvTitle.setText(o.getTitle());
            App.getInstance().getDefaultSetting(SearchLiveActivity.this, new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(SearchLiveActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), o.getHeadImage())).into(ivHead);
                    Glide.with(SearchLiveActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), o.getCoverUrl())).into(ivImg);

                }
            });
            String text = "观看回放";


            tvDesc.setText(text);

            tvStatus.setText("查看回放");
            tvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show(SearchLiveActivity.this, "视频正在制作中");
                }
            });
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(SearchLiveActivity.this, LiveDetailActivity.class);
                    intent.putExtra("liveId",o.getId());
                    startActivity(intent);

//                    Intent intent = new Intent(getActivity(), AudienceActivity.class);
//                    intent.putExtra("id",o.getId());
//                    startActivity(intent);
                }
            });
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
        ApiUtils.getInstance().searchLive(pageNo, pageSize, etSearch.getText().toString()
                ,  liveStatus,getTagIds(),new ApiCallback() {
                    @Override
                    public void success(ResponseData t) {
                        DialogUtil.hideLoading(SearchLiveActivity.this);
                        smartLayout.finishRefresh(true);

                        llSearchResult.setVisibility(View.VISIBLE);
                        SearchLiveBean searchResultBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), SearchLiveBean.class);
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
                        DialogUtil.hideLoading(SearchLiveActivity.this);
                        ToastUtils.show(SearchLiveActivity.this, msg);
                        smartLayout.finishRefresh(true);
                    }

                    @Override
                    public void expcetion(String expectionMsg) {
                        DialogUtil.hideLoading(SearchLiveActivity.this);
                        ToastUtils.show(SearchLiveActivity.this, expectionMsg);
                        smartLayout.finishRefresh(true);

                    }
                });
    }
    private int liveStatus;
    private String getTagIds() {
        if (classFyList == null) {
            return null;
        }

        if (!classFyList.isEmpty()) {
            for (LiveClassfyBean c : classFyList) {
                if (c.isCheck()) {
                    return c.getId()+"";
                }
            }
        }


        return "";
    }
    void loadmore() {

        pageNo ++;
        ApiUtils.getInstance().searchLive(pageNo, pageSize, etSearch.getText().toString()
                ,  liveStatus,getTagIds(),new ApiCallback() {
                    @Override
                    public void success(ResponseData t) {
                        DialogUtil.hideLoading(SearchLiveActivity.this);
                        smartLayout.finishLoadMore(true);

                        llSearchResult.setVisibility(View.VISIBLE);
                        SearchLiveBean searchResultBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), SearchLiveBean.class);
                        if (searchResultBean.getRecords() != null) {
                            data.addAll(searchResultBean.getRecords());
                            recycleSearchResult.getAdapter().notifyDataSetChanged();
                        } else {
                            smartLayout.setNoMoreData(true);
                        }
                    }

                    @Override
                    public void error(String code, String msg) {
                        ToastUtils.show(SearchLiveActivity.this, msg);
                        smartLayout.finishLoadMore(true);
                    }

                    @Override
                    public void expcetion(String expectionMsg) {
                        ToastUtils.show(SearchLiveActivity.this, expectionMsg);
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

                if(classFyList==null){
                    getClassfy();
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

    private  List<LiveClassfyBean> classFyList;
    private void getClassfy(){
        ApiUtils.getInstance().getLiveClassify(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                classFyList =new Gson().fromJson(GsonUtil.GsonString(t.getData()),new TypeToken<List<LiveClassfyBean>>() {}.getType());
                recycleFilterSub.setAdapter(new CommonRcvAdapter<LiveClassfyBean>(classFyList) {

                    @NonNull
                    @Override
                    public AdapterItem createItem(Object type) {
                        return new FilterItem();
                    }
                });
                recycleFilterSub.setLayoutManager(new GridLayoutManager(SearchLiveActivity.this, 3));

if(recycleFilterSub.getItemDecorationCount()==0){
    recycleFilterSub.addItemDecoration(new SpacesItemDecoration(32, 32, 32, 0));

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




    class FilterItem implements AdapterItem<LiveClassfyBean>{
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
        public void handleData(LiveClassfyBean courseNameBean, int position) {
            tvContent.setText(courseNameBean.getTitle());
            tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   for(LiveClassfyBean l:classFyList){
                       if(l.getTitle().equals(courseNameBean.getTitle())){
                           l.setCheck(true);
                       }else{
                           l.setCheck(false);
                       }
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
    protected void onDestroy() {
        super.onDestroy();
    }

}
