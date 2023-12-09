package com.example.waremoon;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PhotosActivity extends AppCompatActivity {

    private CameraActivity cameraActivity;
    private CameraPreview cameraPreview;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraActivity = new CameraActivity();
        cameraPreview = findViewById(R.id.camera_preview);

        sessionManager = new SessionManager(this);

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
