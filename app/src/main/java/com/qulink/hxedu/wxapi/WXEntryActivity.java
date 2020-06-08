package com.qulink.hxedu.wxapi;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.qulink.hxedu.R;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.util.FinalValue;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import org.greenrobot.eventbus.EventBus;


public class WXEntryActivity extends AppCompatActivity implements IWXAPIEventHandler {
    /**
     * 微信登录相关
     */
    private IWXAPI api;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxentry);
        //通过WXAPIFactory工厂获取IWXApI的示例
        api = WXAPIFactory.createWXAPI(this, FinalValue.WECHAT_APP_ID,true);
        //将应用的appid注册到微信
        api.registerApp(FinalValue.WECHAT_APP_ID);
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
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp baseResp) {
        String result = "";
        switch(baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                if(baseResp.getType()==1){
                 EventBus.getDefault().post(new MessageEvent(((SendAuth.Resp) baseResp).code,FinalValue.WECHAT_LOGINEOK));
                    finish();
                }else if(baseResp.getType()==2){
                    EventBus.getDefault().post(new MessageEvent("分享成功",FinalValue.WECHAT_SHAREOK));
                }
                finish();
                //拿到了微信返回的code,立马再去请求access_token String code =
                // ((SendAuth.Resp) resp).code; LogUtils.sf("code = " + code);
                // 就在这个地方，用网络库什么的或者自己封的网络api，发请求去咯，注意是
                // get请求 break; case RETURN_MSG_TYPE_SHARE: UIUtils.showToast("微信分享成功"); finish(); break; }
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                if(baseResp.getType()==1){
                    EventBus.getDefault().post(new MessageEvent("",1));
                }
                finish();
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                if(baseResp.getType()==1){
                    EventBus.getDefault().post(new MessageEvent("",1));
                }
                finish();
                break;
            default:
                finish();
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        api.handleIntent(data,this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
        finish();
    }
}
