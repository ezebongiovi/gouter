package com.testableapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.stepstone.stepper.Step;
import com.stepstone.stepper.adapter.AbstractFragmentStepAdapter;
import com.testableapp.views.StepView;

import java.util.ArrayList;
import java.util.List;

public class StepAdapter extends AbstractFragmentStepAdapter {

    private final ArrayList<StepView> mSteps = new ArrayList<>();

    public StepAdapter(@NonNull final FragmentManager fm, @NonNull final Context context,
                       @NonNull final List<StepView> fragmentList) {
        super(fm, context);
        mSteps.addAll(fragmentList);
    }

    @Override
    public Step createStep(final int position) {
        return mSteps.get(position);
    }

    @Override
    public Step findStep(final int position) {
        return mSteps.get(position);
    }

    @Override
    public int getCount() {
        return mSteps.size();
    }

    @Override
    public boolean isViewFromObject(final View view, final Object object) {
        return ((StepView) object).getFragment().getView() == view;
    }
}
