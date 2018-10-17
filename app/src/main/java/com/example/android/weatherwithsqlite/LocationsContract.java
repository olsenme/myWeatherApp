package com.example.android.weatherwithsqlite;

import android.provider.BaseColumns;


public class LocationsContract {
    private LocationsContract() {}
    public static class SavedLocations implements BaseColumns {
        public static final String TABLE_NAME = "savedCities";
        public static final String COLUMN_FULL_NAME = "cityName";
    }
}
