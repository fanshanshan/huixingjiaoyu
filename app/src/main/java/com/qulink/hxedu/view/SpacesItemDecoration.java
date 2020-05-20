package com.qulink.hxedu.view;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int rightSpace;
    private int bottomSpace;

    private int firstLeftSpace;
    private int firstTopSpace;
    public SpacesItemDecoration(int rightSpace,int bottomSpace,int firstLeftSpace,int firstTopSpace) {
        this.bottomSpace = bottomSpace;
        this.rightSpace = rightSpace;
        this.firstLeftSpace = firstLeftSpace;
        this.firstTopSpace = firstTopSpace;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        //outRect.left = space;
        //outRect.right = space;
        outRect.bottom = bottomSpace;
        outRect.right = rightSpace;

        if (parent.getChildPosition(view) == 0){
            outRect.left=firstLeftSpace;
            outRect.top=firstTopSpace;
        }
        // Add top margin only for the first item to avoid double space between items
        //  outRect.top = space;
    }
}