package com.qulink.hxedu.mvp.presenter;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.mvp.BasePresenter;
import com.qulink.hxedu.mvp.contract.LikeArticalContract;
import com.qulink.hxedu.mvp.model.LikeArticalModel;

public class LikeArticalPresenter extends BasePresenter<LikeArticalContract.View> implements LikeArticalContract.Presenter {
    LikeArticalModel likeAitical;

    public LikeArticalPresenter() {
        likeAitical = new LikeArticalModel();
    }

    @Override
    public void likeArtical(int articalId) {
        likeAitical.likeAitical(articalId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                mView.likeArticalSuc(articalId);
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
    public void cancelLikeArtical(int articalId) {
        likeAitical.cancelLikeAitical(articalId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                mView.cancelLikeArticalSuc(articalId);
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
