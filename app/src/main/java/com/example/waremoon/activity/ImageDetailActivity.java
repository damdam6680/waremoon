package com.example.waremoon.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waremoon.sql.DBHandler;
import com.example.waremoon.adapter.ImageAdapter;
import com.example.waremoon.R;
import com.example.waremoon.handler.SessionManagerHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


public class ImageDetailActivity extends AppCompatActivity {

    private int selectedImagePosition;
    private int selectedImageId;
    private ImageAdapter imageAdapter;
    private ImageView imageView;
    private List<byte[]> userPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_layout);

        imageView = findViewById(R.id.imageDetailImageView);

        selectedImagePosition = getIntent().getIntExtra("position", 0);
        selectedImageId = getIntent().getIntExtra("imageId", 0);

        imageAdapter = new ImageAdapter(this);
        userPhotos = imageAdapter.getUserPhotosFromDatabase();

        if (userPhotos.size() > 0) {
            updateDisplayedImage(selectedImagePosition);
        }

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

        FloatingActionButton buttonDelete = findViewById(R.id.delete);
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDisplayedImage();
            }
        });
    }

    private void showNextImage() {
        if (selectedImagePosition + 1 < userPhotos.size()) {
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
        byte[] imageData = userPhotos.get(position);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.length);

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        imageView.setImageBitmap(bitmap);
    }

    private void deleteDisplayedImage() {
        int userId = new SessionManagerHandler(this).getUserId();
        int imageId = selectedImageId;

        // Usuń zdjęcie z bazy danych
        new DBHandler(this).deleteUserImage(userId, imageId);

        // Przeładuj listę zdjęć po usunięciu
        userPhotos = imageAdapter.getUserPhotosFromDatabase();

        // Wyświetl następne zdjęcie (jeśli istnieje) lub poprzednie (jeśli usuwane zdjęcie było ostatnim)
        if (userPhotos.size() >= 0) {
            if (selectedImagePosition >= userPhotos.size()) {
                selectedImagePosition = userPhotos.size() - 1;
            }
            setResult(RESULT_OK);
            updateDisplayedImage(selectedImagePosition);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}