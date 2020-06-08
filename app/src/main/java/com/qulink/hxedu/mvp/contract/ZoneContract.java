package com.qulink.hxedu.mvp.contract;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.HotArtical;
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.entity.TopPicBean;
import com.qulink.hxedu.mvp.BaseView;

import java.util.List;

public interface ZoneContract {
    interface Model {
        void getHotArtical(ApiCallback apiCallback);

        void getTopPic(ApiCallback apiCallback);

        void getAllPic(ApiCallback apiCallback);

        void loadMorePic(ApiCallback apiCallback);

        void getPicMasterById(int userId,ApiCallback apiCallback);
    }

    interface View extends BaseView {
        void getHotArticalSuc(List<HotArtical> hotArticalList);

        void getTopPicSuc(List<TopPicBean> topPicBeanList);

        void getHotArticalFail(String msg);

        void getAllPicSuc(List<PicBean> list);

        void loadMorePicSuc(List<PicBean> list);

        void getPicMasterSuc(PicMaster picMaster);
    }

    interface Presenter {
        void getHotArtical();

        void getTopPic();

        void getAllPic();

        void loadMorePic();

        void getPicMaster(int userId);
    }
}
