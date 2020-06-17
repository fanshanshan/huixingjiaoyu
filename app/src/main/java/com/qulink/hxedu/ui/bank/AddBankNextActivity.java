package com.qulink.hxedu.ui.bank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.qulink.hxedu.R;
import com.qulink.hxedu.ui.BaseActivity;

public class AddBankNextActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_add_bank_next;
    }

    @Override
    protected void init() {

    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }
}
