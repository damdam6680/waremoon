package com.example.waremoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    private EditText userNameTextField, passwordTextField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameTextField = findViewById(R.id.userNameTextField);
        passwordTextField = findViewById(R.id.passwordTextField);

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String email = userNameTextField.getText().toString().trim();
                String password = passwordTextField.getText().toString().trim();

                // Validate login
                if (isValidLogin(email, password)) {
                    // Start the next activity (e.g., GalleryActivity) if login is successful
                    Intent intent = new Intent(LoginActivity.this, GalleryActivity.class);
                    startActivity(intent);
                } else {
                    // Show an error message or handle the invalid login
                    Toast.makeText(LoginActivity.this, "Invalid login credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Registration button click listener
        Button registrationButton = findViewById(R.id.registration);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(registrationIntent);
            }
        });
    }

    // Validate login credentials
    private boolean isValidLogin(String email, String password) {
        // Call the method from DBHandler to check if the user exists
        DBHandler dbHandler = new DBHandler(this);
        return dbHandler.checkUserCredentials(email, password);
    }
}