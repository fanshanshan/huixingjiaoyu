package com.qulink.hxedu.ui.bank;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.BankBean;
import com.qulink.hxedu.entity.CourseItemBean;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.qulink.hxedu.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class BankListActivity extends BaseActivity {

    @BindView(R.id.recycle)
    EmptyRecyclerView recycle;
    @BindView(R.id.iv_add)
    ImageView ivAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_bank_list;
    }

    @Override
    protected void init() {

        setTitle("银行卡");
        initRecycle();
        getBankCard();
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }


    private List<BankBean> data;

    private void initRecycle() {
        data = new ArrayList<>();
        recycle.setAdapter(new CommonRcvAdapter<BankBean>(data) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setEmptyView(findViewById(R.id.ll_empty));
        recycle.addItemDecoration(new SpacesItemDecoration(0,32,0,32));

    }

    class Item implements AdapterItem<BankBean> {
        @BindView(R.id.tv_bank_name)
        TextView tvBankName;
        @BindView(R.id.tv_bank_type)
        TextView tvBankType;
        @BindView(R.id.tv_bank_number)
        TextView tvBankNumber;

        @Override
        public int getLayoutResId() {
            return R.layout.bank_item;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ButterKnife.bind(this,root);
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(BankBean bankBean, int position) {
            tvBankName.setText(bankBean.getBankName());
            tvBankType.setText(bankBean.getCardAttr());
            tvBankNumber.setText("****  ****  ****  "+bankBean.getCardNumber().substring(bankBean.getCardNumber().length()-5,bankBean.getCardNumber().length()-1));
        }
    }

    private void getBankCard() {
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().getBankard(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(BankListActivity.this);
                        List< BankBean > list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<BankBean>>() {
                        }.getType());

                        if(list!=null&&!list.isEmpty()){
                            data.addAll(list);
                            recycle.getAdapter().notifyDataSetChanged();
                        }
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(BankListActivity.this);
                ToastUtils.show(BankListActivity.this, msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(BankListActivity.this);
                ToastUtils.show(BankListActivity.this, expectionMsg);
            }
        });
    }

    @OnClick(R.id.iv_add)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_add:
                Intent intent = new Intent(this, AddBankActivity.class);
                startActivityForResult(intent, 0);
                break;
        }
    }
}
