package com.testableapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testableapp.R;
import com.testableapp.fragments.base.AbstractFragment;

public class HomeFragment extends AbstractFragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, null, false);
    }

    @Override
    public void showProgressLayout() {

    }

    @Override
    public void showRegularLayout() {

    }
}
