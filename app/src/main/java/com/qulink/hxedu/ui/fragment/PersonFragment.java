package com.qulink.hxedu.ui.fragment;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.zxing.activity.CaptureActivity;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.entity.PersonMenuItem;
import com.qulink.hxedu.entity.RecentLearnBean;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.ui.AdviceActivity;
import com.qulink.hxedu.ui.BadgeDetailActivity;
import com.qulink.hxedu.ui.CollectionCourseActivity;
import com.qulink.hxedu.ui.EditHeadAndNickActivity;
import com.qulink.hxedu.ui.LevelInfoActivity;
import com.qulink.hxedu.ui.LoginActivity;
import com.qulink.hxedu.ui.MyOrderActivity;
import com.qulink.hxedu.ui.RecentLearnActivity;
import com.qulink.hxedu.ui.ScholarShipActivity;
import com.qulink.hxedu.ui.SettingActivity;
import com.qulink.hxedu.ui.StudyRelationActivity;
import com.qulink.hxedu.ui.VipDetailActivity;
import com.qulink.hxedu.ui.auth.NoRealAuthActivity;
import com.qulink.hxedu.ui.bank.BankListActivity;
import com.qulink.hxedu.ui.course.CourseDetailActivity;
import com.qulink.hxedu.ui.live.AuthorActivity;
import com.qulink.hxedu.ui.msg.MsgActivity;
import com.qulink.hxedu.ui.score.ScoreShopActivity;
import com.qulink.hxedu.ui.sign.PlatformAccountSignActivity;
import com.qulink.hxedu.ui.sign.SignActivity;
import com.qulink.hxedu.ui.sign.StudyPlanActivity;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.PermissionUtils;
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
import org.json.JSONException;
import org.json.JSONObject;

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
    @BindView(R.id.iv_msg)
    ImageView ivMsg;
    private View rootView;

    private Activity mActivity;

    private int RECENT_LEARN_CODE=22;
    private int TO_LIVE_CODE=22;
    private int OPEN_QR_CODE=333;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    public PersonFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for thiv_vip_statusis fragment
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_person, container, false);
            ButterKnife.bind(this, rootView);
            initMenu();
            addScrollviewListener();
            initView();
           if(App.getInstance().isLogin(mActivity)){
               initIndexMsgInfo();//查询是否有未读消息
               getRecentLearn();
           }
            refreshLayout.setOnRefreshListener(this);
        }
        return rootView;
    }

    void initView() {
        if (!App.getInstance().isLogin(mActivity)) {
            llUserInfo.setVisibility(View.GONE);
            //ivHeadimg.setImageResource(R.drawable.user_default);
            tvLogin.setVisibility(View.VISIBLE);
            ivHeadimg.setImageResource(R.drawable.user_default);
        } else {
            App.getInstance().getUserInfo(mActivity, new UserInfoCallback() {
                @Override
                public void getUserInfo(UserInfo userInfo) {
                    if (userInfo != null) {
                        tvName.setText(userInfo.getNickname() + "");
                        App.getInstance().getDefaultSetting(mActivity, new DefaultSettingCallback() {
                            @Override
                            public void getDefaultSetting(DefaultSetting defaultSetting) {
                                Glide.with(mActivity).load(
                                        ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), userInfo.getHeadImg())).error(R.drawable.user_default).into(ivHeadimg);
                            }
                        });
                        tvLevel.setBackgroundResource(ViewUtils.getLevelBgByLevel(userInfo.getLevel()));
                        tvLevel.setText("Lv" + userInfo.getLevel());
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

    CommonRcvAdapter<RecentLearnBean.RecentSevenDaysBean> stringCommonRcvAdapter;

    private void getRecentLearn()
    {
        ApiUtils.getInstance().recentLean(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                RecentLearnBean recentLearnBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()),RecentLearnBean.class);
                List<RecentLearnBean.RecentSevenDaysBean> list = new ArrayList<>();
                if(recentLearnBean!=null){
                    if(recentLearnBean.getRecentSevenDays()!=null){
                        list.addAll(recentLearnBean.getRecentSevenDays());
                    } if(recentLearnBean.getToday()!=null){
                        list.addAll(recentLearnBean.getToday());
                    }
                    initStudy(list);
                }
            }

            @Override
            public void error(String code, String msg) {

            }

            @Override
            public void expcetion(String expectionMsg) {

            }
        });
    }
    class RecentLearnItem implements AdapterItem<RecentLearnBean.RecentSevenDaysBean>{
        ImageView ivImg;
        TextView tvTitle;
        LinearLayout llRoot;
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
                llRoot = root.findViewById(R.id.ll_root);
            }

            @Override
            public void setViews() {

            }

            @Override
            public void handleData(RecentLearnBean.RecentSevenDaysBean o, int position) {
                App.getInstance().getDefaultSetting(mActivity, new DefaultSettingCallback() {
                    @Override
                    public void getDefaultSetting(DefaultSetting defaultSetting) {
                        Glide.with(mActivity).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(),o.getCurriculumImage())).into(ivImg);

                    }
                });
                llRoot.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mActivity, CourseDetailActivity.class);
                        intent.putExtra("courseId",o.getVideoId());
                        startActivity(intent);
                    }
                });
                tvTitle.setText(o.getCurriculumName());
            }
        };
    void initStudy(List<RecentLearnBean.RecentSevenDaysBean> list) {
        if (!App.getInstance().isLogin(mActivity) || list.isEmpty()) {
            llNearStudy.setVisibility(View.GONE);
            return;
        } else {
            llNearStudy.setVisibility(View.VISIBLE);
        }

        stringCommonRcvAdapter = new CommonRcvAdapter<RecentLearnBean.RecentSevenDaysBean>(list) {


            @NonNull
            @Override
            public AdapterItem createItem(Object type) {

                return new RecentLearnItem();
            }
        };
        recycleStudy.setAdapter(stringCommonRcvAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
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
        recycleItem.setLayoutManager(new GridLayoutManager(mActivity, 4));
    }

    @OnClick({R.id.tv_more,R.id.tv_level,R.id.iv_vip_status,R.id.iv_saoma, R.id.iv_msg, R.id.rl_study_relation, R.id.ll_user_info, R.id.tv_login, R.id.iv_headimg, R.id.tv_name, R.id.ll_sign, R.id.rl_vip})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rl_study_relation:
                if (App.getInstance().isLogin(mActivity, true)) {
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, StudyRelationActivity.class));
                }
                break;
            case R.id.ll_user_info:
                break;
            case R.id.tv_login:
                RouteUtil.startNewActivity(mActivity, new Intent(mActivity, LoginActivity.class));
                break;
            case R.id.iv_headimg:
                if (App.getInstance().isLogin(mActivity, true)) {
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, EditHeadAndNickActivity.class));
                }
                break;
                case R.id.tv_more:
                if (App.getInstance().isLogin(mActivity, true)) {

               startActivityForResult( new Intent(mActivity, RecentLearnActivity.class),RECENT_LEARN_CODE);
                }
                break;
            case R.id.tv_name:
                if (App.getInstance().isLogin(mActivity, true)) {
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, EditHeadAndNickActivity.class));
                } else {
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, LoginActivity.class));
                }
                break;
            case R.id.ll_sign:
                if (App.getInstance().isLogin(mActivity, true)) {
                    App.getInstance().getUserInfo(mActivity, new UserInfoCallback() {
                        @Override
                        public void getUserInfo(UserInfo userInfo) {
                            if (userInfo.isPlatformAccount()) {
                                RouteUtil.startNewActivity(mActivity, new Intent(mActivity, PlatformAccountSignActivity.class));
                            } else {
                                RouteUtil.startNewActivity(mActivity, new Intent(mActivity, SignActivity.class));
                            }
                        }
                    });
                }
                break;

                case R.id.tv_level:
                if (App.getInstance().isLogin(mActivity, true)) {
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, LevelInfoActivity.class));
                }
                break;
            case R.id.rl_vip:
            case R.id.iv_vip_status:
                if (App.getInstance().isLogin(mActivity, true)) {
                    App.getInstance().getUserInfo(mActivity, new UserInfoCallback() {
                        @Override
                        public void getUserInfo(UserInfo userInfo) {
                            if (userInfo.isRealAuth(getActivity())) {
                                RouteUtil.startNewActivity(mActivity, new Intent(mActivity, VipDetailActivity.class));
                            }
                        }
                    });
                }
                break;
            case R.id.iv_saoma:
                startQr();

                break;
            case R.id.iv_msg:
                if(App.getInstance().isLogin(mActivity,true)){
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, MsgActivity.class));
                }
                break;
        }
    }

    private void startQr() {

        if(                    ContextCompat.checkSelfPermission(mActivity,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED


        ){
            requestPermissions(new String[]{Manifest.permission.CAMERA},TO_LIVE_CODE);

        }else{
            Intent intent = new Intent();
            intent.setClass(mActivity, CaptureActivity.class);
            startActivity(intent);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==TO_LIVE_CODE){
            if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {
                // 权限申请成功
                RouteUtil.startNewActivity(mActivity, new Intent(mActivity, AuthorActivity.class));
            }

        }else if(requestCode==OPEN_QR_CODE){
            if (PermissionUtils.isPermissionRequestSuccess(grantResults)) {
                // 权限申请成功
                Intent intent = new Intent();
                intent.setClass(mActivity, CaptureActivity.class);
                startActivity(intent);
            }
        }

    }

    void menuClickCallback(String title) {
        if (App.getInstance().isLogin(mActivity, true)) {
            switch (title) {
                case "订单":
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, MyOrderActivity.class));
                    break;
                case "奖学账户":
                    if (App.getInstance().isLogin(mActivity, true)) {
                        App.getInstance().getUserInfo(mActivity, new UserInfoCallback() {
                            @Override
                            public void getUserInfo(UserInfo userInfo) {
                                if (userInfo.isRealAuth(getActivity())) {
                                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, ScholarShipActivity.class));
                                }
                            }
                        });
                    }
                    break;
                case "设置":
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, SettingActivity.class));
                    break;
                case "银行卡":
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, BankListActivity.class));
                    break;
                case "开启直播":


                if(                    ContextCompat.checkSelfPermission(mActivity,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED

                ||                     ContextCompat.checkSelfPermission(mActivity,Manifest.permission.RECORD_AUDIO)!= PackageManager.PERMISSION_GRANTED

                ){
                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA},TO_LIVE_CODE);

                }else{
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, AuthorActivity.class));

                }

                    break;
                case "需求收集":
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, AdviceActivity.class));
                    break;
                case "学习计划":
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, StudyPlanActivity.class));
                    break;
                case "积分商城":
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, ScoreShopActivity.class));
                    break;     case "收藏":
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, CollectionCourseActivity.class));
                    break;
                case "积分":
                    App.getInstance().getUserInfo(mActivity, new UserInfoCallback() {
                        @Override
                        public void getUserInfo(UserInfo userInfo) {
                            if (userInfo.isPlatformAccount()) {
                                RouteUtil.startNewActivity(mActivity, new Intent(mActivity, PlatformAccountSignActivity.class));
                            } else {
                                RouteUtil.startNewActivity(mActivity, new Intent(mActivity, SignActivity.class));
                            }
                        }
                    });
                    break;
                case "勋章":
                    RouteUtil.startNewActivity(mActivity, new Intent(mActivity, BadgeDetailActivity.class));
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
            getRecentLearn();
            initIndexMsgInfo();
        } else if (
                messageEvent.getMessage().equals(FinalValue.LOGOUT)) {

            initView();
          //  getRecentLearn();
        } else if (
                messageEvent.getMessage().equals(FinalValue.GET_USERINFO)) {
            initView();
        } else if (
                messageEvent.getMessage().equals(FinalValue.SIGN_SUCCESS)) {
            App.getInstance().getUserInfo(mActivity, new UserInfoCallback() {
                @Override
                public void getUserInfo(UserInfo userInfo) {
                    if (userInfo != null) {
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
                ToastUtils.show(mActivity, msg);
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
        if(App.getInstance().isLogin(mActivity)){
            initIndexMsgInfo();//查询是否有未读消息
            getRecentLearn();
            getUserInfo();
        }else{
            refreshLayout.finishRefresh(true);
        }

    }

    private void initIndexMsgInfo() {
        ApiUtils.getInstance().getIndexMsg(new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                try {
                    JSONObject jsonObject = new JSONObject(GsonUtil.GsonString(t));
                    if (jsonObject.has("data")) {
                        boolean hasMsg = jsonObject.getBoolean("data");
                        if (hasMsg) {
                            ivMsg.setImageResource(R.drawable.has_msg);
                        }else{
                            ivMsg.setImageResource(R.drawable.duihua);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void error(String code, String msg) {
                refreshLayout.finishRefresh(false);
            }

            @Override
            public void expcetion(String expectionMsg) {
                refreshLayout.finishRefresh(false);
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==RECENT_LEARN_CODE&&resultCode==-1){
            getRecentLearn();
        }
    }
}
