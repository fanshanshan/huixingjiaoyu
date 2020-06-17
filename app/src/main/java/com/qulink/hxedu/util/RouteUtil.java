package com.qulink.hxedu.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.qulink.hxedu.ui.zone.TopTopicActivity;

public class RouteUtil {

    //dy6239
    public static void startNewActivity(Activity context, Intent intent){
        context.startActivity(intent);
    }
    public static void startNewActivityAndResult(Activity context, Intent intent,int startCode){
        context.startActivityForResult(intent,startCode);
    }

    public static void goSubjectPage(Activity context,String topicName,int topicId,int joinNum){
        Intent intent = new Intent(context, TopTopicActivity.class);
        intent.putExtra("name", topicName);
        intent.putExtra("id", topicId);
        intent.putExtra("num", joinNum);
        context.startActivity(intent);
    }  public static void goSubjectPage(Activity context,String topicName,int topicId,int joinNum,boolean isPlatformTopic){
        Intent intent = new Intent(context, TopTopicActivity.class);
        intent.putExtra("name", topicName);
        intent.putExtra("id", topicId);
        intent.putExtra("num", joinNum);
        intent.putExtra("isPlatform", isPlatformTopic);
        context.startActivity(intent);
    }



}
