package com.qulink.hxedu.ui;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ToastUtils;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;

public class WithdrawRecordActivity extends BaseActivity {

    @BindView(R.id.tv_date)
    TextView tvDate;

    String amount;
    @BindView(R.id.tv_amount)
    TextView tvAmount;

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
        tvAmount.setText("收益"+amount+" 收益明细如下");
        initDate();
        initData();
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
                // initScolarShipRecord();
            }

        }, year, month, day);
        datePickerDialog.show();
    }

    private int pageNo;
    private int pageSize = FinalValue.limit;

    private void initData() {
        pageNo = 1;
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().withdrawRecord(pageNo, pageSize, tvDate.getText().toString(), new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(WithdrawRecordActivity.this);
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(WithdrawRecordActivity.this);
                ToastUtils.show(WithdrawRecordActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(WithdrawRecordActivity.this);
                ToastUtils.show(WithdrawRecordActivity.this, expectionMsg);

            }
        });
    }
}
