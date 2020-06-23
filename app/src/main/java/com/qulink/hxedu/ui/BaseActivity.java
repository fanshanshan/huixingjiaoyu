package com.qulink.hxedu.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.util.ScreenUtil;

import butterknife.ButterKnife;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public abstract class BaseActivity extends SwipeBackActivity implements View.OnClickListener {

    ImageView ivBack;
    TextView status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_base);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //PushAgent.getInstance(this).onAppStart();
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//        bar = (TextView)findViewById(R.id.bar);
//
//        if(bar!=null){
//            bar.setHeight(SystemUtil.getStatusHeight(this));
//        }
        setContentView(getLayout());
        ButterKnife.bind(this);

        getSwipeBackLayout().setEnableGesture(enableGestureBack());//子activity是否允许滑动退出
        init();
        ivBack = (ImageView) findViewById(R.id.iv_back);
        status = (TextView) findViewById(R.id.status);
        if (ivBack != null) {
            ivBack.setOnClickListener(this::onClick);
        }
        if (status != null) {
            status.setHeight(ScreenUtil.getStatusHeight(this));
        }

    }

    abstract protected int getLayout();

    abstract protected void init();

    abstract protected boolean enableGestureBack();

    protected void setTitle(String title) {
        TextView tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
    }

    protected void setRightTitle(String title) {
        TextView tvRight = (TextView) findViewById(R.id.tv_bar_right);
        if (tvRight != null) {
            tvRight.setText(title);
        }
    } protected void setRightTitle(String title,int color) {
        TextView tvRight = (TextView) findViewById(R.id.tv_bar_right);
        if (tvRight != null) {
            tvRight.setText(title);
            tvRight.setTextColor(color);
        }
    }
    protected void setBackImg(int img) {
        ImageView ivBack = (ImageView) findViewById(R.id.iv_back);
        if (ivBack != null) {
            ivBack.setImageResource(img);
        }
    }
    protected void setBarTtxtColors(int color) {
        TextView tvTitle = (TextView) findViewById(R.id.tv_bar_title);
        if (tvTitle != null) {
            tvTitle.setTextColor(color);
        }

        TextView tvRight = (TextView) findViewById(R.id.tv_bar_right);
        if (tvRight != null) {
            tvRight.setTextColor(color);
        }
    }

    protected void setBarBg(int color) {
        LinearLayout llBar = (LinearLayout) findViewById(R.id.ll_bar);
        if (llBar != null) {
            llBar.setBackgroundColor(color);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                FrameLayout root = getWindow().getDecorView().findViewById(android.R.id.content);
                if (root != null) {
                    View loadingView = root.findViewById(R.id.cover_root);
                    if (loadingView != null) {
                        root.removeView(loadingView);
                    } else {
                        finish();
                    }
                } else {
                    finish();
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            FrameLayout root = getWindow().getDecorView().findViewById(android.R.id.content);
            if (root != null) {
                View loadingView = root.findViewById(R.id.cover_root);
                if (loadingView != null) {
                    root.removeView(loadingView);
                } else {
                    finish();
                }
            } else {
                finish();
            }
        }
        return true;
    }

    protected void hideEmpty(){
        View view = findViewById(R.id.ll_empty);
        if(view!=null){
            view.setVisibility(View.GONE);
        }
    }
    protected void showEmpty(){
        View view = findViewById(R.id.ll_empty);
        if(view!=null){
            view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        App.getInstance().setCurrentActivity(this);
    }
}
