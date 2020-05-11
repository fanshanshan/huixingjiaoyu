package com.qulink.hxedu.ui.fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qulink.hxedu.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexFragment extends Fragment {


    public IndexFragment() {
        // Required empty public constructor
    }


    private View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(rootView==null){
            rootView = inflater.inflate(R.layout.fragment_index, container, false);

        }
        return rootView;
    }

}
