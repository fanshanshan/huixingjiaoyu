package com.qulink.hxedu.ui.sign;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.PlatformGroupScoreBean;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ToastUtils;

import butterknife.BindView;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class PlatformGroupScoreDetailActivity extends BaseActivity {

    @BindView(R.id.recycle)
    RecyclerView recycle;

    String pccId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_platform_group_score_detail;
    }

    @Override
    protected void init() {
        pccId = getIntent().getIntExtra("id",0)+"";
        setTitle(getString(R.string.score));
        getData();
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    void getData() {
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().getPlatformGroupById(getIntent().getIntExtra("id", 0) + "", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(PlatformGroupScoreDetailActivity.this);
                PlatformGroupScoreBean platformGroupScoreBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), PlatformGroupScoreBean.class);

                dealData(platformGroupScoreBean);
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(PlatformGroupScoreDetailActivity.this);
                ToastUtils.show(PlatformGroupScoreDetailActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(PlatformGroupScoreDetailActivity.this);
                ToastUtils.show(PlatformGroupScoreDetailActivity.this, expectionMsg);
            }
        });
    }

    CommonRcvAdapter<PlatformGroupScoreBean.RsBean> commonRcvAdapter;
    void dealData(PlatformGroupScoreBean platformGroupScoreBean) {

        commonRcvAdapter = new CommonRcvAdapter<PlatformGroupScoreBean.RsBean>(platformGroupScoreBean.getRs()) {
            TextView tvContent;
            TextView tvComplete;
            TextView tvAddsore;
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AdapterItem() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.plat_group_detail_item;
                    }

                    @Override
                    public void bindViews(@NonNull View root) {

                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        tvContent = root.findViewById(R.id.tv_content);
                        tvAddsore = root.findViewById(R.id.tv_add_score);
                        tvComplete = root.findViewById(R.id.tv_complete);
                    }

                    @Override
                    public void setViews() {

                    }

                    @Override
                    public void handleData(Object o, int position) {

                        if(o instanceof PlatformGroupScoreBean.RsBean){
                            PlatformGroupScoreBean.RsBean rsBean = (PlatformGroupScoreBean.RsBean)o;
                            tvContent.setText(((PlatformGroupScoreBean.RsBean) o).getName());
                            if(rsBean.isComplete()){
                                tvComplete.setBackgroundResource(R.drawable.btn_grey_bg_circle);
                                tvComplete.setText(getString(R.string.completed));
                            }else{
                                tvComplete.setBackgroundResource(R.drawable.btn_theme_bg_circle);
                                tvComplete.setText(getString(R.string.no_completed));
                            }
                            tvAddsore.setText("+"+rsBean.getCredit()+"积分");
                            tvComplete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if(rsBean.isComplete()){
                                       ToastUtils.show(PlatformGroupScoreDetailActivity.this,getString(R.string.completed));
                                    }else{
                                        complete(pccId,rsBean);
                                    }
                                }
                            });
                        }
                    }
                };
            }
        };
        recycle.setAdapter(commonRcvAdapter);
        recycle.setLayoutManager(new LinearLayoutManager(this));
    }


    void complete(String pccId, PlatformGroupScoreBean.RsBean rsBean){
        DialogUtil.showLoading(this,true);
        ApiUtils.getInstance().completeGroupScore(rsBean.getId()+"", pccId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(PlatformGroupScoreDetailActivity.this);
                rsBean.setStatus(1);
                commonRcvAdapter.notifyDataSetChanged();
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(PlatformGroupScoreDetailActivity.this);
                ToastUtils.show(PlatformGroupScoreDetailActivity.this,msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(PlatformGroupScoreDetailActivity.this);
                ToastUtils.show(PlatformGroupScoreDetailActivity.this,expectionMsg);
            }
        });
    }
}
