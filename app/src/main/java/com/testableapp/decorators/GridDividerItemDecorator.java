package com.testableapp.decorators;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.DimenRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class GridDividerItemDecorator extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public GridDividerItemDecorator(final int dimensionPixelSize) {
        mItemOffset = dimensionPixelSize;
    }

    public GridDividerItemDecorator(@NonNull final Context context, @DimenRes final int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(final Rect outRect, final View view, final RecyclerView parent,
                               final RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.set(mItemOffset, mItemOffset, mItemOffset, mItemOffset);
    }
}
