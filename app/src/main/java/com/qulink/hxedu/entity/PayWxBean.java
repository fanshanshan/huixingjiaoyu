package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class PayWxBean {
    /**
     * package : Sign=WXPay
     * appid : wx9c863272e5ed7596
     * sign : 21B9700EFD78E0893D643C4A472877D7B9339E76199378847825A323AD9DC09B
     * partnerid : 1597973181
     * prepayid : wx1813544719719090d3e17e581294701600
     * noncestr : 1592459687233
     * timestamp : 1592459687
     */

    private String packageX;
    private String appid;
    private String sign;
    private String partnerid;
    private String prepayid;
    private String noncestr;
    private String timestamp;
}
