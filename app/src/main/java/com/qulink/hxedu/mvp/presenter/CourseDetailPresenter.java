package com.qulink.hxedu.mvp.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.CatalogBean;
import com.qulink.hxedu.entity.CourseDetailBean;
import com.qulink.hxedu.entity.PersonNalCourseDetailBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.mvp.BasePresenter;
import com.qulink.hxedu.mvp.contract.CourseDetailContract;
import com.qulink.hxedu.mvp.model.CourseDetailModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
                CourseDetailBean courseDetailBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), CourseDetailBean.class);
                if (mView != null) {
                    mView.hideLoading();
                    mView.getCourseDetailSuc(courseDetailBean);
                }

            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.onError(msg);
                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.onError(expectionMsg);
                }

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
                    if (jsonObject.has("value")) {
                        if (mView != null) {
                            mView.getCourseNumberSuc(jsonObject.getString("value"));

                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.onError(msg);

                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.onError(expectionMsg);

                }
            }
        });
    }

    @Override
    public void getPersonnalCourseDetail(int courseId) {
        model.getPersonnalCourseDetail(courseId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                PersonNalCourseDetailBean courseDetailBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), PersonNalCourseDetailBean.class);
                if (mView != null) {
                    mView.getPersonnalCourseDetail(courseDetailBean);

                }
            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.onError(msg);

                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.onError(expectionMsg);

                }
            }
        });
    }

    @Override
    public void increaseVideoLookNumber(int courseId) {
        model.increaseVideoLookNumber(courseId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                if (mView != null) {
                    mView.increaseVideoLookNumberSuc();
                }
            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.onError(msg);
                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.onError(expectionMsg);
                }

            }
        });
    }

    @Override
    public void getCourseChapter(int courseId) {
        mView.showLoading();
        model.getCourseChapter(courseId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<CatalogBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<CatalogBean>>() {
                }.getType());

                if (mView != null) {
                    mView.hideLoading();
                    mView.getCourseChapterSuc(hotArticalList);
                }
            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.onError(msg);
                }

            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.onError(expectionMsg);
                }

            }
        });
    }
}
