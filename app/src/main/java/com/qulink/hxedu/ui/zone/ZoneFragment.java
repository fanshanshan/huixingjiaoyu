package com.qulink.hxedu.ui.zone;


import android.app.Activity;
import android.content.Context;
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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ctetin.expandabletextviewlibrary.ExpandableTextView;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.adapter.TopicAdapter;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.HotArtical;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.entity.PicBean;
import com.qulink.hxedu.entity.PicMaster;
import com.qulink.hxedu.entity.TopPicBean;
import com.qulink.hxedu.mvp.contract.ArticalContract;
import com.qulink.hxedu.mvp.contract.LikeArticalContract;
import com.qulink.hxedu.mvp.contract.ZoneContract;
import com.qulink.hxedu.mvp.presenter.ArticalPresenter;
import com.qulink.hxedu.mvp.presenter.LikeArticalPresenter;
import com.qulink.hxedu.mvp.presenter.ZonePresenter;
import com.qulink.hxedu.ui.ImagesPreviewActivity;
import com.qulink.hxedu.ui.zone.ArticalDetailActivity;
import com.qulink.hxedu.ui.zone.MoreSubjectActivity;
import com.qulink.hxedu.ui.zone.PersonZoneIndexActivity;
import com.qulink.hxedu.ui.zone.TopTopicActivity;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.FastClick;
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
public class ZoneFragment extends Fragment implements ZoneContract.View, OnRefreshListener, OnLoadMoreListener, LikeArticalContract.View {


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
    private LikeArticalPresenter likeArticalPresenter;

    private Activity mActivity;

