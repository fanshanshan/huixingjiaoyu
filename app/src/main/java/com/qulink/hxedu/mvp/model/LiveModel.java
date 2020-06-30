package com.qulink.hxedu.mvp.model;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.mvp.contract.LiveContract;

public class LiveModel implements LiveContract.Model {
    @Override
    public void getLiveDetail(int liveId, ApiCallback apiCallback) {
        ApiUtils.getInstance().getLiveDetail(liveId,apiCallback);
    }

    @Override
    public void getLiveCatalog(int liveId, ApiCallback apiCallback) {
        ApiUtils.getInstance().getLiveCatalog(liveId,apiCallback);
    }
}
