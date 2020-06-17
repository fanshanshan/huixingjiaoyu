package com.qulink.hxedu.mvp.model;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.mvp.contract.ArticalContract;

public class ArticalModel implements ArticalContract.Model {


    @Override
    public void getArticalDetail(int articalId, ApiCallback apiCallback) {
        ApiUtils.getInstance().getTopicDetail(articalId,apiCallback);
    }

    @Override
    public void getPicMasterById(int userId, ApiCallback apiCallback) {
        ApiUtils.getInstance().getPicMasterById(userId,apiCallback);
    }

    @Override
    public void getComments(int pageNp, int pageSize, int articalId, int sortType, ApiCallback apiCallback) {
        ApiUtils.getInstance().getArticalComments(pageNp,pageSize,articalId,sortType,apiCallback);
    }

    @Override
    public void getReplyByCommentsId(int pageNp, int pageSize, int commentId, ApiCallback apiCallback) {
        ApiUtils.getInstance().getCommentsReply(pageNp,pageSize,commentId,apiCallback);
    }

    @Override
    public void loadMoreComments(int pageNp, int pageSize, int articalId, int sortType, ApiCallback apiCallback) {
        ApiUtils.getInstance().getArticalComments(pageNp,pageSize,articalId,sortType,apiCallback);

    }

    @Override
    public void comment(int articalId, String content, ApiCallback apiCallback) {
        ApiUtils.getInstance().comments(articalId,content,apiCallback);
    }

    @Override
    public void likeComments(int articalId, int commentId, ApiCallback apiCallback) {
        ApiUtils.getInstance().likeComments(articalId,commentId,apiCallback);
    }

    @Override
    public void cancelLikeComments(int articalId, int commentId, ApiCallback apiCallback) {
        ApiUtils.getInstance().cancelLikeComments(articalId,commentId,apiCallback);
    }

    @Override
    public void replyComments(int articalId, int commentId, int toUserId, String content, ApiCallback apiCallback) {
        ApiUtils.getInstance().replyComments(articalId,commentId,toUserId,content,apiCallback);
    }
}
