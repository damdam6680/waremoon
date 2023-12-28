package com.example.waremoon.activity;

import static android.content.Intent.getIntent;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.waremoon.R;

public class NotifactionActivity extends AppCompatActivity {

    TextView textView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        textView = findViewById(R.id.textViewData);
        String data = getIntent().getStringExtra("data");
        textView.setText(data);

    }
}