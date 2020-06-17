package com.qulink.hxedu.ui.msg;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.ZoneMsgBean;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoneMsgFragment extends Fragment implements OnRefreshListener {


    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    @BindView(R.id.tv_pinglun_number)
    TextView tvPinglunNumber;
    @BindView(R.id.tv_pinglun_desc)
    TextView tvPinglunDesc;
    @BindView(R.id.ll_pinglun)
    LinearLayout llPinglun;
    @BindView(R.id.tv_share_number)
    TextView tvShareNumber;
    @BindView(R.id.tv_share_desc)
    TextView tvShareDesc;
    @BindView(R.id.ll_zhuanfa)
    LinearLayout llZhuanfa;
    @BindView(R.id.tv_zan_number)
    TextView tvZanNumber;
    @BindView(R.id.tv_zan_desc)
    TextView tvZanDesc;
    @BindView(R.id.ll_zan)
    LinearLayout llZan;

    public ZoneMsgFragment() {
        // Required empty public constructor
    }

    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_zone_msg, container, false);
            ButterKnife.bind(this, rootView);
            smartLayout.setEnableLoadMore(false);
            smartLayout.setOnRefreshListener(this);
            initData();
        }
        return rootView;
    }

    private ZoneMsgBean zoneMsgBean;

    private void dealData() {
        if (zoneMsgBean == null) {
            return;
        }
        if (zoneMsgBean.getComments() != 0) {
            tvPinglunDesc.setVisibility(View.VISIBLE);
            tvPinglunNumber.setVisibility(View.VISIBLE);
            if (zoneMsgBean.getComments() > 99) {
                tvPinglunNumber.setText("99+");
            } else {
                tvPinglunNumber.setText(zoneMsgBean.getComments()+"");
            }
        }else{
            tvPinglunDesc.setVisibility(View.GONE);
            tvPinglunNumber.setVisibility(View.GONE);
        }
        if (zoneMsgBean.getShares() != 0) {
            tvShareDesc.setVisibility(View.VISIBLE);
            tvShareNumber.setVisibility(View.VISIBLE);
            if (zoneMsgBean.getShares() > 99) {
                tvShareNumber.setText("99+");
            } else {
                tvShareNumber.setText(zoneMsgBean.getShares()+"");
            }
        }else{
            tvShareDesc.setVisibility(View.GONE);
            tvShareNumber.setVisibility(View.GONE);
        }
        if (zoneMsgBean.getThumbs() != 0) {
            tvZanDesc.setVisibility(View.VISIBLE);
            tvZanNumber.setVisibility(View.VISIBLE);
            if (zoneMsgBean.getThumbs() > 99) {
                tvZanNumber.setText("99+");
            } else {
                tvZanNumber.setText(zoneMsgBean.getThumbs()+"");
            }
        } else {
            tvZanDesc.setVisibility(View.GONE);
            tvZanNumber.setVisibility(View.GONE);
        }
    }

    private void initData() {
        ApiUtils.getInstance().getZoneMsg(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                smartLayout.finishRefresh(true);
                zoneMsgBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), ZoneMsgBean.class);
                dealData();

            }

            @Override
            public void error(String code, String msg) {
                smartLayout.finishRefresh(false);

            }

            @Override
            public void expcetion(String expectionMsg) {
                smartLayout.finishRefresh(false);

            }
        });
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initData();
    }


    @OnClick({R.id.ll_pinglun, R.id.ll_zhuanfa, R.id.ll_zan})
    public void onViewClicked(View view) {
        Intent intent = new Intent(getActivity(),ZoneMsgSubPageActivity.class);
        switch (view.getId()) {
            case R.id.ll_pinglun:
                clickType = 1;
                intent.putExtra("title","评论");
                intent.putExtra("type",0);
                break;
            case R.id.ll_zhuanfa:
                clickType = 2;
                intent.putExtra("title","分享");
                intent.putExtra("type",2);
                break;
            case R.id.ll_zan:
                clickType=3;
                intent.putExtra("title","点赞");
                intent.putExtra("type",1);
                break;

        }
        startActivityForResult(intent,0);
    }
    private int clickType;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(zoneMsgBean==null){
            return;
        }
        if(clickType==1){
            zoneMsgBean.setComments(0);
        }else if(clickType==2){
            zoneMsgBean.setShares(0);
        }else if(clickType==3){
            zoneMsgBean.setThumbs(0);
        }
        dealData();
    }
}
