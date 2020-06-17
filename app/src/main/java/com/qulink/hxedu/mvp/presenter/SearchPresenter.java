package com.qulink.hxedu.mvp.presenter;

import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.CourseNameBean;
import com.qulink.hxedu.mvp.BasePresenter;
import com.qulink.hxedu.mvp.contract.SearchContract;
import com.qulink.hxedu.mvp.model.SearchModel;

import java.util.List;

public class SearchPresenter extends BasePresenter<SearchContract.View> implements SearchContract.Presenter {
    private SearchContract.Model model;

    public SearchPresenter() {
        this.model = new SearchModel();
    }

    @Override
    public void getFilterContent(int classifyId) {
        if (mView != null) {
            mView.showLoading();
        }
        model.getFilterContent(classifyId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                mView.hideLoading();


                List<CourseNameBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<CourseNameBean>>() {
                }.getType());
                if (mView != null) {
                    mView.getFilterContentSuccess(list);

                }
            }

            @Override
            public void error(String code, String msg) {

                if (mView != null) {
                    mView.onError(msg);
                    mView.hideLoading();
                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    if (expectionMsg != null) {
                        mView.onError(expectionMsg);
                    }
                    mView.hideLoading();

                }
            }
        });
    }
}
