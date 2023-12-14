package com.example.waremoon.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.waremoon.sql.DBHandler;
import com.example.waremoon.R;
import com.example.waremoon.handler.SessionManagerHandler;

public class LoginActivity extends AppCompatActivity {
    private EditText userNameTextField, passwordTextField;
    private SessionManagerHandler sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userNameTextField = findViewById(R.id.userNameTextField);
        passwordTextField = findViewById(R.id.passwordTextField);
        sessionManager = new SessionManagerHandler(this);

        Button loginButton = findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve user input
                String userName = userNameTextField.getText().toString().trim();
                String password = passwordTextField.getText().toString().trim();

                // W LoginActivity, w sekcji, gdzie przechodzisz do następnego activity
                if (isValidLogin(userName, password)) {
                    // Pobierz dodatkowe informacje o użytkowniku z bazy danych
                    DBHandler dbHandler = new DBHandler(LoginActivity.this);
                    int userId = dbHandler.getUserId(userName);
                    String userNameFromDB = dbHandler.getUserName(userId);

                    // Zapisz dane użytkownika w sesji
                    sessionManager.setUserData(userId, userNameFromDB);

                    Intent intent = new Intent(LoginActivity.this, Test.class);
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
    private boolean isValidLogin(String userName, String password) {
        // Call the method from DBHandler to check if the user exists
        DBHandler dbHandler = new DBHandler(this);
        return dbHandler.checkUserCredentials(userName, password);
    }
}