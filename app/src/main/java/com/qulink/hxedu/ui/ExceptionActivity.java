package com.qulink.hxedu.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.qulink.hxedu.App;
import com.qulink.hxedu.MyActivityManager;
import com.qulink.hxedu.R;
import com.qulink.hxedu.util.DialogUtil;

public class ExceptionActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_exception;
    }

    @Override
    protected void init() {

    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }
}
