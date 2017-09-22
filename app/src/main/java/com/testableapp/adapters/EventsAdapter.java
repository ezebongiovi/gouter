package com.testableapp.adapters;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.testableapp.R;
import com.testableapp.adapters.holders.GenericViewHolder;
import com.testableapp.dto.GEvent;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends PaginationAdapter<GEvent> {

    private List<GEvent> mData = new ArrayList<>();
    private OnEventClick mListener;

    /**
     * Default constructor
     *
     * @param data               the adapter's initial data
     * @param paginationListener listener for being notified when paging is required
     */
    public EventsAdapter(@NonNull final List<GEvent> data,
                         @NonNull final PaginationListener paginationListener) {
        super(data, paginationListener);
    }

    public interface OnEventClick {
        void onClick(@NonNull GEvent event);
    }

    @Override
    protected int getHolderLayout() {
        return R.layout.holder_event;
    }

    @Override
    protected void onBind(final GenericViewHolder<GEvent> holder, final GEvent data) {
        ((TextView) holder.itemView.findViewById(R.id.eventDescription)).setText(data.getDescription());
        ((TextView) holder.itemView.findViewById(R.id.eventAddress)).setText(data.getAddress());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(data);
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setOnEventClick(@NonNull final OnEventClick onClickListener) {
        this.mListener = onClickListener;
    }

    public void setItems(@NonNull final List<GEvent> items) {
        mData.clear();
        mData.addAll(items);

        notifyDataSetChanged();
    }
}
