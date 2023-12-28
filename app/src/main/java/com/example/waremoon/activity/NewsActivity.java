package com.example.waremoon.activity;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.waremoon.R;
import com.example.waremoon.handler.ApiHandler;

import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        linearLayout = findViewById(R.id.linear_layout); // Assuming you have a LinearLayout with the id "linear_layout" in your layout

        ApiHandler apiHandler = new ApiHandler();
        apiHandler.fetchNeoFeed(new ApiHandler.NeoInfoCallback() {
            @Override
            public void onNeoInfoLoaded(List<String> neoInfoList) {
                displayNeoInfo(neoInfoList);
            }

            @Override
            public void onNeoInfoError(String errorMessage) {
                // Handle error, for example, display an error message
            }
        });
    }

    private void displayNeoInfo(List<String> neoInfoList) {
        // Retrieve the list of information about all asteroids
        if (neoInfoList != null && !neoInfoList.isEmpty()) {
            for (String neoInfo : neoInfoList) {
                // Create a new TextView for each piece of information
                TextView textView = new TextView(this);
                textView.setText(neoInfo);
                textView.setTextSize(18);

                // Add the TextView to the LinearLayout
                linearLayout.addView(textView);
            }
        } else {
            // Handle case where the list is empty
        }
    }
}
