package com.example.waremoon.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.waremoon.R;
import com.example.waremoon.handler.UserLocationHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.unity3d.player.UnityPlayerActivity;

import org.shredzone.commons.suncalc.MoonPosition;
import org.shredzone.commons.suncalc.MoonTimes;
import org.shredzone.commons.suncalc.SunPosition;

import java.time.ZoneId;
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

                    SunPosition.Parameters sunParam = SunPosition.compute()
                            .at(userLocation.getLatitude(), userLocation.getLongitude())
                            .timezone("Europe/Warsaw")
                            .on(now);
                    SunPosition sun = sunParam.execute();
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
                Intent panoramaIntent = new Intent(requireActivity(), PanoramaMoonActivity.class);
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

//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX[VV]");

            if (!userLocation.hasLocationPermission((AppCompatActivity) requireActivity())) {

                MainActivity.showNotification("Aplikacja nie ma dostepu do GPS", "Żeby aplikacja działała poprawnie musi posiadac uprawnienia do gps", getContext());
                Log.e("gps", "error");
                return;
            }

            MoonTimes.Parameters moonTime = MoonTimes.compute().on(date);

            MoonPosition.Parameters moonParam = MoonPosition.compute()
                    .at(userLocation.getLatitude(), userLocation.getLongitude())
                    .timezone("Europe/Warsaw")
                    .on(date);

            MoonTimes moon = moonTime.execute();
            MoonPosition moonPosition = moonParam.execute();

            String moonRise = String.valueOf(moon.getRise());
            String moonSet =   String.valueOf(moon.getSet());
            String moonHorizont = "NIE";
            ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Europe/Warsaw"));

            if (currentTime.isAfter(moon.getRise()) && currentTime.isBefore(moon.getSet())) {
                moonHorizont = "TAK";
            }

            ZonedDateTime zonedDateTimeRise = ZonedDateTime.parse(moonRise);
            ZonedDateTime zonedDateTimeSet = ZonedDateTime.parse(moonSet);


            TextView timeTextView = view.findViewById(R.id.moonRiseTextView);

            TextView moonFaze2TextView = view.findViewById(R.id.moonFaze2TextView);

            TextView moonSetTextView = view.findViewById(R.id.moonSetTextView);

            TextView timeTextView1 = view.findViewById(R.id.moonRiseTextView1);

            TextView moonFaze2TextView1 = view.findViewById(R.id.moonFaze2TextView1);

            TextView moonSetTextView1 = view.findViewById(R.id.moonSetTextView1);

            timeTextView.setText(String.format("%02d:%02d", zonedDateTimeRise.getHour(), zonedDateTimeRise.getMinute()));
            moonFaze2TextView.setText(moonHorizont);
            moonSetTextView.setText(String.format("%02d:%02d", zonedDateTimeSet.getHour(), zonedDateTimeSet.getMinute()));

            timeTextView1.setText(("" +  (int) moonPosition.getAltitude()) + "°" );
            moonFaze2TextView1.setText("" + (int)  moonPosition.getDistance() + " km");
            moonSetTextView1.setText("" +  (int)  moonPosition.getAzimuth() + "°");


        }
    }


}