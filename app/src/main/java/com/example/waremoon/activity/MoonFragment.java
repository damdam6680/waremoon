package com.example.waremoon.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.waremoon.R;
import com.example.waremoon.activity.PanoramaActivity;
import com.example.waremoon.handler.UserLocationHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.unity3d.player.UnityPlayerActivity;

import org.shredzone.commons.suncalc.MoonPosition;

import java.time.ZonedDateTime;

public class MoonFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationClient;
    private MediaPlayer mediaPlayer = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_moon, container, false);

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

    private void play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.click2);
        }
        mediaPlayer.start();
    }
}
