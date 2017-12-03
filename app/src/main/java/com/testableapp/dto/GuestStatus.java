package com.testableapp.dto;

import io.reactivex.annotations.NonNull;

/**
 * Created by epasquale on 3/12/17.
 */

public class GuestStatus {
    public final String status;

    public GuestStatus(@NonNull @Guest.Status final String status) {
        this.status = status;
    }
}
