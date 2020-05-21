package com.qulink.hxedu;

import android.app.Activity;

import java.lang.ref.WeakReference;
import java.util.Stack;

/**
 * Created by Rock on 2017/11/24.
 */

public class MyActivityManager {
    private static MyActivityManager sInstance = new MyActivityManager();
    private WeakReference<Activity> sCurrentActivityWeakRef;
    private MyActivityManager() {

    }

    public static MyActivityManager getInstance() {
        return sInstance;
    }
    public Activity getCurrentActivity() {
        Activity currentActivity = null;
        if (sCurrentActivityWeakRef != null) {
            currentActivity = sCurrentActivityWeakRef.get();
        }
        return currentActivity;
    }
    private static Stack mActivityStack;
    public void setCurrentActivity(Activity activity) {
        sCurrentActivityWeakRef = new WeakReference<Activity>(activity);
    }
    /**
     * 推出栈顶Activity
     */
    public void popActivity(Activity activity) {
        if (activity != null) {
            activity.finish();
            mActivityStack.remove(activity);
            activity = null;
        }
    }

    /**
     * 获得当前栈顶Activity
     */
    public Activity currentActivity() {
        //lastElement()获取最后个子元素，这里是栈顶的Activity
        if(mActivityStack == null || mActivityStack.size() ==0){
            return null;
        }
        Activity activity = (Activity) mActivityStack.lastElement();
        return activity;
    }

    /**
     * 将当前Activity推入栈中
     */
    public void pushActivity(Activity activity) {
        if (mActivityStack == null) {
            mActivityStack = new Stack();
        }
        mActivityStack.add(activity);
    }

    /**
     * 弹出指定的clsss所在栈顶部的中所有Activity
     * @clsss : 指定的类
     */
    public void popTopActivitys(Class clsss) {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            if (activity.getClass().equals(clsss)) {
                break;
            }
            popActivity(activity);
        }
    }

    /**
     * 弹出栈中所有Activity
     */
    public void popAllActivitys() {
        while (true) {
            Activity activity = currentActivity();
            if (activity == null) {
                break;
            }
            popActivity(activity);
        }
    }

}