package com.testableapp.adapters.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;

public class GenericViewHolder<T> extends ViewHolder {

    public GenericViewHolder(final View itemView) {
        super(itemView);
    }

    public void onBind(@NonNull final T data) {

    }
}
