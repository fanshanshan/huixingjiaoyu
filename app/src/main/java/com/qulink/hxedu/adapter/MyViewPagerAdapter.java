package com.qulink.hxedu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.qulink.hxedu.R;
import com.qulink.hxedu.util.ScreenUtil;
import com.qulink.hxedu.view.TouchImageView;


import java.util.List;



/**
 * Created by Rock on 2017/8/30.
 */

public class MyViewPagerAdapter extends PagerAdapter {
    private List<String> mdata;
    private Context context;
    public MyViewPagerAdapter(List<String> mdata, Context context) {
        this.mdata = mdata;
        this.context = context;
    }

    public MyViewPagerAdapter(List<String> mdata, List<View> views, Context context, int type) {
        this.mdata = mdata;
        this.context = context;
    }

    @Override

    public int getCount() {
        return mdata.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        View view = inflater.inflate(R.layout.preview_layout, container, false);

        TouchImageView imageViewPreview =  view.findViewById(R.id.iv);


        Glide.with(context).load(mdata.get(position)).into(imageViewPreview);
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

       container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE ;
    }
}
