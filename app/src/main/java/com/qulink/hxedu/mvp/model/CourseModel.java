package com.qulink.hxedu.mvp.model;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.mvp.contract.CourseContract;

public class CourseModel implements CourseContract.Model {
    @Override
    public void getCourseNameById(int id,ApiCallback apiCallback) {
        ApiUtils.getInstance().getSubCourseNameById(id,apiCallback);
    }


}
