package com.seanachaidh.handyandroid.camera;

import android.hardware.camera2.CameraDevice;

public interface HandyCameraCommunication {
    void onCameraDeviceAvailable(CameraDevice device);
}
