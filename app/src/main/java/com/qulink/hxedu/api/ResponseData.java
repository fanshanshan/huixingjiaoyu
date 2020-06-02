package com.qulink.hxedu.api;

public class ResponseData <T>{

    private String data;

    public ResponseData(String data, String code, int count, String msg) {
        this.data = data;
        this.code = code;
        this.count = count;
        this.msg = msg;
    }

    public ResponseData() {
    }

    private String code;
    private int count;

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    private String msg;
}
