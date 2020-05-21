package com.qulink.hxedu.api;

import com.qulink.hxedu.entity.TokenInfo;

import java.util.HashMap;
import java.util.Map;

public class ApiUtils {


    public static  ApiUtils instance;

    public static ApiUtils getInstance(){
        if(instance==null){
            instance = new ApiUtils();
        }
        return instance;
    }

    /**
     *类型 1:注册 2:登陆 3:重置密码 4:重置支付密码 5:更换手机号(原手机号) 6:更换手机号(新手机号) 7:绑定手机
     * @param phone 手机号
     * @param type  类型
     * @param apiCallback
     */
   public void sendSmsCode( String phone,String type,ApiCallback apiCallback){
       Map<String, String> params = new HashMap<>();
       params.put("phone", phone);
       params.put("type", type);
        NetUtil.getInstance().post("user/sendVerifiCode",params,apiCallback);
    }


    /**
     * 类型 1:注册 2:登陆 3:重置密码 4:重置支付密码 5:更换手机号(原手机号) 6:更换手机号(新手机号) 7:绑定手机
     * @param phone
     * @param code
     * @param invationCode
     * @param type
     * @param apiCallback
     */
    public void registe(String phone,String code,String invationCode,String type,ApiCallback apiCallback){
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        params.put("invationCode", invationCode);
        params.put("type", type);
        NetUtil.getInstance().post("user/register",params,apiCallback);
    }


    /**
     * 类型 1:注册 2:登陆 3:重置密码 4:重置支付密码 5:更换手机号(原手机号) 6:更换手机号(新手机号) 7:绑定手机
     * @param phone
     * @param code
     * @param callback
     */
    public void loginBySmsCode(String phone, String code, ApiCallback callback){
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        params.put("invationCode", "");
        params.put("type", "2");
        NetUtil.getInstance().post("user/verifyCodeLogin",params,callback);
    }


    /**
     *充值密码
     * @param phone
     * @param code
     * @param pwd
     * @param callback
     */
    public void resetPwd(String phone,String code,String pwd,ApiCallback callback){
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        params.put("password", pwd);
        NetUtil.getInstance().post("user/resetPassword",params,callback);

    }

    /**
     *账号密码登陆
     * @param phone
     * @param pwd
     * @param callback
     */
    public void loginByPwd(String phone,String pwd,ApiCallback callback){
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", pwd);
        NetUtil.getInstance().post("user/passwordLogin",params,callback);
    }


    /**
     * 获取用户信息
     * @param apiCallback
     */
    public void getUserInfo(ApiCallback apiCallback){
        NetUtil.getInstance().get("user/userInfo",new HashMap<>(),apiCallback);
    }

}
