package com.example.waremoon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

public class GalleryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        GridView gridView = findViewById(R.id.gridView);
        ImageAdapter imageAdapter = new ImageAdapter(this);
        gridView.setAdapter(imageAdapter);

        // Ustaw nasłuchiwacz
        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(String imagePath) {
                // Przejdź do nowego layoutu
                Intent intent = new Intent(GalleryActivity.this, ImageDetailActivity.class);
                intent.putExtra("imagePath", imagePath);
                startActivity(intent);
            }
        });
    }
}
