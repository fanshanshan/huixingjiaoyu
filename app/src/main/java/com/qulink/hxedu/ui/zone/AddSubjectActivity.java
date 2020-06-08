package com.qulink.hxedu.ui.zone;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.SubjectBean;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class AddSubjectActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.tv_search)
    TextView tvSearch;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;

    private String CREATE_SUBJECT = "create";
    private String NOT_CHOOSE_SUBJECT = "no_choose_subject";
    private String SUBJECT = "subject";
    private List dataList;
    private int pageSize = FinalValue.limit*2;
    private int pageNo = 1;
    private CommonRcvAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_add_subject;
    }

    @Override
    protected void init() {
        dataList = new ArrayList<>();
        smartLayout.setOnLoadMoreListener(this);
        smartLayout.setEnableRefresh(false);
        initRecycle();
        searchSubject(etSearch.getText().toString());
        addEditListener();
    }
    private  void addEditListener(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(dataList.size()==1){
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    void initRecycle() {
        adapter = new CommonRcvAdapter(dataList) {
            @Override
            public Object getItemType(Object o) {
                if (o instanceof SubjectBean) {
                    return SUBJECT;
                }
                return o.toString();
            }


            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                if (type.equals(CREATE_SUBJECT)) {
                    return new CreateSubjectItem();
                } else if (type.equals(NOT_CHOOSE_SUBJECT)) {


                    return new NoChooseItem();
                } else {


                    return new AddSubjectItem();
                }

            }
        };

        recycle.setAdapter(adapter);
        recycle.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private void searchSubject(String keyword) {
        DialogUtil.showLoading(this, false);
        smartLayout.setNoMoreData(false);
        pageNo = 1;
        ApiUtils.getInstance().searchSubject(pageSize, pageNo, keyword, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(AddSubjectActivity.this);
                List<SubjectBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<SubjectBean>>() {
                }.getType());
                dataList.clear();
                if (list.isEmpty()) {
                    if(!TextUtils.isEmpty(etSearch.getText().toString())){
                        dataList.add(CREATE_SUBJECT);
                    }
                } else {
                    dataList.add(NOT_CHOOSE_SUBJECT);
                }
                dataList.addAll(list);
                refreshAdapter();
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(AddSubjectActivity.this);
                ToastUtils.show(AddSubjectActivity.this, msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(AddSubjectActivity.this);
                ToastUtils.show(AddSubjectActivity.this, expectionMsg);

            }
        });
    }

    void loadMore() {
        pageNo++;
        ApiUtils.getInstance().searchSubject(pageSize, pageNo, etSearch.getText().toString(), new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(AddSubjectActivity.this);
                List<SubjectBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<SubjectBean>>() {
                }.getType());
                if (list.size() < pageSize) {
                    smartLayout.setNoMoreData(true);
                }
                smartLayout.finishLoadMore(true);

                dataList.addAll(list);
                refreshAdapter();
            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishLoadMore(false);
                DialogUtil.hideLoading(AddSubjectActivity.this);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(AddSubjectActivity.this);
                smartLayout.finishLoadMore(false);


            }
        });
    }

    private void refreshAdapter() {
        if (adapter == null) {
            return;
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {

    }

    private void createSubject(String name){

        if(TextUtils.isEmpty(name)){
            ToastUtils.show(this,"新话题不能为空");
            return;
        }
        if(name.length()>20){
            ToastUtils.show(this,"话题长度不能超过20");

        }
        DialogUtil.showLoading(this,false);
        ApiUtils.getInstance().createSubject(name, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(AddSubjectActivity.this);
                SubjectBean subjectBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()),SubjectBean.class);
                Intent intent = new Intent();
                intent.putExtra("name", subjectBean.getName());
                intent.putExtra("id", subjectBean.getId());
                setResult(RESULT_OK, intent);
                finish();
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(AddSubjectActivity.this);
                ToastUtils.show(AddSubjectActivity.this,msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(AddSubjectActivity.this);
                ToastUtils.show(AddSubjectActivity.this,expectionMsg);

            }
        });
    }

    @OnClick(R.id.tv_search)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_search:


                searchSubject(etSearch.getText().toString());
                break;
        }
    }

    class AddSubjectItem implements AdapterItem<SubjectBean> {
        TextView tvContent;
        LinearLayout llRoot;

        @Override
        public int getLayoutResId() {
            return R.layout.add_subject;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tvContent = root.findViewById(R.id.tv_content);
            llRoot = root.findViewById(R.id.ll_root);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(SubjectBean subjectBean, int position) {
            tvContent.setText(subjectBean.getName());
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("name", subjectBean.getName());
                    intent.putExtra("id", subjectBean.getId());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }
    }

    class CreateSubjectItem implements AdapterItem<String> {
        TextView tvContent;
        @Override
        public int getLayoutResId() {
            return R.layout.create_subject;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tvContent = root.findViewById(R.id.tv_content);
        }

        @Override
        public void setViews() {
            tvContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(TextUtils.isEmpty(etSearch.getText().toString())){
                        ToastUtils.show(AddSubjectActivity.this,"新话题不能为空");
                        return;
                    }
                    createSubject(etSearch.getText().toString());
                }
            });
        }

        @Override
        public void handleData(String o, int position) {
            tvContent.setText("创建新话题："+etSearch.getText().toString());
        }
    }

    class NoChooseItem implements AdapterItem<String> {
        LinearLayout llRoot;
        @Override
        public int getLayoutResId() {
            return R.layout.no_choose_subject;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            llRoot = root.findViewById(R.id.ll_root);
        }

        @Override
        public void setViews() {
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("name","");
                    setResult(RESULT_OK,intent);
                    finish();
                }
            });
        }

        @Override
        public void handleData(String o, int position) {

        }
    }
}
