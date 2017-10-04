package com.testableapp.db;

import android.provider.BaseColumns;

 final class DBContract {

    /* Inner class that defines the table contents */
     static class Entry implements BaseColumns {
        static final String TABLE_NAME = "user";
         static final String COLUMN_NAME_EMAIL = "email";
         static final String COLUMN_NAME_NAME = "name";
         static final String COLUMN_NAME_LAST_NAME = "lastName";
         static final String COLUMN_NAME_TOKEN = "token";
         static final String COLUMN_NAME_PROFILE_PICTURE = "profilePicture";
         static final String COLUMN_COUNTRY_NAME = "countryName";
    }

}
