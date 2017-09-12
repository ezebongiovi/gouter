package com.testableapp.activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ViewFlipper;

import com.testableapp.R;
import com.testableapp.presenters.CreateEventPresenter;
import com.testableapp.views.CreateEventView;

public class CreateEventActivity extends AbstractMvpActivity<CreateEventPresenter>
        implements CreateEventView{

    @Override
    protected boolean shouldAuthenticate() {
        return true;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 @NonNull final CreateEventPresenter presenter) {
        final ViewFlipper viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        viewFlipper.setInAnimation(this, R.anim.slide_down_in);
        viewFlipper.setOutAnimation(this, R.anim.slide_down_out);

        viewFlipper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewFlipper.showNext();
            }
        });
    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_create_event;
    }

    @NonNull
    @Override
    protected CreateEventPresenter createPresenter() {
        return new CreateEventPresenter();
    }
}
