package com.qulink.hxedu.api;


import com.qulink.hxedu.entity.HotCourseBean;

import java.util.HashMap;
import java.util.Map;

public class ApiUtils {

    private static String baseUrl = "https://hx-test.kuaiyunma.com/";//测试服
    //public static String baseUrl = "http://192.168.199.187:8080/";//牛元亮

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
     * @param phone        手机号
     * @param code         验证码
     * @param invationCode 邀请码
     * @param type         类型
     * @param apiCallback  回调
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
     * @param phone        手机号
     * @param code         验证码
     * @param invationCode 邀请码
     * @param type         类型
     * @param apiCallback  回调
     */
    public void bindPhone(String phone, String code, String invationCode, String type, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        params.put("invationCode", invationCode);
        params.put("type", type);
        NetUtil.getInstance().post(baseUrl + "user/bindPhone", params, apiCallback);
    }

    /**
     * 类型 1:注册 2:登陆 3:重置密码 4:重置支付密码 5:更换手机号(原手机号) 6:更换手机号(新手机号) 7:绑定手机
     *
     * @param phone    手机号
     * @param code     验证码
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
     * 重置密码
     *
     * @param phone    手机号
     * @param code     验证码
     * @param pwd      密码
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
     * 重置支付密码
     *
     * @param phone    手机号
     * @param code     验证码
     * @param pwd      密码
     * @param callback 回调
     */
    public void resetPayPwd(String phone, String code, String pwd, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        params.put("password", pwd);
        NetUtil.getInstance().post(baseUrl + "user/resetPayPassword", params, callback);

    }


    /**
     * 修改手机号
     *
     * @param callback 回调
     */
    public void resetPhone(String oldCode, String newPhone, String newCode, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("oldCode", oldCode);
        params.put("newPhone", newPhone);
        params.put("newCode", newCode);
        NetUtil.getInstance().post(baseUrl + "user/resetPhone", params, callback);

    }

    /**
     * 验证旧手机手机号
     *
     * @param callback 回调
     */
    public void verifyOldPhoneCode(String phone, String code, ApiCallback callback) {
        Map<String, String> params = new HashMap<>();
        params.put("phone", phone);
        params.put("code", code);
        NetUtil.getInstance().post(baseUrl + "user/checkOldPhoneCode", params, callback);

    }

    /**
     * 账号密码登陆
     *
     * @param phone    手机号
     * @param pwd      密码
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
     * @param path     头像路径
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
     * @param content     内容
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
     *
     * @param jsonStr     id和积分组合的json字符串
     * @param apiCallback 回调
     */
    public void submitPlatformScore(String jsonStr, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("data", jsonStr);
        NetUtil.getInstance().post(baseUrl + "platformCredit/commitSelfPc", params, apiCallback);
    }


    /**
     * 提交平台积分
     *
     * @param id          分组id
     * @param apiCallback 回调
     */
    public void getPlatformGroupById(String id, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("pccId", id);
        NetUtil.getInstance().post(baseUrl + "platformCredit/getPlatformCreditByCategoryId", params, apiCallback);
    }

    /**
     * 完成积分项
     *
     * @param id          项目id
     * @param pccId       分组id
     * @param apiCallback 回调
     */
    public void completeGroupScore(String id, String pccId, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("pccId", pccId);
        params.put("id", id);
        NetUtil.getInstance().post(baseUrl + "platformCredit/finishPlatformCredit", params, apiCallback);
    }

    //*************************************************************首页相关

