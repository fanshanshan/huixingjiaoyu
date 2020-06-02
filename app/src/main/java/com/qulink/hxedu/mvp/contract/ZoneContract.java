package com.qulink.hxedu.mvp.contract;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.HotArtical;
import com.qulink.hxedu.mvp.BaseView;

import java.util.List;

public interface ZoneContract {
    interface Model {
        void getHotArtical(ApiCallback apiCallback);
    }

    interface View extends BaseView {
        void getHotArticalSuc( List<HotArtical> hotArticalList );
        void getHotArticalFail(String msg);
    }

    interface Presenter {
        void getHotArtical();
    }
}
