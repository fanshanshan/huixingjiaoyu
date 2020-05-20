package com.qulink.hxedu.ui.fragment;


import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qulink.hxedu.R;
import com.qulink.hxedu.view.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadyBeginSubFragment extends Fragment {


    @BindView(R.id.recycle_view)
    RecyclerView recycleView;

    CommonRcvAdapter adapter;

    List<TempData> dataList;
    public ReadyBeginSubFragment() {
        // Required empty public constructor
    }


    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_ready_begin_sub, container, false);
            ButterKnife.bind(this, rootView);
            getData();
        }
        return rootView;
    }

    void getData(){
        dataList = new ArrayList<>();
        dataList.add(new TempData("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1774576164,138926049&fm=26&gp=0.jpg",
                1,"我是直播课程爱我你怕了吗","https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1089923673,1921966740&fm=26&gp=0.jpg",
                "幂总"));
        dataList.add(new TempData("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1774576164,138926049&fm=26&gp=0.jpg",
                2,"我是直播课程爱我你怕了吗","https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1089923673,1921966740&fm=26&gp=0.jpg",
                "幂总"));
        dataList.add(new TempData("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1774576164,138926049&fm=26&gp=0.jpg",
                3,"我是直播课程爱我你怕了吗","https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1089923673,1921966740&fm=26&gp=0.jpg",
                "幂总"));
        dataList.add(new TempData("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1774576164,138926049&fm=26&gp=0.jpg",
                1,"我是直播课程爱我你怕了吗","https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1089923673,1921966740&fm=26&gp=0.jpg",
                "幂总"));

        adapter = new CommonRcvAdapter<TempData>(dataList) {
            ImageView ivHead;
            ImageView ivImg;
            TextView tvTitle;
            TextView tvName;
            TextView tvTime;
            TextView tvStatus;



            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AdapterItem() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.live_item;
                    }

                    @Override
                    public void bindViews(@NonNull View root) {
                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        ivHead = root.findViewById(R.id.iv_head);
                        ivImg = root.findViewById(R.id.iv_img);
                        tvTitle = root.findViewById(R.id.tv_title);
                        tvName = root.findViewById(R.id.tv_name);
                        tvStatus = root.findViewById(R.id.tv_status);
                        tvTime = root.findViewById(R.id.tv_time);
                    }

                    @Override
                    public void setViews() {

                    }

                    @Override
                    public void handleData(Object o, int position) {
                        TempData tempData = null;
                        if(o instanceof TempData){
                            tempData = (TempData)o;
                        }
                        if(tempData!=null){
                            tvTime.setText("2020-03-12 12:00:59");
                            tvName.setText(tempData.name);
                            tvTitle.setText(tempData.title);
                            tvName.setText(tempData.name);
                            Glide.with(getContext()).load(tempData.headimg).into(ivHead);
                            Glide.with(getContext()).load(tempData.img).into(ivImg);
                            Drawable drawableRes=null;
                            String text="";
                            int textColor=0;
                            boolean enabled = true;
                            if(tempData.statu==1){
                                text="直播中";
                                textColor=0xffffffff;
                                drawableRes = ResourcesCompat.getDrawable(getResources(), R.drawable.living_txt_bg,null);;
                            }else if(tempData.statu==2){
                                text="立即报名";
                                textColor=0xffffffff;
                                drawableRes = ResourcesCompat.getDrawable(getResources(), R.drawable.living_join,null);;

                            }else if(tempData.statu==3){
                                enabled = false;
                                text="已报名";
                                textColor=0xffffffff;
                                drawableRes = ResourcesCompat.getDrawable(getResources(), R.drawable.living_joined,null);;

                            }
                            tvStatus.setEnabled(enabled);
                            tvStatus.setText(text);
                            tvStatus.setTextColor(ColorStateList.valueOf(textColor));
                            tvStatus.setBackground(drawableRes);
                        }
                    }
                };
            }
        };
        recycleView.setAdapter(adapter);
        recycleView.addItemDecoration(new SpacesItemDecoration(0,16,0,0));
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

   static class TempData{
        String img;
        int statu;
        String title;
        String headimg;
        String name;

        public TempData(String img, int statu, String title, String headimg, String name) {
            this.img = img;
            this.statu = statu;
            this.title = title;
            this.headimg = headimg;
            this.name = name;
        }
    }
}

