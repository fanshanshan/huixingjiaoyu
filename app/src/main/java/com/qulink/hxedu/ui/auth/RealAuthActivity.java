package com.qulink.hxedu.ui.auth;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.OcrBean;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
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

public class RealAuthActivity extends BaseActivity {
    private final int REQUEST_CUT_CODE = 2;

    private final int REQUEST_CAMERA_CODE = 3;
    @BindView(R.id.iv_open_camera)
    ImageView ivOpenCamera;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_card)
    TextView tvCard;
    @BindView(R.id.iv_card)
    ImageView ivCard;

    private int sex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_real_auth;
    }

    @Override
    protected void init() {
        setTitle("实名认证");
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick({R.id.tv_submit, R.id.iv_open_camera})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_submit:
                realAuth();
                break;
            case R.id.iv_open_camera:
                PermissionUtils.checkAndRequestMorePermissions(RealAuthActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERA_CODE, new PermissionUtils.PermissionRequestSuccessCallBack() {
                    @Override
                    public void onHasPermission() {
                        openCamera();

                    }
                });
                break;
        }
    }

    Uri mTmpFile, cutUri;

    private void openCamera() {
        // 跳转到系统照相机
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // 设置系统相机拍照后的输出路径
            // 创建临时文件
            String savePath = FinalValue.IMG_SAVE_PATH + "/" + (System.currentTimeMillis() + SystemUtil.getRandomString2(12)) + ".jpg";
            mTmpFile = getUriForFile(getApplicationContext(),
                    new File(savePath));

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

    private void realAuth() {
        if (TextUtils.isEmpty(tvName.getText().toString())) {
            ToastUtils.show(this, "请上传身份证正面照");
            return;
        }
        if (TextUtils.isEmpty(tvCard.getText().toString())) {
            ToastUtils.show(this, "请上传身份证正面照");
            return;
        }
        if (TextUtils.isEmpty(imgKey)) {
            ToastUtils.show(this, "请上传身份证正面照");
            return;
        }
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().realAuth(tvName.getText().toString(), tvCard.getText().toString(), sex + "", imgKey, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(RealAuthActivity.this);
                DialogUtil.showAlertDialog(RealAuthActivity.this, "提示", "恭喜您，实名认证成功", "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        App.getInstance().getUserInfo(RealAuthActivity.this, new UserInfoCallback() {
                            @Override
                            public void getUserInfo(UserInfo userInfo) {
                                userInfo.setRealAuthStatus(1);
                            }
                        });
                        dialog.dismiss();
                        finish();
                    }
                });
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(RealAuthActivity.this);
                ToastUtils.show(RealAuthActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(RealAuthActivity.this);
                ToastUtils.show(RealAuthActivity.this, expectionMsg);

            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == REQUEST_CUT_CODE && resultCode == RESULT_OK) {
            //裁剪成功
            //  getQiniuToken();


            Glide.with(RealAuthActivity.this).load(cutUri).into(ivCard);
            getQiniuToken();

        } else if (requestCode == REQUEST_CAMERA_CODE && resultCode == RESULT_OK) {
            //请求相机成功
            startPhotoZoom(mTmpFile);
        }
    }


    private void startPhotoZoom(Uri uri) {
        File CropPhoto = new File(getExternalCacheDir(), SystemUtil.getRandomString2(32) + "Crop.jpg");//这个是创建一个截取后的图片路径和名称。
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


    void getQiniuToken() {
        DialogUtil.showLoading(this, true);
        ApiUtils.getInstance().getQiniuToken(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(RealAuthActivity.this);
                uploadHeadImg(t.getData().toString());
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(RealAuthActivity.this);
                ToastUtils.show(RealAuthActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(RealAuthActivity.this);
                ToastUtils.show(RealAuthActivity.this, expectionMsg);

            }
        });
    }

    private String imgKey;

    void uploadHeadImg(String token) {
        DialogUtil.showLoading(this, true, "正在上传图片");
        String key = SystemUtil.getUploadImageKey(this);
        QiniuUtil.getInstance().put(cutUri.getPath(), key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, final JSONObject res) {
                        DialogUtil.hideLoading(RealAuthActivity.this);
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            try {
                                imgKey = res.getString("key");
                                ocr(imgKey);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            DialogUtil.hideLoading(RealAuthActivity.this);
                            ToastUtils.show(RealAuthActivity.this, info.error);
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
    }

    private void ocr(String path) {
        DialogUtil.showLoading(this, true, "正在识别证件");
        App.getInstance().getDefaultSetting(this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                String url = ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), path);
                ApiUtils.getInstance().ocr(url, new ApiCallback() {
                    @Override
                    public void success(ResponseData t) {
                        DialogUtil.hideLoading(RealAuthActivity.this);
                        OcrBean ocrBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), OcrBean.class);
                        tvName.setText(ocrBean.getName());
                        tvCard.setText(ocrBean.getIdNum());
                        sex = (ocrBean.getSex().equals("男") ? 1 : 0);
                    }

                    @Override
                    public void error(String code, String msg) {
                        DialogUtil.hideLoading(RealAuthActivity.this);
                        ToastUtils.show(RealAuthActivity.this, "请上传身份证正面照");
                    }

                    @Override
                    public void expcetion(String expectionMsg) {
                        DialogUtil.hideLoading(RealAuthActivity.this);
                        ToastUtils.show(RealAuthActivity.this, "请上传身份证正面照");

                    }
                });
            }
        });
    }

    private void submit() {

    }

}
