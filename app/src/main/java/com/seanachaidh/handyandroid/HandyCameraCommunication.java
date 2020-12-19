package com.seanachaidh.handyandroid;

import android.hardware.camera2.CameraDevice;

public interface HandyCameraCommunication {
    void onCameraDeviceAvailable(CameraDevice device);
}
