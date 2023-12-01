package com.example.waremoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button registrationButton = findViewById(R.id.registration);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tworzymy nową intencję, aby uruchomić aktywność GalleryActivity
                Intent registrationIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(registrationIntent);
            }
        });
    }
}
