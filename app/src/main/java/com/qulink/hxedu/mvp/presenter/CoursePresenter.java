package com.qulink.hxedu.mvp.presenter;

import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.CourseBean;
import com.qulink.hxedu.entity.CourseItemBean;
import com.qulink.hxedu.entity.CourseNameBean;
import com.qulink.hxedu.mvp.BasePresenter;
import com.qulink.hxedu.mvp.contract.CourseContract;
import com.qulink.hxedu.mvp.model.CourseModel;
import com.qulink.hxedu.util.FinalValue;

import java.util.List;

public class CoursePresenter extends BasePresenter<CourseContract.View> implements CourseContract.Presenter {
    private CourseContract.Model model;

    public CoursePresenter() {
        this.model = new CourseModel();
    }

    @Override
    public void getCourseNameById(int id) {
        mView.showLoading();
        model.getCourseNameById(id, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                mView.hideLoading();

                List<CourseNameBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<CourseNameBean>>() {
                }.getType());
                mView.getCourseNameSuc(list);
            }

            @Override
            public void error(String code, String msg) {
                mView.hideLoading();
                mView.onError(msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                mView.hideLoading();

                mView.onExpcetion(expectionMsg);
            }
        });
    }




}
