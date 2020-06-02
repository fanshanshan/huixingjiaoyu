package com.qulink.hxedu.api;


import com.qulink.hxedu.entity.HotCourseBean;

import java.util.HashMap;
import java.util.Map;

public class ApiUtils {

    private static String baseUrl = "https://hx-test.kuaiyunma.com/";//测试服
   // public static String baseUrl = "http://192.168.199.186:8080/";//牛元亮

    private static ApiUtils instance;

    public static ApiUtils getInstance() {
        if (instance == null) {
            instance = new ApiUtils();
        }
        return instance;
    }

    /**
     * 类型 1:注册 2:登陆 3:重置密码 4:重置支付密码 5:更换手机号(原手机号) 6:更换手机号(新手机号) 7:绑定手机
     *
     * @param phone       手机号
     * @param type        类型
     * @param apiCallback 回到
     */
    public void sendSmsCode(String phone, String type, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("type", type);
        NetUtil.getInstance().post(baseUrl + "user/sendVerifiCode", params, apiCallback);
    }


    /**
     * 类型 1:注册 2:登陆 3:重置密码 4:重置支付密码 5:更换手机号(原手机号) 6:更换手机号(新手机号) 7:绑定手机
     *
     * @param phone 手机号
     * @param code 验证码
     * @param invationCode 邀请码
     * @param type 类型
     * @param apiCallback 回调
     */
    public void registe(String phone, String code, String invationCode, String type, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        params.put("invationCode", invationCode);
        params.put("type", type);
        NetUtil.getInstance().post(baseUrl + "user/register", params, apiCallback);
    }


    /**
     * 类型 1:注册 2:登陆 3:重置密码 4:重置支付密码 5:更换手机号(原手机号) 6:更换手机号(新手机号) 7:绑定手机
     *
     * @param phone 手机号
     * @param code 验证码
     * @param callback 回调
     */
    public void loginBySmsCode(String phone, String code, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        params.put("invationCode", "");
        params.put("type", "2");
        NetUtil.getInstance().post(baseUrl + "user/verifyCodeLogin", params, callback);
    }


    /**
     * 充值密码
     *
     * @param phone 手机号
     * @param code  验证码
     * @param pwd  密码
     * @param callback 回调
     */
    public void resetPwd(String phone, String code, String pwd, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        params.put("password", pwd);
        NetUtil.getInstance().post(baseUrl + "user/resetPassword", params, callback);

    }

    /**
     * 账号密码登陆
     *
     * @param phone 手机号
     * @param pwd 密码
     * @param callback 回调
     */
    public void loginByPwd(String phone, String pwd, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("password", pwd);
        NetUtil.getInstance().post(baseUrl + "user/passwordLogin", params, callback);
    }

    /**
     * 修改个人信息
     *
     * @param nickName 昵称
     * @param path  头像路径
     * @param callback 回调
     */
    public void updateUserInfo(String nickName, String path, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("nickName", nickName);
        params.put("path", path);
        NetUtil.getInstance().post(baseUrl + "user/updateUserInfo", params, callback);
    }

    /**
     * 获取用户信息
     *
     * @param apiCallback 回调
     */
    public void getUserInfo(ApiCallback apiCallback) {
        NetUtil.getInstance().get(baseUrl + "user/userInfo", new HashMap<>(), apiCallback);
    }

    /**
     * 购买vip
     *
     * @param callback 回调
     */
    public void buyVip(String type, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        NetUtil.getInstance().post(baseUrl + "vip/buyVip", params, callback);
    }

    /**
     * 购买vip支付回调
     *
     * @param callback 回调
     */
    public void buyVipCheck(ApiCallback callback, String orderNo) {
        Map<String, String> params = new HashMap<>();
        params.put("orderSn", orderNo);
        NetUtil.getInstance().post(baseUrl + "vip/buyVipCheck", params, callback);
    }

