package com.qulink.hxedu.mvp.model;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.mvp.contract.IndexInfoContract;

public class IndexInfoModel implements IndexInfoContract.Model {



    @Override
    public void getBanner(ApiCallback apiCallback) {
        ApiUtils.getInstance().getIndexBanner(apiCallback);
    }

    @Override
    public void getCourseItem(ApiCallback apiCallback) {
        ApiUtils.getInstance().getIndexCourseItem(apiCallback);

    }

    @Override
    public void getHotCourse(ApiCallback apiCallback) {
        ApiUtils.getInstance().getIndexSortCourse(1,1,4,apiCallback);
    }

    @Override
    public void getMoneyCourse(ApiCallback apiCallback) {
        ApiUtils.getInstance().getIndexSortCourse(3,1,4,apiCallback);

    }
}
