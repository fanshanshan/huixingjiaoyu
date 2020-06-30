package com.qulink.hxedu.util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * @Description: 文件工具类
 * @Author: Wiggins
 * @Date: 2019/2/1 16:45
 */
public class FileUtils {

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    /**
     **
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    //获取总内存大小
    public static long getTotalMemorySize(Context context) {
        long size = 0;

        //通过读取配置文件方式获取总内大小。文件目录：/proc/meminfo
        File file = new File("/proc/meminfo");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            //根据命令行可以知道，系统总内存大小位于第一行
            String totalMemarysizeStr = reader.readLine();//MemTotal:         513744 kB
            //要获取大小，对字符串截取
            int startIndex = totalMemarysizeStr.indexOf(':');
            int endIndex = totalMemarysizeStr.indexOf('k');
            //截取
            totalMemarysizeStr = totalMemarysizeStr.substring(startIndex + 1, endIndex).trim();
            //转为long类型，得到数据单位是kb
            size = Long.parseLong(totalMemarysizeStr);
            //转为以byte为单位
            size *= 1024;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return size;
    }

    public static byte[] getBytes(String path,Context context) {


        Bitmap bitmap = null;

        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        long sb = getTotalMemorySize(context);
        newOpts.inPreferredConfig = Bitmap.Config.ARGB_8888;
        newOpts.inTempStorage = new byte[12 * 1024];//临时存储
        if(sb<3030959888l){
            Log.e("小于","dsda");
            //避免出现内存溢出的情况，进行相应的属性设置。
            newOpts.inJustDecodeBounds = false;
            int width = newOpts.outWidth;
            int height = newOpts.outHeight;
            float maxSize = 1200;
            int be = 1; if (width >= height && width > maxSize) {
                //缩放比,用高或者宽其中较大的一个数据进行计算
                be = (int) (newOpts.outWidth / maxSize); be++; }
            else if (width < height && height > maxSize) {
                be = (int) (newOpts.outHeight / maxSize); be++;
            }
            newOpts.inSampleSize = 2;//设置采样率

            newOpts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(path, newOpts);
        }else{
            newOpts.inJustDecodeBounds = false;
            bitmap = BitmapFactory.decodeFile(path, newOpts);
        }
        //读取图片 只读边,不读内容

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int options = 0;
        if(sb<3030959888l){
            options = 40;
        }else{
            options = 80;
        }
        if(bitmap==null){
            return null;
        }else{
            try {
                bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                //质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)
                while (baos.toByteArray().length > 100 * 1024) {//循环判断如果压缩后图片是否大于指定大小,大于继续压缩
                    baos.reset();
                    //重置baos即让下一次的写入覆盖之前的内容
                    options -= 5;//图片质量每次减少5
                    if (options <= 5) options = 5;//如果图片质量小于5，为保证压缩后的图片质量，图片最底压缩质量为5
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//将压缩后的图片保存到baos中
                    if (options == 5) break;//如果图片的质量已降到最低则，不再进行压缩
                }
                // LogUtil.i("size=="+baos.toByteArray().length);
                bitmap.recycle();
                return baos.toByteArray();
            }catch (OutOfMemoryError error){
                options = 70;
                try {
                    bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                    //质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)
                    while (baos.toByteArray().length > 100 * 1024) {//循环判断如果压缩后图片是否大于指定大小,大于继续压缩
                        baos.reset();
                        //重置baos即让下一次的写入覆盖之前的内容
                        options -= 10;//图片质量每次减少5
                        if (options <= 10) options = 10;//如果图片质量小于5，为保证压缩后的图片质量，图片最底压缩质量为5
                        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//将压缩后的图片保存到baos中
                        if (options == 10) break;//如果图片的质量已降到最低则，不再进行压缩
                    }
                    // LogUtil.i("size=="+baos.toByteArray().length);
                    bitmap.recycle();
                    return baos.toByteArray();
                }catch (OutOfMemoryError e){
                    options = 30;
                    try {
                        bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);
                        //质量压缩方法，把压缩后的数据存放到baos中 (100表示不压缩，0表示压缩到最小)
                        while (baos.toByteArray().length > 100 * 1024) {//循环判断如果压缩后图片是否大于指定大小,大于继续压缩
                            baos.reset();
                            //重置baos即让下一次的写入覆盖之前的内容
                            options -= 10;//图片质量每次减少5
                            if (options <= 10) options = 10;//如果图片质量小于5，为保证压缩后的图片质量，图片最底压缩质量为5
                            bitmap.compress(Bitmap.CompressFormat.JPEG, options, baos);//将压缩后的图片保存到baos中
                            if (options == 10) break;//如果图片的质量已降到最低则，不再进行压缩
                        }
                        // LogUtil.i("size=="+baos.toByteArray().length);
                        bitmap.recycle();
                        return baos.toByteArray();
                    }catch (OutOfMemoryError outOfMemoryError){
                        return null;
                    }

                }

            }

        }


    }

    public static String convertBitmapToString(byte[] bytes) {
        String s = "";
        try {
            byte[] appicon = bytes;// 转为byte数组
            s = Base64.encodeToString(appicon, Base64.NO_WRAP);
        } catch (OutOfMemoryError error) {
            s = "";
        }
        return s;
    }
}
