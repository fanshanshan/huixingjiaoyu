package com.qulink.hxedu.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.qulink.hxedu.R;
import com.qulink.hxedu.entity.WithdrawRecordBean;

import butterknife.BindView;

public class WithdrawResultActivity extends BaseActivity {

    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.tv_bar_right)
    TextView tvBarRight;
    @BindView(R.id.ll_bar)
    LinearLayout llBar;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_type)
    TextView tvType;
    @BindView(R.id.v_deal)
    View vDeal;
    @BindView(R.id.iv_deal)
    ImageView ivDeal;
    @BindView(R.id.v_suc)
    View vSuc;
    @BindView(R.id.iv_result)
    ImageView ivResult;
    @BindView(R.id.tv_statu1)
    TextView tvStatu1;
    @BindView(R.id.tv_statu1_tie)
    TextView tvStatu1Tie;
    @BindView(R.id.tv_statu2)
    TextView tvStatu2;
    @BindView(R.id.tv_statu3)
    TextView tvStatu3;
    @BindView(R.id.tv_statu3_time)
    TextView tvStatu3Time;
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.tv_amount)
    TextView tvAmount;
    @BindView(R.id.tv_apply_time)
    TextView tvApplyTime;
    @BindView(R.id.tv_result_statu)
    TextView tvResultStatu;
    @BindView(R.id.tv_result_time)
    TextView tvResultTime;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.ll_result)
    LinearLayout llResult;
    private WithdrawRecordBean.PageBean.RecordsBean recordsBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_withdraw_result;
    }

    @Override
    protected void init() {
        setTitle("提现状态");
        recordsBean = (WithdrawRecordBean.PageBean.RecordsBean) getIntent().getSerializableExtra("data");

        if (recordsBean != null) {
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
            tvStatu1Tie.setText(recordsBean.getCreateTime());
            tvStatu1Tie.setTextColor(ContextCompat.getColor(this, R.color.withdraw_dealed));

            if (recordsBean.getStatus() == 0) {
                tvStatu1.setText("发起提现");
                tvStatu2.setText("处理中");
                tvStatu1.setTextColor(ContextCompat.getColor(this, R.color.withdraw_dealed));
                tvStatu2.setTextColor(ContextCompat.getColor(this, R.color.withdraw_wait_dealed));
                vDeal.setVisibility(View.VISIBLE);
                ivDeal.setVisibility(View.VISIBLE);
            }else if(recordsBean.getStatus()==1){
                tvStatu1.setText("发起提现");
                tvStatu2.setText("处理中");
                tvStatu1.setTextColor(ContextCompat.getColor(this, R.color.withdraw_dealed));
                tvStatu2.setTextColor(ContextCompat.getColor(this, R.color.withdraw_dealed));
                tvStatu1Tie.setTextColor(ContextCompat.getColor(this, R.color.withdraw_dealed));

                vDeal.setVisibility(View.VISIBLE);
                ivDeal.setVisibility(View.VISIBLE);
                vSuc.setVisibility(View.VISIBLE);
                ivResult.setVisibility(View.VISIBLE);
                ivResult.setImageResource(R.drawable.checked);
                tvStatu3.setText("已到账");
                tvResultTime.setText(recordsBean.getPaymentTime());
                tvResultStatu.setText("到账时间");

            }else if(recordsBean.getStatus()==2){
                tvStatu1.setText("发起提现");
                tvStatu2.setText("处理中");
                tvStatu1.setTextColor(ContextCompat.getColor(this, R.color.withdraw_dealed));
                tvStatu2.setTextColor(ContextCompat.getColor(this, R.color.withdraw_dealed));
                vDeal.setVisibility(View.VISIBLE);
                ivResult.setVisibility(View.VISIBLE);
                ivDeal.setVisibility(View.VISIBLE);
                vSuc.setVisibility(View.VISIBLE);
                ivResult.setImageResource(R.drawable.cuowu);
                tvStatu3.setText(recordsBean.getFailureReason());
                tvResultTime.setText(recordsBean.getFailureTime());
                tvResultStatu.setText("失败时间");
            }


            tvAccount.setText(recordsBean.getWithdrawAccount());
            tvAmount.setText(recordsBean.getAmount()+"");
            tvOrder.setText(recordsBean.getOrderId());
            tvApplyTime.setText(recordsBean.getCreateTime());

        }
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }
}
