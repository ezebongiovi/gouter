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
import com.testableapp.ui.transformations.CircleTransform;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends PaginationAdapter<User> {

    private final ArrayList<User> mSelectedContacts = new ArrayList<>();
    private boolean mSelectable;
    private int mMaxItems;

    public ContactsAdapter(@NonNull final PaginationListener paginationListener) {
        super(paginationListener);
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
    protected void onBind(final GenericViewHolder holder, final User user) {

        if (user.profilePicture != null && !user.profilePicture.url.isEmpty()) {
            Picasso.with(holder.itemView.getContext()).load(user.profilePicture.url)
                    .placeholder(R.mipmap.ic_launcher)
                    .transform(new CircleTransform())
                    .into((ImageView) holder.itemView.findViewById(R.id.contactAvatar));
        }

        ((TextView) holder.itemView.findViewById(R.id.contactName))
                .setText(holder.itemView.getResources().getString(R.string.user_full_name,
                        user.firstName, user.lastName));

        final CheckBox checkBox = holder.itemView.findViewById(R.id.checkbox);
        checkBox.setVisibility(mSelectable ? View.VISIBLE : View.GONE);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> handleCheck(holder.getAdapterPosition(),
                isChecked));

        holder.itemView.setOnClickListener(v -> {
            if (mSelectable && (mMaxItems == 0 || mMaxItems < mSelectedContacts.size())) {
                checkBox.setChecked(!checkBox.isChecked());

                handleCheck(holder.getAdapterPosition(), checkBox.isSelected());
            }

        });
    }

    private void handleCheck(final int position, final boolean checked) {
        if (checked) {
            mSelectedContacts.add(mData.get(position));
        } else {
            mSelectedContacts.remove(mData.get(position));
        }
    }
}
