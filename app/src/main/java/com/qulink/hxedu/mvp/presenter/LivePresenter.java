package com.qulink.hxedu.mvp.presenter;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.LiveInfoBean;
import com.qulink.hxedu.mvp.BasePresenter;
import com.qulink.hxedu.mvp.contract.LiveContract;
import com.qulink.hxedu.mvp.model.LiveModel;

public class LivePresenter extends BasePresenter<LiveContract.View> implements  LiveContract.Presenter {
    private LiveModel model;

    public LivePresenter() {
        model = new LiveModel();
    }

    @Override
    public void getLiveDetail(int liveId) {
        model.getLiveDetail(liveId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                LiveInfoBean liveInfoBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()),LiveInfoBean.class);
                mView.getLiveDetailSuc(liveInfoBean);

            }

            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        });


    }

    @Override
    public void getLiveCatalog(int liveId) {
        model.getLiveCatalog(liveId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                mView.getCatalogSuc(null);
            }

            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        });
    }
}
