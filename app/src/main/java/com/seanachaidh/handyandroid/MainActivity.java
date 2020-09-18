package com.seanachaidh.handyandroid;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ScrollView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private ScrollView login_view;
    private ScrollView register_view;
    private StartScreenScroll start_screen_scroll;

    private void showScrollView() {
        if(this.start_screen_scroll.getVisibility() == View.INVISIBLE) {
            this.start_screen_scroll.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 1) {
            for(int i = 0; i <= permissions.length; i++){
                String permission = permissions[i];
                int result = grantResults[i];

                if(permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION) && (result == PackageManager.PERMISSION_GRANTED)){
                    Log.d("general", "Permission granting successful");
                }

            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("general", "activity started");

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
        }

        Log.d("general", "loading extra views");

        setContentView(R.layout.activity_main);
        this.start_screen_scroll = (StartScreenScroll) findViewById(R.id.login_scroll);
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
