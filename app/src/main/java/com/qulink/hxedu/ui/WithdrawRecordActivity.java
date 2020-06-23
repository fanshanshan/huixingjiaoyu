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
import com.qulink.hxedu.entity.WithdrawRecordBean;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
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
import de.hdodenhof.circleimageview.CircleImageView;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class WithdrawRecordActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.tv_date)
    TextView tvDate;

    String amount;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.recycle)
    EmptyRecyclerView recycle;
    @BindView(R.id.ll_empty)
    LinearLayout llEmpty;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_withdraw_record;
    }

    int year, month, day;

    private void initDate() {
        String str = "";
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH) + 1;
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
    protected void init() {
        setTitle("累计提现");
        amount = getIntent().getStringExtra("amount");
        if (amount == null) {
            amount = "0.0";
        }

        initRecycle();
        initDate();
        initData();
        smartLayout.setOnLoadMoreListener(this);
        smartLayout.setOnRefreshListener(this);
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick({R.id.tv_date, R.id.ll_date})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_date:
                break;
            case R.id.ll_date:

                showDatePickerDialog();
                break;
        }
    }

    private void showDatePickerDialog() {
        Calendar cd = Calendar.getInstance();

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
                 initData();
            }

        }, year, month, day);
        datePickerDialog.show();
    }

    private int pageNo;
    private int pageSize = FinalValue.limit;
    private List<WithdrawRecordBean.PageBean.RecordsBean> data;

    private void initRecycle() {
        data = new ArrayList<>();
        recycle.setAdapter(new CommonRcvAdapter<WithdrawRecordBean.PageBean.RecordsBean>(data) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });

        recycle.setLayoutManager(new LinearLayoutManager(this));
        recycle.setEmptyView(llEmpty);

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadmore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initData();
    }

    class Item implements AdapterItem<WithdrawRecordBean.PageBean.RecordsBean> {
        @BindView(R.id.iv_img)
        CircleImageView ivImg;
        @BindView(R.id.tv_type)
        TextView tvType;
        @BindView(R.id.tv_amount)
        TextView tvAmount;
        @BindView(R.id.tv_time)
        TextView tvTime;
        @BindView(R.id.tv_status)
        TextView tvStatus;
        @BindView(R.id.ll_root)
        LinearLayout llRoot;

        @Override
        public int getLayoutResId() {
            return R.layout.withdraw_record_item;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ButterKnife.bind(this, root);
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(WithdrawRecordBean.PageBean.RecordsBean recordsBean, int position) {
            if (recordsBean.getType() == 1) {
                ivImg.setImageResource(R.drawable.pay_ali);
                tvType.setText("提现到支付宝");
            } else if (recordsBean.getType() == 2) {
                ivImg.setImageResource(R.drawable.pay_wx);
                tvType.setText("提现到微信");

            } else if (recordsBean.getType() == 3) {
                ivImg.setImageResource(R.drawable.pay_bank);
                tvType.setText("提现到银行卡");

            }


            if (recordsBean.getStatus() == 0) {
                tvStatus.setText("处理中");
                tvStatus.setTextColor(ContextCompat.getColor(WithdrawRecordActivity.this, R.color.withdraw_deal));
            } else if (recordsBean.getStatus() == 1) {
                tvStatus.setText("成功");
                tvStatus.setTextColor(ContextCompat.getColor(WithdrawRecordActivity.this, R.color.withdraw_suc));
            } else if (recordsBean.getStatus() == 2) {
                tvStatus.setText("失败");
                tvStatus.setTextColor(ContextCompat.getColor(WithdrawRecordActivity.this, R.color.withdraw_fail));
            }

            tvAmount.setText(recordsBean.getAmount() + "");
            tvTime.setText(recordsBean.getCreateTime());

            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(WithdrawRecordActivity.this,WithdrawResultActivity.class);
                    intent.putExtra("data",recordsBean);
                    startActivity(intent);
                }
            });
        }
    }

    private void initData() {
        smartLayout.setNoMoreData(false);

        pageNo = 1;
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().withdrawRecord(pageNo, pageSize, tvDate.getText().toString(), new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(WithdrawRecordActivity.this);
                WithdrawRecordBean withdrawRecordBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), WithdrawRecordBean.class);
                tvAmount.setText("收益" + withdrawRecordBean.getTotalAmount() + " 收益明细如下");

                if(withdrawRecordBean.getPage()!=null&&withdrawRecordBean.getPage().getRecords()!=null){
                    if(withdrawRecordBean.getPage().getRecords().size()<pageSize){
                        smartLayout.setNoMoreData(true);

                    }
                    data.clear();
                    data.addAll(withdrawRecordBean.getPage().getRecords());
                    recycle.getAdapter().notifyDataSetChanged();

                }else{
                    smartLayout.finishRefresh(true);
                    smartLayout.setNoMoreData(true);
                }
            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishRefresh(false);

                DialogUtil.hideLoading(WithdrawRecordActivity.this);
                ToastUtils.show(WithdrawRecordActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishRefresh(false);

                DialogUtil.hideLoading(WithdrawRecordActivity.this);
                ToastUtils.show(WithdrawRecordActivity.this, expectionMsg);

            }
        });
    }
    private void loadmore() {

        pageNo ++;
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().withdrawRecord(pageNo, pageSize, tvDate.getText().toString(), new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                WithdrawRecordBean withdrawRecordBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), WithdrawRecordBean.class);
                smartLayout.finishLoadMore(true);
                if(withdrawRecordBean.getPage()!=null&&withdrawRecordBean.getPage().getRecords()!=null){
                    if(withdrawRecordBean.getPage().getRecords().size()<pageSize){
                        smartLayout.setNoMoreData(true);

                    }
                    data.addAll(withdrawRecordBean.getPage().getRecords());
                    recycle.getAdapter().notifyDataSetChanged();

                }else{
                    smartLayout.setNoMoreData(true);
                }
            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishLoadMore(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishLoadMore(false);


            }
        });
    }
}
