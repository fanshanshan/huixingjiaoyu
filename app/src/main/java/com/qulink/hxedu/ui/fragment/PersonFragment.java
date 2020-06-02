package com.qulink.hxedu.ui.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.entity.PersonMenuItem;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.ui.EditHeadAndNickActivity;
import com.qulink.hxedu.ui.LoginActivity;
import com.qulink.hxedu.ui.MyOrderActivity;
import com.qulink.hxedu.ui.SettingActivity;
import com.qulink.hxedu.ui.sign.PlatformAccountSignActivity;
import com.qulink.hxedu.ui.sign.SignActivity;
import com.qulink.hxedu.ui.VipDetailActivity;
import com.qulink.hxedu.ui.live.AuthorActivity;
import com.qulink.hxedu.ui.sign.StudyPlanActivity;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.util.ViewUtils;
import com.qulink.hxedu.view.MyScrollView;
import com.qulink.hxedu.view.SpacesItemDecoration;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

/**
 * A simple {@link Fragment} subclass.
 */
public class PersonFragment extends Fragment implements OnRefreshListener, OnLoadMoreListener {


    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.recycle_study)
    RecyclerView recycleStudy;
    @BindView(R.id.recycle_item)
    RecyclerView recycleItem;
    @BindView(R.id.sc)
    MyScrollView sc;
    @BindView(R.id.ll_top_search_bg)
    LinearLayout llTopSearchBg;
    @BindView(R.id.ll_user_info)
    LinearLayout llUserInfo;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.ll_near_study)
    LinearLayout llNearStudy;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.iv_headimg)
    CircleImageView ivHeadimg;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.refresh_layout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.iv_vip_status)
    ImageView ivVipStatus;
    @BindView(R.id.iv_sign)
    ImageView ivSign;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.tv_level)
    TextView tvLevel;
    @BindView(R.id.ll_sign)
    LinearLayout llSign;
    @BindView(R.id.tv_open_vip)
    TextView tvOpenVip;
    private View rootView;

    public PersonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_person, container, false);
            ButterKnife.bind(this, rootView);
            initStudy();
            initMenu();
            addScrollviewListener();
            initView();
            refreshLayout.setOnRefreshListener(this);
        }
        return rootView;
    }

    void initView() {
        if (!App.getInstance().isLogin(getActivity())) {
            llUserInfo.setVisibility(View.GONE);
            ivHeadimg.setImageResource(R.drawable.user_default);
            tvLogin.setVisibility(View.VISIBLE);
        } else {
            App.getInstance().getUserInfo(getActivity(), new UserInfoCallback() {
                @Override
                public void getUserInfo(UserInfo userInfo) {
                    if (userInfo != null) {
                        tvName.setText(userInfo.getNickname() + "");
                        Glide.with(getActivity()).load(userInfo.getHeadImg()).error(R.drawable.user_default).into(ivHeadimg);
                        tvLevel.setBackgroundResource(ViewUtils.getLevelBgByLevel(userInfo.getLevel()));
                        //会员状态 0否1是
                        if (!userInfo.isVip()) {
                            tvOpenVip.setText(getString(R.string.open_vip));
                            ivVipStatus.setImageResource(R.drawable.normal_logo);
                        } else {
                            tvOpenVip.setText(getString(R.string.vip));
                            ivVipStatus.setImageResource(R.drawable.vip_logo);
                        }
                        //签到状态 0否1是
                        if (!userInfo.isSign()) {
                            ivSign.setImageResource(R.drawable.wqd);
                            tvSign.setText(getString(R.string.wqd_desc));
                        } else {
                            ivSign.setImageResource(R.drawable.qd);
                            tvSign.setText(getString(R.string.qd_desc));
                        }

                    }
                }
            });
            tvLogin.setVisibility(View.GONE);

            llUserInfo.setVisibility(View.VISIBLE);
        }
    }

    //scorllview添加滑动监听
    void addScrollviewListener() {
        sc.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                final float ratio = (float) Math.min(Math.max(scrollY, 0), llTopSearchBg.getHeight()) / llTopSearchBg.getHeight();
                final int newAlpha = (int) (ratio * 255);
                llTopSearchBg.setBackgroundColor(Color.argb((int) newAlpha, 53, 134, 249));
            }
        });
    }

    List<String> studyList;
    CommonRcvAdapter<String> stringCommonRcvAdapter;

    void initStudy() {
        if (!App.getInstance().isLogin(getActivity())) {
            llNearStudy.setVisibility(View.GONE);
            return;
        } else {
            llNearStudy.setVisibility(View.VISIBLE);
        }
        studyList = new ArrayList<>();
        studyList.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3625347790,708645933&fm=26&gp=0.jpg");
        studyList.add("https://dss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=3625347790,708645933&fm=26&gp=0.jpg");
        studyList.add("https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=405250413,4289985831&fm=26&gp=0.jpg");
        studyList.add("https://dss2.bdstatic.com/70cFvnSh_Q1YnxGkpoWK1HF6hhy/it/u=405250413,4289985831&fm=26&gp=0.jpg");

        stringCommonRcvAdapter = new CommonRcvAdapter<String>(studyList) {
            ImageView ivImg;
            TextView tvTitle;

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {

                return new AdapterItem() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.near_study_item;
                    }

                    @Override
                    public void bindViews(@NonNull View root) {

                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                        layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT;
                        ivImg = root.findViewById(R.id.iv_img);
                        tvTitle = root.findViewById(R.id.tv_title);
                    }

                    @Override
                    public void setViews() {

                    }

                    @Override
                    public void handleData(Object o, int position) {
                        Glide.with(getActivity()).load(o.toString()).into(ivImg);
                        tvTitle.setText("阶能力提升训练营阶能力 提升训练营 ");
                    }
                };
            }
        };
        recycleStudy.setAdapter(stringCommonRcvAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
        recycleStudy.addItemDecoration(new SpacesItemDecoration(32, 0, 32, 0));
        recycleStudy.setLayoutManager(linearLayoutManager);
    }

    private List<PersonMenuItem> menuItemList;
    private CommonRcvAdapter<PersonMenuItem> menuItemCommonRcvAdapter;

    void initMenu() {
        menuItemList = new ArrayList<>();
        menuItemList.add(new PersonMenuItem(R.drawable.jf, "积分"));
        menuItemList.add(new PersonMenuItem(R.drawable.dd, "订单"));
        menuItemList.add(new PersonMenuItem(R.drawable.jxzh, "奖学账户"));
        menuItemList.add(new PersonMenuItem(R.drawable.xxjh, "学习计划"));
        menuItemList.add(new PersonMenuItem(R.drawable.yhk, "银行卡"));
        menuItemList.add(new PersonMenuItem(R.drawable.xz, "下载"));
        menuItemList.add(new PersonMenuItem(R.drawable.sc, "收藏"));
        menuItemList.add(new PersonMenuItem(R.drawable.xqsj, "需求收集"));
        menuItemList.add(new PersonMenuItem(R.drawable.kqzb, "开启直播"));
        menuItemList.add(new PersonMenuItem(R.drawable.jfsc, "积分商城"));
        menuItemList.add(new PersonMenuItem(R.drawable.xunzhang, "勋章"));
        menuItemList.add(new PersonMenuItem(R.drawable.sz, "设置"));
        menuItemCommonRcvAdapter = new CommonRcvAdapter<PersonMenuItem>(menuItemList) {
            ImageView ivImg;
            TextView tvTitle;

            LinearLayout llRoot;

            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new AdapterItem() {
                    @Override
                    public int getLayoutResId() {
                        return R.layout.person_menu_item;
                    }

                    @Override
                    public void bindViews(@NonNull View root) {
                        ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
                        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                        ivImg = root.findViewById(R.id.iv_img);
                        tvTitle = root.findViewById(R.id.tv_title);
                        llRoot = root.findViewById(R.id.ll_root);
                    }

                    @Override
                    public void setViews() {

                    }

                    @Override
                    public void handleData(Object o, int position) {
                        if (o instanceof PersonMenuItem) {
                            PersonMenuItem personMenuItem = (PersonMenuItem) o;
                            tvTitle.setText(((PersonMenuItem) o).getTitle());
                            ivImg.setImageResource(((PersonMenuItem) o).getImg());
                            llRoot.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    menuClickCallback(personMenuItem.getTitle());
                                }
                            });

                        }

                    }
                };
            }
        };
        recycleItem.setAdapter(menuItemCommonRcvAdapter);
        recycleItem.addItemDecoration(new SpacesItemDecoration(2, 2, 0, 0));
        recycleItem.setLayoutManager(new GridLayoutManager(getActivity(), 4));
    }

    @OnClick({R.id.ll_user_info, R.id.tv_login, R.id.iv_headimg, R.id.tv_name, R.id.ll_sign, R.id.rl_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_user_info:
                break;
            case R.id.tv_login:
                RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), LoginActivity.class));
                break;
            case R.id.iv_headimg:
                if (App.getInstance().isLogin(getActivity(), true)) {
                    RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), EditHeadAndNickActivity.class));
                } else {
                    RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.tv_name:
                if (App.getInstance().isLogin(getActivity(), true)) {
                    RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), EditHeadAndNickActivity.class));
                } else {
                    RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), LoginActivity.class));
                }
                break;
            case R.id.ll_sign:
                App.getInstance().getUserInfo(getActivity(), new UserInfoCallback() {
                    @Override
                    public void getUserInfo(UserInfo userInfo) {
                        if(userInfo.isPlatformAccount()){
                            RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), PlatformAccountSignActivity.class));
                        }else{
                            RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), SignActivity.class));
                        }
                    }
                });
                break;
            case R.id.rl_vip:
                if (App.getInstance().isLogin(getActivity(), true)) {
                    RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), VipDetailActivity.class));
                }
                break;
        }
    }


    void menuClickCallback(String title) {
        if(App.getInstance().isLogin(getActivity(),true)){
            switch (title) {
                case "订单":
                    RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), MyOrderActivity.class));
                    break;
                case "设置":
                    RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), SettingActivity.class));
                    break;
                case "开启直播":
                    RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), AuthorActivity.class));
                    break;
                case "学习计划":
                    RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), StudyPlanActivity.class));
                    break;
                case "积分":
                    App.getInstance().getUserInfo(getActivity(), new UserInfoCallback() {
                        @Override
                        public void getUserInfo(UserInfo userInfo) {
                            if(userInfo.isPlatformAccount()){
                                RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), PlatformAccountSignActivity.class));
                            }else{
                                RouteUtil.startNewActivity(getActivity(), new Intent(getActivity(), SignActivity.class));
                            }
                        }
                    });
                    break;
            }
        }

    }

    @OnClick(R.id.tv_login)
    public void onViewClicked() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Success(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals(FinalValue.LOGIN_SUCCESS)
        ) {
//            getUserInfo();
            initView();
            initStudy();
        } else if (
                messageEvent.getMessage().equals(FinalValue.LOGOUT)) {

            initView();
            initStudy();
        } else if (
                messageEvent.getMessage().equals(FinalValue.GET_USERINFO)) {
            initView();
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
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }


    void getUserInfo() {
        ApiUtils.getInstance().getUserInfo(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                refreshLayout.finishRefresh(true);
                UserInfo userInfo = GsonUtil.GsonToBean(t.getData().toString(), UserInfo.class);
                App.getInstance().setUserInfo(userInfo);
                initView();
                //    EventBus.getDefault().post(new MessageEvent(FinalValue.GET_USERINFO, 0));
            }

            @Override
            public void error(String code, String msg) {
                ToastUtils.show(getActivity(), msg);
                refreshLayout.finishRefresh(false);
            }

            @Override
            public void expcetion(String e) {
                refreshLayout.finishRefresh(false);

            }
        });
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getUserInfo();
    }


}
