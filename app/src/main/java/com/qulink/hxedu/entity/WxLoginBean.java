package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class WxLoginBean {
    /**
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJleHAiOjE1OTEzNDU4MDAsInVzZXJJZCI6MTJ9.UOiYx0YjsITp972G25mJzT4plW4gA3X7JQDXkgXZh37ery7oBqf5uhFbUo4dniMO-U_YRYpQ0GhFCDKA74vvuw
     * bindPhoneStatus : 0
     */

    private String token;
    private int bindPhoneStatus;
}
