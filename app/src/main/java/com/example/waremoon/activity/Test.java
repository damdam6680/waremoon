package com.example.waremoon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waremoon.R;
import com.example.waremoon.handler.SessionManagerHandler;

public class Test extends AppCompatActivity {

    private SessionManagerHandler sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        sessionManager = new SessionManagerHandler(this);

        // Odbierz nazwę użytkownika przekazaną z LoginActivity
        String userName = sessionManager.getUserName();
        String userId = String.valueOf(sessionManager.getUserId());

        // Znajdź TextView w layoucie main i ustaw nazwę użytkownika
        TextView userTextView = findViewById(R.id.userTextView);
        userTextView.setText("Zalogowany użytkownik: " + userName + userId);

        Button galleryButton = findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tworzymy nową intencję, aby uruchomić aktywność GalleryActivity
                Intent galleryIntent = new Intent(Test.this, GalleryActivity.class);
                startActivity(galleryIntent);
            }
        });

        Button cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Pobierz ID użytkownika z SessionManager
                long userId = sessionManager.getUserId();

                // Create a new intent and pass the user ID to PhotosActivity
                Intent cameraIntent = new Intent(Test.this, PhotosActivity.class);
                cameraIntent.putExtra("USER_ID", userId);
                startActivity(cameraIntent);
            }
        });

        Button userButton = findViewById(R.id.userButton);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tworzymy nową intencję, aby uruchomić aktywność GalleryActivity
                Intent loginIntent = new Intent(Test.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        Button panoramaButton = findViewById(R.id.panoramaButton);
        panoramaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tworzymy nową intencję, aby uruchomić aktywność GalleryActivity
                Intent panoramaIntent = new Intent(Test.this, PanoramaActivity.class);
                startActivity(panoramaIntent);
            }
        });

    }
}
