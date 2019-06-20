package com.app.mylibertadriver.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.app.mylibertadriver.R;
import com.app.mylibertadriver.databinding.FragmentHelpSupportBinding;

/**
 * Create By Rahul Mangal
 * Project SignupLibrary Screen
 */

public class FragmentSupport extends Fragment {
    private FragmentHelpSupportBinding binder;

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binder = DataBindingUtil.inflate(inflater, R.layout.fragment_help_support, container, false);
        View view = binder.getRoot();
        return view;
    }

    public class Presenter {
        public void onHomeClicked(View view) {
        }
    }
}