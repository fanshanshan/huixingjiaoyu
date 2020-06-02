package com.qulink.hxedu.mvp.presenter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.HotArtical;
import com.qulink.hxedu.mvp.BasePresenter;
import com.qulink.hxedu.mvp.BaseView;
import com.qulink.hxedu.mvp.contract.ZoneContract;
import com.qulink.hxedu.mvp.model.ZoneModel;

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
                List<HotArtical> hotArticalList =new Gson().fromJson(t.getData(),new TypeToken<List<HotArtical>>() {}.getType());
                mView.getHotArticalSuc(hotArticalList);
            }

            @Override
            public void error(String code, String msg) {
                mView.getHotArticalFail(msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                mView.getHotArticalFail(expectionMsg);
            }
        });
    }
}
