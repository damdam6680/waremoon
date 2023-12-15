package com.example.waremoon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waremoon.R;
import com.example.waremoon.handler.SessionManagerHandler;
import com.example.waremoon.handler.UserLocationHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.unity3d.player.UnityPlayerActivity;

import org.shredzone.commons.suncalc.MoonPosition;

import java.time.ZonedDateTime;

public class MainActivity extends AppCompatActivity {

    private SessionManagerHandler sessionManager;
    private FusedLocationProviderClient fusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        UserLocationHandler userLocation = new UserLocationHandler(fusedLocationClient);
        userLocation.requestLocationPermission(MainActivity.this);

        Button moonButton = findViewById(R.id.moonButton);
        moonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    ZonedDateTime now = ZonedDateTime.now();
                    MoonPosition.Parameters moonParam = MoonPosition.compute()
                            .at(userLocation.getLatitude(), userLocation.getLongitude())
                            .timezone("Europe/Warsaw")
                            .on(now);
                    MoonPosition moon = moonParam.execute();
                    Intent i = new Intent(MainActivity.this, UnityPlayerActivity.class);
                    i.putExtra("longitude", (float) moon.getAzimuth());
                    i.putExtra("latitude", (float) moon.getAltitude());
                    startActivity(i);

                }
            }
        });

        sessionManager = new SessionManagerHandler(this);

        if (sessionManager.isLoggedIn()) {
            String userName = sessionManager.getUserName();
            String userId = String.valueOf(sessionManager.getUserId());

            TextView userTextView = findViewById(R.id.userTextView);
            userTextView.setText("Zalogowany użytkownik: " + userName + userId);
        }


        Button galleryButton = findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggedIn()) {
                    Intent galleryIntent = new Intent(MainActivity.this, GalleryActivity.class);
                    startActivity(galleryIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Musisz być zalogowany, aby skorzystać z tej funkcji", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button cameraButton = findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sessionManager.isLoggedIn()) {
                    long userId = sessionManager.getUserId();

                    Intent cameraIntent = new Intent(MainActivity.this, PhotosActivity.class);
                    cameraIntent.putExtra("USER_ID", userId);
                    startActivity(cameraIntent);
                } else {
                    Toast.makeText(MainActivity.this, "Musisz być zalogowany, aby skorzystać z tej funkcji", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button userButton = findViewById(R.id.userButton);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        Button userButton2 = findViewById(R.id.userButton2);
        userButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sessionManager.logoutUser();

                updateUIAfterLogout();
            }
        });

        Button panoramaButton = findViewById(R.id.panoramaButton);
        panoramaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent panoramaIntent = new Intent(MainActivity.this, PanoramaActivity.class);
                startActivity(panoramaIntent);
            }
        });

    }

    private void updateUIAfterLogout() {
        TextView userTextView = findViewById(R.id.userTextView);
        userTextView.setText("Zalogowany użytkownik: ");
    }
}
