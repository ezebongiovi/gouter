package com.testableapp.adapters.holders;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.testableapp.R;
import com.testableapp.dto.GEvent;

public class EventsViewHolder extends RecyclerView.ViewHolder {

    public EventsViewHolder(@NonNull final View view) {
        super(view);
    }

    public void onBind(@NonNull final GEvent gEvent) {
        ((TextView) itemView.findViewById(R.id.eventDescription)).setText(gEvent.getDescription());
        ((TextView) itemView.findViewById(R.id.eventAddress)).setText(gEvent.getAddress());
    }
}
