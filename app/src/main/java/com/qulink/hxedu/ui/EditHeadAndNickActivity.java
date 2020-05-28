package com.qulink.hxedu.ui;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.PermissionUtils;
import com.qulink.hxedu.util.QiniuUtil;
import com.qulink.hxedu.util.SystemUtil;
import com.qulink.hxedu.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

public class EditHeadAndNickActivity extends BaseActivity {

    @BindView(R.id.rl_head)
    RelativeLayout rlHead;
    @BindView(R.id.et_nick)
    EditText etNick;

    private final int REQUEST_ALBUM_CODE = 1;
    private final int REQUEST_CUT_CODE = 2;
    private final int REQUEST_CAMERA_CODE = 3;
    private final int REQUEST_CAMERA_PERMISSION_CODE = 4;
    @BindView(R.id.iv_head)
    CircleImageView ivHead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_edit_head_and_nick;
    }

    @Override
    protected void init() {

        setTitle(getString(R.string.edit_info));
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                etNick.setText(userInfo.getNickename());
                etNick.setSelection(userInfo.getNickename().length());
            }
        });
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick({R.id.rl_head,R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_head:
                showChooseDialog();
                break;
            case R.id.tv_save:
                save();
                break;
        }
    }

    void showChooseDialog() {
        String[] items = new String[]{"相机", "相册"};
        DialogUtil.showSingleChooseDialog(this, "选择方式", items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    PermissionUtils.checkAndRequestMorePermissions(EditHeadAndNickActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE, new PermissionUtils.PermissionRequestSuccessCallBack() {
                        @Override
                        public void onHasPermission() {
                            openCamera();

                        }
                    });

                } else if (which == 1) {

                    showAlum();
                }
            }
        });
    }

    Uri mTmpFile, cutUri;

    private void openCamera() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            mTmpFile = getUriForFile(getApplicationContext(),
                    new File(FinalValue.IMG_SAVE_PATH + "/" + (System.currentTimeMillis() + SystemUtil.getRandomString2(12)) + ".jpg"));

            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mTmpFile);
            startActivityForResult(cameraIntent, REQUEST_CAMERA_CODE);
        }

    }

    private static Uri getUriForFile(Context context, File file) {
        if (context == null || file == null) {
            throw new NullPointerException();
        }
        Uri uri;
        if (Build.VERSION.SDK_INT >= 24) {
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.qulink.hxedu.fileprovider", file);
        } else {
            uri = Uri.fromFile(file);
        }
        return uri;
    }


    private void showAlum() {
        Intent albumIntent = new Intent(Intent.ACTION_PICK, null);
        albumIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        startActivityForResult(albumIntent, REQUEST_ALBUM_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        Log.e("结果",resultCode+"");

        if (requestCode == REQUEST_ALBUM_CODE && resultCode == RESULT_OK) {
            //获取相册图片成功
            startPhotoZoom(data.getData());
        } else if (requestCode == REQUEST_CUT_CODE && resultCode == RESULT_OK) {
            //裁剪成功
          //  getQiniuToken();
            Glide.with(EditHeadAndNickActivity.this).load(cutUri).into(ivHead);
        } else if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK) {
            //请求相机成功
            startPhotoZoom(mTmpFile);
        }
    }

    private void startPhotoZoom(Uri uri) {
        File CropPhoto = new File(getExternalCacheDir(), SystemUtil.getRandomString2(32)+"Crop.jpg");//这个是创建一个截取后的图片路径和名称。
        try {
            if (CropPhoto.exists()) {
                CropPhoto.delete();
            }
            CropPhoto.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        cutUri = Uri.fromFile(CropPhoto);
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }
        intent.putExtra("crop", "true");
        intent.putExtra("scale", true);

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        //输出的宽高

        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);

        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cutUri);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        startActivityForResult(intent, REQUEST_CUT_CODE);//这里的RESULT_REQUEST_CODE是在startActivityForResult里使用的返回值。
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {
            // 权限申请成功
            openCamera();
        }
    }

    void getQiniuToken(){
        DialogUtil.showLoading(this,true);
        ApiUtils.getInstance().getQiniuToken(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(EditHeadAndNickActivity.this);
                uploadHeadImg(t.getData().toString());
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(EditHeadAndNickActivity.this);
                ToastUtils.show(EditHeadAndNickActivity.this,msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(EditHeadAndNickActivity.this);
                ToastUtils.show(EditHeadAndNickActivity.this,expectionMsg);

            }
        });
    }

    void uploadHeadImg(String token){
        DialogUtil.showLoading(this,true,"正在上传头像");
        QiniuUtil.getInstance().put(cutUri.getPath(), "", token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info,final JSONObject res) {
                        DialogUtil.hideLoading(EditHeadAndNickActivity.this);
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if(info.isOK()) {
                            try {
                                updateUserInfo(etNick.getText().toString(), res.getString("hash"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            DialogUtil.hideLoading(EditHeadAndNickActivity.this);
                            ToastUtils.show(EditHeadAndNickActivity.this,"上传失败");
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
    }


    String qiniuToken;
    void save(){
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                if(cutUri==null&&etNick.getText().toString().equals(userInfo.getNickename())){
                    updateUserInfo(etNick.getText().toString(),userInfo.getHeadImg());
                }else{
                    if(cutUri==null){
                        App.getInstance().getUserInfo(EditHeadAndNickActivity.this, new UserInfoCallback() {
                            @Override
                            public void getUserInfo(UserInfo userInfo) {
                                if(userInfo!=null){
                                    updateUserInfo(etNick.getText().toString(),userInfo.getHeadImg());
                                }
                            }
                        });
                    }else{
                        if(qiniuToken==null){
                            getQiniuToken();
                        }else{
                            uploadHeadImg(qiniuToken);
                        }
                    }
                }
            }
        });


    }

    void updateUserInfo(String nickName,String headimgPath){
        DialogUtil.showLoading(this,true,"正在提交信息");
        ApiUtils.getInstance().updateUserInfo(nickName, headimgPath, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
             DialogUtil.hideLoading(EditHeadAndNickActivity.this);
             //保存成功 用户信息置空  下次就会获取最新的
                App.getInstance().setUserInfo(null);
             ToastUtils.show(EditHeadAndNickActivity.this,"保存成功");
             finish();
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(EditHeadAndNickActivity.this);
                ToastUtils.show(EditHeadAndNickActivity.this,msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(EditHeadAndNickActivity.this);
                ToastUtils.show(EditHeadAndNickActivity.this,expectionMsg);
            }
        });
    }
}
