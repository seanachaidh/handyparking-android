package com.seanachaidh.handyandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Locale;

public class GPSLocationListener implements LocationListener, Subscribeable {
    private final Context context;
    private final SharedPreferences preferences;
    private final ArrayList<Subscriber> subscribers;

    public GPSLocationListener(Context ctx) {
        this.context = ctx;
        this.preferences = ((AppCompatActivity) this.context).getPreferences(Context.MODE_PRIVATE);
        this.subscribers = new ArrayList<Subscriber>();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        float previousLat = this.preferences.getFloat(context.getString(R.string.location_latitude_key), 0.0f);
        float previousLong = this.preferences.getFloat(context.getString(R.string.location_longtitude_key), 0.0f);

        String logString = String.format(Locale.ENGLISH, "Previous location: %.2f,%.2f", previousLong, previousLat);
        Log.d("location", logString);

        float currentLat = (float) location.getLatitude();
        float currentLong = (float) location.getLongitude();

        logString = String.format(Locale.ENGLISH, "Current location: %.2f,%.2f", currentLong, currentLat);
        Log.d("location", logString);

        SharedPreferences.Editor prefEdit = preferences.edit();
        prefEdit.putFloat(context.getString(R.string.location_latitude_key), currentLat);
        prefEdit.putFloat(context.getString(R.string.location_longtitude_key), currentLong);
        prefEdit.apply();

        //TODO make this into a subscribable
        this.notifySubscribers();

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        Log.d("location", "GPS enabled");
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        Log.d("location", "GPS got disabled");
    }

    @Override
    public void addSubscriber(Subscriber s) {
        subscribers.add(s);
    }

    @Override
    public void notifySubscribers() {
        for(Subscriber s: subscribers) {
            s.onUpdate(this.context);
        }
    }
}
