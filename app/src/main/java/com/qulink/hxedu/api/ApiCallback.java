package com.qulink.hxedu.api;

import javax.security.auth.callback.Callback;

public interface ApiCallback  {


    void success(ResponseData t);
    void error(String code,String msg);
    void expcetion(String expectionMsg);
}
