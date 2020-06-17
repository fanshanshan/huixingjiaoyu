package com.qulink.hxedu.mvp.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.CourseItemBean;
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
                if (mView != null) {
                    mView.onBannerSuccess(t);

                }
            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.onBannerError(msg);
                    mView.onError(msg);
                }

            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.onBannerError(expectionMsg);
                    mView.onError(expectionMsg);

                }

            }
        });
    }

    @Override
    public void getCourseItem() {

        model.getCourseItem(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<CourseItemBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<CourseItemBean>>() {
                }.getType());
                if (mView != null) {
                    mView.onCourseSuccess(list);

                }
            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.onCourseError(msg);
                    mView.onError(msg);
                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.onCourseError(expectionMsg);
                    mView.onError(expectionMsg);
                }

            }
        });
    }

    @Override
    public void getHotCourse() {
        model.getHotCourse(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                HotCourseBean hotCourseBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), HotCourseBean.class);
                mView.onHotCourseSuccess(hotCourseBean);
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
    public void getMoneyCourse() {
        model.getMoneyCourse(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                HotCourseBean hotCourseBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), HotCourseBean.class);
                mView.onMoneyCourseSuccess(hotCourseBean);
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
