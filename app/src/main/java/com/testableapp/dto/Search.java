package com.testableapp.dto;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class Search<T> {

    public final List<T> results = new ArrayList<>();
    public final Pagination paging;

    public Search(@NonNull final List<T> results, @NonNull final Pagination paging) {
        this.results.addAll(results);
        this.paging = paging;
    }

    public static final class Pagination {
        public final int limit;
        public final int offset;
        public final int total;

        public Pagination(final int offset, final int limit, final int total) {
            this.offset = offset;
            this.limit = limit;
            this.total = total;
        }
    }
}
