package com.qulink.hxedu.ui;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.ScholarShipBean;
import com.qulink.hxedu.entity.UserWallet;
import com.qulink.hxedu.util.BasisTimesUtils;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class ScholarShipActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener {

    @BindView(R.id.tv_total_income)
    TextView tvTotalIncome;
    @BindView(R.id.tv_withdraw_money)
    TextView tvWithdrawMoney;
    @BindView(R.id.tv_withdrawed_money)
    TextView tvWithdrawedMoney;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_rule)
    TextView tvRule;
    @BindView(R.id.recycle)
    EmptyRecyclerView recycle;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    @BindView(R.id.ll_date)
    LinearLayout llDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_scholar_ship;
    }

    @Override
    protected void init() {

        setTitle("奖学账户");
        setRightTitle("提现", ContextCompat.getColor(this, R.color.theme_color));
        initRecycle();
        initDate();
        initScolarShipRecord();
        smartLayout.setOnRefreshListener(this);
        smartLayout.setOnLoadMoreListener(this);
    }

    List<ScholarShipBean.ListBean> data;


    private void initDate() {
        String str = "";
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
        if (month < 10) {
            str = year + "-0" + month;
        } else {
            str = year + "-" + month;
        }
        if (day < 10) {
            str = str + "-0" + day;
        } else {
            str = str + "-" + day;
        }
        tvDate.setText(str);
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

//    private void getUserWallet() {
//        DialogUtil.showLoading(this, true);
//        ApiUtils.getInstance().getUserWallet(new ApiCallback() {
//            @Override
//            public void success(ResponseData t) {
//                DialogUtil.hideLoading(ScholarShipActivity.this);
//                UserWallet userWallet = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), UserWallet.class);
//            }
//
//            @Override
//            public void error(String code, String msg) {
//                DialogUtil.hideLoading(ScholarShipActivity.this);
//                ToastUtils.show(ScholarShipActivity.this, msg);
//            }
//
//            @Override
//            public void expcetion(String expectionMsg) {
//                DialogUtil.hideLoading(ScholarShipActivity.this);
//                ToastUtils.show(ScholarShipActivity.this, expectionMsg);
//
//            }
//        });
//    }

    @OnClick({R.id.ll_date, R.id.tv_rule,R.id.tv_bar_right,R.id.ll_withdraw_record})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_date:
                showDatePickerDialog();
                break;
            case R.id.tv_rule:
                DialogUtil.showRuleDialog(this, "奖学协议", "谁他妈知道是啥");
                break;
            case R.id.tv_bar_right:
                Intent intent = new Intent(this,WithdrawActivity.class);
                intent.putExtra("money",tvWithdrawMoney.getText().toString());
                RouteUtil.startNewActivity(this,intent);
                break;
            case R.id.ll_withdraw_record:
                Intent intent1 = new Intent(this,WithdrawRecordActivity.class);
                intent1.putExtra("amount",tvWithdrawedMoney+"");
                RouteUtil.startNewActivity(this,intent1);
                break;
        }
    }

    int year,month,day;
    private void showDatePickerDialog() {
        Calendar cd =Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int y, int m, int d) {
                year = y;
                month = m;
                day = d;
              String str = "";
                if (month < 10) {
                    str = year + "-0" + month;
                } else {
                    str = year + "-" + month;
                }
                if (day < 10) {
                    str = str + "-0" + day;
                } else {
                    str = str + "-" + day;
                }
                tvDate.setText(str);
                initScolarShipRecord();
            }

        }, year, month, day);
        datePickerDialog.show();
    }

    private void initRecycle() {
        data = new ArrayList<>();
        recycle.setAdapter(new CommonRcvAdapter<ScholarShipBean.ListBean>(data) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
        recycle.setEmptyView(findViewById(R.id.ll_empty));
        recycle.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadmore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initScolarShipRecord();
    }

    class Item implements AdapterItem<ScholarShipBean.ListBean> {
        @BindView(R.id.tv_desc)
        TextView tvDesc;
        @BindView(R.id.tv_date)
        TextView tvDate;
        @BindView(R.id.tv_content)
        TextView tvContent;
        @BindView(R.id.v_line)
        View vLine;

        @Override
        public int getLayoutResId() {
            return R.layout.scholar_ship_item;
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
        public void handleData(ScholarShipBean.ListBean listBean, int position) {
            tvContent.setText(listBean.getAmount()+"");
            tvDate.setText(listBean.getUpdateTime());
            tvDesc.setText(listBean.getOperationType()==1?"入学引荐奖":listBean.getOperationType()==2?"推广达人奖":listBean.getOperationType()==3?"优秀助学金奖":"");
        }
    }

    private int page;
    private int pageSize = FinalValue.limit;

    private void initScolarShipRecord() {
        page = 1;
        smartLayout.setNoMoreData(false);
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().getScolarShipRecord(tvDate.getText().toString(), page, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(ScholarShipActivity.this);
                smartLayout.finishRefresh(true);
                ScholarShipBean scholarShipBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), ScholarShipBean.class);
                if (scholarShipBean != null) {
                    tvTotalIncome.setText(scholarShipBean.getAmount()+"");
                    tvWithdrawMoney.setText(scholarShipBean.getCanWithDrawAmount()+"");
                    tvWithdrawedMoney.setText(scholarShipBean.getWithDrawAmount()+"");
                   if(scholarShipBean.getList()!=null){
                       data.clear();
                       data.addAll(scholarShipBean.getList());
                       recycle.getAdapter().notifyDataSetChanged();
                       if (scholarShipBean.getList().size() < pageSize) {
                           smartLayout.setNoMoreData(true);
                       } else {
                           smartLayout.setNoMoreData(false);
                       }
                   }
                } else {
                    smartLayout.setNoMoreData(false);
                }
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(ScholarShipActivity.this);
                ToastUtils.show(ScholarShipActivity.this, msg);
                smartLayout.finishRefresh(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(ScholarShipActivity.this);
                ToastUtils.show(ScholarShipActivity.this, expectionMsg);
                smartLayout.finishRefresh(false);

            }
        });

    }private void loadmore() {
        page ++;
        smartLayout.setNoMoreData(false);
        ApiUtils.getInstance().getScolarShipRecord(tvDate.getText().toString(), page, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishLoadMore(true);
                ScholarShipBean scholarShipBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), ScholarShipBean.class);
                if (scholarShipBean != null) {
                    tvTotalIncome.setText(scholarShipBean.getAmount()+"");
                    tvWithdrawMoney.setText(scholarShipBean.getCanWithDrawAmount()+"");
                    tvWithdrawedMoney.setText(scholarShipBean.getWithDrawAmount()+"");
                   if(scholarShipBean.getList()!=null){
                       data.addAll(scholarShipBean.getList());
                       recycle.getAdapter().notifyDataSetChanged();
                       if (scholarShipBean.getList().size() < pageSize) {
                           smartLayout.setNoMoreData(true);
                       } else {
                           smartLayout.setNoMoreData(false);
                       }
                   }
                } else {
                    smartLayout.setNoMoreData(false);
                }
            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(ScholarShipActivity.this, msg);
                smartLayout.finishLoadMore(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                ToastUtils.show(ScholarShipActivity.this, expectionMsg);
                smartLayout.finishLoadMore(false);

            }
        });

    }
}
