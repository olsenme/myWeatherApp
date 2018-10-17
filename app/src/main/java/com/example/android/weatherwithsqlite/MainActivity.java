package com.example.android.weatherwithsqlite;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

//import com.example.android.weatherwithsqlite.LocationAdapter.OnLocationItemClickListener;
import com.example.android.weatherwithsqlite.utils.OpenWeatherMapUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements ForecastAdapter.OnForecastItemClickListener, LoaderManager.LoaderCallbacks<String>,
        SharedPreferences.OnSharedPreferenceChangeListener,
        LocationAdapter.OnLocationItemClickListener, NavigationView.OnNavigationItemSelectedListener
{
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String FORECAST_URL_KEY = "forecastURL";
    private static final int FORECAST_LOADER_ID = 0;

    private SQLiteDatabase mDB;
    private boolean isSaved = false;
    private String forecastLocation;
    private String temperatureUnits;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private TextView mForecastLocationTV;
    private RecyclerView mForecastItemsRV;
    private RecyclerView mLocationItemsRV;
    private ProgressBar mLoadingIndicatorPB;
    private TextView mLoadingErrorMessageTV;
    private ForecastAdapter mForecastAdapter;
    private LocationAdapter mLocationAdapter;
    private LocationsDBHelper dbHelper;
    private TextView changelocation;
    private NavigationView mNavigationView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Remove shadow under action bar.
        getSupportActionBar().setElevation(0);
        mNavigationView = (NavigationView) findViewById(R.id.navigation);
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        mDrawerToggle =
                new ActionBarDrawerToggle(this, mDrawerLayout,
                        R.string.drawer_open, R.string.drawer_close);
        //??
        //mLocationItemsRV.setVisibility(View.VISIBLE);
        mDrawerLayout.addDrawerListener(mDrawerToggle);




        mForecastLocationTV = findViewById(R.id.tv_forecast_location);

        mLoadingIndicatorPB = findViewById(R.id.pb_loading_indicator);
        mLoadingErrorMessageTV = findViewById(R.id.tv_loading_error_message);
        mForecastItemsRV = findViewById(R.id.rv_forecast_items);
        mLocationItemsRV = findViewById(R.id.rv_locations);

        mForecastAdapter = new ForecastAdapter(this, this);
       //this second paramter
        mLocationAdapter = new LocationAdapter(this, this);

        mForecastItemsRV.setAdapter(mForecastAdapter);
        mLocationItemsRV.setAdapter(mLocationAdapter);

        //dont know what to do for these sizes
        mLocationItemsRV.setLayoutManager(new LinearLayoutManager(this));
        mLocationItemsRV.setHasFixedSize(true);
        mForecastItemsRV.setLayoutManager(new LinearLayoutManager(this));
        mForecastItemsRV.setHasFixedSize(true);

        changelocation = findViewById(R.id.edit_location);
        changelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                showDialog();

            }
        });

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);

        loadForecast(sharedPreferences, true);

        LocationsDBHelper dbHelper = new LocationsDBHelper(this);
        mDB = dbHelper.getWritableDatabase();
    }

    @Override
    protected void onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);
        super.onDestroy();
        mDB.close();
    }

    @Override
    public void onForecastItemClick(OpenWeatherMapUtils.ForecastItem forecastItem) {
        Intent intent = new Intent(this, ForecastItemDetailActivity.class);
        intent.putExtra(OpenWeatherMapUtils.ForecastItem.EXTRA_FORECAST_ITEM, forecastItem);
        startActivity(intent);
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
            case R.id.rv_locations:
            return true;

            default:
               return false;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            ArrayList<String> loc = getLocationItems();
            mLocationAdapter.updateLocationItems(loc);


            return true;
        }
        switch (item.getItemId()) {
            case R.id.action_location:
                showForecastLocationInMap();
                return true;
            case R.id.action_settings:
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
    public void loadForecast(SharedPreferences sharedPreferences, boolean initialLoad) {
        forecastLocation = sharedPreferences.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default_value)
        );
        temperatureUnits = sharedPreferences.getString(
                getString(R.string.pref_units_key),
                getString(R.string.pref_units_default_value)
        );

        mForecastLocationTV.setText(forecastLocation);
        mLoadingIndicatorPB.setVisibility(View.VISIBLE);

        String forecastURL = OpenWeatherMapUtils.buildForecastURL(forecastLocation, temperatureUnits);
        Bundle loaderArgs = new Bundle();
        loaderArgs.putString(FORECAST_URL_KEY, forecastURL);
        LoaderManager loaderManager = getSupportLoaderManager();
        if (initialLoad) {
            loaderManager.initLoader(FORECAST_LOADER_ID, loaderArgs, this);
        } else {
            loaderManager.restartLoader(FORECAST_LOADER_ID, loaderArgs, this);
        }
    }

    public void showForecastLocationInMap() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String forecastLocation = sharedPreferences.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default_value)
        );
        Uri geoUri = Uri.parse("geo:0,0").buildUpon()
                .appendQueryParameter("q", forecastLocation)
                .build();
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, geoUri);
        if (mapIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(mapIntent);
        }
    }

    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        String forecastURL = null;
        if (args != null) {
            forecastURL = args.getString(FORECAST_URL_KEY);
        }
        return new ForecastLoader(this, forecastURL);
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        Log.d(TAG, "got forecast from loader");
        mLoadingIndicatorPB.setVisibility(View.INVISIBLE);
        if (data != null) {
            mLoadingErrorMessageTV.setVisibility(View.INVISIBLE);
            mForecastItemsRV.setVisibility(View.VISIBLE);
            ArrayList<OpenWeatherMapUtils.ForecastItem> forecastItems = OpenWeatherMapUtils.parseForecastJSON(data);
            mForecastAdapter.updateForecastItems(forecastItems);
        } else {
            mForecastItemsRV.setVisibility(View.INVISIBLE);
            mLoadingErrorMessageTV.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {
        // Nothing ...
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        loadForecast(sharedPreferences, false);
        isSaved = checkIsLocationSaved();
       if(sharedPreferences!=null && !isSaved)
            addSearchResultToDB();
    }
    private boolean checkIsLocationSaved( ) {

            String sqlSelection =
                    LocationsContract.SavedLocations.COLUMN_FULL_NAME + " = ?";
            String[] sqlSelectionArgs = {forecastLocation};
            Cursor cursor = mDB.query(
                    LocationsContract.SavedLocations.TABLE_NAME,
                    null,
                    sqlSelection,
                    sqlSelectionArgs,
                    null,
                    null,
                    null
            );
            isSaved = cursor.getCount() > 0;
            cursor.close();

        return isSaved;
    }
    private void addSearchResultToDB() {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String forecastLocation = sharedPreferences.getString(
                getString(R.string.pref_location_key),
                getString(R.string.pref_location_default_value)
        );
            ContentValues row = new ContentValues();
            row.put(LocationsContract.SavedLocations.COLUMN_FULL_NAME, forecastLocation);
            mDB.insert(LocationsContract.SavedLocations.TABLE_NAME, null, row);

    }
    private ArrayList<String> getLocationItems(){
       // LocationsDBHelper dbHelper = new LocationsDBHelper(this);
        //mDB = dbHelper.getReadableDatabase();


        ArrayList<String> locations = new ArrayList<>();
        String location;


            String sqlSelection =
                    LocationsContract.SavedLocations.COLUMN_FULL_NAME + " = ?";
        Cursor cursor = mDB.query(
               LocationsContract.SavedLocations.TABLE_NAME,
                null,
                null,
                null ,
                null,
                null,
                null
        );
//        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            location  = cursor.getString(
                    cursor.getColumnIndex(LocationsContract.SavedLocations.COLUMN_FULL_NAME)
            );
            locations.add(location);
        }
        cursor.close();
        return locations;
    }

    @Override
    public void updateLocation(String location) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(getString(R.string.pref_location_key),location);
        editor.apply();
        mDrawerLayout.closeDrawer(GravityCompat.START);
    }
   /* @Override
    public void onLocationItemClick(String location) {


    }*/
   public void showDialog(){
       AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
       LayoutInflater inflater = this.getLayoutInflater();
       final View dialogView = inflater.inflate(R.layout.location_entry, null);
       dialogBuilder.setView(dialogView);


       final EditText edt = (EditText) dialogView.findViewById(R.id.et_location_entry);


       dialogBuilder.setPositiveButton("Accept", new DialogInterface.OnClickListener() {
           public void onClick(DialogInterface dialog, int whichButton) {
               String  newLocation = edt.getText().toString();
               boolean saved = checkIsLocationSaved();
               if(!saved) {
                   ContentValues row = new ContentValues();
                   row.put(LocationsContract.SavedLocations.COLUMN_FULL_NAME, newLocation);
                   mDB.insert(LocationsContract.SavedLocations.TABLE_NAME, null, row);
                   //do something with edt.getText().toString();
               }
           }
       });

       AlertDialog b = dialogBuilder.create();
       b.show();
   }

   }


