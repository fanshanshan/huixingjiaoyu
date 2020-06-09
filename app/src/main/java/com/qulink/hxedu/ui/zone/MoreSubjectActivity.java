package com.qulink.hxedu.ui.zone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.entity.TopPicBean;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

import java.util.List;

import butterknife.BindView;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class MoreSubjectActivity extends BaseActivity {

    @BindView(R.id.recycle)
    RecyclerView recycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_more_subject;
    }

    @Override
    protected void init() {

        setTitle(getString(R.string.more_subject));
        initData();

    }
    void initRecycle(List<TopPicBean> topPicBeanList){
        recycle.setAdapter(new CommonRcvAdapter<TopPicBean>(topPicBeanList) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        });
    }
    void initData(){
        DialogUtil.showLoading(this,true);
        ApiUtils.getInstance().getMoreSubject(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(MoreSubjectActivity.this);
                List<TopPicBean> hotArticalList =new Gson().fromJson(GsonUtil.GsonString(t.getData()),new TypeToken<List<TopPicBean>>() {}.getType());

                initRecycle(hotArticalList);
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(MoreSubjectActivity.this);
                ToastUtils.show(MoreSubjectActivity.this,msg);

            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(MoreSubjectActivity.this);

                ToastUtils.show(MoreSubjectActivity.this,expectionMsg);
            }
        });
        recycle.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    class Item implements AdapterItem<TopPicBean>{
        TextView tvContent;
        ImageView ivImag;
        LinearLayout llRoot;
        @Override
        public int getLayoutResId() {
            return R.layout.more_subject_item;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tvContent = root.findViewById(R.id.tv_content);
            llRoot = root.findViewById(R.id.ll_root);
            ivImag = root.findViewById(R.id.iv_img);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(TopPicBean topPicBean, int position) {
            tvContent.setText("#"+topPicBean.getName());
            if(CourseUtil.isOk(topPicBean.getHotStatus())){
                ivImag.setImageResource(R.drawable.hot);
            } if(CourseUtil.isOk(topPicBean.getRecStatus())){
                ivImag.setImageResource(R.drawable.tj);
            }
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MoreSubjectActivity.this, TopTopicActivity.class);
                    intent.putExtra("name",topPicBean.getName());
                    intent.putExtra("num",topPicBean.getNumbers());
                    intent.putExtra("id",topPicBean.getId());
                    RouteUtil.startNewActivity(MoreSubjectActivity.this,intent);
                }
            });
        }
    }
}
