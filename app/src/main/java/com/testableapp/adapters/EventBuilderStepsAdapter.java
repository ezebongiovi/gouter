package com.testableapp.adapters;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.testableapp.R;
import com.testableapp.dto.EventBuilderStep;

import java.util.List;

public final class EventBuilderStepsAdapter extends BaseAdapter {
    public interface OnClickListener {
        void onClick(int position);
    }


    private final List<EventBuilderStep> mData;
    private final OnClickListener mListener;

    public EventBuilderStepsAdapter(@NonNull final OnClickListener listener,
                                    @NonNull final List<EventBuilderStep> actions) {
        mListener = listener;
        mData = actions;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(final int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(final int position) {
        return mData.get(position).hashCode();
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.holder_action_flow, parent, false);
        }

        final EventBuilderStep eventBuilderStep = mData.get(position);

        ((ImageView) convertView.findViewById(R.id.icon)).setImageDrawable(eventBuilderStep.drawable);
        ((TextView) convertView.findViewById(R.id.text)).setText(eventBuilderStep.title);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                mListener.onClick(position);
            }
        });

        return convertView;
    }
}