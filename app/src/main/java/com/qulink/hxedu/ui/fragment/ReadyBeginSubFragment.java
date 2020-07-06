package com.qulink.hxedu.ui.fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.LiveDetailBean;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.ui.BuyliveActivity;
import com.qulink.hxedu.ui.SendVipActivity;
import com.qulink.hxedu.ui.live.AudienceActivity;
import com.qulink.hxedu.ui.live.LiveDetailActivity;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.qulink.hxedu.view.SpacesItemDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReadyBeginSubFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.recycle_view)
    EmptyRecyclerView recycleView;

    CommonRcvAdapter adapter;

    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.tv_empty)
    TextView llEmpty;

    private int classFyId;

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
            classFyId = getArguments().getInt("id");
            getData();
            refreshLayout.setOnLoadMoreListener(this);
            refreshLayout.setOnRefreshListener(this);
            initData();
        }
        return rootView;
    }

    private List<LiveDetailBean.RecordsBean> data;

    LiveDetailBean.RecordsBean recordsBean;
    void getData() {
        data = new ArrayList<>();

        adapter = new CommonRcvAdapter<LiveDetailBean.RecordsBean>(data) {


            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Item();
            }
        };
        recycleView.setAdapter(adapter);
        if(recycleView.getItemDecorationCount()==0){
            recycleView.addItemDecoration(new SpacesItemDecoration(0, 16, 0, 0));
        }
        recycleView.setEmptyView(llEmpty);
        recycleView.setLayoutManager(new LinearLayoutManager(getActivity()));

    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadmore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initData();
    }

    class Item implements AdapterItem<LiveDetailBean.RecordsBean> {
        ImageView ivHead;
        ImageView ivImg;
        TextView tvTitle;
        TextView tvName;
        TextView tvDesc;
        TextView tvStatus;
        LinearLayout living_join;

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
            living_join = root.findViewById(R.id.living_join);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(LiveDetailBean.RecordsBean o, int position) {
            tvName.setText(o.getTeacherName());
            tvTitle.setText(o.getTitle());
            App.getInstance().getDefaultSetting(getActivity(), new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(getContext()).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), o.getHeadImage())).into(ivHead);
                    Glide.with(getContext()).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), o.getCoverUrl())).into(ivImg);

                }
            });


            tvDesc.setText("");

            if(o.getParticipated()==0){
                tvStatus.setText("立即报名");
                living_join.setBackgroundResource(R.drawable.living_join);
            }else{
                if(o.getStatus()==2){
                    tvStatus.setText("已报名");
                    living_join.setBackgroundResource(R.drawable.living_joined);
                }else{
                    tvStatus.setText("正在直播");
                    living_join.setBackgroundResource(R.drawable.living_txt_bg);
                }

            }

            tvStatus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    startLive(o.getId());
