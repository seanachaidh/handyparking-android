package com.seanachaidh.handyandroid;

import android.location.GnssStatus;
import android.util.Log;

import androidx.annotation.NonNull;

public class GPSLocationCallback extends GnssStatus.Callback {
    @Override
    public void onStarted() {
        super.onStarted();
        Log.d("location", "Gps started");
    }

    @Override
    public void onStopped() {
        super.onStopped();
        Log.d("location", "Gps stopped");
    }

    @Override
    public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
        super.onSatelliteStatusChanged(status);
    }
}
