package com.qulink.hxedu.ui;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.qulink.hxedu.R;

import butterknife.BindView;

public class RichTextActivity extends BaseActivity {

    String title;
    String content;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getIntent().getStringExtra("title");
        content = getIntent().getStringExtra("content");
        tvContent.setText(Html.fromHtml(content,Html.FROM_HTML_MODE_COMPACT)
        );
        tvTitle.setText(title);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_rich_text;
    }

    @Override
    protected void init() {

    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }
}
