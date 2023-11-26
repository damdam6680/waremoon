package com.example.waremoon;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.List;

public class ImageDetailActivity extends AppCompatActivity {

    private int selectedImagePosition;
    private ImageAdapter imageAdapter;
    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_layout);

        imageView = findViewById(R.id.imageDetailImageView);

        selectedImagePosition = getIntent().getIntExtra("position", 0);

        imageAdapter = new ImageAdapter(this);

        String imagePath = imageAdapter.getImagePathByPosition(selectedImagePosition);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmap);
        imageView.setRotation(90);

        Button buttonNext = findViewById(R.id.next);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNextImage();
            }
        });

        Button buttonPrev = findViewById(R.id.prev);
        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPrevImage();
            }
        });

        Button buttonDelete = findViewById(R.id.delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDisplayedImage();
            }
        });
    }

    private void showNextImage() {
        if (selectedImagePosition + 1 < imageAdapter.getCount()) {
            selectedImagePosition++;
            updateDisplayedImage(selectedImagePosition);
        }
    }

    private void showPrevImage() {
        if (selectedImagePosition - 1 >= 0) {
            selectedImagePosition--;
            updateDisplayedImage(selectedImagePosition);
        }
    }

    private void updateDisplayedImage(int position) {
        String imagePath = imageAdapter.getImagePathByPosition(position);
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
        imageView.setImageBitmap(bitmap);
        imageView.setRotation(90);
    }

    private void deleteDisplayedImage() {
        String imagePath = imageAdapter.getImagePathByPosition(selectedImagePosition);

        File file = new File(imagePath);
        boolean isDeleted = file.delete();

        if (isDeleted) {

            imageAdapter.removeImagePathByPosition(selectedImagePosition);

            updateDisplayedImage(selectedImagePosition);
        }
    }
}