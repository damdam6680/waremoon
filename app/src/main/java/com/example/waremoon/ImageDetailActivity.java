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
    private List<byte[]> userPhotos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_layout);

        imageView = findViewById(R.id.imageDetailImageView);

        selectedImagePosition = getIntent().getIntExtra("position", 0);

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

        Button buttonDelete = findViewById(R.id.delete);
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
        imageView.setImageBitmap(bitmap);
        imageView.setRotation(90);
    }

    private void deleteDisplayedImage() {
        int userId = new SessionManager(this).getUserId();
        int imageId = selectedImagePosition + 1; // Indeksowanie od 1 w bazie danych

        // Usuń zdjęcie z bazy danych
        new DBHandler(this).deleteUserImage(userId, imageId);

        // Przeładuj listę zdjęć po usunięciu
        userPhotos = imageAdapter.getUserPhotosFromDatabase();

        // Wyświetl następne zdjęcie (jeśli istnieje) lub poprzednie (jeśli usuwane zdjęcie było ostatnim)
        if (userPhotos.size() > 0) {
            if (selectedImagePosition >= userPhotos.size()) {
                selectedImagePosition = userPhotos.size() - 1;
            }
            updateDisplayedImage(selectedImagePosition);
        } else {
            // Brak zdjęć, zrób coś odpowiedniego (np. wróć do poprzedniej aktywności)
            finish(); // Przykładowe zakończenie aktywności, można dostosować
        }
    }
}