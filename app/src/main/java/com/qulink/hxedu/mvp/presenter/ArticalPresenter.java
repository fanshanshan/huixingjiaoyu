package com.qulink.hxedu.mvp.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.CommentsBean;
import com.qulink.hxedu.entity.CourseNameBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.mvp.BasePresenter;
import com.qulink.hxedu.mvp.contract.ArticalContract;
import com.qulink.hxedu.mvp.model.ArticalModel;

import java.util.List;

public class ArticalPresenter extends BasePresenter<ArticalContract.View> implements ArticalContract.Presenter {
   ArticalModel articalModel;

    public ArticalPresenter() {
        articalModel = new ArticalModel();
    }



    @Override
    public void getPicMaster(int userId) {
        articalModel.getPicMasterById(userId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<PicMaster> hotArticalList =new Gson().fromJson(GsonUtil.GsonString(t.getData()),new TypeToken<List<PicMaster>>() {}.getType());
                if(hotArticalList.size()>0){
                    mView.getPicMasterSuc(hotArticalList.get(0));
                }
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
    public void getComments(int pageNp, int pageSize, int articalId,int sortType) {
        articalModel.getComments(pageNp, pageSize, articalId, sortType, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<CommentsBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<CommentsBean>>() {
                }.getType());
                mView.getCommentsSuc(list);
            }

            @Override
            public void error(String code, String msg) {
                mView.getCommentsError(msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                mView.getCommentsError(expectionMsg);

            }
        });
    }

    @Override
    public void loadMoreComments(int pageNp, int pageSize, int articalId,int sortType) {
        articalModel.getComments(pageNp, pageSize, articalId, sortType, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                //mView.loadMoreCommentsSuc(t.getCode());
                List<CommentsBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<CommentsBean>>() {
                }.getType());
                mView.loadMoreCommentsSuc(list);
            }

            @Override
            public void error(String code, String msg) {
                mView.loadMoreCommentError(msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                mView.loadMoreCommentError(expectionMsg);

            }
        });
    }

    @Override
    public void comment(int articalId, String content) {
        articalModel.comment(articalId, content, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                mView.commentSuc(content);


            }

            @Override
            public void error(String code, String msg) {
                mView.commentFail(msg);

            }

            @Override
            public void expcetion(String expectionMsg) {


            }
        });
    }
}
