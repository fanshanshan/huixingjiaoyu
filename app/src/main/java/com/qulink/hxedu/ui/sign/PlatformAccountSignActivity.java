package com.qulink.hxedu.ui.sign;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.EdittextAndDesc;
import com.qulink.hxedu.entity.PlatformScoreDetail;
import com.qulink.hxedu.entity.PlatformSubmitParam;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.MyScrollView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class PlatformAccountSignActivity extends BaseActivity {

    @BindView(R.id.tv_my_score)
    TextView tvMyScore;
    @BindView(R.id.sc)
    MyScrollView sc;
    @BindView(R.id.ll_bar)
    LinearLayout llBar;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    @BindView(R.id.recycle_score)
    RecyclerView recycleScore;
    @BindView(R.id.recycle_froup_score)
    RecyclerView recycleFroupScore;

    private PlatformScoreDetail signDetailEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_platform_account_sign;
    }

    @Override
    protected void init() {
        getScoreDetail();
        setBarBg(ContextCompat.getColor(this, R.color.white_transparent));
        setBarTtxtColors(ContextCompat.getColor(this, R.color.white));
        addScrollviewListener();
        setTitle(getString(R.string.score));
        setBackImg(R.drawable.back_white2);
    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }

    void addScrollviewListener() {
        sc.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                final float ratio = (float) Math.min(Math.max(scrollY, 0), llBar.getHeight()) / llBar.getHeight();
                final int newAlpha = (int) (ratio * 255);
                llBar.setBackgroundColor(Color.argb((int) newAlpha, 53, 134, 249));
            }
        });
    }

    private List<EdittextAndDesc> editTextList;

    void dealData(PlatformScoreDetail signDetailEntity) {
        tvSubmit.setVisibility(View.VISIBLE);
        tvMyScore.setText(signDetailEntity.getCredit() + "");
        if (signDetailEntity.isCommit()) {
            tvSubmit.setVisibility(View.GONE);
        } else {
            tvSubmit.setVisibility(View.VISIBLE);
        }

        editTextList = new ArrayList<>();
        for (PlatformScoreDetail.PscListBean p : signDetailEntity.getPscList()) {
        }
        recycleScore.setAdapter(new CommonRcvAdapter<PlatformScoreDetail.PscListBean>(signDetailEntity.getPscList()) {
            TextView tvDesc;
            EditText etScore;

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AdapterItem() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.platform_score_item;
                    }

                    @Override
                    public void bindViews(@NonNull View root) {
                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        tvDesc = root.findViewById(R.id.tv_desc);
                        etScore = root.findViewById(R.id.et_score);

                    }

                    @Override
                    public void setViews() {

                    }

                    @Override
                    public void handleData(Object o, int position) {
                        if (o instanceof PlatformScoreDetail.PscListBean) {
                            PlatformScoreDetail.PscListBean pscListBean = (PlatformScoreDetail.PscListBean) o;
                            tvDesc.setText(pscListBean.getName());
                            editTextList.add(new EdittextAndDesc(etScore, pscListBean.getName(), pscListBean.getId()));

                        }
                    }
                };
            }
        });
        recycleScore.setLayoutManager(new LinearLayoutManager(this));
        initGroupScoreRecycle(signDetailEntity);
    }


    void initGroupScoreRecycle(PlatformScoreDetail signDetailEntity) {
        recycleFroupScore.setAdapter(new CommonRcvAdapter<PlatformScoreDetail.PccListBean>(signDetailEntity.getPccList()) {


            RelativeLayout rlRoot;
            TextView tvContent;

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AdapterItem() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.platform_froup_score_item;
                    }

                    @Override
                    public void bindViews(@NonNull View root) {
                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        tvContent = root.findViewById(R.id.tv_content);
                        rlRoot = root.findViewById(R.id.rl_root);
                    }

                    @Override
                    public void setViews() {

                    }

                    @Override
                    public void handleData(Object o, int position) {
                        if (o instanceof PlatformScoreDetail.PccListBean) {
                            PlatformScoreDetail.PccListBean pccListBean = (PlatformScoreDetail.PccListBean) o;
                            tvContent.setText(pccListBean.getName());
                            rlRoot.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(PlatformAccountSignActivity.this, PlatformGroupScoreDetailActivity.class);
                                    intent.putExtra("id", pccListBean.getId());
                                    RouteUtil.startNewActivity(PlatformAccountSignActivity.this, intent);
                                }
                            });
                        }
                    }
                };
            }
        });
        recycleFroupScore.setLayoutManager(new LinearLayoutManager(this));
    }

    void getScoreDetail() {
        DialogUtil.showLoading(this, false);
        ApiUtils.getInstance().getPlatformScoreDetail(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(PlatformAccountSignActivity.this);
                signDetailEntity = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), PlatformScoreDetail.class);
                dealData(signDetailEntity);
            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(PlatformAccountSignActivity.this, msg);
                DialogUtil.hideLoading(PlatformAccountSignActivity.this);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(PlatformAccountSignActivity.this);
                ToastUtils.show(PlatformAccountSignActivity.this, expectionMsg);

            }
        });
    }

    void subit() {


        List<PlatformSubmitParam> params = new ArrayList<>();
        boolean noNodata = true;
        for (EdittextAndDesc e : editTextList) {
            if (!TextUtils.isEmpty(e.getEditText().getText().toString())) {
                noNodata = false;
                params.add(new PlatformSubmitParam(e.getId(), Integer.parseInt(e.getEditText().getText().toString())));
            }
        }

        if (noNodata) {
            ToastUtils.show(this, "至少填一项积分");
            return;
        }
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().submitPlatformScore(GsonUtil.GsonString(params), new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(PlatformAccountSignActivity.this);
                ToastUtils.show(PlatformAccountSignActivity.this, "提交成功");
                tvSubmit.setVisibility(View.GONE);
                for (EdittextAndDesc e : editTextList) {
                    e.getEditText().setText("");
                }
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(PlatformAccountSignActivity.this);
                ToastUtils.show(PlatformAccountSignActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(PlatformAccountSignActivity.this);
                ToastUtils.show(PlatformAccountSignActivity.this, expectionMsg);
            }
        });
    }

    String getIdByName(String name) {
        String id = "";
        for (PlatformScoreDetail.PscListBean p : signDetailEntity.getPscList()) {
            if (p.getName().equals(name)) {
                id = p.getId() + "";
                break;
            }
        }
        return id;
    }

    @OnClick(R.id.tv_submit)
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                subit();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        editTextList = null;
    }
}
