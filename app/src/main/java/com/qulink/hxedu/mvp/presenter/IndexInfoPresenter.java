package com.qulink.hxedu.mvp.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.HotCourseBean;
import com.qulink.hxedu.mvp.BasePresenter;
import com.qulink.hxedu.mvp.contract.IndexInfoContract;
import com.qulink.hxedu.mvp.model.IndexInfoModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class IndexInfoPresenter extends BasePresenter<IndexInfoContract.View> implements IndexInfoContract.Presenter {

    private IndexInfoContract.Model model;
    public IndexInfoPresenter() {
        this.model = new IndexInfoModel();
    }


    @Override
    public void getBanner() {
        model.getBanner(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                mView.onBannerSuccess(t);

            }

            @Override
            public void error(String code, String msg) {
                mView.onBannerError(msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                mView.onBannerError(expectionMsg);

            }
        });
    }

    @Override
    public void getCourseItem() {

        model.getCourseItem(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                mView.onCourseSuccess(t);
            }

            @Override
            public void error(String code, String msg) {
                mView.onCourseError(msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                mView.onCourseError(expectionMsg);

            }
        });
    }

    @Override
    public void getHotCourse() {
        model.getHotCourse(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                HotCourseBean hotCourseBean = GsonUtil.GsonToBean(t.getData(),HotCourseBean.class);
                mView.onHotCourseSuccess(hotCourseBean);
            }


            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        });
    }

    @Override
    public void getMoneyCourse() {
        model.getHotCourse(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                HotCourseBean hotCourseBean = GsonUtil.GsonToBean(t.getData(),HotCourseBean.class);
                mView.onMoneyCourseSuccess(hotCourseBean);
            }


            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        });
    }


}
