package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class BankBean {
    /**
     * id : 1
     * cardNumber : 123123
     * cardAttr : 借记卡
     * bankName : 建设银行
     * bankOnlyName : 中国建设银行
     * orgCode : 100
     * reservePhone : 15592294006
     */

    private int id;
    private String cardNumber;
    private String cardAttr;
    private String bankName;
    private String bankOnlyName;
    private String orgCode;
    private String reservePhone;
}
