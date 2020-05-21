package com.qulink.hxedu.entity;

/**
 * Created by Administrator on 2018/8/31 0031.
 */

public class MessageEvent {
        private String message;
        private int code;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public MessageEvent(String message, int code) {

        this.message = message;
        this.code = code;
    }

    public MessageEvent(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
}
