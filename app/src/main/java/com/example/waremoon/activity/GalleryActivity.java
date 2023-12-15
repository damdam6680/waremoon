package com.example.waremoon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.waremoon.adapter.ImageAdapter;
import com.example.waremoon.interfaces.OnItemClickListener;
import com.example.waremoon.R;

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

        // Ustaw nasłuchiwacz
        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, int imageId) {
                Intent intent = new Intent(GalleryActivity.this, ImageDetailActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("imageId", imageId);
                startActivityForResult(intent, IMAGE_DETAIL_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_DETAIL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Tutaj sprawdź, czy doszło do zmiany w danych zdjęć
                // i odśwież widok galerii, jeśli to konieczne
                updateGallery();
            }
        }
    }

    private void updateGallery() {
        // Zaktualizuj listę zdjęć w adapterze galerii
        imageAdapter.updateUserPhotos();
        // Poinformuj adapter, że dane zostały zmienione
        imageAdapter.notifyDataSetChanged();
    }
}