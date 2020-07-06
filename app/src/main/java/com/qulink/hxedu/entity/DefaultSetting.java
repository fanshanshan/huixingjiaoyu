package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class DefaultSetting {


    /**
     * ios_version : {"id":34,"dicKey":"ios_version","value":"1.0.0","info":"ios版本号","createTime":"2020-07-01 08:27:21","updateTime":"2020-07-01 08:28:03"}
     * ios_url : {"id":35,"dicKey":"ios_url","value":"www.baidu.com","info":"ios下载url","createTime":"2020-07-01 08:27:30","updateTime":"2020-07-01 08:28:10"}
     * curriculum_expire_day : {"id":29,"dicKey":"curriculum_expire_day","value":"365","info":"课程有效期","createTime":"2020-06-17 13:39:46","updateTime":"2020-06-17 13:40:48"}
     * buy_vip_callback_path_wxpay : {"id":27,"dicKey":"buy_vip_callback_path_wxpay","value":"/vip/buyVipCallbackByWxPay","info":"购买会员微信支付","createTime":"2020-06-05 14:42:39","updateTime":"2020-06-05 14:43:20"}
     * buy_vip_callback_path_alipay : {"id":3,"dicKey":"buy_vip_callback_path_alipay","value":"/vip/buyVipCallbackByAliPay","info":"购买会员支付宝回调path","createTime":"2020-05-26 09:55:19","updateTime":"2020-05-27 09:48:54"}
     * android_version : {"id":31,"dicKey":"android_version","value":"1.0.0","info":"安卓版本号","createTime":"2020-07-01 08:25:45","updateTime":"2020-07-01 08:25:50"}
     * android_content : {"id":33,"dicKey":"android_content","value":"新增功能","info":"安卓更新内容","createTime":"2020-07-01 08:27:05","updateTime":"2020-07-01 08:27:09"}
     * ios_content : {"id":36,"dicKey":"ios_content","value":"新增功能","info":"ios更新内容","createTime":"2020-07-01 08:27:39","updateTime":"2020-07-01 08:28:16"}
     * callback_url : {"id":4,"dicKey":"callback_url","value":"https://hx-test.kuaiyunma.com","info":"回调url","createTime":"2020-05-26 12:03:34","updateTime":"2020-05-27 17:35:45"}
     * img_private_assets_url : {"id":28,"dicKey":"img_private_assets_url","value":"https://huixing-test-assets.kuaiyunma.com","info":"七牛云私有域名","createTime":"2020-06-15 11:58:43","updateTime":"2020-06-15 11:59:01"}
     * img_assets_url : {"id":1,"dicKey":"img_assets_url","value":"huixing-pub-assets.kuaiyunma.com","info":"七牛云共有域名","createTime":"2020-05-21 10:55:59","updateTime":"2020-06-15 11:58:18"}
     * android_url : {"id":32,"dicKey":"android_url","value":"www.baidu.com","info":"安卓下载url","createTime":"2020-07-01 08:26:21","updateTime":"2020-07-01 08:26:25"}
     * vip_price : {"id":2,"dicKey":"vip_price","value":"0.1","info":"会员价格","createTime":"2020-05-25 13:53:42","updateTime":"2020-05-25 13:53:59"}
     * live_curriculum_expire_day : {"id":30,"dicKey":"live_curriculum_expire_day","value":"365","info":"直播课程有效期","createTime":"2020-06-17 13:40:41","updateTime":"2020-06-17 13:42:19"}
     */

    private IosVersionBean ios_version;
    private IosUrlBean ios_url;
    private CurriculumExpireDayBean curriculum_expire_day;
    private BuyVipCallbackPathWxpayBean buy_vip_callback_path_wxpay;
    private BuyVipCallbackPathAlipayBean buy_vip_callback_path_alipay;
    private AndroidVersionBean android_version;
    private AndroidContentBean android_content;
    private IosContentBean ios_content;
    private CallbackUrlBean callback_url;
    private ImgPrivateAssetsUrlBean img_private_assets_url;
    private ImgAssetsUrlBean img_assets_url;
    private AndroidUrlBean android_url;
    private VipPriceBean vip_price;
    private LiveCurriculumExpireDayBean live_curriculum_expire_day;

    @NoArgsConstructor
    @Data
    public static class IosVersionBean {
        /**
         * id : 34
         * dicKey : ios_version
         * value : 1.0.0
         * info : ios版本号
         * createTime : 2020-07-01 08:27:21
         * updateTime : 2020-07-01 08:28:03
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;
    }

    @NoArgsConstructor
    @Data
    public static class IosUrlBean {
        /**
         * id : 35
         * dicKey : ios_url
         * value : www.baidu.com
         * info : ios下载url
         * createTime : 2020-07-01 08:27:30
         * updateTime : 2020-07-01 08:28:10
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;
    }

    @NoArgsConstructor
    @Data
    public static class CurriculumExpireDayBean {
        /**
         * id : 29
         * dicKey : curriculum_expire_day
         * value : 365
         * info : 课程有效期
         * createTime : 2020-06-17 13:39:46
         * updateTime : 2020-06-17 13:40:48
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;
    }

    @NoArgsConstructor
    @Data
    public static class BuyVipCallbackPathWxpayBean {
        /**
         * id : 27
         * dicKey : buy_vip_callback_path_wxpay
         * value : /vip/buyVipCallbackByWxPay
         * info : 购买会员微信支付
         * createTime : 2020-06-05 14:42:39
         * updateTime : 2020-06-05 14:43:20
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;
    }

    @NoArgsConstructor
    @Data
    public static class BuyVipCallbackPathAlipayBean {
        /**
         * id : 3
         * dicKey : buy_vip_callback_path_alipay
         * value : /vip/buyVipCallbackByAliPay
         * info : 购买会员支付宝回调path
         * createTime : 2020-05-26 09:55:19
         * updateTime : 2020-05-27 09:48:54
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;
    }

    @NoArgsConstructor
    @Data
    public static class AndroidVersionBean {
        /**
         * id : 31
         * dicKey : android_version
         * value : 1.0.0
         * info : 安卓版本号
         * createTime : 2020-07-01 08:25:45
         * updateTime : 2020-07-01 08:25:50
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;
    }

    @NoArgsConstructor
    @Data
    public static class AndroidContentBean {
        /**
         * id : 33
         * dicKey : android_content
         * value : 新增功能
         * info : 安卓更新内容
         * createTime : 2020-07-01 08:27:05
         * updateTime : 2020-07-01 08:27:09
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;
    }

    @NoArgsConstructor
    @Data
    public static class IosContentBean {
        /**
         * id : 36
         * dicKey : ios_content
         * value : 新增功能
         * info : ios更新内容
         * createTime : 2020-07-01 08:27:39
         * updateTime : 2020-07-01 08:28:16
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;
    }

    @NoArgsConstructor
    @Data
    public static class CallbackUrlBean {
        /**
         * id : 4
         * dicKey : callback_url
         * value : https://hx-test.kuaiyunma.com
         * info : 回调url
         * createTime : 2020-05-26 12:03:34
         * updateTime : 2020-05-27 17:35:45
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;
    }

    @NoArgsConstructor
    @Data
    public static class ImgPrivateAssetsUrlBean {
        /**
         * id : 28
         * dicKey : img_private_assets_url
         * value : https://huixing-test-assets.kuaiyunma.com
         * info : 七牛云私有域名
         * createTime : 2020-06-15 11:58:43
         * updateTime : 2020-06-15 11:59:01
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;
    }

    @NoArgsConstructor
    @Data
    public static class ImgAssetsUrlBean {
        /**
         * id : 1
         * dicKey : img_assets_url
         * value : huixing-pub-assets.kuaiyunma.com
         * info : 七牛云共有域名
         * createTime : 2020-05-21 10:55:59
         * updateTime : 2020-06-15 11:58:18
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;
    }

    @NoArgsConstructor
    @Data
    public static class AndroidUrlBean {
        /**
         * id : 32
         * dicKey : android_url
         * value : www.baidu.com
         * info : 安卓下载url
         * createTime : 2020-07-01 08:26:21
         * updateTime : 2020-07-01 08:26:25
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;
    }

    @NoArgsConstructor
    @Data
    public static class VipPriceBean {
        /**
         * id : 2
         * dicKey : vip_price
         * value : 0.1
         * info : 会员价格
         * createTime : 2020-05-25 13:53:42
         * updateTime : 2020-05-25 13:53:59
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;
    }

    @NoArgsConstructor
    @Data
    public static class LiveCurriculumExpireDayBean {
        /**
         * id : 30
         * dicKey : live_curriculum_expire_day
         * value : 365
         * info : 直播课程有效期
         * createTime : 2020-06-17 13:40:41
         * updateTime : 2020-06-17 13:42:19
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;
    }
}
