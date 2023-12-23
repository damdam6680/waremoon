package com.example.waremoon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.waremoon.adapter.ImageAdapter;
import com.example.waremoon.handler.SessionManagerHandler;
import com.example.waremoon.interfaces.OnItemClickListener;
import com.example.waremoon.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GalleryActivity extends AppCompatActivity {
    private static final int IMAGE_DETAIL_REQUEST_CODE = 1;

    ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        GridView gridView = findViewById(R.id.gridView);
        imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);

        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, int imageId) {
                Intent intent = new Intent(GalleryActivity.this, ImageDetailActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("imageId", imageId);
                startActivityForResult(intent, IMAGE_DETAIL_REQUEST_CODE);
            }
        });

        FloatingActionButton cameraButton = findViewById(R.id.camera);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManagerHandler sessionManager = new SessionManagerHandler(GalleryActivity.this);
                int userId = sessionManager.getUserId();

                Intent cameraIntent = new Intent(GalleryActivity.this, PhotosActivity.class);
                cameraIntent.putExtra("USER_ID", userId);
                startActivity(cameraIntent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_DETAIL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                updateGallery();
            }
        }
    }

    private void updateGallery() {
        imageAdapter.updateUserPhotos();
        imageAdapter.notifyDataSetChanged();
    }
}