package com.testableapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import java.util.List;

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private final List<PageView> mViews;
    private final Context mContext;

    public interface PageView {
        int getTitle();

        Fragment getFragment();
    }
    
    public FragmentPagerAdapter(@NonNull final Context context,
                                @NonNull final FragmentManager fm, @NonNull final List<PageView> views) {
        super(fm);
        mContext = context;
        mViews = views;
    }
    
    @Override
    public int getCount() {
        return mViews.size();
    }

    @Override
    public CharSequence getPageTitle(final int position) {
        return mContext.getString(mViews.get(position).getTitle());
    }

    @Override
    public Fragment getItem(final int position) {
        return mViews.get(position).getFragment();
    }
}
