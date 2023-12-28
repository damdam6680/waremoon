package com.example.waremoon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waremoon.handler.CameraPreviewHandler;
import com.example.waremoon.R;
import com.example.waremoon.handler.SessionManagerHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class PhotosActivity extends AppCompatActivity {

    private CameraPreviewHandler cameraPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        cameraPreview = findViewById(R.id.camera_preview);

        SessionManagerHandler sessionManager = new SessionManagerHandler(this);

        int userId = sessionManager.getUserId();

        Button takePhotoButton = findViewById(R.id.takePhoto);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPreview.takePicture(userId);
            }
        });

        FloatingActionButton cameraButton = findViewById(R.id.gallery);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManagerHandler sessionManager = new SessionManagerHandler(PhotosActivity.this);
                int userId = sessionManager.getUserId();

                Intent cameraIntent = new Intent(PhotosActivity.this, GalleryActivity.class);
                cameraIntent.putExtra("USER_ID", userId);
                startActivity(cameraIntent);
            }
        });
    }

    @Override
    protected void onDestroy() {
        CameraActivity.releaseCamera(cameraPreview.getCameraInstance());

        super.onDestroy();
    }
}
