package com.qulink.hxedu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.util.RouteUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class VipDetailActivity extends BaseActivity {

    @BindView(R.id.tv_vip_price_desc)
    TextView tvVipPriceDesc;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.tv_buy)
    TextView tvBuy;
    @BindView(R.id.iv_send)
    ImageView ivSend;



    private String endTimeFormat = "%s 元/年";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_vip_detail;
    }

    @Override
    protected void init() {

        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                if(userInfo.isVip()){
                    tvBuy.setVisibility(View.GONE);
                    //tvBuy.setText(getString(R.string.next_vip));
                }else{
                    if(userInfo.isBoughtVip()){
                        tvBuy.setText(getString(R.string.next_vip));
                    }else{
                        tvBuy.setText(getString(R.string.open_vip));
                    }
                }
            }
        });
        App.getInstance().getDefaultSetting(this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {

                tvVipPriceDesc.setText(String.format(endTimeFormat, defaultSetting.getVip_price().getValue()));

            }
        });
    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }

    @OnClick({R.id.iv_send, R.id.tv_buy})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.iv_send:
                RouteUtil.startNewActivity(VipDetailActivity.this,new Intent(VipDetailActivity.this,SendVipActivity.class));
                 intent = new Intent(VipDetailActivity.this,SendVipActivity.class);
                intent.putExtra("type","send");
                RouteUtil.startNewActivity(VipDetailActivity.this,intent);

                break;
            case R.id.tv_buy:
                 intent = new Intent(VipDetailActivity.this,SendVipActivity.class);
                intent.putExtra("type","buy");
                RouteUtil.startNewActivity(VipDetailActivity.this,intent);

                break;
        }
    }


}
