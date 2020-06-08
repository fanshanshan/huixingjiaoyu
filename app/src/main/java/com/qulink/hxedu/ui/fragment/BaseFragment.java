package com.qulink.hxedu.ui.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.qulink.hxedu.R;

import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
/**
 * Fragment的基类
 */

public abstract class BaseFragment extends Fragment {


    protected View rootView;
    protected Activity mActivity;
    private boolean isInit;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        rootView = inflater.inflate(getLayout(),container);

        ButterKnife.bind(this, rootView);
        init();
        return rootView;
    }

    protected abstract void init();
    protected abstract int getLayout();
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mActivity = (Activity)context;
    }

}
