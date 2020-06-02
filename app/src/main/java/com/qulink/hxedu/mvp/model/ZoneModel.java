package com.qulink.hxedu.mvp.model;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.mvp.contract.ZoneContract;

public class ZoneModel implements ZoneContract.Model {
    ZoneContract.View mView;

    public ZoneModel(ZoneContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void getHotArtical(ApiCallback apiCallback) {
        ApiUtils.getInstance().getHotArtical(apiCallback);
    }
}
