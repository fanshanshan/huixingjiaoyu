package com.qulink.hxedu.mvp.contract;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.entity.CourseNameBean;
import com.qulink.hxedu.mvp.BaseView;

import java.util.List;

public interface SearchContract {
    interface Model {
        void getFilterContent(int classifyId, ApiCallback apiCallback);
    }

    interface View extends BaseView {
        void getFilterContentSuccess(List<CourseNameBean> list);
    }

    interface Presenter {
        void getFilterContent(int classifyId);

    }
}
