package com.qulink.hxedu.mvp.contract;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.mvp.BaseView;

public interface LikeArticalContract {
    interface Model {
        void likeAitical(int articalId, ApiCallback apiCallback);

        void cancelLikeAitical(int articalId, ApiCallback apiCallback);
    }

    interface View extends BaseView {
        void likeArticalSuc(int articalId);

        void cancelLikeArticalSuc(int articalId);


    }

    interface Presenter {
        void likeArtical(int articalId);

        void cancelLikeArtical(int articalId);
    }
}
