package com.example.waremoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegistrationActivity extends AppCompatActivity {

    // creating variables for our edittext, button and dbhandler
    private EditText emailTextField, userNameTextField, passwordTextField;
    private Button registration;
    private DBHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tworzymy nową intencję, aby uruchomić aktywność GalleryActivity
                Intent registrationIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(registrationIntent);
            }
        });

        // initializing all our variables.
        emailTextField = findViewById(R.id.emailTextField);
        userNameTextField = findViewById(R.id.userNameTextField);
        passwordTextField = findViewById(R.id.passwordTextField);
        registration = findViewById(R.id.registration);

        // creating a new dbhandler class
        // and passing our context to it.
        dbHandler = new DBHandler(RegistrationActivity.this);

        // below line is to add on click listener for our add course button.
        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // below line is to get data from all edit text fields.
                String email = emailTextField.getText().toString();
                String userName = userNameTextField.getText().toString();
                String password = passwordTextField.getText().toString();

                // validating if the text fields are empty or not.
                if (email.isEmpty() && userName.isEmpty() && password.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }

                // on below line we are calling a method to add new
                // course to sqlite data and pass all our values to it.
                dbHandler.registerUser(email, userName, password);

                // after adding the data we are displaying a toast message.
                Toast.makeText(RegistrationActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                emailTextField.setText("");
                userNameTextField.setText("");
                passwordTextField.setText("");
            }
        });
    }
}