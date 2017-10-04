package com.testableapp.adapters;

import android.support.annotation.NonNull;
import android.widget.TextView;

import com.testableapp.R;
import com.testableapp.adapters.holders.GenericViewHolder;
import com.testableapp.dto.GEvent;

public class EventsAdapter extends PaginationAdapter<GEvent> {

    private OnEventClick mListener;

    /**
     * Default constructor
     *
     * @param paginationListener listener for being notified when paging is required
     */
    public EventsAdapter(@NonNull final PaginationListener paginationListener) {
        super(paginationListener);
    }

    public interface OnEventClick {
        void onClick(@NonNull GEvent event);
    }

    @Override
    protected int getHolderLayout() {
        return R.layout.holder_event;
    }

    @Override
    protected void onBind(final GenericViewHolder holder, final GEvent data) {
        ((TextView) holder.itemView.findViewById(R.id.eventDescription)).setText(data.getDescription());
        ((TextView) holder.itemView.findViewById(R.id.eventAddress)).setText(data.getAddress());

        holder.itemView.setOnClickListener(v -> {
            if (mListener != null) {
                mListener.onClick(data);
            }
        });
    }

    public void setOnEventClick(@NonNull final OnEventClick onClickListener) {
        this.mListener = onClickListener;
    }
}
