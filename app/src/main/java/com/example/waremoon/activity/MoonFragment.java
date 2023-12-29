package com.example.waremoon.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.waremoon.R;
import com.example.waremoon.activity.PanoramaActivity;
import com.example.waremoon.handler.UserLocationHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.unity3d.player.UnityPlayerActivity;

import org.shredzone.commons.suncalc.MoonPosition;
import org.shredzone.commons.suncalc.MoonTimes;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

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
        Button todayButton = view.findViewById(R.id.todayButton);
        Button tomorrowButton = view.findViewById(R.id.tomorrowButton);
        Button weekButton = view.findViewById(R.id.weekButton);

        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZonedDateTime dateTime = ZonedDateTime.now().plusDays(7);
                MoonUpdate(userLocation, dateTime,view);
            }
        });
        tomorrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZonedDateTime dateTime = ZonedDateTime.now().plusDays(1);
                MoonUpdate(userLocation, dateTime,view);
            }
        });

        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZonedDateTime dateTime = ZonedDateTime.now();
                MoonUpdate(userLocation, dateTime,view);
            }
        });
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
        ZonedDateTime dateTime = ZonedDateTime.now();
        MoonUpdate(userLocation, dateTime,view);
        return view;
    }

    private void play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.click2);
        }
        mediaPlayer.start();
    }

    public void MoonUpdate(UserLocationHandler userLocation, ZonedDateTime date,View view){


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX['['VV']']");

            MoonTimes.Parameters moonTime = MoonTimes.compute().on(date);

            MoonPosition.Parameters moonParam = MoonPosition.compute()
                    .at(userLocation.getLatitude(), userLocation.getLongitude())
                    .timezone("Europe/Warsaw")
                    .on(date);

            MoonTimes moon = moonTime.execute();
            MoonPosition moonPosition = moonParam.execute();

            String moonRise = String.valueOf(moon.getRise());
            String moonSet =   String.valueOf(moon.getSet());
            String moonHorizont = "TAK";

            if (moon.isAlwaysUp()){
                moonHorizont = "TAK";
            }else { moonHorizont = "NIE"; }

            ZonedDateTime zonedDateTimeRise = ZonedDateTime.parse(moonRise, formatter);
            ZonedDateTime zonedDateTimeSet = ZonedDateTime.parse(moonSet, formatter);

            TextView timeTextView = view.findViewById(R.id.moonRiseTextView);

            TextView moonFaze2TextView = view.findViewById(R.id.moonFaze2TextView);

            TextView moonSetTextView = view.findViewById(R.id.moonSetTextView);

            TextView timeTextView1 = view.findViewById(R.id.moonRiseTextView1);

            TextView moonFaze2TextView1 = view.findViewById(R.id.moonFaze2TextView1);

            TextView moonSetTextView1 = view.findViewById(R.id.moonSetTextView1);

            timeTextView.setText( zonedDateTimeRise.getHour() + ":" +  zonedDateTimeRise.getMinute());
            moonFaze2TextView.setText(moonHorizont);
            moonSetTextView.setText(zonedDateTimeSet.getHour() + ":" + zonedDateTimeSet.getMinute());

            timeTextView1.setText(("" +  (int) moonPosition.getAltitude()) + "°" );
            moonFaze2TextView1.setText("" + (int)  moonPosition.getDistance() + " km");
            moonSetTextView1.setText("" +  (int)  moonPosition.getAzimuth() + "°");


        }
    }
}
