package com.qulink.hxedu.mvp.contract;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.entity.CourseBean;
import com.qulink.hxedu.entity.CourseNameBean;
import com.qulink.hxedu.mvp.BaseView;

import java.util.List;

public interface CourseContract {
    interface Model {
        void getCourseNameById(int id,ApiCallback apiCallback);
    }

    interface View  extends BaseView {
        void getCourseNameSuc( List<CourseNameBean> list);

    }

    interface Presenter {
        void getCourseNameById(int id);
    }
}
