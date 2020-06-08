package com.qulink.hxedu.mvp.presenter;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.CourseDetailBean;
import com.qulink.hxedu.entity.PersonNalCourseDetailBean;
import com.qulink.hxedu.mvp.BasePresenter;
import com.qulink.hxedu.mvp.contract.CourseDetailContract;
import com.qulink.hxedu.mvp.model.CourseDetailModel;

import org.json.JSONException;
import org.json.JSONObject;

public class CourseDetailPresenter extends BasePresenter<CourseDetailContract.View> implements CourseDetailContract.Presenter {

    CourseDetailContract.Model model;

    public CourseDetailPresenter() {
        this.model = new CourseDetailModel();
    }

    @Override
    public void getCourseDetail(int courseId) {
        mView.showLoading();
        model.getCourseDetail(courseId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                mView.hideLoading();
                CourseDetailBean courseDetailBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()),CourseDetailBean.class);
                mView.getCourseDetailSuc(courseDetailBean);
            }

            @Override
            public void error(String code, String msg) {
                mView.hideLoading();
                mView.onError(msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                mView.hideLoading();
                mView.onError(expectionMsg);

            }
        });
    }

    @Override
    public void getCourseLookNumberNameById(int courseId) {
        model.getCourseLookNumberNameById(courseId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                mView.getCourseNumberSuc("");
                try {
                    JSONObject jsonObject = new JSONObject(t.getData().toString());
                    if(jsonObject.has("value")){
                        mView.getCourseNumberSuc( jsonObject.getString("value"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String code, String msg) {
                mView.onError(msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                mView.onError(expectionMsg);

            }
        });
    }

    @Override
    public void getPersonnalCourseDetail(int courseId) {
        model.getPersonnalCourseDetail(courseId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                PersonNalCourseDetailBean courseDetailBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()),PersonNalCourseDetailBean.class);

                mView.getPersonnalCourseDetail(courseDetailBean);
            }

            @Override
            public void error(String code, String msg) {
                mView.onError(msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                mView.onError(expectionMsg);

            }
        });
    }

    @Override
    public void increaseVideoLookNumber(int courseId) {
        model.increaseVideoLookNumber(courseId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                mView.increaseVideoLookNumberSuc();
            }

            @Override
            public void error(String code, String msg) {
                mView.onError(msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                mView.onError(expectionMsg);

            }
        });
    }
}
