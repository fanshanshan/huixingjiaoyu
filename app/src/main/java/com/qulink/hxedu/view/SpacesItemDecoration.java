package com.qulink.hxedu.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int rightSpace;
    private int bottomSpace;

    public SpacesItemDecoration(int rightSpace,int bottomSpace) {
        this.bottomSpace = bottomSpace;
        this.rightSpace = rightSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        //outRect.left = space;
        //outRect.right = space;
        outRect.bottom = bottomSpace;
        outRect.right = rightSpace;


        // Add top margin only for the first item to avoid double space between items
        //  outRect.top = space;
    }
}