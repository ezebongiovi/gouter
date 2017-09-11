package com.testableapp.adapters;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class GenericPagerAdapter<T extends Fragment> extends FragmentPagerAdapter {

    private final List<T> mFragments = new ArrayList<>();

    public GenericPagerAdapter(@NonNull final FragmentManager fm, @NonNull final List<T> fragments) {
        super(fm);
        mFragments.addAll(fragments);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(final int position) {
        return mFragments.get(position);
    }
}
