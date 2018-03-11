package com.testableapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.testableapp.R;
import com.testableapp.dto.User;
import com.testableapp.fragments.base.AbstractFragment;
import com.testableapp.ui.transformations.CircleTransform;

public class ProfileFragment extends AbstractFragment {

    private static final String EXTRA_USER = "extra-user";

    public static ProfileFragment getInstance(@NonNull final User user) {
        final ProfileFragment profileFragment = new ProfileFragment();
        final Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_USER, user);
        profileFragment.setArguments(bundle);

        return profileFragment;
    }

    @Override
    protected int getResourceId() {
        return R.layout.fragment_profile;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() == null || !getArguments().containsKey(EXTRA_USER)) {
            throw new AssertionError("Use it's static factory method");
        }

        final User user = getArguments().getParcelable(EXTRA_USER);

        //noinspection ConstantConditions
        Picasso.with(getContext()).load(user.profilePicture.url)
                .transform(new CircleTransform())
                .placeholder(R.mipmap.ic_account)
                .into((ImageView) view.findViewById(R.id.picture));
    }
}
