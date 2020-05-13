package com.qulink.hxedu.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.ViewPager;

import com.ortiz.touchview.TouchImageView;
import com.qulink.hxedu.R;
import com.qulink.hxedu.adapter.MyViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;


public class ImagesPreviewActivity extends BaseActivity {
    MyViewPagerAdapter viewPagerAdapter;
    private ViewPager viewPager;
    private TextView tv;
    private RelativeLayout rlBack;
    private int position;
    private int type;
    private ArrayList<String> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewPager  = (ViewPager)findViewById(R.id.viewPager);
        tv  = (TextView)findViewById(R.id.tv);
        data = getIntent().getStringArrayListExtra("data");
        position = getIntent().getIntExtra("position",0);
        tv.setText((position+1)+"/"+data.size());
        viewPagerAdapter = new MyViewPagerAdapter(data, getImageView(data), this,type);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setCurrentItem(position);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                tv.setText(position + 1 + "/" + data.size());
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_show_photo;
    }

    @Override
    protected void init() {

    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    public List<View> getImageView(List<String> selectImage) {
        List<View> list = new ArrayList<>();
        TouchImageView imageView;
        for (String s : selectImage) {
            imageView = new TouchImageView(this);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            View view = LayoutInflater.from(this).inflate(R.layout.photo_item2,null,false);
            list.add(view);
        }
        return list;
    }

}
