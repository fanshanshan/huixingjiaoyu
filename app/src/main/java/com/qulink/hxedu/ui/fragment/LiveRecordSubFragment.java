package com.qulink.hxedu.ui.fragment;


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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.qulink.hxedu.R;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.SpacesItemDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveRecordSubFragment extends Fragment {


    @BindView(R.id.recycle_view)
    RecyclerView recycleView;

    CommonRcvAdapter adapter;

    List<ReadyBeginSubFragment.TempData> dataList;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;

    public LiveRecordSubFragment() {
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
            initRefreshLayout();
        }
        return rootView;
    }

    void initRefreshLayout(){
       refreshLayout.setOnRefreshListener(new OnRefreshListener() {
           @Override
           public void onRefresh(@NonNull RefreshLayout refreshLayout) {
               refreshLayout.finishRefresh(2000,true,false);
           }
       });
       refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
           @Override
           public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
               refreshLayout.finishLoadMore(2000);
           }
       });
    }

    void getData() {
        dataList = new ArrayList<>();
        dataList.add(new ReadyBeginSubFragment.TempData("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1774576164,138926049&fm=26&gp=0.jpg",
                1, "我是直播课程爱我你怕了吗", "https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1089923673,1921966740&fm=26&gp=0.jpg",
                "幂总"));
        dataList.add(new ReadyBeginSubFragment.TempData("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1774576164,138926049&fm=26&gp=0.jpg",
                2, "我是直播课程爱我你怕了吗", "https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1089923673,1921966740&fm=26&gp=0.jpg",
                "幂总"));
        dataList.add(new ReadyBeginSubFragment.TempData("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1774576164,138926049&fm=26&gp=0.jpg",
                3, "我是直播课程爱我你怕了吗", "https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1089923673,1921966740&fm=26&gp=0.jpg",
                "幂总"));
        dataList.add(new ReadyBeginSubFragment.TempData("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1774576164,138926049&fm=26&gp=0.jpg",
                1, "我是直播课程爱我你怕了吗", "https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1089923673,1921966740&fm=26&gp=0.jpg",
                "幂总"));

        adapter = new CommonRcvAdapter<ReadyBeginSubFragment.TempData>(dataList) {
            ImageView ivHead;
            ImageView ivImg;
            TextView tvTitle;
            TextView tvName;
            TextView tvDesc;
            TextView tvStatus;


            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AdapterItem() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.live_record_item;
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
                        tvDesc = root.findViewById(R.id.tv_desc);
                    }

                    @Override
                    public void setViews() {

                    }

                    @Override
                    public void handleData(Object o, int position) {
                        ReadyBeginSubFragment.TempData tempData = null;
                        if (o instanceof ReadyBeginSubFragment.TempData) {
                            tempData = (ReadyBeginSubFragment.TempData) o;
                        }
                        if (tempData != null) {
                            tvName.setText(tempData.name);
                            tvTitle.setText(tempData.title);
                            tvName.setText(tempData.name);
                            Glide.with(getContext()).load(tempData.headimg).into(ivHead);
                            Glide.with(getContext()).load(tempData.img).into(ivImg);
                            String text = "观看回放";

                            if (tempData.statu == 1) {
                                text = "直播已结束，录播视频制作中";
                            } else if (tempData.statu == 2) {
                                text = "直播已结束";

                            }
                            tvDesc.setText(text);

                            tvStatus.setText("查看回放");
                            tvStatus.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ToastUtils.show(getActivity(), "视频正在制作中");
                                }
                            });
                        }
                    }
                };
            }
        };
        recycleView.setAdapter(adapter);
        recycleView.addItemDecoration(new SpacesItemDecoration(0,16));
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}

