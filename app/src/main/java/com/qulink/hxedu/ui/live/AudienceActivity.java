package com.qulink.hxedu.ui.live;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qulink.hxedu.App;
import com.qulink.hxedu.R;
import com.qulink.hxedu.adapter.LiveMsgAdapter;
import com.qulink.hxedu.api.ApiCallback;
import com.qulink.hxedu.api.ApiUtils;
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.api.ResponseData;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.LiveInfoBean;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.live.liveroom.IMLVBLiveRoomListener;
import com.qulink.hxedu.ui.live.liveroom.MLVBLiveRoom;
import com.qulink.hxedu.ui.live.liveroom.debug.GenerateTestUserSig;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.AnchorInfo;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.AudienceInfo;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.AudienceMsg;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.AudienceNotice;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.AuthorMsg;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.AuthorNotice;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.CmdBean;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.LoginInfo;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.RoomInfo;
import com.qulink.hxedu.ui.live.liveroom.roomutil.commondef.SysMsg;
import com.qulink.hxedu.util.DialogUtil;
import com.qulink.hxedu.util.ImageUtils;
import com.qulink.hxedu.util.SystemUtil;
import com.qulink.hxedu.util.ToastUtils;
import com.qulink.hxedu.view.SoftKeyboardStateHelper;
import com.qulink.hxedu.view.SpacesItemDecoration;
import com.qulink.hxedu.view.like.TCHeartLayout;
import com.tencent.rtmp.ui.TXCloudVideoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import kale.adapter.CommonRcvAdapter;
import kale.adapter.item.AdapterItem;

public class AudienceActivity extends BaseActivity implements IMLVBLiveRoomListener {

    String TAG = "AudienceActivity";
    @BindView(R.id.pusher_tx_cloud_view)
    TXCloudVideoView pusherTxCloudView;

