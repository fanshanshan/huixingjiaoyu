package com.qulink.hxedu.video;

public interface VideoStatuListener {


    public    void openVip();

    public  void buyLesson();

    public void startClick();
    public void clickSurface();

    public void onVideoPause();

    public void playNext(int position);
}
