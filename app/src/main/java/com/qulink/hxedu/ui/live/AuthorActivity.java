package com.qulink.hxedu.ui.live;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.live.liveroom.IMLVBLiveRoomListener;
import com.qulink.hxedu.ui.live.liveroom.MLVBLiveRoom;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.tencent.rtmp.TXLivePushConfig;
import com.tencent.rtmp.TXLivePusher;
import com.tencent.rtmp.ui.TXCloudVideoView;

import butterknife.BindView;
import butterknife.OnClick;

public class AuthorActivity extends BaseActivity {

    String TAG = "AuthorActivity";
    @BindView(R.id.pusher_tx_cloud_view)
    TXCloudVideoView pusherTxCloudView;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.iv_turn)
    ImageView ivTurn;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_share_wx)
    ImageView ivShareWx;
    @BindView(R.id.iv_share_qq)
    ImageView ivShareQq;
    @BindView(R.id.iv_share_zone)
    ImageView ivShareZone;
    @BindView(R.id.tv_start_ive)
    TextView tvStartIve;

    @BindView(R.id.ll_preview)
    LinearLayout llPreview;
    MLVBLiveRoom mlvbLiveRoom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_author;
    }

    @Override
    protected void init() {
        //启动本地摄像头预览
        mlvbLiveRoom = MLVBLiveRoom.sharedInstance(this);
//        TXLivePushConfig mLivePushConfig = new TXLivePushConfig();
//        mLivePusher = new TXLivePusher(this);
//        TXCloudVideoView mPusherView = (TXCloudVideoView) findViewById(R.id.pusher_tx_cloud_view);
//        mLivePusher.startCameraPreview(mPusherView);
        mlvbLiveRoom.startLocalPreview(true,pusherTxCloudView);
    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }

    @OnClick({R.id.iv_turn, R.id.iv_close, R.id.iv_share_wx, R.id.iv_share_qq, R.id.iv_share_zone, R.id.tv_start_ive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_turn:
                break;
            case R.id.iv_close:
                break;
            case R.id.iv_share_wx:
                break;
            case R.id.iv_share_qq:
                break;
            case R.id.iv_share_zone:
                break;
            case R.id.tv_start_ive:
                startLive();
                break;
        }
    }

    void startLive() {
        //98946.livepush.myqcloud.com
        mlvbLiveRoom.createRoom("5233", "爱我你怕了吗", new IMLVBLiveRoomListener.CreateRoomCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                ToastUtils.show(AuthorActivity.this, errInfo);
            }

            @Override
            public void onSuccess(String RoomID) {
                llPreview.setVisibility(View.GONE);

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mlvbLiveRoom.setListener(null);
        mlvbLiveRoom.logout();

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if(true){
               DialogUtil.showAlertDialog(AuthorActivity.this, "提示", "确定要关闭直播间吗", "确定", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       releaseLive();
                       finish();
                   }
               }, "点错了", new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {

                       dialog.dismiss();
                   }
               });
           }else{
               finish();
           }
//
        }
        return true;
    }

    void releaseLive(){
    }
}
