package com.example.waremoon;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

public class GalleryActivity extends AppCompatActivity {

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
            public void onItemClick(int position) {
                Intent intent = new Intent(GalleryActivity.this, ImageDetailActivity.class);
                intent.putExtra("position", position);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        imageAdapter.notifyDataSetChanged();
    }
}
