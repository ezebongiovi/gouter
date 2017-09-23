package com.testableapp.adapters;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.testableapp.R;
import com.testableapp.adapters.holders.GenericViewHolder;
import com.testableapp.dto.User;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends PaginationAdapter<User> {

    private final ArrayList<User> mSelectedContacts = new ArrayList<>();
    private boolean mSelectable;
    private int mMaxItems;

    public ContactsAdapter(@NonNull final PaginationListener paginationListener,
                           @NonNull final List<User> data) {
        super(data, paginationListener);
    }

    public void setMaxSelectedItems(final int maxSelectedItems) {
        mMaxItems = maxSelectedItems;
    }

    public void setSelectable(final boolean selectable) {
        mSelectable = selectable;
    }

    public List<User> getSelectedContacts() {
        return mSelectedContacts;
    }

    @Override
    protected int getHolderLayout() {
        return R.layout.holder_contact;
    }

    @Override
    protected void onBind(final GenericViewHolder<User> holder, final User user) {

        if (user.getProfilePicture() != null && !user.getProfilePicture().isEmpty()) {
            Picasso.with(holder.itemView.getContext()).load(user.getProfilePicture())
                    .placeholder(R.mipmap.ic_launcher)
                    .into((ImageView) holder.itemView.findViewById(R.id.contactAvatar));
        }

        ((TextView) holder.itemView.findViewById(R.id.contactName))
                .setText(holder.itemView.getResources().getString(R.string.user_full_name,
                        user.getFirstName(), user.getLastName()));

        holder.itemView.findViewById(R.id.checkbox).setVisibility(mSelectable ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (mSelectable || (mMaxItems == 0 && mMaxItems < mSelectedContacts.size())) {
                    final CheckBox checkBox = (CheckBox) holder.itemView.findViewById(R.id.checkbox);
                    checkBox.setChecked(!checkBox.isChecked());

                    if (checkBox.isChecked()) {
                        mSelectedContacts.add(mData.get(holder.getAdapterPosition()));
                    } else {
                        mSelectedContacts.remove(mData.get(holder.getAdapterPosition()));
                    }
                }

            }
        });
    }
}