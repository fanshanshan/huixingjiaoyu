package com.qulink.hxedu;


/**
 *  @author x024
 */

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.qulink.hxedu.ui.ExceptionActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;

/**
 * 收集错误报告并上传到服务器
 *
 * @author x024
 *
 */
public class CrashHanlder implements UncaughtExceptionHandler {
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    // CrashHandler实例
    private static CrashHanlder INSTANCE = new CrashHanlder();
    // 程序的Context对象
    private Context mContext;

    private CrashHanlder() {
    }

    public static CrashHanlder getInstance() {
        return INSTANCE;
    }

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Context context) {
        mContext = context;
        // 获取系统默认的UncaughtException处理
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 设置该CrashHandler为程序的默认处理
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /**
     * 异常捕获
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        if (!handleException(ex) && mDefaultHandler != null) {
            // 如果用户没有处理则让系统默认的异常处理器来处
            mDefaultHandler.uncaughtException(thread, ex);
        } else {
            RouteUtil.startNewActivity(MyActivityManager.getInstance().currentActivity(),new Intent(mContext, ExceptionActivity.class));
        }
    }

    /**
     * 自定义错误捕获
     *
     * @param ex
     * @return true:如果处理了该异常信息;否则返回false.
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return false;
        }

        // 收集错误信息
        getCrashInfo(ex);

        return true;
    }

    /**
     * 收集错误信息
     *
     * @param ex
     */
    private void getCrashInfo(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String errorMessage = writer.toString();
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            String mFilePath = Environment.getExternalStorageDirectory() + "/" + App.ERROR_FILENAME;
//            FileTxt.WirteTxt(mFilePath, FileTxt.ReadTxt(mFilePath) + '\n' + errorMessage);
//        } else {
//            Log.i(App.TAG, "哦豁，说好的SD呢...");
//        }

    }

}

