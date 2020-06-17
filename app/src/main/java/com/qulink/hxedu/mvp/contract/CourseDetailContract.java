package com.qulink.hxedu.mvp.contract;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.entity.CatalogBean;
import com.qulink.hxedu.entity.CourseDetailBean;
import com.qulink.hxedu.entity.PersonNalCourseDetailBean;
import com.qulink.hxedu.mvp.BaseView;

import java.util.List;

public interface CourseDetailContract {
    interface Model {
        void getCourseDetail(int courseId,ApiCallback apiCallback);
        void getCourseLookNumberNameById(int courseId,ApiCallback apiCallback);//废弃
        void getPersonnalCourseDetail(int courseId,ApiCallback apiCallback);//废弃
        void increaseVideoLookNumber(int courseId,ApiCallback apiCallback);
        void getCourseChapter(int courseId,ApiCallback apiCallback);
    }

    interface View  extends BaseView {
        void getCourseDetailSuc(CourseDetailBean courseDetailBean);
        void getCourseNumberSuc( String s);
        void getPersonnalCourseDetail( PersonNalCourseDetailBean s);
        void increaseVideoLookNumberSuc();
        void getCourseChapterSuc(List<CatalogBean> s);
    }

    interface Presenter {
        void getCourseDetail(int courseId);
        void getCourseLookNumberNameById(int courseId);
        void getPersonnalCourseDetail(int courseId);
        void increaseVideoLookNumber(int courseId);
        void getCourseChapter(int courseId);

    }
}
