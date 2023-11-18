package com.example.waremoon;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class PhotosActivity extends AppCompatActivity {

    private CameraActivity cameraActivity;
    private CameraPreview cameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraActivity = new CameraActivity();
        cameraPreview = findViewById(R.id.camera_preview);

        Button takePhotoButton = findViewById(R.id.takePhoto);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPreview.takePicture();
            }
        });
    }
}
