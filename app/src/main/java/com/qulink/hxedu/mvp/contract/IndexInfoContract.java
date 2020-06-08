package com.qulink.hxedu.mvp.contract;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.CourseItemBean;
import com.qulink.hxedu.entity.HotCourseBean;
import com.qulink.hxedu.mvp.BaseView;

import java.util.List;

public interface IndexInfoContract {
    interface Model {
        void getBanner(ApiCallback apiCallback);
        void getCourseItem(ApiCallback apiCallback);
        void getHotCourse(ApiCallback apiCallback);
        void getMoneyCourse(ApiCallback apiCallback);
    }

    interface View extends BaseView {
        void onBannerSuccess(ResponseData data);
        void onCourseSuccess(List<CourseItemBean> list);
        void onHotCourseSuccess(HotCourseBean hotCourseBean);
        void onMoneyCourseSuccess(HotCourseBean hotCourseBean);

        void onBannerError(String msg);
        void onCourseError(String msg);
    }

    interface Presenter {
        void getBanner();
        void getCourseItem();
        void getHotCourse();
        void getMoneyCourse();

    }
}
