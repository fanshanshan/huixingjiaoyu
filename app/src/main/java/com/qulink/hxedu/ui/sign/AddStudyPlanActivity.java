package com.qulink.hxedu.ui.sign;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class AddStudyPlanActivity extends BaseActivity {

    @BindView(R.id.et_plan)
    EditText etPlan;
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.tv_input_number)
    TextView tvInputNumber;

    private int length;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_add_study_plan;
    }
    private int num = 100;

    @Override
    protected void init() {
//etNoteContent是EditText
        length = getIntent().getIntExtra("length",0);
        setTitle(getString(R.string.add_plan));
        etPlan.addTextChangedListener(new TextWatcher() {
            private CharSequence wordNum;//记录输入的字数
            private int selectionStart;
            private int selectionEnd;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                wordNum= s;//实时记录输入的字数
            }

            @Override
            public void afterTextChanged(Editable s) {
                int number = num - s.length();
                //TextView显示剩余字数
                tvInputNumber.setText(wordNum.length()+"/" +num);

                      selectionStart=etPlan.getSelectionStart();
                selectionEnd = etPlan.getSelectionEnd();
                if (wordNum.length() > num) {
                    //删除多余输入的字（不会显示出来）
                    s.delete(selectionStart - 1, selectionEnd);
                    int tempSelection = selectionEnd;
                    etPlan.setText(s);
                    etPlan.setSelection(tempSelection);//设置光标在最后
                }
            }
        });
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    @OnClick({R.id.et_plan, R.id.tv_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.et_plan:
                break;
            case R.id.tv_save:
                save();
                break;
        }
    }

    void save() {
        if(length> FinalValue.maxStudyLimit||length==FinalValue.maxStudyLimit){
            ToastUtils.show(AddStudyPlanActivity.this,"每天最多添加"+FinalValue.maxStudyLimit+"个学习计划");

            return;
        }
        if (TextUtils.isEmpty(etPlan.getText().toString())) {
            ToastUtils.show(this, "请输入学习计划");
            return;
        }
        if (etPlan.getText().toString().length() > 100) {
            ToastUtils.show(this, "内容长度不能超过100");
            return;
        }

        DialogUtil.showLoading(this,true);
        ApiUtils.getInstance().addStudyPlan(etPlan.getText().toString(), new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                length++;
                DialogUtil.hideLoading(AddStudyPlanActivity.this);
                DialogUtil.showAlertDialog(AddStudyPlanActivity.this, "提示", "添加学习计划成功", "继续添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        etPlan.setText("");
                    }
                }, "完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        setResult(2);
                        finish();
                    }
                });
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(AddStudyPlanActivity.this);
                ToastUtils.show(AddStudyPlanActivity.this,msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(AddStudyPlanActivity.this);
                ToastUtils.show(AddStudyPlanActivity.this,expectionMsg);

            }
        });
    }
}
