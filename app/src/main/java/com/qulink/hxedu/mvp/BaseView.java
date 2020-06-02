package com.qulink.hxedu.mvp;

import com.qulink.hxedu.api.ResponseData;

public interface BaseView {

    /**
     * 显示加载中
     */
    void showLoading();

    /**
     * 隐藏加载
     */
    void hideLoading();

    /**
     * 数据获取失败
     * @param msg
     */
    void onError(String msg);


    void onSuccess(ResponseData data);


    void onExpcetion(String msg);
}
