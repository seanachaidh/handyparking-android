package com.seanachaidh.handyandroid;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.seanachaidh.handyparking.Resources.LoginResource;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class AppActivity extends AppCompatActivity implements LocationListener {

    private boolean hasCentered = false;
    public static final int REQUEST_CODE = 2;
    public static final String[] PERMISSONS = {
            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    };

    private void checkPermissions() {
        ArrayList<String> toRequest = new ArrayList<>();
        for (String p : AppActivity.PERMISSONS) {
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
        });

        checkPermissions();

        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 1, this);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }


    public void handleLogout(MenuItem item) {
        LoginResource resource = new LoginResource(ClientSingleton.getInstance().getClient());
        final AppActivity ctx = this;
        String token = this.getIntent().getStringExtra("token");
        if(!token.equals("")) {
            HashMap<String, String> headers = new HashMap<>();

            headers.put("Authorization", "bearer " + token);
            CompletableFuture<Boolean> result =  resource.post(null, null, headers);
            result.whenComplete(new BiConsumer<Boolean, Throwable>() {
                @Override
                public void accept(Boolean aBoolean, Throwable throwable) {
                    Intent intent = new Intent();
                    intent.putExtra("logout", aBoolean);
                    ctx.setResult(1, intent);
                    ctx.finish();
                }
            });
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(!hasCentered) {
            hasCentered = true;
            MapView v = findViewById(R.id.markerMap);
            v.getController().setCenter(new GeoPoint(location.getLatitude(), location.getLongitude()));
        }
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
