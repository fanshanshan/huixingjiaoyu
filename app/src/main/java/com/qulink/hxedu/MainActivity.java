package com.qulink.hxedu;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;

import androidx.fragment.app.FragmentManager;

import com.qulink.hxedu.ui.BaseActivity;
import com.qulink.hxedu.ui.fragment.IndexFragment;
import com.qulink.hxedu.ui.fragment.LiveFragment;
import com.qulink.hxedu.ui.fragment.PersonFragment;
import com.qulink.hxedu.ui.fragment.ZoneFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.rb_index)
    RadioButton rbIndex;
    @BindView(R.id.rb_live)
    RadioButton rbLive;
    @BindView(R.id.rb_zone)
    RadioButton rbZone;
    @BindView(R.id.rb_person)
    RadioButton rbPerson;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this);
        initFragment(savedInstanceState);
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void init() {
        selectTab = 0;

    }

    @Override
    protected boolean enableGestureBack() {
        return false;
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
                fragmentManager.beginTransaction().hide(liveFragment).hide(zoneFragment).hide(personFragment).show(zoneFragment).commit();

                break;
            case R.id.rb_person:
                rbIndex.setChecked(true);
                fragmentManager.beginTransaction().hide(liveFragment).hide(zoneFragment).hide(indexFragment).show(personFragment).commit();

                break;
        }
    }
}
