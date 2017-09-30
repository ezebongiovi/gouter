package com.testableapp.views;

import android.support.v4.app.Fragment;

import com.stepstone.stepper.Step;

public interface StepView extends Step {
    Fragment getFragment();
}
