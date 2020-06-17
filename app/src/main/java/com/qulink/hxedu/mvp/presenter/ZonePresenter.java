package com.qulink.hxedu.mvp.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.HotArtical;
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.entity.TopPicBean;
import com.qulink.hxedu.mvp.BasePresenter;
import com.qulink.hxedu.mvp.BaseView;
import com.qulink.hxedu.mvp.contract.ZoneContract;
import com.qulink.hxedu.mvp.model.ZoneModel;
import com.qulink.hxedu.util.FinalValue;

import java.util.List;

public class ZonePresenter extends BasePresenter<ZoneContract.View> implements ZoneContract.Presenter {

    ZoneContract.Model model;

    public ZonePresenter() {
        this.model = new ZoneModel(mView);
    }

    @Override
    public void getHotArtical() {
        model.getHotArtical(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<PicBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<PicBean>>() {
                }.getType());
                if (mView != null) {
                    mView.getHotArticalSuc(hotArticalList);
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
                    mView.onExpcetion(expectionMsg);
                }
            }
        });
    }

    @Override
    public void getTopPic() {
        model.getTopPic(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<TopPicBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<TopPicBean>>() {
                }.getType());
                if (mView != null) {
                    mView.getTopPicSuc(hotArticalList);
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
    public void getAllPic() {
        model.getAllPic(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<PicBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<PicBean>>() {
                }.getType());
                if (mView != null) {
                    mView.getAllPicSuc(hotArticalList);
                    if (hotArticalList.size() < FinalValue.limit) {
                        mView.noMore();
                    }
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
                    mView.onExpcetion(expectionMsg);
                }
            }
        });
    }

    @Override
    public void loadMorePic() {
        model.loadMorePic(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<PicBean> hotArticalList = new Gson().fromJson(GsonUtil.GsonString(t.getData()), new TypeToken<List<PicBean>>() {
                }.getType());
                if (mView != null) {
                    if (hotArticalList.size() < FinalValue.limit) {
                        mView.noMore();
                    }
                    mView.loadMorePicSuc(hotArticalList);
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
                    mView.onExpcetion(expectionMsg);
                }
            }
        });
    }

    @Override
    public void getPicMaster(int userId) {
        model.getPicMasterById(userId, new ApiCallback() {
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
                    mView.onExpcetion(msg);
                }
            }

            @Override
            public void expcetion(String expectionMsg) {
                if (mView != null) {
                    mView.onExpcetion(expectionMsg);
                }

            }
        });
    }
}
