package com.seanachaidh.handyandroid;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.seanachaidh.handyparking.ParkingSpot;
import com.seanachaidh.handyparking.Resources.ParkingspotSpecificResource;

import java.util.HashMap;

public class MarkerInfoActivity extends AppCompatActivity {
    private int parkingSpotId;
    private ParkingspotSpecificResource parkingspotResource;
    private boolean loaded = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        parkingSpotId = this.getIntent().getIntExtra(getString(R.string.INFO_VIEW_PARKINGID), -1);

        if(parkingSpotId == -1) {
            finish();
        }

        parkingspotResource = ClientSingleton.getInstance().getParkingspotSpecificResource();
        setContentView(R.layout.marker_info_layout);
        this.initializeInfo();
    }

    public void handleImageClick(View v) {
        Log.d(getString(R.string.LOGTAG), "image clicked");
    }


    private void setInfo(ParkingSpot p) {
        Log.d(getString(R.string.LOGTAG), "Setting parking: " + p.getId());
        TextView latitudeTV = findViewById(R.id.marker_info_latitude);
        TextView longtitudeTV = findViewById(R.id.marker_info_longtitude);

        longtitudeTV.setText(String.valueOf(p.getCoordinate().getLongtitude()));
        latitudeTV.setText(String.valueOf(p.getCoordinate().getLatitude()));

        RatingBar rating = findViewById(R.id.marker_info_rating);
        rating.setRating(p.getRating());

        SwitchCompat switchCompat = findViewById(R.id.marker_info_bezet);
        switchCompat.setChecked(p.isOccupied());

        p.downloadImage(ClientSingleton.getInstance().getClient()).whenComplete(((byteBuffer, throwable) -> {
            ImageView imageView = findViewById(R.id.marker_info_image);
            Bitmap bitmap = BitmapFactory.decodeByteArray(byteBuffer.array(), byteBuffer.arrayOffset(), byteBuffer.array().length);
            imageView.setImageBitmap(bitmap);
            this.loaded = true;
        }));
    }
    private void initializeInfo() {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", String.valueOf(this.parkingSpotId));
        parkingspotResource.get(params, null, null).whenComplete(((parkingSpots, throwable) -> {
            ParkingSpot p = parkingSpots[0];
            this.setInfo(p);
        }));
    }

    public boolean isLoaded() {
        return loaded;
    }
}
