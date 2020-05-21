package com.qulink.hxedu.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qulink.hxedu.R;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.PrefUtils;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class SearchActivity extends BaseActivity {

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
    @BindView(R.id.recycle_hot_subject)
    RecyclerView recycleHotSubject;

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
        getSearchHistory();
        initHotSubject();

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
        return true;
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


    private CommonRcvAdapter<String> hotSubjectAdapter;
    private List<String> subjectData;

    void initHotSubject(){
    subjectData = new ArrayList<>();
    subjectData.add("yi");
    subjectData.add("giao");
    subjectData.add("wo");
    subjectData.add("li");
    subjectData.add("giao");
    subjectData.add("yahong");
    hotSubjectAdapter = new CommonRcvAdapter<String>(subjectData) {
        TextView tvTitle;

        @NonNull
        @Override
        public AdapterItem createItem(Object type) {
            return new AdapterItem() {
                @Override
                public int getLayoutResId() {
                    return R.layout.search_hot_subject_item;
                }

                @Override
                public void bindViews(@NonNull View root) {
                    ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                    layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    tvTitle = root.findViewById(R.id.tv_title);
                }

                @Override
                public void setViews() {

                }

                @Override
                public void handleData(Object o, int position) {
                    tvTitle.setText("# "+o.toString());
                }
            };
        }
    };
    recycleHotSubject.setAdapter(hotSubjectAdapter);
    recycleHotSubject.setLayoutManager(new LinearLayoutManager(this));
    }

}
