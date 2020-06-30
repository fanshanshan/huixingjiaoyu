package com.qulink.hxedu.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

import com.qulink.hxedu.BuildConfig;
import com.qulink.hxedu.R;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.PrefUtils;
import com.qulink.hxedu.util.ToastUtils;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;


/**
 * Created by Administrator on 2018/9/19 0019.
 */
public class LoadingService extends IntentService {
    NotificationManager nm;
    private String url, path;

    public LoadingService(String name) {
        super(name);
    }

    public LoadingService() {
        super("MyService");

    }


    public static void startUploadImg(Context context) {
        Intent intent = new Intent(context, LoadingService.class);
        context.startService(intent);
    }


    public void onCreate() {
        super.onCreate();


    }


    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        updateApk();
    }


    private void updateApk() {
        final String path = PrefUtils.getString(this, "apk_path", "");
        RequestParams params = new RequestParams(PrefUtils.getString(this, "apk_url", ""));
        params.setSaveFilePath(path);
        x.http().get(params, new Callback.ProgressCallback<File>() {

            @Override
            public void onSuccess(File result) {
                ToastUtils.show(LoadingService.this, "下载完成");
                installApk(path);
                stopSelf();
//                closeNoti();
//                beginInstall(path);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Log.e("下载失败", ex.toString());
                ToastUtils.show(LoadingService.this, "下载失败");
            }

            @Override
            public void onCancelled(CancelledException cex) {
            }

            @Override
            public void onFinished() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onStarted() {
                ToastUtils.show(LoadingService.this, "正在后台下载...");
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                //sendNotifi((int) total, (int) current);
            }
        });

    }

    /**
     * 安装下载的新版本
     */
    protected void installApk(String loadPath) {
        File file = new File(loadPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);

        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //
            // 判断版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            Uri uri = Uri.fromFile(file);

            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        this.startActivity(intent);


    }

    private void sendNotifi(final int total, final int current) {
        NotificationChannel channel = null;
        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(PUSH_CHANNEL_ID,PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.RED); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            builder = new NotificationCompat.Builder(this,PUSH_CHANNEL_ID);
            getNotificationManager().createNotificationChannel(channel);
        }else{
            builder = new NotificationCompat.Builder(this);
        }

        Intent i = new Intent(this, LoadingService.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(this, 0, i,
                PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(intent);

        //通知栏未展开时显示的小图标
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        Notification notification = builder.build();
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        // 表明在点击了通知栏中的"清除通知"后，此通知不清除，经常与FLAG_ONGOING_EVENT一起使用
        notification.flags |= Notification.FLAG_NO_CLEAR;
        RemoteViews contentView = new RemoteViews(getPackageName(),
                R.layout.notification_item);
        contentView.setProgressBar(R.id.seek, total, current, false);

        notification.contentView = contentView;
        getNotificationManager().notify(NOTIFICATION_CODE, notification);
    }

    private void closeNoti() {
        getNotificationManager().cancel(NOTIFICATION_CODE);
    }

    private  NotificationManager getNotificationManager(){
        if(manager==null){
            manager =(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return manager;
    }
    int NOTIFICATION_CODE = 5233;
    int NOTIFICATION_OPEN_CODE = 521;
    private static final int PUSH_NOTIFICATION_ID = (0x001);
    private static final String PUSH_CHANNEL_ID = "PUSH_NOTIFY_ID";
    private static final String PUSH_CHANNEL_NAME = "PUSH_NOTIFY_NAME";
    private NotificationManager manager;
    private void beginInstall(String loadpath) {
        NotificationChannel channel = null;
        NotificationCompat.Builder builder;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(PUSH_CHANNEL_ID,PUSH_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.RED); //小红点颜色
            channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
            builder = new NotificationCompat.Builder(this,PUSH_CHANNEL_ID);
            getNotificationManager().createNotificationChannel(channel);
        }else{
            builder = new NotificationCompat.Builder(this);
        }
        // 创建一个开启安装App界面的意图
        Intent installIntent = new Intent(Intent.ACTION_VIEW);

        File file = new File(loadpath);

        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //
            // 判断版本是否在7.0以上
            Uri apkUri = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            installIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            installIntent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            installIntent.addCategory(Intent.CATEGORY_DEFAULT);
            Uri uri = Uri.fromFile(file);
            installIntent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        installIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        // 创建一个Notification并设置相关属性
        builder.setAutoCancel(true)
        .setShowWhen(true).setSmallIcon(R.mipmap.ic_launcher).setContentTitle("千音") .setContentText("下载完成,点击安装");//设置通知的内容
        // 创建PendingIntent，用于点击通知栏后实现的意图操作
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, installIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentIntent(pendingIntent);
        builder.setDefaults(Notification.DEFAULT_SOUND);
        Notification notification = builder.build();
        // 设置为默认的声音
        //notification.flags =  Notification.FLAG_NO_CLEAR;
        getNotificationManager().notify(NOTIFICATION_OPEN_CODE, notification);// 显示通知

    }
}
