package com.testableapp.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ViewFlipper;

import com.jakewharton.rxbinding2.widget.RxTextView;
import com.testableapp.R;
import com.testableapp.adapters.ContactsAdapter;
import com.testableapp.adapters.PaginationAdapter;
import com.testableapp.dto.User;
import com.testableapp.presenters.ContactPickerPresenter;
import com.testableapp.views.ContactPickerView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class ContactPicker extends LinearLayout implements ContactPickerView {

    private final ContactPickerPresenter mPresenter = new ContactPickerPresenter();
    private final ContactsAdapter mAdapter;
    private boolean mSelectable;
    private final int mMaxItems;
    private final EditText searchField;
    private ContactListener mListener;

    public interface ContactListener {
        void onSelectedContact(@NonNull User contact);
    }

    public ContactPicker(@NonNull final Context context) {
        this(context, null);
    }

    public ContactPicker(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("CheckResult")
    public ContactPicker(@NonNull final Context context, @Nullable final AttributeSet attrs,
                         final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.widget_contact_picker, this);

        final TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ContactPicker,
                0, 0);

        try {
            mSelectable = a.getBoolean(R.styleable.ContactPicker_selectable, false);
            mMaxItems = a.getInteger(R.styleable.ContactPicker_maxItems, 0);
        } finally {
            a.recycle();
        }

        final RecyclerView recyclerView = findViewById(R.id.contactPickerList);
        mAdapter = new ContactsAdapter(new PaginationAdapter.PaginationListener() {
            @Override
            public void onEndReached(final int offset) {
                mPresenter.search(searchField.getText().toString().trim(), offset);
            }
        }, contact -> {
            if (mListener != null) {
                mListener.onSelectedContact(contact);
            }
        });

        mAdapter.setSelectable(mSelectable);
        mAdapter.setMaxSelectedItems(mMaxItems);
        mAdapter.attachTo(recyclerView);

        searchField = findViewById(R.id.searchField);
        RxTextView.textChanges(searchField).debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(charSequence -> {
                    final Handler handler = new Handler(getContext().getMainLooper());
                    handler.post(() -> {
                        if (charSequence.toString().isEmpty()) {
                            mPresenter.search(null, 0);
                        } else {
                            mPresenter.search(charSequence.toString().trim(), 0);
                        }
                    });
                });
    }

    public List<User> getSelectedContacts() {
        return mAdapter.getSelectedContacts();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mPresenter.attachView(this);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mPresenter.detachView();
    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onError(@NonNull final String message) {
        Snackbar.make(this, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgressLayout() {
        ((ViewFlipper) findViewById(R.id.viewFlipper)).setDisplayedChild(1);
    }

    @Override
    public void showRegularLayout() {
        ((ViewFlipper) findViewById(R.id.viewFlipper)).setDisplayedChild(0);
    }

    @Override
    public void onGenericError() {

    }

    @Override
    public void loadContacts(@NonNull final List<User> users) {
        mAdapter.setItems(users);
    }

    @Override
    public void addContacts(@NonNull final List<User> users) {
        mAdapter.addItems(users);
    }

    @Override
    public void showListLoading() {
        mAdapter.showLoading();
    }

    @Override
    public void hideListLoading() {
        mAdapter.hideLoading();
    }


    public void setSelectable(final boolean selectable) {
        mSelectable = selectable;
        mAdapter.setSelectable(true);
        mAdapter.notifyDataSetChanged();
    }

    public void setListener(@NonNull final ContactListener listener) {
        this.mListener = listener;
    }
}
