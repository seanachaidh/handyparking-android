package com.seanachaidh.handyandroid;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.seanachaidh.handyparking.Coordinate;

import java.util.ArrayList;
import java.util.Locale;

public class GPSLocationListener implements LocationListener, Subscribeable {
    private ArrayList<Subscriber> subscribers;

    public GPSLocationListener() {
        this.subscribers = new ArrayList<Subscriber>();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        String logString;
        float currentLat = (float) location.getLatitude();
        float currentLong = (float) location.getLongitude();

        logString = String.format(Locale.ENGLISH, "Current location: %.2f,%.2f", currentLong, currentLat);
        Log.d("location", logString);

        //TODO make this into a subscribable
        this.notifySubscribers(new Coordinate(currentLat, currentLong));
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
    public void notifySubscribers(Object data) {
        for(Subscriber s: subscribers) {
            s.onUpdate(data);
        }
    }
}
