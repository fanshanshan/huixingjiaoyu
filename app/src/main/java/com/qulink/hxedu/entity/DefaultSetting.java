package com.qulink.hxedu.entity;

public class DefaultSetting {


    /**
     * callback_url : {"id":4,"dicKey":"callback_url","value":"http://natapp.deveye.cn","info":"回调url","createTime":"2020-05-26 12:03:34","updateTime":"2020-05-27 16:50:13"}
     * img_assets_url : {"id":1,"dicKey":"img_assets_url","value":"https://huixing-test-assets.kuaiyunma.com","info":"图片域名","createTime":"2020-05-21 10:55:59","updateTime":"2020-05-21 10:56:13"}
     * buy_vip_callback_path_alipay : {"id":3,"dicKey":"buy_vip_callback_path_alipay","value":"/vip/buyVipCallbackByAliPay","info":"购买会员支付宝回调path","createTime":"2020-05-26 09:55:19","updateTime":"2020-05-27 09:48:54"}
     * vip_price : {"id":2,"dicKey":"vip_price","value":"0.1","info":"会员价格","createTime":"2020-05-25 13:53:42","updateTime":"2020-05-25 13:53:59"}
     */

    private CallbackUrlBean callback_url;
    private ImgAssetsUrlBean img_assets_url;
    private BuyVipCallbackPathAlipayBean buy_vip_callback_path_alipay;
    private VipPriceBean vip_price;

    public CallbackUrlBean getCallback_url() {
        return callback_url;
    }

    public void setCallback_url(CallbackUrlBean callback_url) {
        this.callback_url = callback_url;
    }

    public ImgAssetsUrlBean getImg_assets_url() {
        return img_assets_url;
    }

    public void setImg_assets_url(ImgAssetsUrlBean img_assets_url) {
        this.img_assets_url = img_assets_url;
    }

    public BuyVipCallbackPathAlipayBean getBuy_vip_callback_path_alipay() {
        return buy_vip_callback_path_alipay;
    }

    public void setBuy_vip_callback_path_alipay(BuyVipCallbackPathAlipayBean buy_vip_callback_path_alipay) {
        this.buy_vip_callback_path_alipay = buy_vip_callback_path_alipay;
    }

    public VipPriceBean getVip_price() {
        return vip_price;
    }

    public void setVip_price(VipPriceBean vip_price) {
        this.vip_price = vip_price;
    }

    public static class CallbackUrlBean {
        /**
         * id : 4
         * dicKey : callback_url
         * value : http://natapp.deveye.cn
         * info : 回调url
         * createTime : 2020-05-26 12:03:34
         * updateTime : 2020-05-27 16:50:13
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDicKey() {
            return dicKey;
        }

        public void setDicKey(String dicKey) {
            this.dicKey = dicKey;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

    public static class ImgAssetsUrlBean {
        /**
         * id : 1
         * dicKey : img_assets_url
         * value : https://huixing-test-assets.kuaiyunma.com
         * info : 图片域名
         * createTime : 2020-05-21 10:55:59
         * updateTime : 2020-05-21 10:56:13
         */

        private int id;
        private String dicKey;
        private String value;
        private String info;
        private String createTime;
        private String updateTime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDicKey() {
            return dicKey;
        }

        public void setDicKey(String dicKey) {
            this.dicKey = dicKey;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDicKey() {
            return dicKey;
        }

        public void setDicKey(String dicKey) {
            this.dicKey = dicKey;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }

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

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDicKey() {
            return dicKey;
        }

        public void setDicKey(String dicKey) {
            this.dicKey = dicKey;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getInfo() {
            return info;
        }

        public void setInfo(String info) {
            this.info = info;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(String updateTime) {
            this.updateTime = updateTime;
        }
    }
}
