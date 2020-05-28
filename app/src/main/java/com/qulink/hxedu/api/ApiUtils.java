package com.qulink.hxedu.api;

import com.qulink.hxedu.entity.TokenInfo;

import java.util.HashMap;
import java.util.Map;

public class ApiUtils {

    public static String baseUrl = "https://hx-test.kuaiyunma.com/";

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
        NetUtil.getInstance().post(baseUrl+"user/sendVerifiCode",params,apiCallback);
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
        NetUtil.getInstance().post(baseUrl+"user/register",params,apiCallback);
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
        NetUtil.getInstance().post(baseUrl+"user/verifyCodeLogin",params,callback);
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
        NetUtil.getInstance().post(baseUrl+"user/resetPassword",params,callback);

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
        NetUtil.getInstance().post(baseUrl+"user/passwordLogin",params,callback);
    }
    /**
     *修改个人信息
     * @param nickName
     * @param path
     * @param callback
     */
    public void updateUserInfo(String nickName,String path,ApiCallback callback){
        Map<String, String> params = new HashMap<>();
        params.put("nickName", nickName);
        params.put("path", path);
        NetUtil.getInstance().post(baseUrl+"user/updateUserInfo",params,callback);
    }

    /**
     * 获取用户信息
     * @param apiCallback
     */
    public void getUserInfo(ApiCallback apiCallback){
        NetUtil.getInstance().get(baseUrl+"user/userInfo",new HashMap<>(),apiCallback);
    }

    /**
     *购买vip
     * @param
     * @param
     * @param callback
     */
    public void buyVip(String type,ApiCallback callback){
        Map<String, String> params = new HashMap<>();
        params.put("type",type);
        NetUtil.getInstance().post(baseUrl+"vip/buyVip",params,callback);
    }
    /**
     *购买vip支付回调
     * @param
     * @param
     * @param callback
     */
    public void buyVipCheck(ApiCallback callback,String orderNo){
        Map<String, String> params = new HashMap<>();
        params.put("orderSn",orderNo);
        NetUtil.getInstance().post(baseUrl+"vip/buyVipCheck",params,callback);
    }
    /**
     *赠送vip
     * @param
     * @param type 类型 1:支付宝 2:微信 3:银行卡
     * @param callback
     */
    public void sendVip(String phone,String type,ApiCallback callback){
        Map<String, String> params = new HashMap<>();
        params.put("type",type);
        params.put("phone",phone);
        NetUtil.getInstance().post(baseUrl+"vip/giveVip",params,callback);
    }

    /**
     *赠送vip支付回调
     * @param
     * @param
     * @param callback
     */
    public void sendVipCheck(String order,String phone,ApiCallback callback){
        Map<String, String> params = new HashMap<>();
        params.put("orderSn",order);
        params.put("phone",phone);
        NetUtil.getInstance().post(baseUrl+"vip/giveVipCheck",params,callback);
    }

    /**
     *获取七牛token
     * @param
     * @param
     * @param callback
     */
    public void getQiniuToken(ApiCallback callback){
        Map<String, String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl+"setting/getQiNiuYunToken",params,callback);
    }

    /**
     *获取七牛url
     * @param
     * @param
     * @param callback
     */
    public void getQiniuUrl(ApiCallback callback){
        Map<String, String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl+"setting/getSettingMap",params,callback);
    }

    /**
     *签到
     * @param
     * @param
     * @param callback
     */
    public void sign(ApiCallback callback){
        Map<String, String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl+"user/userSign",params,callback);
    }


    public void getScoreDetail(ApiCallback apiCallback){
        NetUtil.getInstance().get(baseUrl+"user/creditInfo",new HashMap<>(),apiCallback);
    }
}
