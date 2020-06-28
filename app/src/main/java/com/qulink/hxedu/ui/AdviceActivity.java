package com.qulink.hxedu.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.PhotoUploadBean;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.QiniuUtil;
import com.qulink.hxedu.util.ScreenUtil;
import com.qulink.hxedu.util.SystemUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.SpacesItemDecoration;
import com.scrat.app.selectorlibrary.ImageSelector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class AdviceActivity extends BaseActivity {

    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.tv_input_number)
    TextView tvInputNumber;
    @BindView(R.id.recycle)
    RecyclerView recycle;
    private int maxContentSize = 251;
    private int CHOOSE_PHOTO_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void addEditListener() {
        etContent.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum = s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                //TextView显示剩余字数
                tvInputNumber.setText(wordNum.length() + "/" + maxContentSize);
                selectionStart = etContent.getSelectionStart();
                selectionEnd = etContent.getSelectionEnd();
                if (wordNum.length() > maxContentSize) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    etContent.setText(s);
                    etContent.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_advice;
    }

    private int maxCount = 3;

    private int itemHeight;

    @Override
    protected void init() {
        setTitle("需求收集");
        itemHeight = (int) (ScreenUtil.getScreenWidth(this) - ScreenUtil.dip2px(this, 30) - 24) / 3;
        initRecycle();
        addEditListener();
    }

    private CommonRcvAdapter adapter;
    private List selectImageList;

    private void initRecycle() {
        selectImageList = new ArrayList<>();
        selectImageList.add("footer");
        adapter = new CommonRcvAdapter(selectImageList) {
            ImageView ivImage;
            ImageView ivDelete;
            ImageView ivAdd;

            @Override
            public Object getItemType(Object s) {

                return s.toString();
            }

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                if (type.toString().equals("footer")) {
                    return new AdapterItem() {
                        @Override
                        public int getLayoutResId() {
                            return R.layout.select_photo;
                        }

                        @Override
                        public void bindViews(@NonNull View root) {
                            ivAdd = root.findViewById(R.id.iv_add);
                            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                            layoutParams.height = itemHeight;
                            layoutParams.width = itemHeight;
                        }

                        @Override
                        public void setViews() {
                            ivAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ImageSelector.show(AdviceActivity.this, CHOOSE_PHOTO_CODE, maxCount - selectImageList.size()+1 );

                                }
                            });
                        }

                        @Override
                        public void handleData(Object o, int position) {

                        }
                    };
                } else {
                    return new AdapterItem<PhotoUploadBean>() {
                        @Override
                        public int getLayoutResId() {
                            return R.layout.image_select_item;
                        }

                        @Override
                        public void bindViews(@NonNull View root) {
                            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                            layoutParams.height = itemHeight;
                            layoutParams.width = itemHeight;
                            ivDelete = root.findViewById(R.id.iv_delete);
                            ivImage = root.findViewById(R.id.iv_image);
                        }

                        @Override
                        public void setViews() {

                        }

                        @Override
                        public void handleData(PhotoUploadBean s, int position) {
                            Glide.with(AdviceActivity.this).load(s.getLocalPath()).into(ivImage);
                            ivDelete.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    selectImageList.remove(s);
                                    refreshAdapter();
                                }
                            });
                        }
                    };

                }
            }
        };
        recycle.setAdapter(adapter);
        recycle.setLayoutManager(new GridLayoutManager(this, 3));
        recycle.addItemDecoration(new SpacesItemDecoration(12, 12, 0, 0));
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == CHOOSE_PHOTO_CODE) {
                final List<String> resultData = data.getStringArrayListExtra("data");
                if (resultData.isEmpty()) {
                    return;
                }
                selectImageList.remove(selectImageList.size() - 1);

                PhotoUploadBean photoUploadBean;
                for (String s : resultData) {
                    photoUploadBean = new PhotoUploadBean();
                    photoUploadBean.setLocalPath(s);
                    selectImageList.add(photoUploadBean);

                }
                refreshAdapter();
            }


        }
    }

    private void submit() {
        if (needUploadPhoto()) {
            getQiniuToken();
        } else {
            DialogUtil.showLoading(this, true, "正在提交");
            ApiUtils.getInstance().advice(etContent.getText().toString(), getImagePath(), new ApiCallback() {
                @Override
                public void success(ResponseData t) {
                    DialogUtil.hideLoading(AdviceActivity.this);
                    ToastUtils.show(AdviceActivity.this, "提交成功");
                    finish();

                }

                @Override
                public void error(String code, String msg) {
                    DialogUtil.hideLoading(AdviceActivity.this);
                    ToastUtils.show(AdviceActivity.this, msg);

                }

                @Override
                public void expcetion(String expectionMsg) {
                    DialogUtil.hideLoading(AdviceActivity.this);
                    ToastUtils.show(AdviceActivity.this, expectionMsg);
                }
            });
        }
    }

    private void upload(String path, String token) {
        String key = SystemUtil.getUploadImageKey(this);
        DialogUtil.showLoading(this,true,"正在上传图片");
        QiniuUtil.getInstance().put(path, key, token,
                new UpCompletionHandler() {
                    @Override
                    public void complete(String key, ResponseInfo info, final JSONObject res) {
                        DialogUtil.hideLoading(AdviceActivity.this);
                        //res包含hash、key等信息，具体字段取决于上传策略的设置
                        if (info.isOK()) {
                            try {
                                boolean isOver = true;
                                for (Object o : selectImageList) {
                                    if (o instanceof PhotoUploadBean) {
                                        PhotoUploadBean p = (PhotoUploadBean) o;
                                        if (p.getLocalPath().equals(path)) {
                                            p.setQiniuPath(res.getString("key"));
                                        }
                                        if (TextUtils.isEmpty(p.getQiniuPath())) {
                                            isOver = false;
                                        }
                                    }
                                }
                                if (isOver) {
                                    DialogUtil.hideLoading(AdviceActivity.this);
                                    submit();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {
                            DialogUtil.hideLoading(AdviceActivity.this);
                            ToastUtils.show(AdviceActivity.this, info.error);
                            //如果失败，这里可以把info信息上报自己的服务器，便于后面分析上传错误原因
                        }
                        Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res);
                    }
                }, null);
    }

    private void getQiniuToken() {
        DialogUtil.showLoading(this, true, "请稍候");
        ApiUtils.getInstance().getQiniuToken(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(AdviceActivity.this);
                for (Object o : selectImageList) {
                    if (o instanceof PhotoUploadBean) {
                        PhotoUploadBean p = (PhotoUploadBean) o;
                        if (TextUtils.isEmpty(p.getQiniuPath())) {
                            upload(p.getLocalPath(), t.getData().toString());
                        }
                    }
                }
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(AdviceActivity.this);
                ToastUtils.show(AdviceActivity.this, msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(AdviceActivity.this);
                ToastUtils.show(AdviceActivity.this, expectionMsg);

            }
        });
    }

    private boolean needUploadPhoto() {
        boolean need = false;
        for (Object o : selectImageList) {
            if (o instanceof PhotoUploadBean) {
                PhotoUploadBean p = (PhotoUploadBean) o;
                if (TextUtils.isEmpty(p.getQiniuPath())) {
                    need = true;
                }
            }
        }
        return need;
    }

    private String getImagePath() {
        String imagePath = "";
        if (!selectImageList.isEmpty()) {
            for (Object o : selectImageList) {
                if (o instanceof PhotoUploadBean) {
                    PhotoUploadBean p = (PhotoUploadBean) o;
                    if (!TextUtils.isEmpty(p.getQiniuPath())) {
                        imagePath += p.getQiniuPath() + ",";
                    }
                }
            }
            if (imagePath.length() > 1) {
                imagePath = imagePath.substring(0, imagePath.length() - 1);

            }
        }

        return imagePath;
    }

    @OnClick(R.id.tv_submit)
    public void onViewClicked() {
        submit();
    }

    void refreshAdapter() {
        boolean addFooter = true;
        for(Object o:selectImageList){
            if(o instanceof String){
                addFooter =false;
            }
        }
        if(addFooter&&selectImageList.size()<maxCount){
            selectImageList.add("footer");
        }

        adapter.notifyDataSetChanged();
    }

}
