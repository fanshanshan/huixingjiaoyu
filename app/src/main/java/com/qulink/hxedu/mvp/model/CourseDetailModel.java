package com.qulink.hxedu.mvp.model;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.mvp.contract.CourseDetailContract;

public class CourseDetailModel implements CourseDetailContract.Model {
    @Override
    public void getCourseDetail(int courseId, ApiCallback apiCallback) {
        ApiUtils.getInstance().getCourseDetail(courseId,apiCallback);
    }

    @Override
    public void getCourseLookNumberNameById(int courseId, ApiCallback apiCallback) {
        ApiUtils.getInstance().getCourseLookNum(courseId,apiCallback);
    }

    @Override
    public void getPersonnalCourseDetail(int courseId, ApiCallback apiCallback) {
        ApiUtils.getInstance().getCourseDetailForPersonnal(courseId,apiCallback);
    }

    @Override
    public void increaseVideoLookNumber(int courseId, ApiCallback apiCallback) {
        ApiUtils.getInstance().increaseLookNumberToServer(courseId,apiCallback);
    }

    @Override
    public void getCourseChapter(int courseId, ApiCallback apiCallback) {
        ApiUtils.getInstance().getCourseChapter(courseId,apiCallback);
    }
}
