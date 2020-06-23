package com.qulink.hxedu.ui;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.StudyRelationBean;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.util.CourseUtil;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.QRCodeUtil;
import com.qulink.hxedu.util.RouteUtil;
import com.qulink.hxedu.util.ScreenUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.EmptyRecyclerView;
import com.qulink.hxedu.view.MyScrollView;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class StudyRelationActivity extends BaseActivity implements OnLoadMoreListener, OnRefreshListener {

    @BindView(R.id.iv_qrcode)
    ImageView ivQrcode;
    @BindView(R.id.tv_invite_code)
    TextView tvInviteCode;
    @BindView(R.id.iv_copy)
    ImageView ivCopy;
    @BindView(R.id.sc)
    MyScrollView sc;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_bar_title)
    TextView tvBarTitle;
    @BindView(R.id.tv_bar_right)
    TextView tvBarRight;
    @BindView(R.id.ll_bar)
    LinearLayout llBar;
    @BindView(R.id.tv_share)
    TextView tvShare;
    @BindView(R.id.rl_high)
    RelativeLayout rlHigh;
    @BindView(R.id.v_line)
    View vLine;
    @BindView(R.id.tv_high)
    TextView tvHigh;
    @BindView(R.id.tv_low)
    TextView tvLow;
    @BindView(R.id.recycle)
    EmptyRecyclerView recycle;
    @BindView(R.id.smart_layout)
    SmartRefreshLayout smartLayout;
    @BindView(R.id.rl_lower_bar)
    RelativeLayout rlLowerBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_study_relation;
    }

    @Override
    protected void init() {
        createQrcode();
        setTitle("学习关系");
        setBarBg(ContextCompat.getColor(this, R.color.white_transparent));
        setBackImg(R.drawable.back_white2);
        setBarTtxtColors(ContextCompat.getColor(this, R.color.white));
        getStudyRelation();
        addScrollviewListener();
    }


    private Handler handler = new Handler(){

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(qrBitmap!=null){
                ivQrcode.setImageBitmap(qrBitmap);

            }

        }
    };
    private void getQrBitMap(Bitmap bitmap){
        new Thread(new Runnable() {
            @Override
            public void run() {
                qrBitmap = QRCodeUtil.createQRCodeWithLogo(studyRelationBean.getInvitationCode(), ScreenUtil.dip2px(StudyRelationActivity.this, 200), bitmap);
          handler.sendEmptyMessage(1);
            }
        }).start();
    }
    private void dealData() {
        if (studyRelationBean == null) {
            return;
        }
        tvLow.setText(studyRelationBean.getJuniorNums() + "人");
        tvInviteCode.setText(studyRelationBean.getInvitationCode());
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
        getQrBitMap(bitmap);
        if (studyRelationBean.getSeniorName() == null || studyRelationBean.getSeniorPhone() == null) {
            rlHigh.setVisibility(View.GONE);
            vLine.setVisibility(View.GONE);
        } else {
            rlHigh.setVisibility(View.VISIBLE);

            vLine.setVisibility(View.VISIBLE);

            tvHigh.setText(studyRelationBean.getSeniorName() + "    " + studyRelationBean.getSeniorPhone());
        }
        if (studyRelationBean.getList() != null && !studyRelationBean.getList().isEmpty()) {
            recycle.setAdapter(new CommonRcvAdapter<StudyRelationBean.ListBean>(studyRelationBean.getList()) {
                @NonNull
                @Override
                public AdapterItem createItem(Object type) {
                    return new Item();
                }
            });
            recycle.setEmptyView(findViewById(R.id.empty));

            recycle.setLayoutManager(new LinearLayoutManager(this));
            rlLowerBar.setVisibility(View.VISIBLE);
        } else {
            rlLowerBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        loadMore();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        getStudyRelation();
    }

    class Item implements AdapterItem<StudyRelationBean.ListBean> {
        @BindView(R.id.iv_hg)
        ImageView ivHg;
        @BindView(R.id.tv_name)
        TextView tvName;
        @BindView(R.id.tv_sex)
        TextView tvSex;
        @BindView(R.id.tv_invite_number)
        TextView tvInviteNumber;
        @BindView(R.id.ll_root)
        RelativeLayout llRoot;

        @Override
        public int getLayoutResId() {
            return R.layout.lower_item;
        }

        @Override
        public void bindViews(@NonNull View root) {
            ViewGroup.LayoutParams layoutParams = root.getLayoutParams();
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            ButterKnife.bind(this, root);
        }

        @Override
        public void setViews() {

        }

        @Override
        public void handleData(StudyRelationBean.ListBean listBean, int position) {
            tvInviteNumber.setText(listBean.getJuniorNums() + "人");
            tvName.setText(listBean.getName());
            tvSex.setText(listBean.getSex());
            if (CourseUtil.isOk(listBean.getStatus())) {
                ivHg.setVisibility(View.VISIBLE);
            } else {
                ivHg.setVisibility(View.INVISIBLE);
            }
            llRoot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(StudyRelationActivity.this,StudySubRelationActivity.class);
                    intent.putExtra("id",listBean.getId());
                    intent.putExtra("name",listBean.getName());
                    intent.putExtra("num",listBean.getJuniorNums());
                    RouteUtil.startNewActivity(StudyRelationActivity.this,intent);
                }
            });
        }
    }

    //scorllview添加滑动监听
    void addScrollviewListener() {
        sc.setOnScrollListener(new MyScrollView.OnScrollListener() {
            @Override
            public void onScroll(int scrollY) {
                final float ratio = (float) Math.min(Math.max(scrollY, 0), llBar.getHeight()) / llBar.getHeight();
                final int newAlpha = (int) (ratio * 255);
                llBar.setBackgroundColor(Color.argb((int) newAlpha, 53, 134, 249));
            }
        });
    }

    @Override
    protected boolean enableGestureBack() {
        return true;
    }

    private void createQrcode() {
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {

            }
        });
    }

    private int page = 1;
    private int pageSize = FinalValue.limit;

    private StudyRelationBean studyRelationBean;

    private void getStudyRelation() {
        DialogUtil.showLoading(this, true);
        page = 1;
        smartLayout.setNoMoreData(false);
        ApiUtils.getInstance().getStudyRelation(page, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(StudyRelationActivity.this);
                studyRelationBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), StudyRelationBean.class);
                if (studyRelationBean.getList().size() < pageSize) {
                    smartLayout.setNoMoreData(true);
                }
                dealData();
                smartLayout.finishRefresh(true);

            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(StudyRelationActivity.this);
                ToastUtils.show(StudyRelationActivity.this, msg);
                smartLayout.finishRefresh(false);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(StudyRelationActivity.this);
                ToastUtils.show(StudyRelationActivity.this, expectionMsg);
                smartLayout.finishRefresh(false);

            }
        });
    }

    private void loadMore() {
        page++;
        ApiUtils.getInstance().getStudyRelation(page, pageSize, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(StudyRelationActivity.this);
                StudyRelationBean data = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), StudyRelationBean.class);
                if (data != null && data.getList() != null) {
                    if (data.getList().size() < pageSize) {
                        smartLayout.setNoMoreData(true);
                    }
                    studyRelationBean.getList().addAll(data.getList());
                    recycle.getAdapter().notifyDataSetChanged();
                } else {
                    smartLayout.setNoMoreData(true);

                }


                smartLayout.finishLoadMore(true);

            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(StudyRelationActivity.this);
                ToastUtils.show(StudyRelationActivity.this, msg);
                smartLayout.finishLoadMore(false);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(StudyRelationActivity.this);
                ToastUtils.show(StudyRelationActivity.this, expectionMsg);
                smartLayout.finishLoadMore(false);

            }
        });
    }

    private void copyInviteCode() {
        if (studyRelationBean == null) {
            return;
        }
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
// 将文本内容放到系统剪贴板里。
        assert cm != null;
        cm.setPrimaryClip(ClipData.newPlainText("copy", studyRelationBean.getInvitationCode()));
        ToastUtils.show(this, "邀请码已复制");
    }

    @OnClick({R.id.tv_invite_code, R.id.iv_copy, R.id.tv_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_invite_code:
                break;
            case R.id.tv_share:
                showShareDialog();
                break;
            case R.id.iv_copy:
                copyInviteCode();

                break;
        }
    }

    void showShareDialog() {
        String[] items = new String[]{"微信", "朋友圈"};
        DialogUtil.showSingleChooseDialog(this, "选择方式", items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    share(1);

                } else if (which == 1) {
                    share(2);

                }
            }
        });
    }

    private static IWXAPI api;
    private Bitmap qrBitmap;

    private void share(int type) {

        if (qrBitmap == null) {
            return;
        }
//初始化 WXImageObject 和 WXMediaMessage 对象
        WXImageObject imgObj = new WXImageObject(qrBitmap);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

//设置缩略图
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//        bmp.recycle();
//        msg.thumbData = Util.bmpToByteArray(thumbBmp, true);

//构造一个Req
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = System.currentTimeMillis() + "";
        req.message = msg;
        req.scene = type == 1 ? SendMessageToWX.Req.WXSceneSession : SendMessageToWX.Req.WXSceneTimeline;
        //req.userOpenId = getOpenId();
//调用api接口，发送数据到微信
        if (api == null) {
            api = WXAPIFactory.createWXAPI(this, FinalValue.WECHAT_APP_ID);

        }
        api.sendReq(req);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(handler!=null){
            handler.removeCallbacksAndMessages(null);
            handler = null;
        }
        if (qrBitmap != null) {
            qrBitmap.recycle();

        }
    }
}
