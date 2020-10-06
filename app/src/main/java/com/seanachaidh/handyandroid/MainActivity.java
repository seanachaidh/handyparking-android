package com.seanachaidh.handyandroid;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.util.LocationUtils;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    public static final String[] permissons = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private ScrollView login_view;
    private ScrollView register_view;
    private StartScreenScroll start_screen_scroll;
    private MapView map;
    private IMapController mapController;


    private LocationManager locationManager;

    private void showScrollView() {
        if (this.start_screen_scroll.getVisibility() == View.INVISIBLE) {
            this.start_screen_scroll.setVisibility(View.VISIBLE);
        }
    }

    private void checkPermissions(String[] permissions) {
        ArrayList<String> toRequest = new ArrayList<String>();
        for (String p : permissions) {
            if (this.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                //we need to ask
                toRequest.add(p);
            }
        }
        if (toRequest.size() > 0) {
            this.requestPermissions(toRequest.toArray(new String[0]), REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE) {
            for (int i = 0; i < permissions.length; i++) {
                String permission = permissions[i];
                int result = grantResults[i];

                if (result == PackageManager.PERMISSION_GRANTED) {
                    Log.d("general", "Permission " + permission + " grandted");
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        Log.d("general", "activity started");

        checkPermissions(permissons);

        GPSLocationListener gpsLocationListener = new GPSLocationListener(this);

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 1, gpsLocationListener);
        }


        Log.d("general", "loading extra views");

        setContentView(R.layout.activity_main);
        this.start_screen_scroll = (StartScreenScroll) findViewById(R.id.login_scroll);
        this.map = (HandyMapView) findViewById(R.id.backgroundMap);
        this.map.setTileSource(TileSourceFactory.MAPNIK);
        gpsLocationListener.addSubscriber((Subscriber) this.map);

    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Check if the user has logged in or not
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        String token = prefs.getString(getString(R.string.token_key), "");

    }

    public void showLogin(View view) {
        showScrollView();
        this.start_screen_scroll.useLogin();

    }

    public void showRegister(View view) {
        showScrollView();
        this.start_screen_scroll.useRegister();
    }

}
