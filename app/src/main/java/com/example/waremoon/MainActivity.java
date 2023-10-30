package com.example.waremoon;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.shredzone.commons.suncalc.MoonPosition;

import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private FusedLocationProviderClient fusedLocationClient;

    String date;
    private Button button;
    private Button button2;
    private Button button3;

    private TextView textView;
    private TextView textView1;
    private TextView textView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);

        TextView textView = findViewById(R.id.textView);
        TextView textView1 = findViewById(R.id.textView2);
        TextView textView2 = findViewById(R.id.textView3);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        UserLocation userLocation = new UserLocation(fusedLocationClient);
        userLocation.requestLocationPermission(MainActivity.this);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                textView.setText("Latitude: " + userLocation.getLatitude() + " Longitude: " + userLocation.getLongitude());
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
                textView1.setText("Obecny czas: " + date);
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onClick(View v) {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    ZonedDateTime now = ZonedDateTime.now();
                    MoonPosition.Parameters moonParam = MoonPosition.compute()
                            .at(userLocation.getLatitude(), userLocation.getLongitude())
                            .timezone("Europe/Warsaw")
                            .on(now);
                    MoonPosition moon = moonParam.execute();
                    textView2.setText(String.format(
                            "The moon can be seen %.1f° clockwise from the North and "
                                    + "%.1f° above the horizon.\nIt is about %.0f km away right now.",
                            moon.getAzimuth(),
                            moon.getAltitude(),
                            moon.getDistance()));
                }
            }
        });
    }


}
