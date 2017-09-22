package com.testableapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testableapp.R;
import com.testableapp.adapters.holders.EventsViewHolder;
import com.testableapp.dto.GEvent;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends RecyclerView.Adapter<EventsViewHolder> {

    private List<GEvent> mData = new ArrayList<>();
    private OnEventClick mListener;

    public interface OnEventClick {
        void onClick(@NonNull GEvent event);
    }

    @Override
    public EventsViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new EventsViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_event, parent, false));
    }

    @Override
    public void onBindViewHolder(final EventsViewHolder holder, final int position) {
        holder.onBind(mData.get(holder.getAdapterPosition()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onClick(mData.get(holder.getAdapterPosition()));
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
