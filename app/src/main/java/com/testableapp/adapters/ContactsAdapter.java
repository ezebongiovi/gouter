package com.testableapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testableapp.R;
import com.testableapp.adapters.holders.ContactViewHolder;
import com.testableapp.dto.User;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<ContactViewHolder> {

    private final ArrayList<User> mList = new ArrayList<>();
    private final ArrayList<User> mSelectedContacts = new ArrayList<>();
    private final boolean mSelectable;
    private final int mMaxItems;

    public ContactsAdapter(final boolean selectable, final int maxItems) {
        mSelectable = selectable;
        mMaxItems = maxItems;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(final ViewGroup parent,
                                                final int viewType) {
        return new ContactViewHolder(mSelectable, LayoutInflater.from(parent.getContext())
                .inflate(R.layout.holder_contact, parent, false));
    }

    @Override
    public void onBindViewHolder(final ContactViewHolder holder, final int position) {
        holder.onBind(mList.get(holder.getAdapterPosition()));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                // Selects item only if we're under the max items limit
                if (mSelectable || (mMaxItems == 0 && mMaxItems < mSelectedContacts.size())) {
                    mSelectedContacts.add(mList.get(holder.getAdapterPosition()));
                    holder.toggle();
                }
            }
        });
    }

    public List<User> getSelectedContacts() {
        return mSelectedContacts;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void loadContacts(@NonNull final List<User> users) {
        mList.clear();
        mList.addAll(users);
        notifyDataSetChanged();
    }

    public void addContacts(@NonNull final List<User> users) {
        final int index = mList.size();

        mList.addAll(users);
        notifyItemInserted(index);
    }
}
