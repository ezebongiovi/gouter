package com.testableapp.adapters;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.support.v7.widget.RecyclerViewScrollEvent;
import com.jakewharton.rxbinding2.support.v7.widget.RxRecyclerView;
import com.testableapp.R;
import com.testableapp.adapters.holders.GenericViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public abstract class PaginationAdapter<T> extends RecyclerView.Adapter<GenericViewHolder> {

    /**
     * Listener for paging adapter.
     */
    public interface PaginationListener {

        /**
         * Called when the ond of the list has been reached
         */
        void onEndReached(int offset);
    }

    private final PaginationListener mPaginationListener;
    private boolean isLoading;
    final List<T> mData = new ArrayList<>();

    /**
     * Default constructor
     *
     * @param paginationListener listener for being notified when paging is required
     */
    public PaginationAdapter(@NonNull final PaginationListener paginationListener) {
        mPaginationListener = paginationListener;
    }

    @Override
    public int getItemCount() {
        return isLoading ? mData.size() + 1 : mData.size();
    }

    /**
     * Adds items to the adapter
     *
     * @param data the items being added
     */
    public void addItems(@NonNull final List<T> data) {
        hideLoading();

        final int index = mData.size();

        mData.addAll(data);
        notifyItemRangeInserted(index, mData.size() - 1);
    }

    /**
     * Resets adapter's value
     *
     * @param data the new adapter's value
     */
    public void setItems(@NonNull final List<T> data) {
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(final int position) {
        return isLoading && position == mData.size() ?
                R.layout.holder_progress : getHolderLayout();
    }

    @Override
    public GenericViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        return new GenericViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(viewType, parent, false));
    }

    @Override
    public void onBindViewHolder(final GenericViewHolder holder, final int position) {
        if (holder.getItemViewType() != R.layout.holder_progress) {
            onBind(holder, mData.get(holder.getAdapterPosition()));
        }
    }

    /**
     * Attaches adapter to RecyclerView by setting it's scrollListener
     *
     * @param recyclerView the recyclerView being attached to
     */
    public void attachTo(@NonNull final RecyclerView recyclerView) {
        RxRecyclerView.scrollEvents(recyclerView).debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<RecyclerViewScrollEvent>() {
                    @Override
                    public void accept(final RecyclerViewScrollEvent recyclerViewScrollEvent)
                            throws Exception {
                        final RecyclerView recyclerView = recyclerViewScrollEvent.view();

                        final LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView
                                .getLayoutManager();

                        if (!isLoading && layoutManager.findLastVisibleItemPosition() == mData.size() - 1) {
                            mPaginationListener.onEndReached(mData.size());
                        }
                    }
                });

        recyclerView.setAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
    }

    public void showLoading() {
        isLoading = true;
        notifyItemInserted(mData.size());
    }

    public void hideLoading() {
        isLoading = false;
        notifyItemRemoved(mData.size());
    }

    @LayoutRes
    protected abstract int getHolderLayout();

    protected abstract void onBind(final GenericViewHolder holder, final T data);
}
