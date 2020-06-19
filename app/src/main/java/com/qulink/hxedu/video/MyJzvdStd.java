package com.qulink.hxedu.video;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.CatalogBean;
import com.qulink.hxedu.entity.VideoInfo;
import com.qulink.hxedu.util.ToastUtils;

import java.util.List;

import cn.jzvd.JZDataSource;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * 这里可以监听到视频播放的生命周期和播放状态
 * 所有关于视频的逻辑都应该写在这里
 * Created by Nathen on 2017/7/2.
 */
public class MyJzvdStd extends JzvdStd {

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    private int increaseDivider = 3;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            times++;
            if(times>(increaseDivider*60)){
                times=0;
                increaseLookNumber();
            }
            if(handler!=null){
                handler.sendEmptyMessageDelayed(1,1000);
            }
        }
    };
    private void increaseLookNumber(){
        ApiUtils.getInstance().increaseLookTime(courseId, increaseDivider, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                ToastUtils.show(getContext(),"上报成功");
            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(getContext(),"上报失败"+msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                ToastUtils.show(getContext(),"上报失败"+expectionMsg);
            }
        });
    }
    private int times;
    private void startTimer(){
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler.sendEmptyMessageDelayed(1,1000);
        }
    }
    private void pauseTimer(){
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
        }
    }
    private  void stopTimer(){
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
    }
    private int maxFreeSecond = 5;
    private LinearLayout llBuy;
    private TextView tvNuy;
    private TextView tvRepeat;

    private int courseId;
    public int getVideoType() {
        return videoType;
    }

    public void setVideoType(int videoType) {
        this.videoType = videoType;
    }

    List<CatalogBean> catalogList;
    private int playCurrentIndex;
    private VideoStatuListener videoStatuListener;

    public VideoStatuListener getVideoStatuListener() {
        return videoStatuListener;
    }

    public void setVideoStatuListener(VideoStatuListener videoStatuListener) {
        this.videoStatuListener = videoStatuListener;
    }

    private int videoType;//1免费 2购买 3vip
    public int getPlayCurrentIndex() {
        return playCurrentIndex;
    }

    public void setPlayCurrentIndex(int playCurrentIndex) {
        this.playCurrentIndex = playCurrentIndex;
    }

    public List<CatalogBean> getCatalogList() {
        return catalogList;
    }

    public void mStartPlay(){
        llBuy.setVisibility(GONE);
        if(catalogList.get(playCurrentIndex).getVideoInfo()==null){
            getVideoUrl(catalogList.get(playCurrentIndex).getVideoId());
        }else{
            setUp(catalogList.get(playCurrentIndex).getVideoInfo().getVideoUrl(),"sb");
            startVideo();
        }
    }

    public void setCatalogList(List<CatalogBean> catalogList) {
        this.catalogList = catalogList;
    }


    public void changePlayIndex(int index){
        llBuy.setVisibility(GONE);
        this.playCurrentIndex = index;
        if(catalogList.get(playCurrentIndex).getVideoInfo()==null){
            getVideoUrl(catalogList.get(playCurrentIndex).getVideoId());
        }else{
            setUp(catalogList.get(playCurrentIndex).getVideoInfo().getVideoUrl(),catalogList.get(playCurrentIndex).getVideoInfo().getVideoName());
            changeUrl(jzDataSource,0);
        }

    }


    public int getFreeLookMinute() {
        return freeLookMinute;
    }

    public void setFreeLookMinute(int freeLookMinute) {
        this.freeLookMinute = freeLookMinute;
    }

    private int freeLookMinute = 5;
    public MyJzvdStd(Context context) {
        super(context);
    }

    public MyJzvdStd(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        llBuy = findViewById(R.id.ll_buy);
        tvNuy = findViewById(R.id.tv_buy);
        tvNuy.setOnClickListener(this::onClick);
        tvRepeat = findViewById(R.id.tv_repeat);
        tvRepeat.setOnClickListener(this::onClick);

    }

    @Override
    public void onClick(View v) {
        if(catalogList==null){
            if(videoStatuListener!=null){
                videoStatuListener.startClick();
            }
        }else{
            int i = v.getId();
            switch (i){
                case R.id.tv_buy:
                    if(videoType==2){
                        if(videoStatuListener!=null){
                            videoStatuListener.buyLesson();
                        }
                    }else if(videoType==3||videoType==4){
                        if(videoStatuListener!=null){
                            videoStatuListener.openVip();
                        }
                    }
                    break;
                case R.id.tv_repeat:
                    llBuy.setVisibility(GONE);
                    isNoPay = false;
                    changeUrl(jzDataSource,0);
                    progressBar.setProgress(0);
                    break;
                    default:
                        super.onClick(v);

            }
        }


    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        super.onTouch(v, event);
        int id = v.getId();
        if (id == cn.jzvd.R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (mChangePosition) {
                        Log.i(TAG, "Touch screen seek position");
                    }
                    if (mChangeVolume) {
                        Log.i(TAG, "Touch screen change volume");
                    }
                    break;
            }
        }

        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.video_play_layout;
    }

    @Override
    public void startVideo() {
        super.startVideo();
        Log.i(TAG, "startVideo");
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        Log.i(TAG, "Seek position ");
    }

    @Override
    public void gotoFullscreen() {
        super.gotoFullscreen();
        Log.i(TAG, "goto Fullscreen");
    }

    @Override
    public void gotoNormalScreen() {
        super.gotoNormalScreen();
        Log.i(TAG, "quit Fullscreen");
    }

    @Override
    public void autoFullscreen(float x) {
        super.autoFullscreen(x);
        Log.i(TAG, "auto Fullscreen");
    }

    @Override
    public void onClickUiToggle() {
        super.onClickUiToggle();
        Log.i(TAG, "click blank");
    }

    private boolean isPlaying;

    //onState 代表了播放器引擎的回调，播放视频各个过程的状态的回调
    @Override
    public void onStateNormal() {
        super.onStateNormal();
    }

    @Override
    public void onStatePreparing() {
        super.onStatePreparing();
    }

    @Override
    public void onStatePlaying() {
        super.onStatePlaying();
        if(!isPlaying){
            isPlaying = true;
            startTimer();
        }
    }

    @Override
    public void onStatePause() {
        super.onStatePause();
        isPlaying = false;
        pauseTimer();
    }

    @Override
    public void onStateError() {
        super.onStateError();
        isPlaying = false;
        pauseTimer();
    }

    @Override
    public void onStateAutoComplete() {
        super.onStateAutoComplete();
        playCurrentIndex++;
        if (playCurrentIndex<catalogList.size()){
            setUp(catalogList.get(playCurrentIndex).getVideoInfo().getVideoUrl(),catalogList.get(playCurrentIndex).getVideoInfo().getVideoName());
            changeUrl(jzDataSource,0);
        }

    }


    //changeUiTo 真能能修改ui的方法
    @Override
    public void changeUiToNormal() {
        super.changeUiToNormal();
    }

    @Override
    public void changeUiToPreparing() {
        super.changeUiToPreparing();
    }

    @Override
    public void changeUiToPlayingShow() {
        super.changeUiToPlayingShow();
    }

    @Override
    public void changeUiToPlayingClear() {
        super.changeUiToPlayingClear();
    }

    @Override
    public void changeUiToPauseShow() {
        super.changeUiToPauseShow();
    }

    @Override
    public void changeUiToPauseClear() {
        super.changeUiToPauseClear();
    }

    @Override
    public void changeUiToComplete() {
        super.changeUiToComplete();
    }

    @Override
    public void changeUiToError() {
        super.changeUiToError();
    }

    @Override
    public void onInfo(int what, int extra) {
        super.onInfo(what, extra);
    }

    @Override
    public void onError(int what, int extra) {
        super.onError(what, extra);
    }


    boolean isNoPay = false;
    @Override
    public void onProgress(int progress, long position, long duration) {
        super.onProgress(progress, position, duration);

        long totalSeconds = position / 1000;
        if (videoType!=1) {   //这里是从服务器拿到特定字段判断是否付费  这里模拟未付费
            if (totalSeconds >= maxFreeSecond) {   //考虑未付费情况下拖动进度条超过试看时间
                if (!isNoPay) {   //加个标记 因为此函数一直在回调
                    progressBar.setProgress(maxFreeSecond);
                    goOnPlayOnPause();
                 //   changeUrl(jzDataSource,maxFreeSecond);
                   // Jzvd.goOnPlayOnPause();           //在适当的时候（支付成功后）播放
                    startButton.setEnabled(false);    //在适当的时候取消
                    isNoPay = true;
                    llBuy.setVisibility(VISIBLE);
                    tvNuy.setText(videoType==2?"购买课程":videoType==3?"开通会员":"立即兑换");
                }
            }
        }


    }

    public void dealCourseAndResume(){
        llBuy.setVisibility(GONE);
        if(catalogList==null){
            if(videoStatuListener!=null){
                videoStatuListener.startClick();
            }
        }else{
            videoType=1;
            goOnPlayOnResume();
        }

    }
    private void getVideoUrl(int videoId){

        ApiUtils.getInstance().getCourseVideoUrl(videoId+"", new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                VideoInfo videoInfo = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()),VideoInfo.class);
                catalogList.get(playCurrentIndex).setVideoInfo(videoInfo);
                setUp(catalogList.get(playCurrentIndex).getVideoInfo().getVideoUrl(),catalogList.get(playCurrentIndex).getVideoInfo().getVideoName());
                changeUrl(jzDataSource,0);
            }

            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        });
    }


    public void destroy(){
        if(handler!=null){
            handler.removeCallbacksAndMessages(this);
            handler=null;
            releaseAllVideos();
        }
    }
}