    MLVBLiveRoom mlvbLiveRoom;
    @BindView(R.id.recycle_msg)
    RecyclerView recycleMsg;
    @BindView(R.id.et_comment)
    EditText etComment;
    @BindView(R.id.iv_author_img)
    CircleImageView ivAuthorImg;
    @BindView(R.id.tv_author_name)
    TextView tvAuthorName;
    @BindView(R.id.recycle_audience)
    RecyclerView recycleAudience;
    @BindView(R.id.tv_audience_number)
    TextView tvAudienceNumber;

    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.rl_contro)
    RelativeLayout rlContro;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.ll_sb_Ui)
    LinearLayout llSbUi;
    @BindView(R.id.iv_like)
    ImageView ivLike;
    @BindView(R.id.iv_share)
    ImageView ivShare;
    @BindView(R.id.heart_layout)
    TCHeartLayout heartLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_audience;
    }

    @Override
    protected void init() {

        liveId = getIntent().getIntExtra("id", 0);
        //启动本地摄像头预览
        mlvbLiveRoom = MLVBLiveRoom.sharedInstance(this);
//        TXLivePushConfig mLivePushConfig = new TXLivePushConfig();
//        mLivePusher = new TXLivePusher(this);
//        TXCloudVideoView mPusherView = (TXCloudVideoView) findViewById(R.id.pusher_tx_cloud_view);
//        mLivePusher.startCameraPreview(mPusherView);
        mlvbLiveRoom.setListener(this);
        setListenerFotEditText(rlRoot);

        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {//这里可以做具体的操作
                    //searchSubject(etSearch.getText().toString());

                    if (TextUtils.isEmpty(etComment.getText().toString())) {
                        ToastUtils.show(AudienceActivity.this, "请输入内容");
                    } else {
                        sendmsg();
                    }
                    return true;
                }
                return false;
            }
        });

        getLiveDetail();
    }

    private int liveId;
    LiveInfoBean liveInfoBean;

    private void getLiveDetail() {
        DialogUtil.showLoading(this, true, "请稍候");
        ApiUtils.getInstance().getLiveDetail(liveId, new ApiCallback() {
            @Override
            public void success(ResponseData t) {
                DialogUtil.hideLoading(AudienceActivity.this);
                liveInfoBean = GsonUtil.GsonToBean(GsonUtil.GsonString(t.getData()), LiveInfoBean.class);
                startLive(liveInfoBean);
            }

            @Override
            public void error(String code, String msg) {
                DialogUtil.hideLoading(AudienceActivity.this);
                startLive(null);
                showFailDialog(msg);
            }

            @Override
            public void expcetion(String expectionMsg) {
                DialogUtil.hideLoading(AudienceActivity.this);

                showFailDialog(expectionMsg);

            }
        });
    }

    private void sendmsg() {
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                CmdBean cmdBean = new CmdBean();
                cmdBean.setLevel(userInfo.getLevel());
                cmdBean.setType(LiveParam.AUDIENCE_MSG);
                mlvbLiveRoom.sendRoomCustomMsg(GsonUtil.GsonString(cmdBean), etComment.getText().toString(), new SendRoomCustomMsgCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        ToastUtils.show(AudienceActivity.this, errInfo);
                    }

                    @Override
                    public void onSuccess() {
                        SystemUtil.closeKeybord(etComment, AudienceActivity.this);
                        AnchorInfo anchorInfo = new AnchorInfo();
                        anchorInfo.userAvatar = userInfo.getHeadImg();
                        anchorInfo.userName = userInfo.getNickname();
                        anchorInfo.userID = userInfo.getId() + "";
                        addMsg(new AuthorMsg(anchorInfo, etComment.getText().toString()));

                        etComment.setText("");
                    }
                });
            }
        });

    }


    //监听软键盘的打开和关闭
    private void setListenerFotEditText(View view) {
        SoftKeyboardStateHelper softKeyboardStateHelper = new SoftKeyboardStateHelper(view);
        softKeyboardStateHelper.addSoftKeyboardStateListener(new SoftKeyboardStateHelper.SoftKeyboardStateListener() {
            @Override
            public void onSoftKeyboardOpened(int keyboardHeightInPx) {
                //键盘打开
                // showSearchResult();
                etComment.setVisibility(View.VISIBLE);
                etComment.requestFocus();
            }

            @Override
            public void onSoftKeyboardClosed() {
                //键盘关闭
                // resetCommentType();
                etComment.setVisibility(View.GONE);
                recycleMsg.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }

    private void likeClick() {
        heartLayout.addFavor();
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {

                CmdBean cmdBean = new CmdBean();
                cmdBean.setLevel(userInfo.getLevel());
                cmdBean.setType(LiveParam.ANCHOR_NOTICE);
                mlvbLiveRoom.sendRoomCustomMsg(GsonUtil.GsonString(cmdBean), LiveParam.LIKE_DESC, new SendRoomCustomMsgCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        ToastUtils.show(AudienceActivity.this, errInfo);
                    }

                    @Override
                    public void onSuccess() {
                        AudienceInfo anchorInfo = new AudienceInfo();
                        anchorInfo.userAvatar = userInfo.getHeadImg();
                        anchorInfo.userName = userInfo.getNickname();
                        anchorInfo.userID = userInfo.getId() + "";
                        addMsg(new AudienceNotice(anchorInfo, LiveParam.LIKE_DESC));

                    }
                });
            }
        });

    }
    private void enterRoom() {
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {

                CmdBean cmdBean = new CmdBean();
                cmdBean.setLevel(userInfo.getLevel());
                cmdBean.setType(LiveParam.AUDIENCE_NOTICE);
                mlvbLiveRoom.sendRoomCustomMsg(GsonUtil.GsonString(cmdBean), LiveParam.ENTER_DESC, new SendRoomCustomMsgCallback() {
                    @Override
                    public void onError(int errCode, String errInfo) {
                        ToastUtils.show(AudienceActivity.this, errInfo);
                    }

                    @Override
                    public void onSuccess() {
//                        AudienceInfo anchorInfo = new AudienceInfo();
//                        anchorInfo.userAvatar = userInfo.getHeadImg();
//                        anchorInfo.userName = userInfo.getNickname();
//                        anchorInfo.userID = userInfo.getId() + "";
//                        addMsg(new AudienceNotice(anchorInfo, LiveParam.ENTER_DESC));

                    }
                });
            }
        });

    }

    @OnClick({R.id.iv_like, R.id.iv_share, R.id.ll_sb_Ui, R.id.iv_close2, R.id.tv_comment})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_like:
                likeClick();
                break;
            case R.id.ll_sb_Ui:
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_close2:
                showleaveDialog();

                break;

            case R.id.tv_comment:
                SystemUtil.openKeybord(etComment, AudienceActivity.this);
                break;
        }
    }

    private void showFailDialog(String msg) {
        DialogUtil.showAlertDialog(AudienceActivity.this, "提示", msg, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
    }

    void startLive(LiveInfoBean liveInfoBean) {
        //98946.livepush.myqcloud.com
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                LoginInfo loginInfo = new LoginInfo();
                loginInfo.sdkAppID = GenerateTestUserSig.SDKAPPID;
                loginInfo.userID = userInfo.getId() + "";
                loginInfo.userSig = GenerateTestUserSig.genTestUserSig(userInfo.getId() + "");
                loginInfo.userName = userInfo.getNickname();
                App.getInstance().getDefaultSetting(AudienceActivity.this, new DefaultSettingCallback() {
                    @Override
                    public void getDefaultSetting(DefaultSetting defaultSetting) {
                        loginInfo.userAvatar = ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), userInfo.getHeadImg());

                        if (App.getInstance().isLoginLive()) {
                            mlvbLiveRoom.getRoomList(0, 100, new GetRoomListCallback() {
                                @Override
                                public void onError(int errCode, String errInfo) {

                                }

                                @Override
                                public void onSuccess(ArrayList<RoomInfo> roomInfoList) {

                                }
                            });
                            DialogUtil.showLoading(AudienceActivity.this, true, "正在进入房间");
                            mlvbLiveRoom.enterRoom(liveInfoBean.getUserId() + "", pusherTxCloudView, new EnterRoomCallback() {
                                @Override
                                public void onError(int errCode, String errInfo) {
                                    DialogUtil.hideLoading(AudienceActivity.this);
                                    showFailDialog(errInfo);
                                }

                                @Override
                                public void onSuccess() {
                                    DialogUtil.hideLoading(AudienceActivity.this);

                                    dealOpenLiveSuc(liveInfoBean);

                                }
                            });
                        } else {
                            DialogUtil.showLoading(AudienceActivity.this, true, "请稍候");
                            mlvbLiveRoom.login(loginInfo, new LoginCallback() {
                                @Override
                                public void onError(int errCode, String errInfo) {
                                    DialogUtil.hideLoading(AudienceActivity.this);
                                    showFailDialog(errInfo);
                                }

                                @Override
                                public void onSuccess() {
                                    mlvbLiveRoom.getRoomList(0, 100, new GetRoomListCallback() {
                                        @Override
                                        public void onError(int errCode, String errInfo) {

                                        }

                                        @Override
                                        public void onSuccess(ArrayList<RoomInfo> roomInfoList) {

                                        }
                                    });
                                    DialogUtil.hideLoading(AudienceActivity.this);
                                    App.getInstance().setLoginLive(true);
                                    DialogUtil.showLoading(AudienceActivity.this, true, "正在进入房间");
                                    mlvbLiveRoom.enterRoom(liveInfoBean.getUserId() + "", pusherTxCloudView, new EnterRoomCallback() {
                                        @Override
                                        public void onError(int errCode, String errInfo) {
                                            DialogUtil.hideLoading(AudienceActivity.this);

                                            showFailDialog(errInfo);
                                        }

                                        @Override
                                        public void onSuccess() {
                                            DialogUtil.hideLoading(AudienceActivity.this);

                                            dealOpenLiveSuc(liveInfoBean);

                                        }
                                    });

                                }
                            });
                        }


                    }
                });
            }
        });


    }

    private void addMsg(Object o) {
        msglist.add(o);
        recycleMsg.getAdapter().notifyItemInserted(msglist.size());
        recycleMsg.scrollToPosition(msglist.size() - 1);
    }


    private void dealOpenLiveSuc(LiveInfoBean loginInfo) {
        rlContro.setVisibility(View.VISIBLE);
        llSbUi.setVisibility(View.VISIBLE);
        Glide.with(AudienceActivity.this).load(loginInfo.getHeadImage()).into(ivAuthorImg);
        tvAuthorName.setText(loginInfo.getTeacherName());
        initAudienceRecycle();
        initMsgRecycle();
        enterRoom();
        addMsg(new SysMsg("文明直播间，别乱所花"));
        refreshAudienceRecycle();
    }
    private void refreshAudienceRecycle(){
        mlvbLiveRoom.getAudienceList(new GetAudienceListCallback() {
            @Override
            public void onError(int errCode, String errInfo) {

            }

            @Override
            public void onSuccess(ArrayList<AudienceInfo> audienceInfoList) {
                audienceInfoList.clear();
                tvAudienceNumber.setText(audienceInfoList.size());
                audienceInfoList.addAll(audienceInfoList);
                recycleAudience.getAdapter().notifyDataSetChanged();
            }
        });
    }

    private List msglist;

    private void initMsgRecycle() {
        msglist = new ArrayList();
        recycleMsg.setAdapter(new LiveMsgAdapter(msglist, this));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycleMsg.setLayoutManager(linearLayoutManager);
        linearLayoutManager.setStackFromEnd(true);
        recycleMsg.addItemDecoration(new SpacesItemDecoration(0, 16, 0, 0));
    }

    private ArrayList<AudienceInfo> audienceInfoList;

    private void initAudienceRecycle() {
        audienceInfoList = new ArrayList<>();
        recycleAudience.setAdapter(new CommonRcvAdapter<AudienceInfo>(audienceInfoList) {
            @NonNull
            @Override
            public AdapterItem createItem(Object type) {
                return new Audienceitem();
            }
        });
        recycleAudience.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
    }

    @OnClick(R.id.tv_comment)
    public void onViewClicked() {
    }

    class Audienceitem implements AdapterItem<AudienceInfo> {
        @BindView(R.id.iv_img)
        CircleImageView ivImg;

        @Override
        public int getLayoutResId() {
            return R.layout.audience_item;
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
        public void handleData(AudienceInfo audienceInfo, int position) {
            App.getInstance().getDefaultSetting(AudienceActivity.this, new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(AudienceActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), audienceInfo.userAvatar));
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (App.getInstance().isLoginLive()) {
            mlvbLiveRoom.exitRoom(new ExitRoomCallback() {
                @Override
                public void onError(int errCode, String errInfo) {

                }

                @Override
                public void onSuccess() {

                }
            });
        }
        mlvbLiveRoom.setListener(null);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showleaveDialog();

//
        }
        return true;
    }


    private void showleaveDialog() {
        DialogUtil.showAlertDialog(AudienceActivity.this, "提示", "确定离开直播间", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        }, "点错了", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();
            }
        });
    }

    @Override
    public void onError(int errCode, String errMsg, Bundle extraInfo) {

        DialogUtil.showAlertDialog(this, "提示", errMsg, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onWarning(int warningCode, String warningMsg, Bundle extraInfo) {
        DialogUtil.showAlertDialog(AudienceActivity.this, "提示", warningMsg, "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onDebugLog(String log) {

    }

    @Override
    public void onRoomDestroy(String roomID) {
        showFailDialog("主播已关闭直播间");

    }

    @Override
    public void onAnchorEnter(AnchorInfo anchorInfo) {

    }

    @Override
    public void onAnchorExit(AnchorInfo anchorInfo) {
        showFailDialog("主播已关闭直播间");
    }

    @Override
    public void onAudienceEnter(AudienceInfo audienceInfo) {
        audienceInfoList.add(audienceInfo);
        tvAudienceNumber.setText(audienceInfoList.size() + "人");

        recycleAudience.getAdapter().notifyDataSetChanged();
        addMsg(new AudienceNotice(audienceInfo, "来了"));

    }

    @Override
    public void onAudienceExit(AudienceInfo audienceInfo) {
        for (AudienceInfo audienceInfo1 : audienceInfoList) {
            if (audienceInfo1.userID == audienceInfo.userID) {
                audienceInfoList.remove(audienceInfo1);
                break;
            }
        }
        recycleAudience.getAdapter().notifyDataSetChanged();
        tvAudienceNumber.setText(audienceInfoList.size() + "人");

        addMsg(new AudienceNotice(audienceInfo, "走了"));

    }

    @Override
    public void onRequestJoinAnchor(AnchorInfo anchorInfo, String reason) {

    }

    @Override
    public void onKickoutJoinAnchor() {

    }

    @Override
    public void onRequestRoomPK(AnchorInfo anchorInfo) {

    }

    @Override
    public void onQuitRoomPK(AnchorInfo anchorInfo) {

    }

    @Override
    public void onRecvRoomTextMsg(String roomID, String userID, String userName, String userAvatar, String message) {


    }

    @Override
    public void onRecvRoomCustomMsg(String roomID, String userID, String userName, String userAvatar, String cmd, String message) {
        CmdBean cmdBean = GsonUtil.GsonToBean(cmd, CmdBean.class);
        if (cmdBean.getType().equals(LiveParam.ANCHOR_MSG)) {
            AnchorInfo anchorInfo = new AnchorInfo();
            anchorInfo.userAvatar = userAvatar;
            anchorInfo.userName = userName;
            anchorInfo.userID = userID;
            anchorInfo.level = cmdBean.getLevel();
            addMsg(new AuthorMsg(anchorInfo, message));
        } else if (cmdBean.getType().equals(LiveParam.ANCHOR_NOTICE)) {
            AnchorInfo anchorInfo = new AnchorInfo();
            anchorInfo.userAvatar = userAvatar;
            anchorInfo.userName = userName;
            anchorInfo.userID = userID;
            anchorInfo.level = cmdBean.getLevel();

            addMsg(new AuthorNotice(anchorInfo, message));
        } else if (cmdBean.getType().equals(LiveParam.AUDIENCE_MSG)) {
            AudienceInfo anchorInfo = new AudienceInfo();
            anchorInfo.userAvatar = userAvatar;
            anchorInfo.userName = userName;
            anchorInfo.userID = userID;
            anchorInfo.level = cmdBean.getLevel();
            addMsg(new AudienceNotice(anchorInfo, message));
        }else if (cmdBean.getType().equals(LiveParam.AUDIENCE_NOTICE)) {
            AudienceInfo anchorInfo = new AudienceInfo();
            anchorInfo.userAvatar = userAvatar;
            anchorInfo.userName = userName;
            anchorInfo.userID = userID;
            anchorInfo.level = cmdBean.getLevel();
            addMsg(new AudienceNotice(anchorInfo, message));
        }

        if (message.equals(LiveParam.LIKE_DESC)) {
            heartLayout.addFavor();
        } if (message.equals(LiveParam.ENTER_DESC)) {
            refreshAudienceRecycle();

        }
    }
}
