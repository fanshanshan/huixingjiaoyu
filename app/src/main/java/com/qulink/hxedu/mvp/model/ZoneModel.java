package com.qulink.hxedu.mvp.model;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.mvp.contract.ZoneContract;
import com.qulink.hxedu.util.FinalValue;

public class ZoneModel implements ZoneContract.Model {
    ZoneContract.View mView;
    int pageNo = FinalValue.startpageNo;
    int pageSize = FinalValue.limit;
    public ZoneModel(ZoneContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getHotArtical(ApiCallback apiCallback) {
        ApiUtils.getInstance().getHotArtical(apiCallback);
    }

    @Override
    public void getTopPic(ApiCallback apiCallback) {
        ApiUtils.getInstance().getTopPic(apiCallback);
    }

    @Override
    public void getAllPic(ApiCallback apiCallback) {
        pageNo = FinalValue.startpageNo;
        ApiUtils.getInstance().getTopPic(pageNo,pageSize,"","",apiCallback);
    }

    @Override
    public void loadMorePic(ApiCallback apiCallback) {
        pageNo++;
        ApiUtils.getInstance().getTopPic(pageNo,pageSize,"","",apiCallback);
    }

    @Override
    public void getPicMasterById(int userId, ApiCallback apiCallback) {

        ApiUtils.getInstance().getPicMasterById(userId,apiCallback);
    }

}
