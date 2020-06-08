package com.qulink.hxedu.mvp.contract;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.entity.PersonNalCourseDetailBean;
import com.qulink.hxedu.entity.PersonZoneIndexBean;
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.mvp.BaseView;

import java.util.List;

public interface PersonZoneIndexContract {
    interface Model {
        void getPersonIndex(ApiCallback apiCallback);
        void getMyTopic(int pageNo,int pageSize,ApiCallback apiCallback);
        void loadMyTopic(int pageNo,int pageSize,ApiCallback apiCallback);
        void getPicMasterById(int userId,ApiCallback apiCallback);

    }

    interface View extends BaseView {
        void getPersonIndexSuc(PersonZoneIndexBean s);
        void getMyTopicSuc( List<PicBean> hotArticalList);
        void loadmoreTopicSuc( List<PicBean> hotArticalList);
        void getPicMasterSuc(PicMaster picMaster);

    }

    interface Presenter {
        void getPersonIndex();
        void getMyToPic();
        void loadMoreMyTopic();
        void getPicMaster(int userId);

    }
}