    /**
     * 获取首页轮播
     *
     * @param apiCallback 回调
     */
    public void getIndexBanner(ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl + "curriculum/getCarousel", params, apiCallback);
    }

    /**
     * 获取首页课程一级分类
     *
     * @param apiCallback 回调
     */
    public void getIndexCourseItem(ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl + "curriculum/getClassify", params, apiCallback);
    }

    /**
     * 获取课程分类信息
     *
     * @param classifyId  id
     * @param apiCallback 回调
     */
    public void getSubCourseNameById(int classifyId, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("classifyId", classifyId + "");
        NetUtil.getInstance().get(baseUrl + "curriculum/getTags", params, apiCallback);
    }

    /**
     * 获取课程子列表
     *
     * @param classifyId     由getTags接口返回classify
     * @param tagId          由getTags接口返回tags
     * @param pageNo         页码从1开始,必须大于0
     * @param pageSize       分页数量从1开始,必须大于0,小于等于20
     * @param curriculumType 1-热门课程,2-会员免费,3-付费课程,4-会员专享价
     * @param apiCallback    回调
     */
    public void getCourseList(int classifyId, int tagId, int pageNo, int pageSize, int curriculumType, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("classifyId", classifyId + "");
        params.put("tagId", tagId + "");
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("curriculumType", curriculumType + "");
        NetUtil.getInstance().get(baseUrl + "curriculum/search", params, apiCallback);
    }

    /**
     * @param curriculumType 1-热门课程,2-会员免费,3-付费课程,4-会员专享价
     * @param pageNo         页码从1开始,必须大于0
     * @param pageSize       分页数量从1开始,必须大于0,小于等于20
     * @param apiCallback    回调
     */
    public void getIndexSortCourse(int curriculumType, int pageNo, int pageSize, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("curriculumType", curriculumType + "");
        NetUtil.getInstance().get(baseUrl + "curriculum/getCurriculum", params, apiCallback);
    }


    /**
     * 获取课程详情
     *
     * @param curriculumId 课程id
     * @param apiCallback  回调
     */
    public void getCourseDetail(int curriculumId, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("curriculumId", curriculumId + "");
        NetUtil.getInstance().get(baseUrl + "curriculum/getDetail", map, apiCallback);
    }

    /**
     * 获取课程观看人数
     *
     * @param curriculumId 课程id
     * @param apiCallback  回调
     */
    public void getCourseLookNum(int curriculumId, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("curriculumId", curriculumId + "");
        NetUtil.getInstance().get(baseUrl + "curriculum/getParticipant", map, apiCallback);
    }

    /**
     * 获取本人关于此课程的信息
     *
     * @param curriculumId 课程id
     * @param apiCallback  回调
     */
    public void getCourseDetailForPersonnal(int curriculumId, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("curriculumId", curriculumId + "");
        NetUtil.getInstance().get(baseUrl + "curriculum/getPersonalCurriculum", map, apiCallback);
    }


    /**
     * 上报观看次数
     *
     * @param curriculumId 课程id
     * @param apiCallback  回调
     */
    public void increaseLookNumberToServer(int curriculumId, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("curriculumId", curriculumId + "");
        NetUtil.getInstance().post(baseUrl + "curriculum/increaseParticipant", map, apiCallback);
    }
    //**********************************************************社区相关

    /**
     * @param apiCallback 回调
     */
    public void getHotArtical(ApiCallback apiCallback) {
        NetUtil.getInstance().post(baseUrl + "community/topArticle", new HashMap<>(), apiCallback);
    }


    /**
     * 获取顶部话题
     *
     * @param apiCallback 回调
     */
    public void getTopPic(ApiCallback apiCallback) {
        NetUtil.getInstance().post(baseUrl + "community/getTopics", new HashMap<>(), apiCallback);

    }

    /**
     * 社区首页帖子列表
     *
     * @param pageNo      页码 1开始
     * @param pageSize    数量
     * @param topicName   搜索内容
     * @param topicId     主题id
     * @param apiCallback 回调
     */
    public void getTopPic(int pageNo, int pageSize, String topicName, String topicId, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", pageSize + "");
        map.put("topicName", topicName + "");
        map.put("topicId", topicId + "");
        NetUtil.getInstance().post(baseUrl + "community/articles", map, apiCallback);

    }

    /**
     * 获取帖子主人信息
     *
     * @param currentUserIds 帖主id
     * @param apiCallback    回调
     */
    public void getPicMasterById(int currentUserIds, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("currentUserIds", currentUserIds + "");
        NetUtil.getInstance().postCache(baseUrl + "community/articleUserInfo", map, apiCallback);

    }

    /**
     * 个人社区主页
     *
     * @param apiCallback 回调
     */
    public void getPersonZoneIndex(ApiCallback apiCallback) {
        NetUtil.getInstance().post(baseUrl + "community/cmInfoByUserId", new HashMap<>(), apiCallback);
    }

    /**
     * @param pageSize    页码
     * @param pageNo      size
     * @param name        搜索关键词
     * @param apiCallback 回调
     */
    public void searchSubject(int pageSize, int pageNo, String name, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        param.put("pageSize", pageSize + "");
        param.put("pageNo", pageNo + "");
        param.put("name", name + "");
        NetUtil.getInstance().post(baseUrl + "community/searchTopic", param, apiCallback);
    }


    /**
     * 创建话题
     *
     * @param name        话题描述
     * @param apiCallback 回调
     */
    public void createSubject(String name, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        param.put("name", name + "");
        NetUtil.getInstance().post(baseUrl + "community/addTopic", param, apiCallback);
    }

    /**
     * 发布贴子
     *
     * @param topicId     话题id
     * @param title       标题
     * @param content     内容
     * @param imgPath     图片路径
     * @param apiCallback 回调
     */
    public void createTopic(int topicId, String title, String content, String imgPath, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        param.put("topicId", topicId + "");
        param.put("title", title + "");
        param.put("content", content + "");
        param.put("imgPath", imgPath + "");
        NetUtil.getInstance().post(baseUrl + "community/addArticle", param, apiCallback);
    }

    /**
     * 获取我参与的话题
     *
     * @param apiCallback 回调
     */
    public void getMyTopic(int pageNo, int pageSize, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        param.put("pageSize", pageSize + "");
        param.put("pageNo", pageNo + "");
        NetUtil.getInstance().post(baseUrl + "community/articleByUserId", param, apiCallback);
    }

    /**
     * 获取我参与的话题
     *
     * @param apiCallback 回调
     */
    public void getTopicDetail(int topicId, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        //   param.put("articleId", topicId + "");
        param.put("articleId", "33");
        param.put("pageNo", "1");
        param.put("pageSize", "1");
        param.put("sortType", "0");
        NetUtil.getInstance().post(baseUrl + "community/getArticleDetails", param, apiCallback);
    }


    /**
     * 获取更多话题
     *
     * @param apiCallback 回调
     */
    public void getMoreSubject(ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        NetUtil.getInstance().post(baseUrl + "community/getMoreTopics", param, apiCallback);
    }

    /**
     * 文章点赞
     *
     * @param articleId   文章id
     * @param apiCallback 回调
     */
    public void likeArtical(int articleId, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        param.put("articleId", articleId + "");
        NetUtil.getInstance().post(baseUrl + "community/doThumbs", param, apiCallback);
    }

    /**
     * 文章取消点赞
     *
     * @param articleId   文章id
     * @param apiCallback 回调
     */
    public void cancelLikeArtical(int articleId, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        param.put("articleId", articleId + "");
        NetUtil.getInstance().post(baseUrl + "community/cancelThumbs", param, apiCallback);
    }

    /**
     * 获取文章评论列表
     *
     * @param pageNo      页码
     * @param pageSize    数量
     * @param articleId   文章id
     * @param sortType    排序类型 排序方式 0-点赞数 1-最新
     * @param apiCallback 回调
     */
    public void getArticalComments(int pageNo, int pageSize, int articleId, int sortType, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        param.put("articleId", articleId + "");
        param.put("pageNo", pageNo + "");
        param.put("pageSize", pageSize + "");
        param.put("sortType", sortType + "");
        NetUtil.getInstance().post(baseUrl + "community/comments", param, apiCallback);
    }

    /**
     * 获取文章评论列表
     *
     * @param pageNo      页码
     * @param pageSize    数量
     * @param apiCallback 回调
     */
    public void getCommentsReply(int pageNo, int pageSize, int commentId, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        param.put("pageNo", pageNo + "");
        param.put("pageSize", pageSize + "");
        param.put("commentId", commentId + "");
        NetUtil.getInstance().post(baseUrl + "community/replys", param, apiCallback);
    }

    /**
     * 评论贴子
     *
     * @param articleId   贴子id
     * @param content     评论内容
     * @param apiCallback 回调
     */
    public void comments(int articleId, String content, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        param.put("articleId", articleId + "");
        param.put("content", content);
        NetUtil.getInstance().post(baseUrl + "community/doComment", param, apiCallback);
    }

    /**
     * 获取我参与评论的贴子
     *
     * @param pageNo
     * @param pageSize
     * @param apiCallback
     */
    public void getMyCommentsArtical(int pageNo, int pageSize, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        param.put("pageNo", pageNo + "");
        param.put("pageSize", pageSize + "");
        NetUtil.getInstance().post(baseUrl + "community/getMeCommentArticle", param, apiCallback);
    }

    /**
     * 获取我点赞的贴子
     *
     * @param pageNo
     * @param pageSize
     * @param apiCallback
     */
    public void getMyLikeArtical(int pageNo, int pageSize, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        param.put("pageNo", pageNo + "");
        param.put("pageSize", pageSize + "");
        NetUtil.getInstance().post(baseUrl + "community/getMeThumbArticle", param, apiCallback);
    }

    /**
     * 回复评论
     *
     * @param articleId   贴子id
     * @param content     评论内容
     * @param apiCallback 回调
     */
    public void replyComments(int articleId, int commentId, int toUserId, String content, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        param.put("articleId", articleId + "");
        param.put("commentId", commentId + "");
        param.put("toUserId", toUserId + "");
        param.put("content", content);
        NetUtil.getInstance().post(baseUrl + "community/doRepley", param, apiCallback);
    }

    /**
     * 评论点赞
     *
     * @param articleId   贴子id
     * @param commentId   评论id
     * @param apiCallback 回调
     */
    public void likeComments(int articleId, int commentId, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        param.put("articleId", articleId + "");
        param.put("commentId", commentId + "");
        NetUtil.getInstance().post(baseUrl + "community/doCommentThumb", param, apiCallback);
    }

    /**
     * 评论取消点赞
     *
     * @param articleId   贴子id
     * @param commentId   评论id
     * @param apiCallback 回调
     */
    public void cancelLikeComments(int articleId, int commentId, ApiCallback apiCallback) {
        Map<String, String> param = new HashMap<>();
        param.put("articleId", articleId + "");
        param.put("commentId", commentId + "");
        NetUtil.getInstance().post(baseUrl + "community/cancelCommentThumb", param, apiCallback);
    }

    //*************************************************************************
    public void wxLogin(String code, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("code", code);
        NetUtil.getInstance().post(baseUrl + "user/wxLogin", map, apiCallback);
    }

    /**
     * 积分明细
     *
     * @param page
     * @param pageSize
     * @param apiCallback
     */
    public void scoreDetail(int page, int pageSize, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("pageSize", pageSize + "");
        NetUtil.getInstance().post(baseUrl + "user/creditDetails", map, apiCallback);
    }

    /**
     * 学习关系
     *
     * @param page
     * @param pageSize
     * @param apiCallback
     */
    public void getStudyRelation(int page, int pageSize, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("pageSize", pageSize + "");
        NetUtil.getInstance().post(baseUrl + "user/relationShips", map, apiCallback);
    }

    /**
     * 二级学习关系
     *
     * @param page
     * @param pageSize
     * @param apiCallback
     */
    public void getStudySubRelation(int id, int page, int pageSize, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("page", page + "");
        map.put("id", id + "");
        map.put("pageSize", pageSize + "");
        NetUtil.getInstance().post(baseUrl + "user/getRelationsById", map, apiCallback);
    }


    /**
     * 徽章详情
     *
     * @param apiCallback
     */
    public void getBadgeInfo(ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        NetUtil.getInstance().get(baseUrl + "user/badgeInfo", map, apiCallback);
    }

    /**
     * 徽章详情
     *
     * @param apiCallback
     */
    public void getBadgeInfoByType(int type, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("type", type + "");
        NetUtil.getInstance().get(baseUrl + "user/getBadgeInfo", map, apiCallback);
    }

    /**
     * 钱包
     *
     * @param apiCallback
     */
    public void getUserWallet(ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        NetUtil.getInstance().post(baseUrl + "userWallet/balance", map, apiCallback);
    }

    /**
     * 获取奖学记录
     * data 年月
     *
     * @param apiCallback
     */
    public void getScolarShipRecord(String date, int page, int pageSize, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();

        map.put("localDate", date);
        map.put("page", page + "");
        map.put("pageSize", pageSize + "");
        NetUtil.getInstance().post(baseUrl + "scholarship/getScholarship", map, apiCallback);
    }

    /**
     * 获取绑定的银行卡列表
     * data 年月
     *
     * @param apiCallback
     */
    public void getBankard(ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        NetUtil.getInstance().get(baseUrl + "bankcard", map, apiCallback);
    }

    /**
     * 绑定银行卡
     *
     * @param apiCallback
     */
    public void bindBankCard(String req, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("bankCardReq", req);
        NetUtil.getInstance().get(baseUrl + "bankcard/bind", map, apiCallback);
    }

    /**
     * 积分商城
     *
     * @param apiCallback
     */
    public void scoreShopList(int pageNo, int pageSize, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("pageNo", pageNo + "");
        map.put("pageSize", pageSize + "");
        NetUtil.getInstance().post(baseUrl + "integration/curriculum", map, apiCallback);
    }

    /**
     * 兑换课程
     *
     * @param apiCallback
     */
    public void exchangeCourse(int curriculumId, int payType, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("payType", payType + "");
        map.put("curriculumId", curriculumId + "");
        NetUtil.getInstance().post(baseUrl + "integration/curriculum/exchange", map, apiCallback);
    }

    /**
     * 积分课程详情
     *
     * @param apiCallback
     */
    public void getShopCourseDetail(int curriculumId, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("curriculumId", curriculumId + "");
        NetUtil.getInstance().post(baseUrl + "integration/curriculum/detail", map, apiCallback);
    }

    /**
     * 课程目录
     *
     * @param apiCallback
     */
    public void getCourseChapter(int curriculumId, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("curriculumId", curriculumId + "");
        NetUtil.getInstance().get(baseUrl + "curriculum/getChapter", map, apiCallback);
    }/**
     * 收藏课程
     *
     * @param apiCallback
     */
    public void collectionCourse(int curriculumId, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("curriculumId", curriculumId + "");
        NetUtil.getInstance().post(baseUrl + "curriculum/collectCurriculum", map, apiCallback);
    }
