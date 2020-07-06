package com.qulink.hxedu;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.qulink.hxedu.callback.DefaultSettingCallback;
import com.qulink.hxedu.callback.UserInfoCallback;
import com.qulink.hxedu.entity.DefaultSetting;
import com.qulink.hxedu.entity.MessageEvent;
import com.qulink.hxedu.entity.UserInfo;
import com.qulink.hxedu.jpush.TagAliasOperatorHelper;
import com.qulink.hxedu.service.LoadingService;
import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.fragment.IndexFragment;
import com.qulink.hxedu.ui.fragment.LiveFragment;
import com.qulink.hxedu.ui.fragment.PersonFragment;
import com.qulink.hxedu.ui.zone.ZoneFragment;
import com.qulink.hxedu.util.FinalValue;
import com.qulink.hxedu.util.PrefUtils;
import com.qulink.hxedu.util.SystemUtil;
import com.zzhoujay.richtext.RichText;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity {

    @BindView(R.id.rb_index)
    RadioButton rbIndex;
    @BindView(R.id.rb_live)
    RadioButton rbLive;
    @BindView(R.id.rb_zone)
    RadioButton rbZone;
    @BindView(R.id.rb_person)
    RadioButton rbPerson;
    @BindView(R.id.fragment_contanier)
    LinearLayout fragmentContanier;
    @BindView(R.id.tab_parent)
    RadioGroup tabParent;
    private int selectTab;

    private IndexFragment indexFragment;
    private LiveFragment liveFragment;
    private ZoneFragment zoneFragment;
    private PersonFragment personFragment;
    private FragmentManager fragmentManager;
    private String INDEX_FRAGMENT_TAG = "index";
    private String LIVE_FRAGMENT_TAG = "live";
    private String ZONE_FRAGMENT_TAG = "zone";
    private String PERSON_FRAGMENT_TAG = "person";

    private final int SET_ALIAS = 6001;
    private final int DELETE_ALIAS = 6002;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case SET_ALIAS:
                    JPushInterface.setAlias(MainActivity.this,FinalValue.jpushScene,msg.obj.toString());

                    break;
                case DELETE_ALIAS:
                    JPushInterface.deleteAlias(MainActivity.this,2);
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        ButterKnife.bind(this);
        initFragment(savedInstanceState);
        JPushInterface.init(this);
        JPushInterface.getAlias(this,3);
        handler.sendEmptyMessage(DELETE_ALIAS);
        Message message = new Message();
        message.obj = 12+"";
        message.what=SET_ALIAS;
        handler.sendMessage(message);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {

        rbIndex.setChecked(true);

        App.getInstance().getDefaultSetting(this, new DefaultSettingCallback() {
            @Override
            public void getDefaultSetting(DefaultSetting defaultSetting) {
                checkVersion(defaultSetting);
            }
        });
    }

    @Override
    protected boolean enableGestureBack() {
        return false;
    }

    private boolean isQuit;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {

            if (isQuit == false) {
                isQuit = true;
                Toast.makeText(getBaseContext(), "再按一次退出", Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isQuit = false;
                    }
                }, 2000);
            } else {
                finish();

            }
        }
        return true;
    }

    void initFragment(Bundle savedInstanceState) {

        fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            indexFragment = (IndexFragment) fragmentManager.findFragmentByTag(INDEX_FRAGMENT_TAG);
            liveFragment = (LiveFragment) fragmentManager.findFragmentByTag(LIVE_FRAGMENT_TAG);
            zoneFragment = (ZoneFragment) fragmentManager.findFragmentByTag(ZONE_FRAGMENT_TAG);
            personFragment = (PersonFragment) fragmentManager.findFragmentByTag(PERSON_FRAGMENT_TAG);
        } else {
            indexFragment = new IndexFragment();
            liveFragment = new LiveFragment();
            zoneFragment = new ZoneFragment();
            personFragment = new PersonFragment();
        }


        if (!indexFragment.isAdded() && (IndexFragment) fragmentManager.findFragmentByTag(INDEX_FRAGMENT_TAG) == null) {
            fragmentManager.beginTransaction().add(R.id.fragment_contanier, indexFragment, INDEX_FRAGMENT_TAG).commit();
        }
        if (!liveFragment.isAdded() && (LiveFragment) fragmentManager.findFragmentByTag(LIVE_FRAGMENT_TAG) == null) {
            fragmentManager.beginTransaction().add(R.id.fragment_contanier, liveFragment, LIVE_FRAGMENT_TAG).commit();
        }
        if (!zoneFragment.isAdded() && (ZoneFragment) fragmentManager.findFragmentByTag(ZONE_FRAGMENT_TAG) == null) {
            fragmentManager.beginTransaction().add(R.id.fragment_contanier, zoneFragment, ZONE_FRAGMENT_TAG).commit();
        }
        if (!personFragment.isAdded() && (PersonFragment) fragmentManager.findFragmentByTag(PERSON_FRAGMENT_TAG) == null) {
            fragmentManager.beginTransaction().add(R.id.fragment_contanier, personFragment, PERSON_FRAGMENT_TAG).commit();
        }
    }

    @OnClick({R.id.rb_index, R.id.rb_live, R.id.rb_zone, R.id.rb_person})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_index:
                rbIndex.setChecked(true);
                fragmentManager.beginTransaction().hide(liveFragment).hide(zoneFragment).hide(personFragment).show(indexFragment).commit();
                break;
            case R.id.rb_live:
                rbLive.setChecked(true);
                fragmentManager.beginTransaction().hide(indexFragment).hide(zoneFragment).hide(personFragment).show(liveFragment).commit();

                break;
            case R.id.rb_zone:
                rbZone.setChecked(true);
                fragmentManager.beginTransaction().hide(liveFragment).hide(indexFragment).hide(personFragment).show(zoneFragment).commit();
                break;
            case R.id.rb_person:
                rbPerson.setChecked(true);
                fragmentManager.beginTransaction().hide(liveFragment).hide(zoneFragment).hide(indexFragment).show(personFragment).commit();

                break;
        }
    }
    public static int sequence = 1;


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void Success(MessageEvent messageEvent) {
        if (messageEvent.getMessage().equals(FinalValue.TOKEN_ERROR)
        ) {
//            DialogUtil.showAlertDialog(this, "提示", "登陆状态已过期，请重新登陆", "确定", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                    App.getInstance().logout();
//                    EventBus.getDefault().post(new MessageEvent(FinalValue.LOGOUT, 0));
//                    RouteUtil.startNewActivity(MainActivity.this, new Intent(MainActivity.this, LoginActivity.class));
//                }
//            }, "一会再说", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });

        }else if(messageEvent.getMessage().equals(FinalValue.SET_ALIS)){
            Message message = new Message();
            message.what = SET_ALIAS;
            message.obj = messageEvent.getCode();
            handler.sendMessage(message);

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


    private void checkVersion(DefaultSetting defaultSetting){
        try {
            if(SystemUtil.compareVersion(SystemUtil.getVersionName(MainActivity.this),defaultSetting.getAndroid_version().getValue())==-1){
                String path = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)+"/"+defaultSetting.getAndroid_version().getValue()+"-hxedu.apk";
                PrefUtils.putString(MainActivity.this,"apk_url",defaultSetting.getAndroid_url().getValue());
                PrefUtils.putString(MainActivity.this,"apk_path",path);
                showVersionDialog(defaultSetting.getAndroid_content().getValue(),defaultSetting.getAndroid_version().getValue(),path);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void showVersionDialog(String content,String version,String path){
        View diaView = View.inflate(this, R.layout.version_dialogg, null);

        Dialog dialog = new Dialog(MainActivity.this, R.style.my_dialog);
        diaView.findViewById(R.id.tv_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                loadAPK(path);
            }
        });
        diaView.findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        TextView tvVersionDesc = diaView.findViewById(R.id.tv_version_desc);
        TextView tvContent = diaView.findViewById(R.id.tv_content);
        tvVersionDesc.setText("v"+version+"更新说明");
        tvContent.setText(content);
        RichText.from(content).into(tvContent);

        dialog.setContentView(diaView);
        ViewGroup.LayoutParams layoutParams = diaView.getLayoutParams();
        //设置为全屏的宽
        layoutParams.width = getResources().getDisplayMetrics().widthPixels/10*8;
        dialog.setCanceledOnTouchOutside(false);

        diaView.setLayoutParams(layoutParams);
        dialog.show();
    }
    private void loadAPK(String path){

        File file = new File(path);
        if(file.exists()){
            file.delete();
        }
        LoadingService.startUploadImg(MainActivity.this);
    }
}
