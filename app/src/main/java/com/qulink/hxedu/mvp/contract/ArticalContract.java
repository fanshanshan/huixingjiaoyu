package com.qulink.hxedu.mvp.contract;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.entity.CommentsBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.mvp.BaseView;

import java.util.List;

public interface ArticalContract {
    interface Model {


        void getPicMasterById(int userId, ApiCallback apiCallback);
        void getComments(int pageNp,int pageSize,int articalId,int sortType, ApiCallback apiCallback);
        void loadMoreComments(int pageNp,int pageSize,int articalId,int sortType, ApiCallback apiCallback);
        void comment(int articalId,String content,ApiCallback apiCallback);
    }

    interface View extends BaseView {

        void getPicMasterSuc(PicMaster picMaster);

        void getCommentsSuc(List<CommentsBean> list);
        void loadMoreCommentsSuc(List<CommentsBean> list);

        void getCommentsError(String msg);
        void loadMoreCommentError(String msg);

        void commentSuc(String content);
        void commentFail(String content);
    }

    interface Presenter {


        void getPicMaster(int userId);

        void getComments(int pageNp,int pageSize,int articalId,int sortType);
        void loadMoreComments(int pageNp,int pageSize,int articalId,int sortType);
        void comment(int articalId,String content);

    }
}
