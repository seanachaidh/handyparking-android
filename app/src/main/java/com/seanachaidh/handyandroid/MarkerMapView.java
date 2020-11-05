package com.seanachaidh.handyandroid;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.util.AttributeSet;

import androidx.appcompat.app.AppCompatActivity;

import com.seanachaidh.handyparking.Coordinate;
import com.seanachaidh.handyparking.ParkingSpot;
import com.seanachaidh.handyparking.Resources.ParkingspotResource;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;


public class MarkerMapView extends MapView implements Subscriber {
    private boolean hasCentered = false;


    public MarkerMapView(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs) {
        super(context, tileProvider, tileRequestCompleteHandler, attrs);
        //TODO: Extraheren naar een aparte methode
        loadMarkersSlow();
        this.getController().setZoom(HandyMapView.ZOOM_LEVEL);
    }

    public MarkerMapView(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs, boolean hardwareAccelerated) {
        super(context, tileProvider, tileRequestCompleteHandler, attrs, hardwareAccelerated);
        loadMarkersSlow();
        this.getController().setZoom(HandyMapView.ZOOM_LEVEL);
    }

    public MarkerMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadMarkersSlow();
        this.getController().setZoom(HandyMapView.ZOOM_LEVEL);
    }

    public MarkerMapView(Context context) {
        super(context);
        loadMarkersSlow();
        this.getController().setZoom(HandyMapView.ZOOM_LEVEL);
    }

    public MarkerMapView(Context context, MapTileProviderBase aTileProvider) {
        super(context, aTileProvider);
        loadMarkersSlow();
        this.getController().setZoom(HandyMapView.ZOOM_LEVEL);
    }

    public MarkerMapView(Context context, MapTileProviderBase aTileProvider, Handler tileRequestCompleteHandler) {
        super(context, aTileProvider, tileRequestCompleteHandler);
        loadMarkersSlow();
        this.getController().setZoom(HandyMapView.ZOOM_LEVEL);
    }

    private void loadMarkers() {
        ParkingspotResource parkingspotResource = new ParkingspotResource(ClientSingleton.getInstance().getClient());
        final MarkerMapView parent = this;
        CompletableFuture<ParkingSpot[]> future = parkingspotResource.get(null, null, null);
        future.whenComplete(new BiConsumer<ParkingSpot[], Throwable>() {
            @Override
            public void accept(ParkingSpot[] parkingSpots, Throwable throwable) {
                List<IGeoPoint> points = new ArrayList<>();

                for(ParkingSpot parkingSpot: parkingSpots) {
                    points.add(new LabelledGeoPoint(parkingSpot.getCoordinate().getLatitude(), parkingSpot.getCoordinate().getLongtitude(), "Point"));

                }

                Paint textStyle = new Paint();
                textStyle.setStyle(Paint.Style.FILL);
                textStyle.setColor(Color.parseColor("#0000ff"));
                textStyle.setTextAlign(Paint.Align.CENTER);
                textStyle.setTextSize(24);

                SimplePointTheme pt = new SimplePointTheme(points, true);
                SimpleFastPointOverlayOptions opts = SimpleFastPointOverlayOptions.getDefaultStyle()
                        .setAlgorithm(SimpleFastPointOverlayOptions.RenderingAlgorithm.MAXIMUM_OPTIMIZATION)
                        .setIsClickable(true).setCellSize(15).setTextStyle(textStyle).setRadius(7);
                final SimpleFastPointOverlay overlay = new SimpleFastPointOverlay(pt, opts);
                parent.getOverlays().add(overlay);
            }
        });
    }

    public void loadMarkersSlow() {
        ParkingspotResource parkingspotResource = new ParkingspotResource(ClientSingleton.getInstance().getClient());
        final MarkerMapView parent = this;
        CompletableFuture<ParkingSpot[]> future = parkingspotResource.get(null, null, null);
        future.whenComplete(new BiConsumer<ParkingSpot[], Throwable>() {
            @Override
            public void accept(ParkingSpot[] parkingSpots, Throwable throwable) {
                for(ParkingSpot p: parkingSpots) {
                    parent.addMarker(p.getCoordinate().getLatitude(), p.getCoordinate().getLongtitude());
                }
            }
        });
    }

    /**
     * Adds a new marker to the map
     * TODO: Test this
     * @param longtitude the longtitude of the marker
     * @param latitude the latitude of the marker
     */
    void addMarker(double latitude, double longtitude) {
        Marker m = new Marker(this);
        m.setPosition(new GeoPoint(latitude, longtitude));
        m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP);
        this.getOverlays().add(m);
    }

    @Override
    public void onUpdate(Object data) {
        //TODO: Dit kan beter
        if(!hasCentered) {
            this.hasCentered = true;
            AppCompatActivity context = (AppCompatActivity) data;
            SharedPreferences prefs = context.getPreferences(Context.MODE_PRIVATE);
            float currentLong = prefs.getFloat(context.getString(R.string.location_longtitude_key), 0.0f);
            float currentLat = prefs.getFloat(context.getString(R.string.location_latitude_key), 0.0f);

            getController().setCenter(new GeoPoint(currentLat, currentLong));
        }
    }
}