    /**
     * 赠送vip
     *
     * @param type     类型 1:支付宝 2:微信 3:银行卡
     * @param callback 回调
     */
    public void sendVip(String phone, String type, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type);
        params.put("phone", phone);
        NetUtil.getInstance().post(baseUrl + "vip/giveVip", params, callback);
    }

    /**
     * 赠送vip支付回调
     *
     * @param callback 回调
     */
    public void sendVipCheck(String order, String phone, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("orderSn", order);
        params.put("phone", phone);
        NetUtil.getInstance().post(baseUrl + "vip/giveVipCheck", params, callback);
    }

    /**
     * 获取七牛token
     *
     * @param callback 回调
     */
    public void getQiniuToken(ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl + "setting/getQiNiuYunToken", params, callback);
    }

    /**
     * 获取七牛url
     *
     * @param callback 回调
     */
    public void getQiniuUrl(ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl + "setting/getSettingMap", params, callback);
    }

    /**
     * 签到
     *
     * @param callback 回调
     */
    public void sign(ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl + "user/userSign", params, callback);
    }


    /**
     * 获取积分详情
     *
     * @param apiCallback 回调
     */
    public void getScoreDetail(ApiCallback apiCallback) {
        NetUtil.getInstance().get(baseUrl + "user/creditInfo", new HashMap<>(), apiCallback);
    }

    /**
     * 获取平台账户积分详情
     *
     * @param apiCallback 回调
     */
    public void getPlatformScoreDetail(ApiCallback apiCallback) {
        NetUtil.getInstance().get(baseUrl + "platformCredit/getPlatformCredit", new HashMap<>(), apiCallback);
    }


    /**
     * 添加学习计划
     *
     * @param content 内容
     * @param apiCallback 回调
     */
    public void addStudyPlan(String content, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("content", content);
        NetUtil.getInstance().post(baseUrl + "learningPlan/addLearningPlan", params, apiCallback);
    }

    /**
     * 获取学习计划详情
     *
     * @param apiCallback 回调
     */
    public void getStudyPlanSetail(ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl + "learningPlan/getLearningPlan", params, apiCallback);
    }

    /**
     * 完成学习计划
     *
     * @param id          学习计划id
     * @param apiCallback 回调
     */
    public void finishStudyPlanById(String id, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("learningPlanId", id);
        NetUtil.getInstance().post(baseUrl + "learningPlan/finishLearningPlan", params, apiCallback);
    }


    /**
     * 提交平台积分
     * @param jsonStr id和积分组合的json字符串
     * @param apiCallback 回调
     */
    public void submitPlatformScore(String jsonStr,ApiCallback apiCallback){
        Map<String,String> params = new HashMap<>();
        params.put("data", jsonStr);
        NetUtil.getInstance().post(baseUrl+"platformCredit/commitSelfPc",params,apiCallback);
    }


    /**
     * 提交平台积分
     * @param id 分组id
     * @param apiCallback 回调
     */
    public void getPlatformGroupById(String id,ApiCallback apiCallback){
        Map<String,String> params = new HashMap<>();
        params.put("pccId", id);
        NetUtil.getInstance().post(baseUrl+"platformCredit/getPlatformCreditByCategoryId",params,apiCallback);
    }

    /**
     * 完成积分项
     * @param id 项目id
     * @param pccId 分组id
     * @param apiCallback 回调
     */
    public void completeGroupScore(String id,String pccId,ApiCallback apiCallback){
        Map<String,String> params = new HashMap<>();
        params.put("pccId", pccId);
        params.put("id", id);
        NetUtil.getInstance().post(baseUrl+"platformCredit/finishPlatformCredit",params,apiCallback);
    }

    //*************************************************************首页相关
    /**
     * 获取首页轮播
     * @param apiCallback 回调
     */
    public void getIndexBanner(ApiCallback apiCallback){
        Map<String,String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl+"curriculum/getCarousel",params,apiCallback);
    }

    /**
     * 获取首页课程一级分类
     * @param apiCallback 回调
     */
    public void getIndexCourseItem(ApiCallback apiCallback){
        Map<String,String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl+"curriculum/getClassify",params,apiCallback);
    }


    /**
     *
     * @param curriculumType 1-热门课程,2-会员免费,3-付费课程,4-会员专享价
     * @param pageNo 页码从1开始,必须大于0
     * @param pageSize 分页数量从1开始,必须大于0,小于等于20
     * @param apiCallback 回调
     */
    public void getIndexSortCourse(int curriculumType,int pageNo,int pageSize,ApiCallback apiCallback){
        Map<String,String> params = new HashMap<>();
        params.put("pageNo",pageNo+"");
        params.put("pageSize",pageSize+"");
        params.put("curriculumType",curriculumType+"");
        NetUtil.getInstance().get(baseUrl+"curriculum/getCurriculum",params,apiCallback);
    }

    //**********************************************************社区相关

    /**
     *
     * @param apiCallback 回调
     */
    public void getHotArtical(ApiCallback apiCallback){
        //NetUtil.getInstance().post2(baseUrl+"community/topArticle",new HashMap<>(),apiCallback);
    }


}
