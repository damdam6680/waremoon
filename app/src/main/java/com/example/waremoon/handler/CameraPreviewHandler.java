package com.example.waremoon.handler;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import com.example.waremoon.activity.CameraActivity;
import com.example.waremoon.sql.DBHandler;

import java.io.IOException;
import java.util.List;

/**
 * A basic Camera preview class
 */
@SuppressLint("ViewConstructor")
public class CameraPreviewHandler extends SurfaceView implements SurfaceHolder.Callback {
    private final SurfaceHolder mHolder;
    private final Camera mCamera;
    private boolean isPictureProcessing = false;

    public CameraPreviewHandler(Context context, AttributeSet attrs) {
        super(context, attrs);
        mCamera = CameraActivity.getCameraInstance((Activity) context);

        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public CameraPreviewHandler(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mCamera = CameraActivity.getCameraInstance((Activity) context);

        mHolder = getHolder();
        mHolder.addCallback(this);
        // deprecated setting, but required on Android versions prior to 3.0
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        // The Surface has been created, now tell the camera where to draw the preview.
        try {
            mCamera.setPreviewDisplay(holder);

            // Set the camera display orientation
            setCameraDisplayOrientation((Activity) getContext(), Camera.CameraInfo.CAMERA_FACING_BACK, mCamera);

            mCamera.startPreview();
        } catch (IOException e) {
            Log.d("Camera", "Error setting camera preview: " + e.getMessage());
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // empty. Take care of releasing the Camera preview in your activity.
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // If your preview can change or rotate, take care of those events here.
        // Make sure to stop the preview before resizing or reformatting it.

        if (mHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            mCamera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }

        try {
            mCamera.setPreviewDisplay(mHolder);

            // Set the camera display orientation
            setCameraDisplayOrientation((Activity) getContext(), Camera.CameraInfo.CAMERA_FACING_BACK, mCamera);

            mCamera.startPreview();
        } catch (Exception e) {
            Log.d("Camera", "Error starting camera preview: " + e.getMessage());
        }
    }

    private void setCameraDisplayOrientation(Activity activity, int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);

        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;

        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate for mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }
    public void takePicture(final int userId, final int desiredWidth, final int desiredHeight) {
        if (!isPictureProcessing && mCamera != null) {
            isPictureProcessing = true;

            // Get the supported picture sizes
            Camera.Parameters parameters = mCamera.getParameters();
            List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();

            // Find the closest supported size to the desired size
            Camera.Size bestSize = getBestPictureSize(supportedPictureSizes, desiredWidth, desiredHeight);

            // Set the picture size
            parameters.setPictureSize(bestSize.width, bestSize.height);
            mCamera.setParameters(parameters);

            mCamera.takePicture(null, null, new Camera.PictureCallback() {
                @Override
                public void onPictureTaken(byte[] data, Camera camera) {
                    // Save the image to the database
                    DBHandler dbHandler = new DBHandler(getContext());
                    dbHandler.insertUserImage(userId, data);

                    // Restart the preview to allow taking more pictures
                    camera.startPreview();

                    // Reset the flag since picture processing is complete
                    isPictureProcessing = false;
                }
            });
        } else {
            Log.e("CameraPreview", "Camera is null or picture is being processed");
        }
    }

    private Camera.Size getBestPictureSize(List<Camera.Size> supportedSizes, int desiredWidth, int desiredHeight) {
        Camera.Size bestSize = supportedSizes.get(0);
        int currentDiff = Math.abs(bestSize.width * bestSize.height - desiredWidth * desiredHeight);

        for (Camera.Size size : supportedSizes) {
            int diff = Math.abs(size.width * size.height - desiredWidth * desiredHeight);
            if (diff < currentDiff) {
                bestSize = size;
                currentDiff = diff;
            }
        }

        return bestSize;
    }
}