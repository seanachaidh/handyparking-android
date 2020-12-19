package com.seanachaidh.handyandroid;

import android.content.Context;
import android.hardware.camera2.CameraDevice;
import android.util.Log;

import androidx.annotation.NonNull;

public class HandyCameraCallback extends CameraDevice.StateCallback {

    private final HandyCameraCommunication communication;

    public HandyCameraCallback(HandyCameraCommunication communication) {
        super();
        this.communication = communication;
    }

    @Override
    public void onOpened(@NonNull CameraDevice camera) {
        Log.d("handypariking", "Camera successvol geopend");
        communication.onCameraDeviceAvailable(camera);
    }

    @Override
    public void onDisconnected(@NonNull CameraDevice camera) {

    }

    @Override
    public void onError(@NonNull CameraDevice camera, int error) {
        
    }
}
