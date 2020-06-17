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
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class AuthorActivity extends BaseActivity {

    String TAG = "AuthorActivity";
//    @BindView(R.id.pusher_tx_cloud_view)
//    TXCloudVideoView pusherTxCloudView;
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

//    TXLivePusher mLivePusher;
    @BindView(R.id.ll_preview)
    LinearLayout llPreview;

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
//        TXLivePushConfig mLivePushConfig = new TXLivePushConfig();
//        mLivePusher = new TXLivePusher(this);
//        TXCloudVideoView mPusherView = (TXCloudVideoView) findViewById(R.id.pusher_tx_cloud_view);
//        mLivePusher.startCameraPreview(mPusherView);
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
        String rtmpURL = "rtmp://spark-test.haoyanzhen.com/live/5233?txSecret=ee3f12eedab8ba1daedefdf459fb3a7b&txTime=5ECD3CFF"; //此处填写您的 rtmp 推流地址
//        int ret = mLivePusher.startPusher(rtmpURL.trim());
//        if (ret == -5) {
//            Log.i(TAG, "startRTMPPush: license 校验失败");
//            ToastUtils.show(this, "license 校验失败");
//        } else {
//            //隐藏预览布局
//            llPreview.setVisibility(View.GONE);
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
//     //      if( mLivePusher.isPushing()){
//               DialogUtil.showAlertDialog(AuthorActivity.this, "提示", "确定要关闭直播间吗", "确定", new DialogInterface.OnClickListener() {
//                   @Override
//                   public void onClick(DialogInterface dialog, int which) {
//                       releaseLive();
//                       finish();
//                   }
//               }, "点错了", new DialogInterface.OnClickListener() {
//                   @Override
//                   public void onClick(DialogInterface dialog, int which) {
//
//                       dialog.dismiss();
//                   }
//               });
//           }else{
//               finish();
//           }
//
        }
        return true;
    }

//    void releaseLive(){
//        mLivePusher.stopPusher();
//        mLivePusher.stopCameraPreview(true);
//    }
}
