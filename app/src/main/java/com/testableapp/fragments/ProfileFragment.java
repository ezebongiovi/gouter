package com.testableapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.testableapp.R;
import com.testableapp.activities.LoginActivity;
import com.testableapp.dto.User;
import com.testableapp.fragments.base.AbstractFragment;
import com.testableapp.manager.AuthenticationManager;
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
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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

    @Override
    public void onCreateOptionsMenu(final Menu menu, final MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.profile, menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (item.getItemId() == R.id.actionLogout) {
            new AlertDialog.Builder(getContext())
                    .setPositiveButton(getString(R.string.action_logout), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            AuthenticationManager.getInstance().logOut(getContext());
                            startActivity(new Intent(getContext(), LoginActivity.class)
                                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                            getActivity().finish();
                        }
                    }).setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(final DialogInterface dialog, final int which) {
                    dialog.dismiss();
                }
            }).setCancelable(true).setTitle(getString(R.string.session_close_title))
                    .setMessage(getString(R.string.session_close_message)).create().show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
