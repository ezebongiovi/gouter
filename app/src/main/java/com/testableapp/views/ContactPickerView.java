package com.testableapp.views;

import android.support.annotation.NonNull;

import com.testableapp.dto.User;

import java.util.List;

public interface ContactPickerView extends AbstractView {
    void loadContacts(@NonNull List<User> data);

    void addContacts(@NonNull List<User> data);

    void showListLoading();

    void hideListLoading();
}
