package com.seanachaidh.handyandroid;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LocationListener {

    public static final int REQUEST_CODE = 1;
    public static final String[] PERMISSONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private StartScreenScroll start_screen_scroll;


    public MainActivity() {
    }

    private void showScrollView() {
        if (this.start_screen_scroll.getVisibility() == View.INVISIBLE) {
            this.start_screen_scroll.setVisibility(View.VISIBLE);
        }
    }

    private void checkPermissions() {
        ArrayList<String> toRequest = new ArrayList<>();
        for (String p : MainActivity.PERMISSONS) {
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

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        Log.d("general", "activity started");

        checkPermissions();

        if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000, 1, this);
        }


        Log.d("general", "loading extra views");

        setContentView(R.layout.activity_main);
        this.start_screen_scroll = findViewById(R.id.login_scroll);
        MapView map = (LoginMapView) findViewById(R.id.backgroundMap);
        map.setTileSource(TileSourceFactory.MAPNIK);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        //Check if the user has logged in or not
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        String token = prefs.getString(getString(R.string.token_key), "");
        if(!token.equals("")) {
            startAppActivity(token);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*
         * For debugging purposes logout is done in both cases
         */
        if(requestCode == 1) {
            assert data != null;
            boolean logoutResult = data.getBooleanExtra("logout", false);
            if(logoutResult){
                logout();
                Log.d("debug", "logout succeeded");

            } else {
                logout();
                Log.d("debug", "Logout not succeeded");
            }
        }
    }

    private void logout() {
        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(this.getString(R.string.token_key), "");
        editor.apply();
    }

    public void startAppActivity(String token) {
        Intent intent = new Intent(this, AppActivity.class);
        intent.putExtra("token", token);
        startActivityForResult(intent, 1);
    }

    public void showLogin(View view) {
        showScrollView();
        this.start_screen_scroll.useLogin();

    }

    public void showRegister(View view) {
        showScrollView();
        this.start_screen_scroll.useRegister();
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        LoginMapView v = findViewById(R.id.backgroundMap);
        v.getController().setCenter(new GeoPoint(location.getLatitude(), location.getLongitude()));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }
}
