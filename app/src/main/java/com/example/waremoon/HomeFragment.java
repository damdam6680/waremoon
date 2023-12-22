package com.example.waremoon;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.waremoon.R;
import com.example.waremoon.activity.GalleryActivity;
import com.example.waremoon.activity.LoginActivity;
import com.example.waremoon.activity.PanoramaActivity;
import com.example.waremoon.activity.PhotosActivity;
import com.example.waremoon.handler.SessionManagerHandler;
import com.example.waremoon.handler.UserLocationHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.unity3d.player.UnityPlayerActivity;

import org.shredzone.commons.suncalc.MoonPosition;

import java.time.ZonedDateTime;

public class HomeFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationClient;
    private MediaPlayer mediaPlayer = null;
    private SessionManagerHandler sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        UserLocationHandler userLocation = new UserLocationHandler(fusedLocationClient);
        userLocation.requestLocationPermission((AppCompatActivity) requireActivity());

        Button moonButton = view.findViewById(R.id.moonButton);
        moonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    ZonedDateTime now = ZonedDateTime.now();
                    MoonPosition.Parameters moonParam = MoonPosition.compute()
                            .at(userLocation.getLatitude(), userLocation.getLongitude())
                            .timezone("Europe/Warsaw")
                            .on(now);
                    MoonPosition moon = moonParam.execute();
                    Intent i = new Intent(requireActivity(), UnityPlayerActivity.class);
                    i.putExtra("longitude", (float) moon.getAzimuth());
                    i.putExtra("latitude", (float) moon.getAltitude());
                    startActivity(i);
                }
            }
        });

        sessionManager = new SessionManagerHandler(requireActivity());

        if (sessionManager.isLoggedIn()) {
            String userName = sessionManager.getUserName();
            String userId = String.valueOf(sessionManager.getUserId());

            TextView userTextView = view.findViewById(R.id.userTextView);
            userTextView.setText("Zalogowany użytkownik: " + userName + userId);
        }

        Button galleryButton = view.findViewById(R.id.galleryButton);
        galleryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
                if (sessionManager.isLoggedIn()) {
                    Intent galleryIntent = new Intent(requireActivity(), GalleryActivity.class);
                    startActivity(galleryIntent);
                } else {
                    Toast.makeText(requireActivity(), "Musisz być zalogowany, aby skorzystać z tej funkcji", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button cameraButton = view.findViewById(R.id.cameraButton);
        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
                if (sessionManager.isLoggedIn()) {
                    long userId = sessionManager.getUserId();

                    Intent cameraIntent = new Intent(requireActivity(), PhotosActivity.class);
                    cameraIntent.putExtra("USER_ID", userId);
                    startActivity(cameraIntent);
                } else {
                    Toast.makeText(requireActivity(), "Musisz być zalogowany, aby skorzystać z tej funkcji", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button userButton = view.findViewById(R.id.userButton);
        userButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
                Intent loginIntent = new Intent(requireActivity(), LoginActivity.class);
                startActivity(loginIntent);
            }
        });

        Button userButton2 = view.findViewById(R.id.userButton2);
        userButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
                sessionManager.logoutUser();
                updateUIAfterLogout(view);
            }
        });

        Button panoramaButton = view.findViewById(R.id.panoramaButton);
        panoramaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
                Intent panoramaIntent = new Intent(requireActivity(), PanoramaActivity.class);
                startActivity(panoramaIntent);
            }
        });

        return view;
    }

    private void updateUIAfterLogout(View view) {
        TextView userTextView = view.findViewById(R.id.userTextView);
        userTextView.setText("Zalogowany użytkownik: ");
    }

    private void play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.click2);
        }
        mediaPlayer.start();
    }
}
