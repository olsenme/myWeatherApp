package com.example.android.weatherwithsqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;



public class LocationsDBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "locations.db";
    private static int DATABASE_VERSION = 1;

    public LocationsDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_SAVED_LOCATIONS_TABLE =
                "CREATE TABLE " + LocationsContract.SavedLocations.TABLE_NAME + "(" +
                LocationsContract.SavedLocations._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LocationsContract.SavedLocations.COLUMN_FULL_NAME + " TEXT NOT NULL " + ");";
        db.execSQL(SQL_CREATE_SAVED_LOCATIONS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + LocationsContract.SavedLocations.TABLE_NAME + ";");
        onCreate(db);
    }
}
