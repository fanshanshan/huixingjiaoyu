package com.qulink.hxedu.mvp.model;

import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.mvp.contract.SearchContract;

public class SearchModel implements SearchContract.Model {
    @Override
    public void getFilterContent(int classifyId, ApiCallback apiCallback) {
        ApiUtils.getInstance().getSubCourseNameById(classifyId,apiCallback);
    }
}
