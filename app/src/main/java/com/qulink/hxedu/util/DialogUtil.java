package com.qulink.hxedu.util;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.qulink.hxedu.R;

public class DialogUtil {


    /**
     * 显示加载圈
     *
     * @param activity
     * @param isCover  是否需要遮罩 防止点击
     */
    public static void showLoading(Activity activity, boolean isCover) {
        if (isLoading(activity)) {
            return;
        }
        FrameLayout root = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (root != null) {
            View loadingView = LayoutInflater.from(activity).inflate(R.layout.progress_view, null);
            if (isCover) {
                //遮罩层设置点击事件，拦截底层视图的点击事件
                loadingView.findViewById(R.id.cover).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
            loadingView.findViewById(R.id.cover).setVisibility(isCover ? View.VISIBLE : View.GONE);
            root.removeView(loadingView);
            root.addView(loadingView);
        }
    }

    /**
     * 显示加载圈
     *
     * @param activity
     * @param isCover  是否需要遮罩 防止点击
     */
    public static void showLoading(Activity activity, boolean isCover,String desc) {
        if (isLoading(activity)) {
            return;
        }
        FrameLayout root = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (root != null) {
            View loadingView = LayoutInflater.from(activity).inflate(R.layout.progress_view, null);;

            TextView tvDesc = loadingView.findViewById(R.id.tv_desc);
            tvDesc.setText(desc);
            tvDesc.setVisibility(View.VISIBLE);
            if (isCover) {
                //遮罩层设置点击事件，拦截底层视图的点击事件
                loadingView.findViewById(R.id.cover).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                    }
                });
            }
            loadingView.findViewById(R.id.cover).setVisibility(isCover ? View.VISIBLE : View.GONE);
            root.removeView(loadingView);
            root.addView(loadingView);
        }
    }
    /**
     * 隐藏加载圈
     *
     * @param activity
     */
    public static void hideLoading(Activity activity) {
        FrameLayout root = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (root != null) {
            View loadingView = root.findViewById(R.id.cover_root);
            if (loadingView != null) {
                root.removeView(loadingView);
            }
        }
    }

    /**
     * 加载圈是否正在显示
     *
     * @param activity
     * @return
     */
    public static boolean isLoading(Activity activity) {
        FrameLayout root = activity.getWindow().getDecorView().findViewById(android.R.id.content);
        if (root != null) {
            View loadingView = root.findViewById(R.id.cover_root);
            return loadingView != null && root.indexOfChild(loadingView) != -1;
        }
        return false;
    }


    public static void showAlertDialog(Context context,String title,String msg,String sureTitle,DialogInterface.OnClickListener sureCallBack,String cancelTitle,DialogInterface.OnClickListener cancelCallback){
        new AlertDialog.Builder(context).setIcon(R.drawable.logo).setTitle(title)
                .setMessage(msg).setPositiveButton(sureTitle, sureCallBack).setNegativeButton( cancelTitle, cancelCallback).create().show();
    }  public static void showAlertDialog(Context context,String title,String msg,String sureTitle,DialogInterface.OnClickListener sureCallBack){
        new AlertDialog.Builder(context).setIcon(R.drawable.logo).setTitle(title)
                .setMessage(msg).setPositiveButton(sureTitle, sureCallBack).create().show();
    }

    public static void showSingleChooseDialog(Context context,String title,String[] items,DialogInterface.OnClickListener itemClickCallback){
        new AlertDialog.Builder(context).setIcon(R.drawable.logo).setTitle(title).setItems(items,itemClickCallback).create().show();
    }

}
