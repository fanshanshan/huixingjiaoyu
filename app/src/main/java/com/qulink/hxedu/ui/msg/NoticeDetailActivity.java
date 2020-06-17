package com.qulink.hxedu.ui.msg;

import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.qulink.hxedu.R;
import com.qulink.hxedu.entity.NoticeDetail;
import com.qulink.hxedu.entity.SystemNoticeBean;
import com.qulink.hxedu.ui.BaseActivity;
import com.zzhoujay.richtext.RichText;

import butterknife.BindView;

public class NoticeDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_notice_detail;
    }
    NoticeDetail noticeDetail;
    @Override
    protected void init() {

        setTitle("公告详情");

        noticeDetail = ( NoticeDetail)getIntent().getSerializableExtra("data");
        if(noticeDetail!=null){
            tvTitle.setText(noticeDetail.getTitle());
            tvTime.setText(noticeDetail.getCreateTime());
            RichText.from(noticeDetail.getDetail()).into(tvContent);


        }
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }
}
