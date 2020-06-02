package com.qulink.hxedu.entity;

import android.widget.EditText;

public class EdittextAndDesc {
    private EditText editText;
    private String desc;
    private int id;

    public EdittextAndDesc(String desc, int id) {
        this.desc = desc;
        this.id = id;
    }

    public EdittextAndDesc(EditText editText, String desc, int id) {
        this.editText = editText;
        this.desc = desc;
        this.id = id;
    }

    public EditText getEditText() {
        return editText;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
