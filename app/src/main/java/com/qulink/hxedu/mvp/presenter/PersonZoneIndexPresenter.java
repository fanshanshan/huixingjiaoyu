package com.qulink.hxedu.mvp.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.PersonNalCourseDetailBean;
import com.qulink.hxedu.entity.PersonZoneIndexBean;
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.mvp.BasePresenter;
import com.qulink.hxedu.mvp.contract.PersonZoneIndexContract;
import com.qulink.hxedu.mvp.model.PersonZoneIndexModel;
import com.qulink.hxedu.util.FinalValue;

import java.util.List;

public class PersonZoneIndexPresenter  extends BasePresenter<PersonZoneIndexContract.View> implements PersonZoneIndexContract.Presenter {
    private PersonZoneIndexModel model;
    private  int pageNo;
    public PersonZoneIndexPresenter() {
        this.model = new PersonZoneIndexModel();
    }


    @Override
    public void getPersonIndex() {
        mView.showLoading();
        model.getPersonIndex(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                mView.hideLoading();

                PersonZoneIndexBean personNalCourseDetailBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()),PersonZoneIndexBean.class);

                mView.getPersonIndexSuc(personNalCourseDetailBean);
            }

            @Override
            public void error(String code, String msg) {
                mView.hideLoading();
                mView.onError(msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                mView.hideLoading();
                mView.onError(expectionMsg);


            }
        });
    }

    @Override
    public void getMyToPic() {
        pageNo = 1;
        model.getMyTopic(pageNo,FinalValue.limit,new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<PicBean> hotArticalList =new Gson().fromJson(GsonUtil.GsonString(t.getData()),new TypeToken<List<PicBean>>() {}.getType());
                if(hotArticalList.size()< FinalValue.limit){
                    mView.noMore();
                }
                mView.getMyTopicSuc(hotArticalList);
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

    @Override
    public void loadMoreMyTopic() {
        pageNo++;
        model.getMyTopic(pageNo,FinalValue.limit,new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<PicBean> hotArticalList =new Gson().fromJson(GsonUtil.GsonString(t.getData()),new TypeToken<List<PicBean>>() {}.getType());
                if(hotArticalList.size()< FinalValue.limit){
                    mView.noMore();
                }
                mView.loadmoreTopicSuc(hotArticalList);
            }

            @Override
            public void error(String code, String msg) {
                mView.onError(msg);
                mView.noMore();
            }

            @Override
            public void expcetion(String expectionMsg) {
                mView.onError(expectionMsg);
                mView.noMore();
            }
        });
    }
    @Override
    public void getPicMaster(int userId) {
        model.getPicMasterById(userId,new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                List<PicMaster> hotArticalList =new Gson().fromJson(GsonUtil.GsonString(t.getData()),new TypeToken<List<PicMaster>>() {}.getType());
                if(hotArticalList.size()>0){
                    mView.getPicMasterSuc(hotArticalList.get(0));
                }
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
