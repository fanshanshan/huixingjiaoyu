package com.qulink.hxedu.ui.live.liveroom.roomutil.commondef;

public class AudienceNotice {
    public AudienceNotice(AudienceInfo audienceInfo, String msg) {
        this.audienceInfo = audienceInfo;
        this.msg = msg;
    }

    public AudienceInfo audienceInfo;
    public String msg;
}
