package com.qulink.hxedu.ui.fragment;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.HotArtical;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.entity.TopPicBean;
import com.qulink.hxedu.mvp.contract.ZoneContract;
import com.qulink.hxedu.mvp.presenter.ZonePresenter;
import com.qulink.hxedu.ui.ImagesPreviewActivity;
import com.qulink.hxedu.ui.zone.PersonZoneIndexActivity;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;
import kale.adapter.util.IAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZoneFragment extends Fragment implements ZoneContract.View, OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.recycle_official_action)
    RecyclerView recycleOfficialAction;
    @BindView(R.id.recycle_subject)
    RecyclerView recycleSubject;
    @BindView(R.id.recycle_share_content)
    RecyclerView recycleShareContent;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    @BindView(R.id.iv_person)
    ImageView ivPerson;
    private View rootView;

    private ZonePresenter mPresenter;

    public ZoneFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_zone, container, false);
            ButterKnife.bind(this, rootView);

            mPresenter = new ZonePresenter();
            mPresenter.attachView(this);
            mPresenter.getHotArtical();
            mPresenter.getTopPic();
            mPresenter.getAllPic();
            smartLayout.setOnRefreshListener(this);
            smartLayout.setOnLoadMoreListener(this);

        }
        return rootView;
    }


    private void initHotArtical(List<HotArtical> actionList) {

        recycleOfficialAction.setAdapter(new CommonRcvAdapter<HotArtical>(actionList) {

            @Override
            public Object getItemType(HotArtical hotArtical) {
                return hotArtical;
            }

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new HotArticalItem();
            }
        });
        recycleOfficialAction.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void initTopPicRecycle(List<TopPicBean> list) {
        TopPicBean topPicBean = new TopPicBean();
        topPicBean.setType(-1);
        list.add(topPicBean);
        recycleSubject.setAdapter(new CommonRcvAdapter<TopPicBean>(list) {
            @Override
            public Object getItemType(TopPicBean subjectBean) {

                return subjectBean.getType();
            }

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {

                if ((int) type == -1) {
                    return new MoreItem();
                }
                return new TitleItem();
            }
        });
        recycleSubject.setLayoutManager(new GridLayoutManager(getActivity(), 2));

    }


    @Override
    public void getHotArticalSuc(List<HotArtical> hotArticalList) {
        initHotArtical(hotArticalList);
    }

    @Override
    public void getTopPicSuc(List<TopPicBean> topPicBeanList) {
        initTopPicRecycle(topPicBeanList);

    }

    @Override
    public void getHotArticalFail(String msg) {

    }

    @Override
    public void getAllPicSuc(List<PicBean> picBeans) {
        initPicRecycle(picBeans);
        smartLayout.finishRefresh(true);
    }

    @Override
    public void loadMorePicSuc(List<PicBean> picBeans) {
        List<PicBean> list = ((IAdapter<PicBean>) recycleShareContent.getAdapter()).getData();
        list.addAll(picBeans);
        ((IAdapter<PicBean>) recycleShareContent.getAdapter()).setData(list);
        recycleShareContent.getAdapter().notifyDataSetChanged();
        smartLayout.finishLoadMore(true);
    }

    @Override
    public void getPicMasterSuc(PicMaster picMaster) {
        List<PicBean> list = ((IAdapter<PicBean>) recycleShareContent.getAdapter()).getData();
        for (PicBean p : list) {
            if (p.getUserId() == picMaster.getUserId()) {
                p.setPicMaster(picMaster);
                p.setInitMaster(false);
            }
        }
        recycleShareContent.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void onError(String msg) {
        smartLayout.finishRefresh(false);

    }

    @Override
    public void onSuccess(ResponseData data) {

    }

    @Override
    public void onExpcetion(String msg) {
        smartLayout.finishRefresh(false);

    }

    @Override
    public void noMore() {
        smartLayout.setNoMoreData(true);
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPresenter.getHotArtical();
        mPresenter.getTopPic();
        mPresenter.getAllPic();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPresenter.loadMorePic();
    }

    @OnClick(R.id.iv_person)
    public void onViewClicked(View view) {
        switch (view.getId()){
            case R.id.iv_person:
                RouteUtil.startNewActivity(getActivity(),new Intent(getActivity(), PersonZoneIndexActivity.class));
                break;
        }
    }

    class HotArticalItem implements AdapterItem<HotArtical> {
        TextView tvTtitle;

        @Override
        public int getLayoutResId() {
            return R.layout.official_action_item;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tvTtitle = root.findViewById(R.id.tv_title);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(HotArtical o, int position) {
            tvTtitle.setText(o.getContent());

        }


    }

    class TitleItem implements AdapterItem<TopPicBean> {
        TextView tvTitle;
        ImageView ivImag;

        @Override
        public int getLayoutResId() {
            return R.layout.subject_item;
        }

        @Override
        public void bindViews(@NonNull View root) {

            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tvTitle = root.findViewById(R.id.tv_title);
            ivImag = root.findViewById(R.id.iv_img);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(TopPicBean subjectBean, int position) {

            tvTitle.setText(subjectBean.getName());
            if (CourseUtil.isOk(subjectBean.getHotStatus()
            )) {
                ivImag.setImageResource(R.drawable.hot);
            }
//            else if (subjectBean.type == 2) {
//                ivImag.setImageResource(R.drawable.tj);
//            }
        }
    }

    class MoreItem implements AdapterItem {
        TextView tvMore;

        @Override
        public int getLayoutResId() {
            return R.layout.more_subject_text;
        }

        @Override
        public void bindViews(@NonNull View root) {

            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            tvMore = root.findViewById(R.id.tv_more);
        }

        @Override
        public void setViews() {

            tvMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtils.show(getActivity(), "更多");
                }
            });
        }

        @Override
        public void handleData(Object o, int position) {

        }
    }


    void initPicRecycle(List<PicBean> list) {
        recycleShareContent.setAdapter(new CommonRcvAdapter<PicBean>(list) {


            @NonNull
            @Override
            public AdapterItem createItem(Object type) {


                return new TopicItem();
            }
        });
        recycleShareContent.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    class TopicItem implements AdapterItem<PicBean>{
        TextView tvName;
        TextView tvCommentNum;
        TextView tvLikeNum;
        TextView tvItem;
        ExpandableTextView expandableTextView;
        RecyclerView imgRecycleview;
        ImageView ivHeadimg;
        LinearLayout levelContanier;

        @Override
        public int getLayoutResId() {
            return R.layout.subject_share_item;
        }

        @Override
        public void bindViews(@NonNull View root) {

            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;

            tvName = root.findViewById(R.id.tv_name);
            tvCommentNum = root.findViewById(R.id.tv_comment_num);
            tvLikeNum = root.findViewById(R.id.tv_like_num);
            expandableTextView = root.findViewById(R.id.content);
            tvItem = root.findViewById(R.id.tv_item);
            ivHeadimg = root.findViewById(R.id.iv_headimg);
            imgRecycleview = root.findViewById(R.id.recycle_img);
            levelContanier = root.findViewById(R.id.ll_level_contanier);

        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(final PicBean shareContentBean, int position) {
            if (!shareContentBean.isInitMaster()) {
                if (shareContentBean.getPicMaster() == null) {
                    shareContentBean.setInitMaster(true);
                    mPresenter.getPicMaster(shareContentBean.getUserId());
                } else {
                    tvName.setText(shareContentBean.getPicMaster().getNickname() + "");
                    App.getInstance().getDefaultSetting(getActivity(), new DefaultSettingCallback() {
                        @Override
                        public void getDefaultSetting(DefaultSetting defaultSetting) {
                            Glide.with(getActivity()).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), shareContentBean.getPicMaster().getHeadImg())).into(ivHeadimg);
                        }
                    });
                    if(CourseUtil.isOk(shareContentBean.getPicMaster().getStatus())){
                        ImageView imageView = new ImageView(getActivity());
                        imageView.setImageResource(R.drawable.hg);
                        levelContanier.addView(imageView);
                    }
                    if(!shareContentBean.getPicMaster().getBadge().isEmpty()){

                        ImageView  imageView = new ImageView(getActivity());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.leftMargin = 6;
                        imageView.setLayoutParams(layoutParams);
                        imageView.setImageResource(R.drawable.xunzhang);
                        levelContanier.addView(imageView);
                    }
                }
            }
            tvLikeNum.setText(shareContentBean.getThumbs() + "");
            tvCommentNum.setText(shareContentBean.getComments() + "");
            expandableTextView.setContent(shareContentBean.getContent());
            levelContanier.removeAllViews();


                            if (!TextUtils.isEmpty(shareContentBean.getTopicName() )) {
                                tvItem.setText(shareContentBean.getTopicName() );
                                tvItem.setVisibility(View.VISIBLE);
                            } else {
                                tvItem.setVisibility(View.GONE);

                            }


            if (!TextUtils.isEmpty(shareContentBean.getImgPath())) {
                App.getInstance().getDefaultSetting(getActivity(), new DefaultSettingCallback() {
                    @Override
                    public void getDefaultSetting(DefaultSetting defaultSetting) {
                        final ArrayList<String> imgLig = new ArrayList<>();
                        String[] strings = shareContentBean.getImgPath().split(",");
                        for (String s : strings) {
                            s = ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), s);
                            imgLig.add(s);
                        }

                        imgRecycleview.setAdapter(new CommonRcvAdapter<String>(imgLig) {

                            ImageView iv;

                            @NonNull
                            @Override
                            public AdapterItem createItem(Object type) {
                                return new AdapterItem<String>() {
                                    @Override
                                    public int getLayoutResId() {
                                        return R.layout.imageview_item;
                                    }

                                    @Override
                                    public void bindViews(@NonNull View root) {
                                        iv = root.findViewById(R.id.iv);
                                    }

                                    @Override
                                    public void setViews() {


                                    }

                                    @Override
                                    public void handleData(String o, int position) {
                                        iv.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(getActivity(), ImagesPreviewActivity.class);
                                                intent.putExtra("position", position);
                                                intent.putStringArrayListExtra("data", imgLig);
                                                startActivity(intent);
                                            }
                                        });
                                        try {
                                            Glide.with(getActivity()).load(o).into(iv);

                                        }catch (Exception e){}
                                    }
                                };
                            }
                        });
                        imgRecycleview.setLayoutManager(new GridLayoutManager(getActivity(), 3));

                    }
                });
            }


        }
    }
//
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Success(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals(FinalValue.LOGIN_SUCCESS)
        ) {
//            mPresenter.getHotArtical();
//            mPresenter.getTopPic();
//            mPresenter.getAllPic();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
        mPresenter.detachView();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

}




