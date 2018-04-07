package com.testableapp.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.testableapp.R;
import com.testableapp.adapters.holders.GenericViewHolder;
import com.testableapp.dto.Guest;
import com.testableapp.ui.transformations.CircleTransform;
import com.testableapp.widgets.GuestsView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.annotations.NonNull;

/**
 * Created by epasquale on 27/11/17.
 */

public class GuestsAdapter extends RecyclerView.Adapter<GenericViewHolder> {

    private final List<Guest> mFilteredData = new ArrayList<>();
    private final List<Guest> mData = new ArrayList<>();
    private GuestsView.GuestClickListener mListener;

    public GuestsAdapter(@NonNull final List<Guest> guests, GuestsView.GuestClickListener listener) {
        mListener = listener;
        mData.addAll(guests);
        mFilteredData.addAll(guests);
    }

    @Override
    public GenericViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new GenericViewHolder(LayoutInflater.from(parent.getContext()).inflate(viewType,
                parent, false));
    }

    @Override
    public int getItemViewType(int position) {
        return R.layout.holder_guest;
    }

    @Override
    public void onBindViewHolder(final GenericViewHolder holder, final int position) {
        final Guest guest = mFilteredData.get(holder.getAdapterPosition());
        final Context context = holder.itemView.getContext();

        ((TextView) holder.itemView.findViewById(R.id.contactName))
                .setText(String.format(context.getResources().getString(R.string.name),
                        guest.user.firstName, guest.user.lastName));

        if (guest.user.profilePicture != null) {
            Picasso.with(context).load(guest.user.profilePicture.url).transform(new CircleTransform())
                    .into((ImageView) holder.itemView.findViewById(R.id.contactAvatar));
        }

        if (guest.status.equals(Guest.STATUS_ACCEPTED)) {
            holder.itemView.findViewById(R.id.selectIndicator).setVisibility(View.VISIBLE);
        } else {
            holder.itemView.findViewById(R.id.selectIndicator).setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onClick(guest);
            }
        });
    }

    public void setItems(@NonNull final List<Guest> newList) {
        // If the list is empty we remove every item from filtered list
        if (newList.isEmpty()) {
            notifyItemRangeRemoved(0, mFilteredData.size());
            mFilteredData.clear();

            return;
        }

        // If it's not empty will handle addition and deletion
        for (int i = 0; i < newList.size(); i++) {

            if (mFilteredData.isEmpty()) {
                addGuest(newList.get(i));
            } else {
                for (int k = 0; k < mFilteredData.size(); k++) {
                    if (!newList.contains(mFilteredData.get(k))) {
                        removeGuest(mFilteredData.get(k));
                    } else if (!mFilteredData.contains(newList.get(i))) {
                        addGuest(newList.get(i));
                    }
                }
            }
        }
    }

    private void addGuest(@NonNull final Guest guest) {
        mFilteredData.add(guest);

        Collections.sort(mFilteredData, (guest1, t1) -> (guest1.user.firstName + guest1.user.lastName)
                .compareToIgnoreCase((t1.user.firstName + t1.user.lastName)));

        notifyItemInserted(mFilteredData.indexOf(guest));
    }

    private void removeGuest(@NonNull final Guest guest) {
        final int index = mFilteredData.indexOf(guest);

        mFilteredData.remove(guest);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mFilteredData.size();
    }
}
