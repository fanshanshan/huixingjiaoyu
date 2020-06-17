package com.qulink.hxedu.mvp.contract;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.entity.CommentsBean;
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.mvp.BaseView;

import java.util.List;

public interface ArticalContract {
    interface Model {

        void getArticalDetail(int articalId,ApiCallback apiCallback);
        void getPicMasterById(int userId, ApiCallback apiCallback);
        void getComments(int pageNp,int pageSize,int articalId,int sortType, ApiCallback apiCallback);
        void getReplyByCommentsId(int pageNp,int pageSize,int commentId, ApiCallback apiCallback);
        void loadMoreComments(int pageNp,int pageSize,int articalId,int sortType, ApiCallback apiCallback);
        void comment(int articalId,String content,ApiCallback apiCallback);
        void likeComments(int articalId,int commentId,ApiCallback apiCallback);
        void cancelLikeComments(int articalId,int commentId,ApiCallback apiCallback);
        void replyComments(int articalId,int commentId,int toUserId,String content,ApiCallback apiCallback);

    }

    interface View extends BaseView {

        void getPicMasterSuc(PicMaster picMaster);
        void getPicMasterFail(int userId);

        void getCommentsSuc(List<CommentsBean> list);
        void loadMoreCommentsSuc(List<CommentsBean> list);
        void loadMoreReplySuc(List<CommentsBean.ReplyListBean> list,int commentId);

        void getCommentsError(String msg);
        void loadMoreCommentError(String msg);

        void commentSuc(String content,int commentId,int userId);
        void replyCommentSuc(String content,String toUserName,int commentId,int userId,int rootCommentId,int toUserId,int rootCommentUserId);
        void commentFail(String content);
        void likeCommentSuc(int commentId);
        void cancelLikeCommentSuc(int commentId);

        void getArticalDetailSuc(PicBean picBean);
    }

    interface Presenter {


        void getPicMaster(int userId);

        void getComments(int pageNp,int pageSize,int articalId,int sortType);
        void loadMoreComments(int pageNp,int pageSize,int articalId,int sortType);
        void comment(int articalId,String content);
        void replyComments(int articalId,int commentId,int toUserId,int rootCommentId,String content,String toUserName,int rootCommentUserId);

        void likeComments(int articalId,int commentId);
        void cancelLikeComments(int articalId,int commentId);
        void getArticalDetail(int articalId);
        void getReplyByCommentsId(int pageNp,int pageSize,int commentId);


    }
}
