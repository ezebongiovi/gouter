package com.testableapp.adapters.holders;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.testableapp.R;
import com.testableapp.dto.User;

public class ContactViewHolder extends RecyclerView.ViewHolder {

    private final Context mContext;
    private final boolean mSelectable;
    private boolean selected = false;

    public ContactViewHolder(boolean selectable, View view) {
        super(view);
        mContext = itemView.getContext();
        mSelectable = selectable;
    }

    public void onBind(@NonNull final User user) {
        Picasso.with(mContext).load(user.getProfilePicture()).placeholder(R.mipmap.ic_launcher)
                .into((ImageView) itemView.findViewById(R.id.contactAvatar));

        ((TextView) itemView.findViewById(R.id.contactName))
                .setText(itemView.getResources().getString(R.string.user_full_name,
                        user.getFirstName(), user.getLastName()));

        itemView.findViewById(R.id.checkbox).setVisibility(mSelectable ? View.VISIBLE : View.GONE);
    }

    public void toggle() {
        ((CheckBox) itemView.findViewById(R.id.checkbox)).setChecked(!selected);
        selected = !selected;
    }
}
