package com.testableapp.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import com.testableapp.dto.Authentication;
import com.testableapp.dto.Country;
import com.testableapp.dto.Image;
import com.testableapp.dto.User;

public class DBHelper extends SQLiteOpenHelper {

    private final SQLiteDatabase db = getWritableDatabase();
    private static DBHelper INSTANCE;
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "account.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DBContract.Entry.TABLE_NAME + " (" +
                    DBContract.Entry._ID + " TEXT PRIMARY KEY," +
                    DBContract.Entry.COLUMN_NAME_NAME + " TEXT," +
                    DBContract.Entry.COLUMN_NAME_LAST_NAME + " TEXT," +
                    DBContract.Entry.COLUMN_NAME_PROFILE_PICTURE + " TEXT," +
                    DBContract.Entry.COLUMN_NAME_TOKEN + " TEXT," +
                    DBContract.Entry.COLUMN_NAME_EMAIL + " TEXT," +
                    DBContract.Entry.COLUMN_COUNTRY_NAME + " TEXT)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DBContract.Entry.TABLE_NAME;

    /**
     * Factory method
     *
     * @param context the application's mContext
     * @return the DBHelper instance
     */
    public static DBHelper getInstance(@NonNull final Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DBHelper(context);
        }

        return INSTANCE;
    }

    private DBHelper(final Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(final SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void onUpgrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public long onLogin(@NonNull final User user) {
        final ContentValues values = new ContentValues();
        values.put(DBContract.Entry.COLUMN_NAME_EMAIL, user.authentication.getEmail());
        values.put(DBContract.Entry.COLUMN_NAME_NAME, user.firstName);
        values.put(DBContract.Entry.COLUMN_NAME_LAST_NAME, user.lastName);
        values.put(DBContract.Entry.COLUMN_NAME_PROFILE_PICTURE, user.profilePicture.url);
        values.put(DBContract.Entry.COLUMN_NAME_TOKEN, user.authentication.getAccessToken());
        values.put(DBContract.Entry.COLUMN_COUNTRY_NAME, "Argentina");

        return db.insert(DBContract.Entry.TABLE_NAME, null, values);
    }

    public User getLoggedUser() {
        final Cursor c = db.rawQuery("SELECT * FROM " + DBContract.Entry.TABLE_NAME, null);
        c.moveToFirst();

        if (c.getCount() > 0) {
            return new User(c.getString(0), c.getString(1),
                    c.getString(2), new Image(c.getString(3)),
                    new Authentication.Builder().withEmail(c.getString(5))
                            .withAccessToken(c.getString(4)).build(), new Country(c.getString(6)));
        }

        return null;
    }

    public void onLogOut() {
        db.delete(DBContract.Entry.TABLE_NAME, null, null);
    }
}