//                    if(o.getParticipated()==0){
//                        if(o.getType()==1){
//                            joinLive(o);
//                        }else  if(o.getType()==2){
//                         App.getInstance().getUserInfo(getActivity(), new UserInfoCallback() {
//                             @Override
//                             public void getUserInfo(UserInfo userInfo) {
//                                 if(userInfo.isVip()){
//                                     joinLive(o);
//                                 }else{
//                                     recordsBean = o;
//                                     goToVipPage();
//
//                                 }
//                             }
//                         });
//                        }else{
//                            recordsBean = o;
//                            goToBuyLessonPage();
//                        }
//                    }else{
//                        if(o.getStatus()==2){
//                           startLive(o.getId());
//                        }else{
//                            ToastUtils.show(getActivity(),"直播还未开始");
//                        }
//                    }
                }
            });
        }
    }
    private void startLive(int id){
        Intent intent = new Intent(getActivity(), AudienceActivity.class);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    private void goToVipPage() {
        Intent intent = new Intent(getActivity(), SendVipActivity.class);
        intent.putExtra("withResult", true);
        intent.putExtra("type", "buy");

        startActivityForResult(intent, OPEN_VIP_CODE);
    }
    private int BUY_LESSON_CODE = 2;
    private int OPEN_VIP_CODE = 3;
    private void goToBuyLessonPage() {
        Intent intent = new Intent(getActivity(), BuyliveActivity.class);
        intent.putExtra("withResult", true);
        intent.putExtra("liveId", recordsBean.getId());
        startActivityForResult(intent, OPEN_VIP_CODE);


    }
    private int nextIntent = 0;//
    private void joinLive(LiveDetailBean.RecordsBean o){
        DialogUtil.showLoading(getActivity(),true);
        ApiUtils.getInstance().joinLive(o.getId(), 0, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                o.setParticipated(1);
                recycleView.getAdapter().notifyDataSetChanged();
                DialogUtil.hideLoading(getActivity());
                if(nextIntent==1){
                    Intent intent = new Intent(getActivity(), AudienceActivity.class);
                    intent.putExtra("id",o.getId());
                    startActivity(intent);
                }else{
                    ToastUtils.show(getActivity(),"报名成功");

                }


            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(getActivity(),msg);
                DialogUtil.hideLoading(getActivity());

            }

            @Override
            public void expcetion(String expectionMsg) {
                ToastUtils.show(getActivity(),expectionMsg);
                DialogUtil.hideLoading(getActivity());

            }
        });
    }
    private boolean activityIsOver(String startData){
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        boolean isOPen = false;
        try {
            isOPen = date.after(dateFormat.parse(startData));
        } catch (ParseException e) {
            e.printStackTrace();
            isOPen = true;
        }
        return  isOPen;
    }
    private int pageNo;
    private int pageSize = FinalValue.limit;

    private void initData() {
        pageNo = 1;
        llEmpty.setVisibility(View.GONE);

        refreshLayout.setNoMoreData(false);
        ApiUtils.getInstance().getLiveList(pageNo, pageSize, 2, classFyId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                LiveDetailBean liveDetailBean = new Gson().fromJson(GsonUtil.GsonString(t.getData()), LiveDetailBean.class);
                if (liveDetailBean.getRecords() != null && !liveDetailBean.getRecords().isEmpty()) {
                    data.clear();
                    data.addAll(liveDetailBean.getRecords());
                    if (liveDetailBean.getRecords().size() < pageSize) {
                        refreshLayout.setNoMoreData(true);
                        llEmpty.setVisibility(View.GONE);

                    }
                    recycleView.getAdapter().notifyDataSetChanged();
                } else {
                    llEmpty.setVisibility(View.VISIBLE);

                    refreshLayout.setNoMoreData(true);
                }
                refreshLayout.finishRefresh(true);
            }

            @Override
            public void error(String code, String msg) {
                refreshLayout.finishRefresh(true);

            }

            @Override
            public void expcetion(String expectionMsg) {
                refreshLayout.finishRefresh(true);

            }
        });

    }

    private void loadmore() {
        pageNo++;
        ApiUtils.getInstance().getLiveList(pageNo, pageSize, 2, classFyId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                LiveDetailBean liveDetailBean = new Gson().fromJson(GsonUtil.GsonString(t.getData()), LiveDetailBean.class);
                if (liveDetailBean.getRecords() != null && !liveDetailBean.getRecords().isEmpty()) {
                    data.addAll(liveDetailBean.getRecords());
                    if (liveDetailBean.getRecords().size() < pageSize) {
                        refreshLayout.setNoMoreData(true);

                    }
                    recycleView.getAdapter().notifyDataSetChanged();

                } else {
                    refreshLayout.setNoMoreData(true);
                }
                refreshLayout.finishLoadMore(true);
            }

            @Override
            public void error(String code, String msg) {
                refreshLayout.finishLoadMore(true);

            }

            @Override
            public void expcetion(String expectionMsg) {
                refreshLayout.finishLoadMore(true);

            }
        });

    }


    private void showOpenVipDialog() {
        DialogUtil.showAlertDialog(getActivity(), "提示", "报名该直播需要开通会员？", "立即开通", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                goToVipPage();
            }
        }, "取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == BUY_LESSON_CODE) {
               if(recordsBean!=null){
                   startLive(recordsBean.getId());
                   recordsBean = null;
               }
            } else if (requestCode == OPEN_VIP_CODE) {
                if(recordsBean!=null){
                   joinLive(recordsBean);
                    recordsBean = null;
                }
            }
        }
    }


}

