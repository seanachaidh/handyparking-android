package com.seanachaidh.handyandroid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.seanachaidh.handyparking.Coordinate;
import com.seanachaidh.handyparking.ParkingSpot;
import com.seanachaidh.handyparking.Resources.ParkingspotResource;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.tileprovider.MapTileProviderBase;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.Projection;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.infowindow.MarkerInfoWindow;
import org.osmdroid.views.overlay.simplefastpoint.LabelledGeoPoint;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlay;
import org.osmdroid.views.overlay.simplefastpoint.SimpleFastPointOverlayOptions;
import org.osmdroid.views.overlay.simplefastpoint.SimplePointTheme;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;


public class MarkerMapView extends MapView implements GestureDetector.OnGestureListener {
    private GestureDetector gestureDetector;
    private void initMarkerMap() {
        loadMarkersSlow();
        this.getController().setZoom(LoginMapView.ZOOM_LEVEL);
        gestureDetector = new GestureDetector(this.getContext(), this);
    }

    public MarkerMapView(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs) {
        super(context, tileProvider, tileRequestCompleteHandler, attrs);
        initMarkerMap();
    }

    public MarkerMapView(Context context, MapTileProviderBase tileProvider, Handler tileRequestCompleteHandler, AttributeSet attrs, boolean hardwareAccelerated) {
        super(context, tileProvider, tileRequestCompleteHandler, attrs, hardwareAccelerated);
        initMarkerMap();
    }

    public MarkerMapView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initMarkerMap();
    }

    public MarkerMapView(Context context) {
        super(context);
        initMarkerMap();
    }

    public MarkerMapView(Context context, MapTileProviderBase aTileProvider) {
        super(context, aTileProvider);
        initMarkerMap();
    }

    public MarkerMapView(Context context, MapTileProviderBase aTileProvider, Handler tileRequestCompleteHandler) {
        super(context, aTileProvider, tileRequestCompleteHandler);
        initMarkerMap();
    }

    private void loadMarkers() {
        ParkingspotResource parkingspotResource = new ParkingspotResource(ClientSingleton.getInstance().getClient());
        final MarkerMapView parent = this;
        CompletableFuture<ParkingSpot[]> future = parkingspotResource.get(null, null, null);
        future.whenComplete((parkingSpots, throwable) -> {
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
        });
    }

    public void loadMarkersSlow() {
        ParkingspotResource parkingspotResource = new ParkingspotResource(ClientSingleton.getInstance().getClient());
        final MarkerMapView parent = this;
        CompletableFuture<ParkingSpot[]> future = parkingspotResource.get(null, null, null);
        future.whenComplete((parkingSpots, throwable) -> {
            for(ParkingSpot p: parkingSpots) {
                parent.addMarker(p.getId(), p.getCoordinate().getLatitude(), p.getCoordinate().getLongtitude());
            }
        });
    }

    /**
     * Adds a new marker to the map
     * @param id Id of the makerker
     * @param longtitude the longtitude of the marker
     * @param latitude the latitude of the marker
     */
    void addMarker(final int id, final double latitude, final double longtitude) {
        AppCompatActivity parentContext = (AppCompatActivity) this.getContext();
        MarkerInfoWindow infoWindow = new MarkerInfoWindow(R.layout.marker_layout, this);

        Marker m = new Marker(this);
        m.setPosition(new GeoPoint(latitude, longtitude));
        m.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_TOP);
        this.getOverlays().add(m);

        m.setOnMarkerClickListener((marker, mapView) -> {
            Log.d(this.getContext().getString(R.string.LOGTAG), String.format("Location clicked %f;%f", latitude, longtitude));
            Intent intent = new Intent(parentContext, MarkerInfoActivity.class);
            intent.putExtra(this.getContext().getApplicationContext().getString(R.string.INFO_VIEW_PARKINGID), id);
            parentContext.startActivity(intent);
            return false;
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        return gestureDetector.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.d(this.getContext().getString(R.string.LOGTAG), "long press caught");
        int x = (int) e.getX();
        int y = (int) e.getY();
        Projection projection = this.getProjection();
        IGeoPoint clickedLocation = projection.fromPixels(x, y);
        Log.d(this.getContext().getString(R.string.LOGTAG), "clicked on: " + clickedLocation.getLatitude() + ";" + clickedLocation.getLongitude());
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}