/**
     * 取消收藏
     *
     * @param apiCallback
     */
    public void cancelCollectionCourse(String collectIds, ApiCallback apiCallback) {
        Map<String, String> map = new HashMap<>();
        map.put("collectIds", collectIds);
        NetUtil.getInstance().post(baseUrl + "curriculum/collect/batchCancel", map, apiCallback);
    }

    /**
     * 课程搜索
     *
     * @param classifyId
     * @param tagId
     * @param pageNo
     * @param pageSize
     * @param searchTex
     * @param apiCallback
     */
    public void searchCourse(int classifyId, int tagId, int pageNo, int pageSize, String searchTex, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("classifyId", classifyId + "");
        params.put("tagId", tagId + "");
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("searchTex", searchTex + "");
        NetUtil.getInstance().get(baseUrl + "curriculum/search", params, apiCallback);
    }

    /**
     * 直播分类
     *
     * @param apiCallback
     */
    public void getLiveClassify(ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        NetUtil.getInstance().post(baseUrl + "live/classify", params, apiCallback);
    }

    /**
     * 直播列表
     *
     * @param apiCallback
     */
    public void getLiveList(int pageNo, int pageSize, int liveStatus, int classifyId, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("liveStatus", liveStatus + "");
        params.put("classifyId", classifyId + "");
        NetUtil.getInstance().post(baseUrl + "live/list", params, apiCallback);
    }

    /**
     * 提现 转账方式：1-支付宝，2-微信，3-银行卡
     *
     * @param apiCallback
     */
    public void withdraw(int type, String amount, String payPassword, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("type", type + "");
        params.put("amount", amount);
        params.put("payPassword", payPassword);
        NetUtil.getInstance().post(baseUrl + "userWallet/transfer", params, apiCallback);
    }

    /**
     * 提现记录
     *
     * @param apiCallback
     */
    public void withdrawRecord(int pageNo, int pageSize, String date, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        params.put("deadline", date);
        NetUtil.getInstance().post(baseUrl + "withdrawRecord/list", params, apiCallback);
    }

    /**
     * 需求反馈
     *
     * @param apiCallback
     */
    public void advice(String content, String img, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("content", content);
        params.put("img", img);
        NetUtil.getInstance().post(baseUrl + "withdrawRecord/list", params, apiCallback);
    }

    /**
     * 获取是否有未读消息
     *
     * @param apiCallback
     */
    public void getIndexMsg(ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl + "message/msgReadStatus", params, apiCallback);
    }

    /**
     * 获取是否有未读消息
     *
     * @param apiCallback
     */
    public void getMsgReadStatus(ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl + "message/getMessageReadStatus", params, apiCallback);
    }


    /**
     * 获取系统消息
     *
     * @param apiCallback
     */
    public void getSystemMsg(int pageNo, int pageSize, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("page", pageNo + "");
        params.put("pageSize", pageSize + "");
        NetUtil.getInstance().post(baseUrl + "message/getUserMessage", params, apiCallback);
    }

    /**
     * 获取系统公告
     *
     * @param apiCallback
     */
    public void getSystemNotice(int pageNo, int pageSize, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        NetUtil.getInstance().post(baseUrl + "message/notice/list", params, apiCallback);
    }

    /**
     * 设置系统消息状态为已读
     *
     * @param apiCallback
     */
    public void updateMsgReadStatus(int msgId, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("id", msgId + "");
        NetUtil.getInstance().post(baseUrl + "message/readUserMessage", params, apiCallback);
    }

    /**
     * 获取系统公告详情
     *
     * @param apiCallback
     */
    public void getNoticeDetail(int msgId, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("id", msgId + "");
        NetUtil.getInstance().post(baseUrl + "message/notice/detail", params, apiCallback);
    }

    /**
     * 获取活动详情
     *
     * @param apiCallback
     */
    public void getActivityDetail(int msgId, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("activityId", msgId + "");
        NetUtil.getInstance().post(baseUrl + "message/activity/detail", params, apiCallback);
    }

    /**
     * 报名参加活动
     *
     * @param apiCallback
     */
    public void joinActivity(int activityId, String mobileNumber, String wxNumber, int payType, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("activityId", activityId + "");
        params.put("payType", payType + "");
        params.put("mobileNumber", mobileNumber);
        params.put("wxNumber", wxNumber);
        NetUtil.getInstance().post(baseUrl + "activity/participant", params, apiCallback);
    }

    /**
     * 获取社区消息
     *
     * @param apiCallback
     */
    public void getZoneMsg(ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl + "message/getCommunityMessageNums", params, apiCallback);
    }

    /**
     * 获取不同类型的社区消息
     * type 类型 0:评论 1:点赞 2:分享
     *
     * @param apiCallback
     */
    public void getZoneMsgByType(int page, int pageSize, int type, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("pageSize", pageSize + "");
        params.put("type", type + "");
        NetUtil.getInstance().post(baseUrl + "message/getCommunityMessageByType", params, apiCallback);
    }

    /**
     * 获取消息里的直播列表
     *
     * @param apiCallback
     */
    public void getLiveMsgList(int pageNo, int pageSize, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("pageNo", pageNo + "");
        params.put("pageSize", pageSize + "");
        NetUtil.getInstance().post(baseUrl + "message/live/list", params, apiCallback);
    }

    /*
     *查询是否有人赠送vip啦
     *
     * @param apiCallback
     */
    public void getSendVipMsg(ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        NetUtil.getInstance().get(baseUrl + "vip/getGiveMsg", params, apiCallback);
    }/*
     *赠送会员消息更改状态为已读
     *
     * @param apiCallback
     */

    public void readSendVipMsg(int id, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("id", id + "");
        NetUtil.getInstance().post(baseUrl + "vip/readGiveMsg", params, apiCallback);
    }/*
     *ocr实名认证
     *
     * @param apiCallback
     */

    public void realAuth(String realName, String idCard, String sex, ApiCallback apiCallback) {
        Map<String, String> params = new HashMap<>();
        params.put("realName", realName);
        params.put("idCard", idCard);
        params.put("sex", sex);
        NetUtil.getInstance().post(baseUrl + "user/realAuth", params, apiCallback);
    }

}
