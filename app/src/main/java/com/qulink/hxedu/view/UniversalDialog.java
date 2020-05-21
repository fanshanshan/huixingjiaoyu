package com.qulink.hxedu.view;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.qulink.hxedu.R;


/**
 * Created by Rock on 2017/11/1.
 */

public class UniversalDialog extends Dialog {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public UniversalDialog(Context context) {
        super(context);
    }

    public UniversalDialog(Context context, String title){
        super(context);
    }

    public static class Builder{
        private Context context;
        private String content;
        private TextView tv_content;
        private Button rightButton;
        private Button leftButton;
        private View divider;
        private String leftButtonText;
        private String rightButtonText;
        private OnClickListener rightClickListener;
        private OnClickListener leftClickListener;
        public Builder(Context context){
            this.context = context;
        }
        public Builder setRightButton(String text, OnClickListener clickListener){
            this.rightClickListener = clickListener;
            rightButtonText = text;
            return  this;
        }
        public Builder setLeftButton(String text, OnClickListener clickListener){
            leftButtonText = text;
            this.leftClickListener = clickListener;
            return  this;
        }

        public Builder setContent(String content){
            this.content= content;
            return  this;
        }
        public UniversalDialog create(){
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final UniversalDialog dialog = new UniversalDialog(context);
           View view = inflater.inflate(R.layout.universal_dialog,null,false);
            tv_content =(TextView) view.findViewById(R.id.content);
            divider =(View) view.findViewById(R.id.divider);
            tv_content.setText(content);
            tv_content.setMovementMethod(LinkMovementMethod.getInstance());

            if(leftClickListener!=null&&rightClickListener!=null){
                divider.setVisibility(View.VISIBLE);
            }
            if(leftClickListener!=null){

                leftButton = (Button) view.findViewById(R.id.left);
                leftButton.setVisibility(View.VISIBLE);
                leftButton.setText(leftButtonText);
                leftButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        leftClickListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                    }
                });
            }
            if(rightClickListener!=null){
                rightButton = (Button) view.findViewById(R.id.right);
                rightButton.setText(rightButtonText);
                rightButton.setVisibility(View.VISIBLE);
                rightButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        rightClickListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                    }
                });
            }

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(view);
            dialog.setCancelable(true);
            Window dialogWindow = dialog.getWindow();
            dialogWindow.setGravity(Gravity.CENTER);
            //dialogWindow.setWindowAnimations(R.style.ActionSheetDialogAnimation);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            WindowManager wm = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            lp.width =wm.getDefaultDisplay().getWidth()/5*4;
            dialogWindow.setAttributes(lp);
            return dialog;
        }
    }
}
