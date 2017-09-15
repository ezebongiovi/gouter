package com.testableapp.db;

import android.provider.BaseColumns;

public final class DBContract {

    /* Inner class that defines the table contents */
    public static class Entry implements BaseColumns {
        public static final String TABLE_NAME = "user";
        public static final String COLUMN_NAME_EMAIL = "email";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_LAST_NAME = "lastName";
        public static final String COLUMN_NAME_TOKEN = "token";
        public static final String COLUMN_NAME_PROFILE_PICTURE = "profilePicture";
    }

}
