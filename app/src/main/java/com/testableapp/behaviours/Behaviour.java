package com.testableapp.behaviours;

import android.content.Context;
import android.support.annotation.NonNull;

public abstract class Behaviour {

    protected final Context mContext;

    public Behaviour(@NonNull final Context context) {
        mContext = context;
    }

    public abstract void execute();
}