    public ZoneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    private List<PicBean> picBeanList;
    private TopicAdapter topicAdapter;
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_zone, container, false);
            ButterKnife.bind(this, rootView);

            mPresenter = new ZonePresenter();
            likeArticalPresenter = new LikeArticalPresenter();
            mPresenter.attachView(this);
            likeArticalPresenter.attachView(this);
            mPresenter.getHotArtical();
            mPresenter.getTopPic();
            mPresenter.getAllPic();
            smartLayout.setOnRefreshListener(this);
            smartLayout.setOnLoadMoreListener(this);
            initPicRecycle();

        }
        return rootView;
    }


    private void initHotArtical(List<PicBean> actionList) {

        recycleOfficialAction.setAdapter(new CommonRcvAdapter<PicBean>(actionList) {

            @Override
            public Object getItemType(PicBean hotArtical) {
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
                    return new MoreItem
                            ();
                }
                return new TitleItem();
            }
        });
        recycleSubject.setLayoutManager(new GridLayoutManager(getActivity(), 2));

    }


    @Override
    public void getHotArticalSuc(List<PicBean> hotArticalList) {
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
        picBeanList.clear();
        picBeanList.addAll(picBeans);
        topicAdapter.notifyDataSetChanged();
        smartLayout.finishRefresh(true);
    }

    @Override
    public void loadMorePicSuc(List<PicBean> picBeans) {

        if(picBeans!=null&&!picBeans.isEmpty()){
            picBeanList.addAll(picBeans);
            topicAdapter.notifyItemRangeChanged(picBeanList.size()-picBeans.size(),picBeans.size());
            smartLayout.finishLoadMore(true);
        }

    }

    @Override
    public void getPicMasterSuc(PicMaster picMaster) {

        for(int i = 0;i<picBeanList.size();i++){
            if(picBeanList.get(i).getUserId()==picMaster.getUserId()&&picBeanList.get(i).getPicMaster()==null){
                picBeanList.get(i).setPicMaster(picMaster);
                picBeanList.get(i).setInitMaster(false);
                recycleShareContent.getAdapter().notifyItemChanged(i,picBeanList);
                break;
            }
        }
    }

    @Override
    public void getPicMasterFail(int userId) {
        for(int i = 0;i<picBeanList.size();i++){
            if(picBeanList.get(i).getUserId()==userId&&picBeanList.get(i).getPicMaster()==null){
                picBeanList.get(i).setPicMaster(new PicMaster());
                picBeanList.get(i).setInitMaster(false);
                recycleShareContent.getAdapter().notifyItemChanged(i,picBeanList);
                break;
            }
        }
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
        smartLayout.setNoMoreData(false);
        mPresenter.getHotArtical();
        mPresenter.getTopPic();
        mPresenter.getAllPic();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        mPresenter.loadMorePic();
    }

    @OnClick({R.id.iv_person,R.id.iv_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_person:
                if(App.getInstance().isLogin(mActivity,true)){
                    RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), PersonZoneIndexActivity.class));
                }
                break; case R.id.iv_search:
                RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), SearchTopicActivity.class));
                break;
        }
    }

    @Override
    public void likeArticalSuc(int articalId) {

        for(int i = 0;i<picBeanList.size();i++){
            if(picBeanList.get(i).getId()==articalId){
                picBeanList.get(i).setThumbStatus(1);
                picBeanList.get(i).setThumbs(picBeanList.get(i).getThumbs()+1);

                recycleShareContent.getAdapter().notifyItemChanged(i,picBeanList);
                break;
            }
        }
    }

    @Override
    public void cancelLikeArticalSuc(int articalId) {

        for(int i = 0;i<picBeanList.size();i++){
            if(picBeanList.get(i).getId()==articalId){
                picBeanList.get(i).setThumbStatus(0);
                picBeanList.get(i).setThumbs(picBeanList.get(i).getThumbs()-1);
                recycleShareContent.getAdapter().notifyItemChanged(i,picBeanList);
                break;
            }
        }
    }

    class HotArticalItem implements AdapterItem<PicBean> {
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
        public void handleData(PicBean o, int position) {
            tvTtitle.setText(o.getContent());

            tvTtitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, ArticalDetailActivity.class);
                    intent.putExtra("id",o.getId());
                    startActivity(intent);
                }
            });
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
            if (CourseUtil.isOk(subjectBean.getHotStatus())) {
                ivImag.setImageResource(R.drawable.hot);
            }
            if (CourseUtil.isOk(subjectBean.getRecStatus())) {
                ivImag.setImageResource(R.drawable.tj);
            }
            tvTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, TopTopicActivity.class);
                    intent.putExtra("name", subjectBean.getName());
                    intent.putExtra("num", subjectBean.getNumbers());
                    intent.putExtra("id", subjectBean.getId());
                    RouteUtil.startNewActivity(mActivity, intent);
                }
            });
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
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, MoreSubjectActivity.class));
                }
            });
        }

        @Override
        public void handleData(Object o, int position) {

        }
    }


    void initPicRecycle() {
        if(picBeanList==null){
            picBeanList = new ArrayList<>();
        }
        topicAdapter = new TopicAdapter(picBeanList,mActivity);
        topicAdapter.setClickListener(new TopicAdapter.PicAdapterController() {
            @Override
            public void getPicMaster(int userId) {
                mPresenter.getPicMaster(userId);
            }

            @Override
            public void cancelLikeArtical(int id) {

                likeArticalPresenter.cancelLikeArtical(id);
            }

            @Override
            public void likeArtical(int id) {
                likeArticalPresenter.likeArtical(id);

            }

            @Override
            public void goArticalDetail(PicBean picBean) {
                Intent intent = new Intent(mActivity, ArticalDetailActivity.class);
                intent.putExtra("data",picBean);
                intent.putExtra("getDetail",true);
              startActivityForResult(intent,0);
            }
        });
        recycleShareContent.setAdapter(topicAdapter);
        recycleShareContent.setLayoutManager(new LinearLayoutManager(getActivity()));
    }


    //
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Success(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals(FinalValue.LOGIN_SUCCESS)
        ) {
            mPresenter.getHotArtical();
            mPresenter.getTopPic();
            mPresenter.getAllPic();
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
        likeArticalPresenter.detachView();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==-1){
            PicBean picBean = (PicBean) data.getSerializableExtra("data");
            if(picBean==null){
                return;
            }
            for(int i = 0;i<picBeanList.size();i++){
                if(picBeanList.get(i).getId()==picBean.getId()){
                    picBeanList.get(i).setThumbStatus(picBean.getThumbStatus());
                    picBeanList.get(i).setThumbs(picBean.getThumbs());
                    picBeanList.get(i).setComments(picBean.getComments());
                    topicAdapter.notifyItemChanged(i,picBeanList);
                    break;
                }
            }
        }
    }
}




