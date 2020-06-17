package com.qulink.hxedu.mvp.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.ArticalDetailBean;
import com.qulink.hxedu.entity.CommentsBean;
import com.qulink.hxedu.entity.CourseNameBean;
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.mvp.BasePresenter;
import com.qulink.hxedu.mvp.contract.ArticalContract;
import com.qulink.hxedu.mvp.model.ArticalModel;

import org.json.JSONException;
import org.json.JSONObject;

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
                List<PicMaster> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<PicMaster>>() {
                }.getType());
                if (hotArticalList.size() > 0) {
                    if (mView != null) {
                        mView.getPicMasterSuc(hotArticalList.get(0));

                    }
                }
            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.getPicMasterFail(userId);

                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.getPicMasterFail(userId);

                }

            }
        });
    }

    @Override
    public void getComments(int pageNp, int pageSize, int articalId, int sortType) {
        articalModel.getComments(pageNp, pageSize, articalId, sortType, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<CommentsBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<CommentsBean>>() {
                }.getType());
                if (!list.isEmpty()) {

                    for (CommentsBean c : list) {
                        if (c.getReplyList().size() < pageSize) {
                            c.setNoMoreReply(true);
                        } else {
                            c.setNoMoreReply(false);
                        }
                        for (CommentsBean.ReplyListBean r : c.getReplyList()) {
                            r.setRootCommentId(c.getId());
                            r.setRootCommentUserId(c.getUserId());
                        }
                    }
                }
                if (mView != null) {
                    mView.getCommentsSuc(list);
                }
            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.getCommentsError(msg);
                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.getCommentsError(expectionMsg);
                }

            }
        });
    }

    @Override
    public void loadMoreComments(int pageNp, int pageSize, int articalId, int sortType) {
        articalModel.getComments(pageNp, pageSize, articalId, sortType, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                //mView.loadMoreCommentsSuc(t.getCode());
                List<CommentsBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<CommentsBean>>() {
                }.getType());
                if (!list.isEmpty()) {
                    for (CommentsBean c : list) {
                        if (c.getReplyList().size() < pageSize) {
                            c.setNoMoreReply(true);
                        } else {
                            c.setNoMoreReply(false);

                        }
                        for (CommentsBean.ReplyListBean r : c.getReplyList()) {
                            r.setRootCommentId(c.getId());
                            r.setRootCommentUserId(c.getUserId());
                        }
                    }
                }
                if (mView != null) {
                    mView.loadMoreCommentsSuc(list);
                }
            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.loadMoreCommentError(msg);
                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.loadMoreCommentError(expectionMsg);
                }

            }
        });
    }

    @Override
    public void comment(int articalId, String content) {
        articalModel.comment(articalId, content, new ApiCallback() {
            @Override
            public void success(ResponseData t) {

                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.GsonString(t.getData()));
                    if (mView != null) {
                        mView.commentSuc(content, jsonObject.getInt("commentId"), jsonObject.getInt("userId"));

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.commentFail(msg);
                }

            }

            @Override
            public void expcetion(String expectionMsg) {

                if (mView != null) {
                    mView.onError(expectionMsg);
                }

            }
        });
    }

    @Override
    public void replyComments(int articalId, int commentId, int toUserId, int rootCommentId, String content, String toUserName, int rootCommentUserId) {
        articalModel.replyComments(articalId, commentId, toUserId, content, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.GsonString(t.getData()));
                    if (mView != null) {
                        mView.replyCommentSuc(content, toUserName, jsonObject.getInt("replyId"), jsonObject.getInt("userId"), rootCommentId, toUserId, rootCommentUserId);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.onError(msg);
                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.onError(expectionMsg);
                }

            }
        });
    }

    @Override
    public void likeComments(int articalId, int commentId) {
        articalModel.likeComments(articalId, commentId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                if (mView != null) {
                    mView.likeCommentSuc(commentId);
                }
            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.onError(msg);
                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.onError(expectionMsg);
                }

            }
        });
    }

    @Override
    public void cancelLikeComments(int articalId, int commentId) {
        articalModel.cancelLikeComments(articalId, commentId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                if (mView != null) {
                    mView.cancelLikeCommentSuc(commentId);

                }
            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.onError(msg);
                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.onError(expectionMsg);
                }

            }
        });
    }

    @Override
    public void getArticalDetail(int articalId) {
        mView.showLoading();
        articalModel.getArticalDetail(articalId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                ArticalDetailBean picBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), ArticalDetailBean.class);
                if (mView != null) {
                    mView.getArticalDetailSuc(picBean.getArticelDetails());
                    mView.hideLoading();
                }
            }

            @Override
            public void error(String code, String msg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.onError(msg);
                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.hideLoading();
                    mView.onError(expectionMsg);
                }


            }
        });
    }

    @Override
    public void getReplyByCommentsId(int pageNp, int pageSize, int commentId) {
        articalModel.getReplyByCommentsId(pageNp, pageSize, commentId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<CommentsBean.ReplyListBean> list = GsonUtil.getInstance().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<CommentsBean.ReplyListBean>>() {
                }.getType());

                mView.loadMoreReplySuc(list, commentId);
            }

            @Override
            public void error(String code, String msg) {
                mView.onError(msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                mView.onError(expectionMsg);

            }
        });
    }
}
