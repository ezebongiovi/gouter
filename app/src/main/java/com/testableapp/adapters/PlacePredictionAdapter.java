package com.testableapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.testableapp.R;
import com.testableapp.adapters.holders.GenericViewHolder;
import com.testableapp.dto.Place;
import com.testableapp.views.PlacePickerView;

import java.util.ArrayList;
import java.util.List;

public class PlacePredictionAdapter extends RecyclerView.Adapter<GenericViewHolder> {
    private final List<Place> mData = new ArrayList<>();
    private final PlacePickerView placePickerView;

    public PlacePredictionAdapter(@NonNull final PlacePickerView placePickerView) {
        this.placePickerView = placePickerView;
    }

    @Override
    public GenericViewHolder onCreateViewHolder(final ViewGroup parent,
                                                final int viewType) {
        return new GenericViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(final GenericViewHolder holder,
                                 final int position) {
        // Click listener
        holder.itemView.setOnClickListener(v -> placePickerView.onPlaceSelected(mData
                .get(holder.getAdapterPosition())));

        // Holder data binding
        ((TextView) holder.itemView.findViewById(R.id.textField))
                .setText(mData.get(holder.getAdapterPosition()).formattedAddress);
    }

    public void setItems(@NonNull final List<Place> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.holder_place;
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }
}
