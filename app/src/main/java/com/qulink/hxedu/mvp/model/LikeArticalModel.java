package com.qulink.hxedu.mvp.model;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.mvp.contract.LikeArticalContract;

public class LikeArticalModel implements LikeArticalContract.Model {
    @Override
    public void likeAitical(int articalId, ApiCallback apiCallback) {
        ApiUtils.getInstance().likeArtical(articalId,apiCallback);
    }

    @Override
    public void cancelLikeAitical(int articalId, ApiCallback apiCallback) {
        ApiUtils.getInstance().cancelLikeArtical(articalId,apiCallback);

    }
}
