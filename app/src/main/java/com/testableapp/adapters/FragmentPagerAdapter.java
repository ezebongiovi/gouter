package com.testableapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.testableapp.dto.GEvent;

import java.util.List;

public class FragmentPagerAdapter extends android.support.v4.app.FragmentPagerAdapter {

    private final List<PageView> mViews;
    private final Context mContext;

    public interface PageView {
        String getTitle(@NonNull Context context);

        Fragment getFragment();

        void update(@NonNull GEvent mEvent);
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
        return mViews.get(position).getTitle(mContext);
    }

    @Override
    public Fragment getItem(final int position) {
        return mViews.get(position).getFragment();
    }
}
