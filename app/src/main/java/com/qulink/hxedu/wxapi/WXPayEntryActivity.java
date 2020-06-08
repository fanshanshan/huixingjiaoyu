package com.qulink.hxedu.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.util.FinalValue;
import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {
	
	private static final String TAG = "WXPayEntryActivity";
	
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    	api = WXAPIFactory.createWXAPI(this, FinalValue.WECHAT_APP_ID);
		try {
			boolean result =  api.handleIntent(getIntent(), this);
			if(!result){
				finish();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
    }



	@Override
	public void onReq(BaseReq req) {
	}

	@Override
	public void onResp(BaseResp resp) {

		if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
			if(resp.errCode==0){
				EventBus.getDefault().post(new MessageEvent("微信支付成功", FinalValue.WECHAT_PAYEOK));
			}
			finish();
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		api.handleIntent(data,this);
	}


}