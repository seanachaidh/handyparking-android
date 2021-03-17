package com.seanachaidh.handyandroid.camera;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.seanachaidh.handyandroid.R;

public class HandyCameraActivity extends AppCompatActivity implements HandyCameraCommunication {

    private CameraManager manager;
    private String[] cids = null;

//    private CaptureRequest createCaptureRequest() {
//    }


    private void initializeCamera() {
        this.manager = (CameraManager) getSystemService(CAMERA_SERVICE);
        try {
            this.cids = this.manager.getCameraIdList();
        } catch (CameraAccessException e) {
            Log.d(getString(R.string.LOGTAG), "Geen toegang tot camera");
        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            try {
                manager.openCamera(cids[0], new HandyCameraCallback(this), null);
            } catch (CameraAccessException e) {
                Log.d(getString(R.string.LOGTAG),"Error while opening camera");
            }
        } else {
            Log.d(getString(R.string.LOGTAG), "Camera permission was not granted");
        }

    }

    public HandyCameraActivity() {
        
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        initializeCamera();
    }

    @Override
    public void onCameraDeviceAvailable(CameraDevice device) {
        SurfaceView surfaceView = findViewById(R.id.cameraView);
        CaptureRequest request = null;
        Log.d(getString(R.string.LOGTAG), "Camera became available");
        Log.d(getString(R.string.LOGTAG), "Initiating camara request");
        try {
            CaptureRequest.Builder builder = device.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            builder.addTarget(surfaceView.getHolder().getSurface());
            request = builder.build();
        } catch (CameraAccessException e) {
            Log.d("handyparking", "Error in creating capture request");
            e.printStackTrace();
        }
        
    }
}
