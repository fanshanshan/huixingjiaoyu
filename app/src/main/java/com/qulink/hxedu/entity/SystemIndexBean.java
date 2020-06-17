package com.qulink.hxedu.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class SystemIndexBean {
    /**
     * umrStatus : true
     * cmrStatus : false
     * lmrStatus : false
     * amrStatus : false
     */

    private boolean umrStatus;
    private boolean cmrStatus;
    private boolean lmrStatus;
    private boolean amrStatus;
}
