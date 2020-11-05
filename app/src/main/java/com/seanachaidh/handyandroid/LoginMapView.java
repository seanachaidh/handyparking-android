package com.seanachaidh.handyandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.appcompat.app.AppCompatActivity;

import com.seanachaidh.handyparking.Coordinate;

import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.prefs.Preferences;

public class LoginMapView extends MapView implements Subscriber {

    public static final double ZOOM_LEVEL = 15.0;

    public LoginMapView(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs) {
        super(context, tileProvider, tileRequestCompleteHandler, attrs);
        this.getController().setZoom(ZOOM_LEVEL);
    }

    public LoginMapView(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs, boolean hardwareAccelerated) {
        super(context, tileProvider, tileRequestCompleteHandler, attrs, hardwareAccelerated);
        this.getController().setZoom(ZOOM_LEVEL);
    }

    public LoginMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.getController().setZoom(ZOOM_LEVEL);
    }

    public LoginMapView(Context context) {
        super(context);
        this.getController().setZoom(ZOOM_LEVEL);
    }

    public LoginMapView(Context context, MapTileProviderBase aTileProvider) {
        super(context, aTileProvider);
        this.getController().setZoom(ZOOM_LEVEL);
    }

    public LoginMapView(Context context, MapTileProviderBase aTileProvider, Handler tileRequestCompleteHandler) {
        super(context, aTileProvider, tileRequestCompleteHandler);
        this.getController().setZoom(ZOOM_LEVEL);
    }

    @Override
    public void onUpdate(Object data) {
        Coordinate c = (Coordinate) data;
        getController().setCenter(new GeoPoint(c.getLatitude(), c.getLongtitude()));
    }
}
