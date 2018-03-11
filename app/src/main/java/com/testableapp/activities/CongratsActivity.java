package com.testableapp.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.testableapp.R;
import com.testableapp.dto.GEvent;
import com.testableapp.presenters.EmptyPresenter;

public class CongratsActivity extends AbstractActivity {

    private static final String EXTRA_EVENT = "extra-event";

    public CongratsActivity() {
        super(FLAG_HIDE_TOOLBAR);
    }

    /**
     * @param context
     * @param event
     * @return
     */
    public static Intent getIntent(@NonNull final Context context, @NonNull final GEvent event) {
        final Intent intent = new Intent(context, CongratsActivity.class);
        intent.putExtra(EXTRA_EVENT, event);


        return intent;
    }

    @Override
    protected boolean shouldAuthenticate() {
        return true;
    }

    @Override
    public void onCreateActivity(@Nullable final Bundle savedInstanceState,
                                 @NonNull final EmptyPresenter presenter) {

    }

    @Override
    public int getLayoutResourceId() {
        return R.layout.activity_congrats;
    }
}
