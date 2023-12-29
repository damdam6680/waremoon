package com.example.waremoon.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.waremoon.R;
import com.example.waremoon.handler.UserLocationHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.unity3d.player.UnityPlayerActivity;

import org.shredzone.commons.suncalc.SunPosition;
import org.shredzone.commons.suncalc.SunTimes;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class SunFragment extends Fragment {

    private FusedLocationProviderClient fusedLocationClient;
    private MediaPlayer mediaPlayer = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_sun, container, false);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        UserLocationHandler userLocation = new UserLocationHandler(fusedLocationClient);
        userLocation.requestLocationPermission((AppCompatActivity) requireActivity());

        Button sunButton = view.findViewById(R.id.sunButton);
        Button todayButton = view.findViewById(R.id.todayButton);
        Button tomorrowButton = view.findViewById(R.id.tomorrowButton);
        Button weekButton = view.findViewById(R.id.weekButton);

        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZonedDateTime dateTime = ZonedDateTime.now().plusDays(7);
                SunUpdate(userLocation, dateTime,view);
            }
        });
        tomorrowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZonedDateTime dateTime = ZonedDateTime.now().plusDays(1);
                SunUpdate(userLocation, dateTime,view);
            }
        });

        todayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ZonedDateTime dateTime = ZonedDateTime.now();
                SunUpdate(userLocation, dateTime,view);
            }
        });
        sunButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    ZonedDateTime now = ZonedDateTime.now();
                    SunPosition.Parameters sunParam = SunPosition.compute()
                            .at(userLocation.getLatitude(), userLocation.getLongitude())
                            .timezone("Europe/Warsaw")
                            .on(now);
                    SunPosition sun = sunParam.execute();
                    Intent i = new Intent(requireActivity(), UnityPlayerActivity.class);
                    i.putExtra("longitude", (float) sun.getAzimuth());
                    i.putExtra("latitude", (float) sun.getAltitude());
                    startActivity(i);
                }
            }
        });

        Button panoramaButton = view.findViewById(R.id.panoramaButton);
        panoramaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
                Intent panoramaIntent = new Intent(requireActivity(), PanoramaSunActivity.class);
                startActivity(panoramaIntent);
            }
        });
        ZonedDateTime dateTime = ZonedDateTime.now();
        SunUpdate(userLocation, dateTime,view);
        return view;
    }

    private void play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(requireActivity(), R.raw.click2);
        }
        mediaPlayer.start();
    }

    public void SunUpdate(UserLocationHandler userLocation, ZonedDateTime date,View view){


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX['['VV']']");

            SunTimes.Parameters sunTime = SunTimes.compute().on(date);

            SunPosition.Parameters sunParam = SunPosition.compute()
                    .at(userLocation.getLatitude(), userLocation.getLongitude())
                    .timezone("Europe/Warsaw")
                    .on(date);

            SunTimes sun = sunTime.execute();
            SunPosition sunPosition = sunParam.execute();

            String sunRise = String.valueOf(sun.getRise());
            String sunSet =   String.valueOf(sun.getSet());
            String sunHorizont = "TAK";

            if (sun.isAlwaysUp()){
                sunHorizont = "TAK";
            }else { sunHorizont = "NIE"; }

            ZonedDateTime zonedDateTimeRise = ZonedDateTime.parse(sunRise, formatter);
            ZonedDateTime zonedDateTimeSet = ZonedDateTime.parse(sunSet, formatter);

            TextView timeTextView = view.findViewById(R.id.sunRiseTextView);

            TextView sunFaze2TextView = view.findViewById(R.id.sunFaze2TextView);

            TextView sunSetTextView = view.findViewById(R.id.sunSetTextView);

            TextView timeTextView1 = view.findViewById(R.id.sunRiseTextView1);

            TextView sunFaze2TextView1 = view.findViewById(R.id.sunFaze2TextView1);

            TextView sunSetTextView1 = view.findViewById(R.id.sunSetTextView1);

            timeTextView.setText( zonedDateTimeRise.getHour() + ":" +  zonedDateTimeRise.getMinute());
            sunFaze2TextView.setText(sunHorizont);
            sunSetTextView.setText(zonedDateTimeSet.getHour() + ":" + zonedDateTimeSet.getMinute());

            timeTextView1.setText(("" +  (int) sunPosition.getAltitude()) + "°" );
            sunFaze2TextView1.setText("" + (int)  sunPosition.getDistance() + " km");
            sunSetTextView1.setText("" +  (int)  sunPosition.getAzimuth() + "°");


        }
    }
}
