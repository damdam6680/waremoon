package com.example.waremoon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waremoon.sql.DBHandler;
import com.example.waremoon.R;

public class RegistrationActivity extends AppCompatActivity {

    private EditText emailTextField, userNameTextField, passwordTextField;
    private DBHandler dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        TextView loginLink = findViewById(R.id.login);
        loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registrationIntent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(registrationIntent);
            }
        });

        emailTextField = findViewById(R.id.emailTextField);
        userNameTextField = findViewById(R.id.userNameTextField);
        passwordTextField = findViewById(R.id.passwordTextField);
        Button registration = findViewById(R.id.registration);

        dbHandler = new DBHandler(RegistrationActivity.this);

        registration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailTextField.getText().toString();
                String userName = userNameTextField.getText().toString();
                String password = passwordTextField.getText().toString();

                if (email.isEmpty() && userName.isEmpty() && password.isEmpty()) {
                    Toast.makeText(RegistrationActivity.this, "Please enter all the data..", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isEmailOk(email) && isUserNameOk(userName) && isPasswordOk(password)) {
                    dbHandler.registerUser(email, userName, password);

                    Toast.makeText(RegistrationActivity.this, "Registration successful.", Toast.LENGTH_SHORT).show();
                    emailTextField.setText("");
                    userNameTextField.setText("");
                    passwordTextField.setText("");
                }
                else {
                    Toast.makeText(RegistrationActivity.this, "Set validate data.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean isEmailOk(String s) {
        return s.matches("\\w{2,24}[a-z]\\d*@{1}\\w{0,24}[a-z].*");
    }

    private boolean isUserNameOk(String s) {
        return s.matches("\\w{2,24}[a-z].*");
    }

    private boolean isPasswordOk(String s) {
        return s.matches("(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{8,}$");
    }

}

