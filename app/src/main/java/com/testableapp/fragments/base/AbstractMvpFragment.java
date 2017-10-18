package com.testableapp.fragments.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.testableapp.R;
import com.testableapp.presenters.AbstractPresenter;
import com.testableapp.views.AbstractView;

public abstract class AbstractMvpFragment<P extends AbstractPresenter>
        extends Fragment implements AbstractView {

    private static final String TAG_LIFE_CYCLE = "Fragment/LifeCycle";
    protected P mPresenter;

    @Override
    public void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LifeCycle", "onCreate");

        mPresenter = createPresenter();

        onCreateFragment(savedInstanceState, mPresenter);
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        Log.d(TAG_LIFE_CYCLE, "onCreateView");

        final View view = inflater.inflate(R.layout.fragment_abstract, container, false);
        LayoutInflater.from(getContext()).inflate(getResourceId(),
                view.findViewById(R.id.rootView), true);

        return view;
    }

    public void onCreateFragment(@Nullable final Bundle savedInstanceState,
                                 @NonNull final P presenter) {
        // Nothing to do
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG_LIFE_CYCLE, "onResume");
        mPresenter.attachView(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG_LIFE_CYCLE, "onPause");

        mPresenter.detachView();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG_LIFE_CYCLE, "onDestroy");
        mPresenter.detachView();
    }

    @Override
    public void onNetworkError() {
        Snackbar.make(getView().findViewById(R.id.rootView), "Error de conexión",
                Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onError(@NonNull final String message) {
        Snackbar.make(getView().findViewById(R.id.rootView), message,
                Snackbar.LENGTH_LONG).show();
    }

    @NonNull
    protected abstract P createPresenter();

    protected abstract int getResourceId();
}
