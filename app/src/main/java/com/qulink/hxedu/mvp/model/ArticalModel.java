package com.qulink.hxedu.mvp.model;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.mvp.contract.ArticalContract;

public class ArticalModel implements ArticalContract.Model {


    @Override
    public void getPicMasterById(int userId, ApiCallback apiCallback) {
        ApiUtils.getInstance().getPicMasterById(userId,apiCallback);
    }

    @Override
    public void getComments(int pageNp, int pageSize, int articalId, int sortType, ApiCallback apiCallback) {
        ApiUtils.getInstance().getArticalComments(pageNp,pageSize,articalId,sortType,apiCallback);
    }

    @Override
    public void loadMoreComments(int pageNp, int pageSize, int articalId, int sortType, ApiCallback apiCallback) {
        ApiUtils.getInstance().getArticalComments(pageNp,pageSize,articalId,sortType,apiCallback);

    }

    @Override
    public void comment(int articalId, String content, ApiCallback apiCallback) {
        ApiUtils.getInstance().comments(articalId,content,apiCallback);
    }
}
