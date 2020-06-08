package com.qulink.hxedu.mvp.model;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.mvp.contract.IndexInfoContract;
import com.qulink.hxedu.util.FinalValue;

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
        ApiUtils.getInstance().getIndexSortCourse(FinalValue.hotCourseCurriculumType,1,4,apiCallback);
    }

    @Override
    public void getMoneyCourse(ApiCallback apiCallback) {
        ApiUtils.getInstance().getIndexSortCourse(FinalValue.moneyCourseCurriculumType,1,4,apiCallback);

    }
}
