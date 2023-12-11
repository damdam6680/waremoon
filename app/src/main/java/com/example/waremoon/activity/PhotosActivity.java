package com.example.waremoon.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waremoon.handler.CameraPreviewHandler;
import com.example.waremoon.R;
import com.example.waremoon.handler.SessionManagerHandler;

public class PhotosActivity extends AppCompatActivity {

    private CameraActivity cameraActivity;
    private CameraPreviewHandler cameraPreview;

    private SessionManagerHandler sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraActivity = new CameraActivity();
        cameraPreview = findViewById(R.id.camera_preview);

        sessionManager = new SessionManagerHandler(this);

        int userId = sessionManager.getUserId();

        Button takePhotoButton = findViewById(R.id.takePhoto);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPreview.takePicture(userId);
            }
        });
    }


}
