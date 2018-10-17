package com.example.android.weatherwithsqlite;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.weatherwithsqlite.utils.OpenWeatherMapUtils;

import java.util.ArrayList;



public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationItemViewHolder> {
    private SQLiteDatabase mDB;
    private ArrayList<String> mLocations;
    private static final String TAG = LocationAdapter.class.getSimpleName();
    private LocationAdapter.OnLocationItemClickListener mLocationItemClickListener;
    private Context context;

    public interface OnLocationItemClickListener {
        void updateLocation(String location);
    }
    public LocationAdapter(Context context, OnLocationItemClickListener clickListener) {
        context = context;
      mLocationItemClickListener = clickListener;
    }
    public void printDb(ArrayList<String> ss) {
        for (String s : ss)
            Log.d("Printing db contents", s);

    }
    @Override
    public LocationAdapter.LocationItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.location, parent, false);
        return new LocationAdapter.LocationItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(LocationAdapter.LocationItemViewHolder holder, int position) {
        holder.bind(mLocations.get(position));
    }
    public void updateLocationItems(ArrayList<String> locations) {
        mLocations = locations;
      //  printDb(mLocations);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        if (mLocations != null) {
            return mLocations.size();
        }
        else {
            return 0;
        }
    }
    class LocationItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mLocation;
       // private TextView changemLocation;
            public LocationItemViewHolder(View itemView) {
                super(itemView);
                mLocation = itemView.findViewById(R.id.tv_location);
              //  changemLocation.findViewById(R.id.edit_location);
                itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            String location = mLocations.get(getAdapterPosition());
            mLocationItemClickListener.updateLocation(location);
            }
        public void bind(String location) {

                mLocation.setText(location);
               // changemLocation.setText();
            }
        }
    }

