package com.example.waremoon;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.io.File;
import java.io.IOException;
import java.util.Date;
// Test
public class CameraActivity {

    private static final int CAMERA_PERMISSION_REQUEST_CODE = 100;

    public CameraActivity() {
    }

    public boolean checkCameraHardware(Context context) {
        PackageManager packageManager = context.getPackageManager();

        return packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
    }

    public static Camera getCameraInstance(Activity activity) {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            // Request the permission.
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_REQUEST_CODE);

            return null;
        }
        Camera camera = null;
        try {
            camera = Camera.open(); // attempt to get a Camera instance
        } catch (Exception e) {
            // Camera is not available (in use or does not exist)
            Log.e("CameraActivity", "Error opening camera: " + e.getMessage());
        }

        return camera; // returns null if camera is unavailable
    }
}