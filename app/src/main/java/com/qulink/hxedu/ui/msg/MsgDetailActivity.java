package com.qulink.hxedu.ui.msg;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.qulink.hxedu.R;
import com.qulink.hxedu.ui.BaseActivity;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;

public class MsgDetailActivity extends BaseActivity {

    String title;
    String content;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        tvContent.setText(Html.fromHtml(content,Html.FROM_HTML_MODE_COMPACT));
        RichText.from(content).into(tvContent);


        setTitle(title);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_msg_detail;
    }

    @Override
    protected void init() {

    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }
}
