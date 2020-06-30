package com.qulink.hxedu.ui.live;

import android.content.DialogInterface;
import android.graphics.BitmapFactory;
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
import com.qulink.hxedu.api.GsonUtil;
import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.DefaultSetting;
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

public class AuthorActivity extends BaseActivity implements IMLVBLiveRoomListener {

    String TAG = "AuthorActivity";
    @BindView(R.id.pusher_tx_cloud_view)
    TXCloudVideoView pusherTxCloudView;
    @BindView(R.id.status)
    TextView status;
    @BindView(R.id.iv_turn)
    ImageView ivTurn;
    @BindView(R.id.iv_close)
    ImageView ivClose;
    @BindView(R.id.iv_share_wx)
    ImageView ivShareWx;
    @BindView(R.id.iv_share_qq)
    ImageView ivShareQq;
    @BindView(R.id.iv_share_zone)
    ImageView ivShareZone;
    @BindView(R.id.tv_start_ive)
    TextView tvStartIve;

    @BindView(R.id.ll_preview)
    LinearLayout llPreview;
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

    LoginInfo loginInfo;
    @BindView(R.id.tv_comment)
    TextView tvComment;
    @BindView(R.id.rl_contro)
    RelativeLayout rlContro;
    @BindView(R.id.rl_root)
    RelativeLayout rlRoot;
    @BindView(R.id.iv_close2)
    ImageView ivClose2;
    @BindView(R.id.ll_sb_Ui)
    LinearLayout llSbUi;
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
        return R.layout.activity_author;
    }

    @Override
    protected void init() {


        //启动本地摄像头预览
        mlvbLiveRoom = MLVBLiveRoom.sharedInstance(this);
//        TXLivePushConfig mLivePushConfig = new TXLivePushConfig();
//        mLivePusher = new TXLivePusher(this);
//        TXCloudVideoView mPusherView = (TXCloudVideoView) findViewById(R.id.pusher_tx_cloud_view);
//        mLivePusher.startCameraPreview(mPusherView);
        mlvbLiveRoom.startLocalPreview(true, pusherTxCloudView);
        mlvbLiveRoom.setListener(this);
        setListenerFotEditText(rlRoot);

        etComment.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {//这里可以做具体的操作
                    //searchSubject(etSearch.getText().toString());

                    if (TextUtils.isEmpty(etComment.getText().toString())) {
                        ToastUtils.show(AuthorActivity.this, "请输入内容");
                    } else {
                        sendmsg();
                    }
                    return true;
                }
                return false;
            }
        });
    }

    private void sendmsg() {
        CmdBean cmdBean = new CmdBean();
        cmdBean.setLevel(loginInfo.level);
        cmdBean.setType(LiveParam.ANCHOR_MSG);
        mlvbLiveRoom.sendRoomCustomMsg(GsonUtil.GsonString(cmdBean), etComment.getText().toString(), new SendRoomCustomMsgCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                    ToastUtils.show(AuthorActivity.this,errInfo);
            }

            @Override
            public void onSuccess() {
                AnchorInfo anchorInfo = new AnchorInfo();
                anchorInfo.userAvatar = loginInfo.userAvatar;
                anchorInfo.userName = loginInfo.userName;
                anchorInfo.userID = loginInfo.userID;
                anchorInfo.level = cmdBean.getLevel();
                addMsg(new AuthorMsg(anchorInfo, etComment.getText().toString()));
                SystemUtil.closeKeybord(etComment, AuthorActivity.this);

                etComment.setText("");
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

    private void likeClick(){
        heartLayout.addFavor();

        CmdBean cmdBean = new CmdBean();
        cmdBean.setLevel(loginInfo.level);
        cmdBean.setType(LiveParam.ANCHOR_NOTICE);
        mlvbLiveRoom.sendRoomCustomMsg(GsonUtil.GsonString(cmdBean), etComment.getText().toString(), new SendRoomCustomMsgCallback() {
            @Override
            public void onError(int errCode, String errInfo) {
                ToastUtils.show(AuthorActivity.this,errInfo);
            }

            @Override
            public void onSuccess() {
                AnchorInfo anchorInfo = new AnchorInfo();
                anchorInfo.userAvatar = loginInfo.userAvatar;
                anchorInfo.userName = loginInfo.userName;
                anchorInfo.userID = loginInfo.userID;
                addMsg(new AuthorNotice(anchorInfo, LiveParam.LIKE_DESC));

            }
        });
    }
    @OnClick({R.id.iv_like,R.id.iv_share, R.id.ll_sb_Ui, R.id.iv_close2, R.id.tv_comment, R.id.iv_turn, R.id.iv_close, R.id.iv_share_wx, R.id.iv_share_qq, R.id.iv_share_zone, R.id.tv_start_ive})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_like:
                likeClick();
                break;
            case R.id.ll_sb_Ui:
                break;
            case R.id.iv_turn:
                mlvbLiveRoom.switchCamera();
                break;
            case R.id.iv_share:
                break;
            case R.id.iv_close:
                if (loginInfo == null) {
                    finish();
                } else {
                    showleaveDialog();
                }
                break;
            case R.id.iv_close2:
                if (loginInfo == null) {
                    finish();
                } else {
                    showleaveDialog();
                }
                break;
            case R.id.iv_share_wx:
                break;
            case R.id.iv_share_qq:
                break;
            case R.id.iv_share_zone:
                break;
            case R.id.tv_start_ive:
                startLive();
                break;
            case R.id.tv_comment:
                SystemUtil.openKeybord(etComment, AuthorActivity.this);
                break;
        }
    }

    void startLive() {
        //98946.livepush.myqcloud.com
        App.getInstance().getUserInfo(this, new UserInfoCallback() {
            @Override
            public void getUserInfo(UserInfo userInfo) {
                loginInfo = new LoginInfo();
                loginInfo.sdkAppID = GenerateTestUserSig.SDKAPPID;
                loginInfo.userID = userInfo.getId() + "";
                loginInfo.level = userInfo.getLevel();
                loginInfo.userSig = GenerateTestUserSig.genTestUserSig(userInfo.getId() + "");
                loginInfo.userName = userInfo.getNickname();
                App.getInstance().getDefaultSetting(AuthorActivity.this, new DefaultSettingCallback() {
                    @Override
                    public void getDefaultSetting(DefaultSetting defaultSetting) {
                        loginInfo.userAvatar = ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), userInfo.getHeadImg());
                       if(App.getInstance().isLoginLive()){
                           DialogUtil.showLoading(AuthorActivity.this, true, "正在创建房间");

                           mlvbLiveRoom.createRoom(userInfo.getId() + "", userInfo.getNickname(), new CreateRoomCallback() {
                               @Override
                               public void onError(int errCode, String errInfo) {
                                   DialogUtil.hideLoading(AuthorActivity.this);

                                   ToastUtils.show(AuthorActivity.this, errInfo);
                               }

                               @Override
                               public void onSuccess(String RoomID) {
                                   DialogUtil.hideLoading(AuthorActivity.this);

                                   dealOpenLiveSuc(loginInfo);
                               }
                           });

                       }else{
                           DialogUtil.showLoading(AuthorActivity.this, true, "请稍候");
                           mlvbLiveRoom.login(loginInfo, new LoginCallback() {
                               @Override
                               public void onError(int errCode, String errInfo) {
                                   ToastUtils.show(AuthorActivity.this, errInfo);
                                   DialogUtil.hideLoading(AuthorActivity.this);
                               }

                               @Override
                               public void onSuccess() {
                                   DialogUtil.hideLoading(AuthorActivity.this);

                                   DialogUtil.showLoading(AuthorActivity.this, true, "正在创建房间");

                                   mlvbLiveRoom.createRoom(userInfo.getId() + "", userInfo.getNickname(), new CreateRoomCallback() {
                                       @Override
                                       public void onError(int errCode, String errInfo) {
                                           DialogUtil.hideLoading(AuthorActivity.this);

                                           ToastUtils.show(AuthorActivity.this, errInfo);
                                       }

                                       @Override
                                       public void onSuccess(String RoomID) {
                                           DialogUtil.hideLoading(AuthorActivity.this);

                                           dealOpenLiveSuc(loginInfo);
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



    private void dealOpenLiveSuc(LoginInfo loginInfo) {
        mlvbLiveRoom.setCameraMuteImage(BitmapFactory.decodeResource(getResources(), R.drawable.pause_publish));

        llPreview.setVisibility(View.GONE);
        rlContro.setVisibility(View.VISIBLE);
        llSbUi.setVisibility(View.VISIBLE);
        Glide.with(AuthorActivity.this).load(loginInfo.userAvatar).into(ivAuthorImg);
        tvAuthorName.setText(loginInfo.userName);
        initAudienceRecycle();
        initMsgRecycle();
        addMsg(new SysMsg("文明直播间，别乱所花"));
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
            App.getInstance().getDefaultSetting(AuthorActivity.this, new DefaultSettingCallback() {
                @Override
                public void getDefaultSetting(DefaultSetting defaultSetting) {
                    Glide.with(AuthorActivity.this).load(ImageUtils.splitImgUrl(defaultSetting.getImg_assets_url().getValue(), audienceInfo.userAvatar));
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(App.getInstance().isLoginLive()){
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
            if (loginInfo != null) {
                showleaveDialog();
            } else {
                finish();
            }
//
        }
        return true;
    }


    private void showleaveDialog() {
        DialogUtil.showAlertDialog(AuthorActivity.this, "提示", "确定结束直播", "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        DialogUtil.showAlertDialog(AuthorActivity.this, "提示", warningMsg, "确定", new DialogInterface.OnClickListener() {
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

    }

    @Override
    public void onAnchorEnter(AnchorInfo anchorInfo) {

    }

    @Override
    public void onAnchorExit(AnchorInfo anchorInfo) {

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

        CmdBean cmdBean = GsonUtil.GsonToBean(cmd,CmdBean.class);
        if(cmdBean.getType().equals(LiveParam.ANCHOR_MSG)){
            AnchorInfo anchorInfo = new AnchorInfo();
            anchorInfo.userAvatar = userAvatar;
            anchorInfo.userName = userName;
            anchorInfo.userID = userID;
            anchorInfo.level = cmdBean.getLevel();
            addMsg(new AuthorMsg(anchorInfo, message));
        }else  if(cmdBean.getType().equals(LiveParam.ANCHOR_NOTICE)){
            AnchorInfo anchorInfo = new AnchorInfo();
            anchorInfo.userAvatar = userAvatar;
            anchorInfo.userName = userName;
            anchorInfo.userID = userID;
            anchorInfo.level = cmdBean.getLevel();

            addMsg(new AuthorNotice(anchorInfo, message));
        }else  if(cmdBean.getType().equals(LiveParam.AUDIENCE_MSG)){
            AudienceInfo anchorInfo = new AudienceInfo();
            anchorInfo.userAvatar = userAvatar;
            anchorInfo.userName = userName;
            anchorInfo.userID = userID;
            anchorInfo.level = cmdBean.getLevel();
            addMsg(new AudienceNotice(anchorInfo, message));
        }

        if(message.equals(LiveParam.LIKE_DESC)){
            heartLayout.addFavor();
        }
    }
}
