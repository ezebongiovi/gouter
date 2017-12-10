package com.testableapp.fragments.steps;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.stepstone.stepper.VerificationError;
import com.testableapp.R;
import com.testableapp.adapters.holders.GenericViewHolder;
import com.testableapp.dto.GEvent;
import com.testableapp.dto.User;
import com.testableapp.models.EventsModel;
import com.testableapp.ui.transformations.CircleTransform;
import com.testableapp.views.StepView;

public class CreateEventConfirm extends Fragment implements StepView {


    private GEvent mEvent;
    private RecyclerView.Adapter<GenericViewHolder> mAdapter;

    private void init() {
        Picasso.with(getContext()).load(mEvent.coverFile)
                .into((ImageView) getView().findViewById(R.id.coverView));

        ((TextView) getView().findViewById(R.id.addressView)).setText(mEvent.address.formattedAddress);
        ((TextView) getView().findViewById(R.id.dateView)).setText(mEvent.date.toString());
        ((TextView) getView().findViewById(R.id.descriptionView)).setText(mEvent.description);

        final RecyclerView recyclerView = getView().findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        mAdapter = new RecyclerView.Adapter<GenericViewHolder>() {

            @Override
            public GenericViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
                return new GenericViewHolder(LayoutInflater.from(getContext())
                        .inflate(R.layout.holder_guest_grid, parent, false));
            }

            @Override
            public void onBindViewHolder(final GenericViewHolder holder, final int position) {
                final int adapterPosition = holder.getAdapterPosition();
                final User guest = mEvent.guests.get(adapterPosition).user;
                final View holderView = holder.itemView;

                ((TextView) holderView.findViewById(R.id.nameView))
                        .setText(guest.firstName);
                ((TextView) holderView.findViewById(R.id.lastNameView))
                        .setText(guest.lastName);

                if (guest.profilePicture != null) {
                    Picasso.with(getContext()).load(Uri.parse(guest.profilePicture.url))
                            .transform(new CircleTransform())
                            .into((ImageView) holderView.findViewById(R.id.avatarView));
                }
            }

            @Override
            public int getItemCount() {
                return mEvent.guests.size();
            }
        };

        recyclerView.setAdapter(mAdapter);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_step_create_event_confirm,
                container, false);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Nullable
    @Override
    public VerificationError verifyStep() {
        return null;
    }

    @Override
    public void onSelected() {
        mEvent = EventsModel.Repository.eventBuilder.build();
        init();
    }

    @Override
    public void onError(@NonNull final VerificationError error) {

    }
}
