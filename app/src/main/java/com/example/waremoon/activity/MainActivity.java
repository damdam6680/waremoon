package com.example.waremoon.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.waremoon.NewsFragment;
import com.example.waremoon.MoonFragment;
import com.example.waremoon.SunFragment;
import com.example.waremoon.R;
import com.example.waremoon.CameraFragment;
import com.example.waremoon.GalleryFragment;
import com.example.waremoon.handler.ApiHandler;
import com.example.waremoon.handler.SessionManagerHandler;
import com.example.waremoon.handler.UserLocationHandler;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationView;

import org.shredzone.commons.suncalc.MoonPosition;
import org.shredzone.commons.suncalc.MoonTimes;

import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FusedLocationProviderClient fusedLocationClient;
    private SessionManagerHandler sessionManager;
    private DrawerLayout drawerLayout;
    private MediaPlayer mediaPlayer = null;
    boolean torchOn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        UserLocationHandler userLocation = new UserLocationHandler(fusedLocationClient);
        userLocation.requestLocationPermission(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,
                R.string.close_nav);

        drawerLayout.addDrawerListener(toggle);

        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MoonFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_moon);
        }
        updateUI();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_moon) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new MoonFragment()).commit();
        } else if (itemId == R.id.nav_camera) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CameraFragment()).commit();
        } else if (itemId == R.id.nav_gallery) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new GalleryFragment()).commit();
        } else if (itemId == R.id.nav_news) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NewsFragment()).commit();

        } else if (itemId == R.id.nav_sun) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SunFragment()).commit();
        } else if (itemId == R.id.nav_torch) {
            toggleTorch();
        } else if (itemId == R.id.nav_logout) {
            if (sessionManager.isLoggedIn()) {
                sessionManager.logoutUser();

                updateUI();
            } else {
                Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void updateUI() {
        sessionManager = new SessionManagerHandler(this);
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView userNameTextView = headerView.findViewById(R.id.userNameTextView);
        Menu menu = navigationView.getMenu();
        MenuItem logoutMenuItem = menu.findItem(R.id.nav_logout);

        if (sessionManager.isLoggedIn()) {
            String userName = sessionManager.getUserName();
            userNameTextView.setText("Zalogowany użytkownik: " + userName);
            logoutMenuItem.setTitle("Logout");
        } else {
            userNameTextView.setText("Zalogowany użytkownik: ");
            logoutMenuItem.setTitle("Login");
        }
    }

    private void play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(MainActivity.this, R.raw.click2);
        }
        mediaPlayer.start();
    }

    private void toggleTorch() {

        final CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        String cameraId;

        try {
            cameraId = cameraManager.getCameraIdList()[0];
        } catch (CameraAccessException e) {
            throw new RuntimeException(e);
        }

        NavigationView navigationView = findViewById(R.id.nav_view);
        Menu menu = navigationView.getMenu();
        MenuItem menuItem = menu.findItem(R.id.nav_torch);

        if (!torchOn) {
            try {
                cameraManager.setTorchMode(cameraId, true);
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }
            torchOn = true;
            Toast.makeText(MainActivity.this, "Lataraka włączona", Toast.LENGTH_SHORT).show();
            menuItem.setTitle("Latarka (Włączona)");
        } else {
            try {
                cameraManager.setTorchMode(cameraId, false);
            } catch (CameraAccessException e) {
                throw new RuntimeException(e);
            }
            torchOn = false;
            Toast.makeText(MainActivity.this, "Lataraka wyłączona", Toast.LENGTH_SHORT).show();
            menuItem.setTitle("Latarka (Wyłączona)");
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }




}