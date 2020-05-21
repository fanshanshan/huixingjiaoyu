package com.qulink.hxedu.ui.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qulink.hxedu.R;
import com.qulink.hxedu.ui.CourseDetailActivity;
import com.qulink.hxedu.util.RouteUtil;
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
public class SubOrderFragment extends Fragment {


    @BindView(R.id.recycle_course)
    RecyclerView recycleCourse;
    private View rootView;

    public SubOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_sub_order, container, false);
            ButterKnife.bind(this,rootView);
            initData();
        }
        return rootView;
    }

    List<SubCourceFragment.Data> dataList;
    CommonRcvAdapter<SubCourceFragment.Data> commonRcvAdapter;

    void initData() {
        dataList = new ArrayList<>();
        dataList.add(new SubCourceFragment.Data("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3625347790,708645933&fm=26&gp=0.jpg",
                "杨幂最新私服曝光,黑色针织长裙展现温柔一面,锁骨都..._手机网易网",
                250, "VIP免费", 2324));
        dataList.add(new SubCourceFragment.Data("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3625347790,708645933&fm=26&gp=0.jpg",
                "杨幂最新私服曝光,黑色针织长裙展现温柔一面,锁骨都..._手机网易网",
                250, "VIP免费", 2324));
        dataList.add(new SubCourceFragment.Data("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3625347790,708645933&fm=26&gp=0.jpg",
                "杨幂最新私服曝光,黑色针织长裙展现温柔一面,锁骨都..._手机网易网",
                250, "VIP免费", 2324));
        dataList.add(new SubCourceFragment.Data("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3625347790,708645933&fm=26&gp=0.jpg",
                "杨幂最新私服曝光,黑色针织长裙展现温柔一面,锁骨都..._手机网易网",
                250, "VIP免费", 2324));
        dataList.add(new SubCourceFragment.Data("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3625347790,708645933&fm=26&gp=0.jpg",
                "杨幂最新私服曝光,黑色针织长裙展现温柔一面,锁骨都..._手机网易网",
                250, "VIP免费", 2324));
        commonRcvAdapter = new CommonRcvAdapter<SubCourceFragment.Data>(dataList) {
            ImageView ivImg;
            TextView tvTitle;
            LinearLayout llRoot;

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AdapterItem() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.my_order_item;
                    }

                    @Override
                    public void bindViews(@NonNull View root) {

                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        tvTitle = root.findViewById(R.id.tv_title);
                        llRoot = root.findViewById(R.id.ll_root);
                        ivImg = root.findViewById(R.id.iv_img);
                    }

                    @Override
                    public void setViews() {
                        llRoot.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), CourseDetailActivity.class));
                            }
                        });
                    }

                    @Override
                    public void handleData(Object o, int position) {
                        if (o instanceof SubCourceFragment.Data) {
                            SubCourceFragment.Data data = (SubCourceFragment.Data) o;
                            tvTitle.setText(data.title);
                            Glide.with(getActivity()).load(data.img).into(ivImg);
                        }
                    }
                };
            }
        };
        recycleCourse.setAdapter(commonRcvAdapter);
        recycleCourse.addItemDecoration(new SpacesItemDecoration(0, 4, 0, 40));
        recycleCourse.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    public static class Data {
        String img;
        String title;

        public Data(String img, String title, double price, String desc, int joinNum) {
            this.img = img;
            this.title = title;
            this.price = price;
            this.desc = desc;
            this.joinNum = joinNum;
        }

        double price;
        String desc;
        int joinNum;
    }
}
