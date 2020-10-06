package com.seanachaidh.handyandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.prefs.Preferences;

public class HandyMapView extends MapView implements Subscriber {

    public static final double ZOOM_LEVEL = 15.0;

    public HandyMapView(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs) {
        super(context, tileProvider, tileRequestCompleteHandler, attrs);
        this.getController().setZoom(ZOOM_LEVEL);
    }

    public HandyMapView(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs, boolean hardwareAccelerated) {
        super(context, tileProvider, tileRequestCompleteHandler, attrs, hardwareAccelerated);
        this.getController().setZoom(ZOOM_LEVEL);
    }

    public HandyMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getController().setZoom(ZOOM_LEVEL);
    }

    public HandyMapView(Context context) {
        super(context);
        this.getController().setZoom(ZOOM_LEVEL);
    }

    public HandyMapView(Context context, MapTileProviderBase aTileProvider) {
        super(context, aTileProvider);
        this.getController().setZoom(ZOOM_LEVEL);
    }

    public HandyMapView(Context context, MapTileProviderBase aTileProvider, Handler tileRequestCompleteHandler) {
        super(context, aTileProvider, tileRequestCompleteHandler);
        this.getController().setZoom(ZOOM_LEVEL);
    }

    @Override
    public void onUpdate(Object data) {
        AppCompatActivity context = (AppCompatActivity) data;
        SharedPreferences prefs = context.getPreferences(Context.MODE_PRIVATE);
        float currentLong = prefs.getFloat(context.getString(R.string.location_longtitude_key), 0.0f);
        float currentLat = prefs.getFloat(context.getString(R.string.location_latitude_key), 0.0f);

        getController().setCenter(new GeoPoint(currentLat, currentLong));

    }
}
