package com.qulink.hxedu.mvp.model;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.mvp.contract.PersonZoneIndexContract;

public class PersonZoneIndexModel implements PersonZoneIndexContract.Model {
    @Override
    public void getPersonIndex(ApiCallback apiCallback) {
        ApiUtils.getInstance().getPersonZoneIndex(apiCallback);
    }

    @Override
    public void getMyTopic(int pageNo,int pageSize,ApiCallback apiCallback) {
        ApiUtils.getInstance().getMyTopic(pageNo,pageSize,apiCallback);
    }

    @Override
    public void loadMyTopic(int pageNo, int pageSize, ApiCallback apiCallback) {
        ApiUtils.getInstance().getMyTopic(pageNo,pageSize,apiCallback);


    }

    @Override
    public void getPicMasterById(int userId, ApiCallback apiCallback) {

        ApiUtils.getInstance().getPicMasterById(userId,apiCallback);
    }
}
