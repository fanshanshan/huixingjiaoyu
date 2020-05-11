package com.qulink.hxedu.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.qulink.hxedu.R;

import me.imid.swipebacklayout.lib.app.SwipeBackActivity;

public abstract  class BaseActivity extends SwipeBackActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_base);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //PushAgent.getInstance(this).onAppStart();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //去掉Activity上面的状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

//        bar = (TextView)findViewById(R.id.bar);
//
//        if(bar!=null){
//            bar.setHeight(SystemUtil.getStatusHeight(this));
//        }
        setContentView(getLayout());
        getSwipeBackLayout().setEnableGesture(enableGestureBack());//子activity是否允许滑动退出
        init();
    }

    abstract protected int getLayout();
    abstract protected void init();

    abstract  protected  boolean enableGestureBack();




}
