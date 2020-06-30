package com.qulink.hxedu.mvp.contract;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.entity.LiveInfoBean;
import com.qulink.hxedu.mvp.BaseView;

public interface LiveContract {
    interface Model {
        void getLiveDetail(int liveId, ApiCallback apiCallback);
        void getLiveCatalog(int liveId, ApiCallback apiCallback);
    }

    interface View  extends BaseView {
        void getLiveDetailSuc(LiveInfoBean liveInfoBean);
        void getCatalogSuc(LiveInfoBean liveInfoBean);
    }

    interface Presenter {
        void getLiveDetail(int liveId);
        void getLiveCatalog(int liveId);
    }
}
