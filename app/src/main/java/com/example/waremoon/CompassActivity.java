package com.example.waremoon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class CompassActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor magnetometer;
    private Sensor accelerometer;


    private Camera mCamera;
    private CameraPreview mPreview;
    private float[] lastAccelerometer = new float[3];
    private float[] lastMagnetometer = new float[3];
    private float[] rotationMatrix = new float[9];
    private float[] orientationValues = new float[3];

    private TextView textViewDirection;
    private TextView textViewTilt;

    private TextView compass;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compass);


        CameraPreview cameraPreview = findViewById(R.id.camera_preview);




        textViewDirection = findViewById(R.id.textViewDirection);
        textViewTilt = findViewById(R.id.textViewTilt);
        compass = findViewById(R.id.compas);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

        if (accelerometer != null && magnetometer != null) {
            sensorManager.registerListener(
                    this,
                    accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI
            );
            sensorManager.registerListener(
                    this,
                    magnetometer,
                    SensorManager.SENSOR_DELAY_NORMAL,
                    SensorManager.SENSOR_DELAY_UI
            );
        } else {
            // Obsługa braku jednego z sensorów
            // Możesz dodać odpowiednią obsługę w tym miejscu
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Nie jesteśmy zainteresowani zmianami dokładności
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == accelerometer) {
            System.arraycopy(event.values, 0, lastAccelerometer, 0, event.values.length);
        } else if (event.sensor == magnetometer) {
            System.arraycopy(event.values, 0, lastMagnetometer, 0, event.values.length);
        }

        updateOrientation();
    }

    private void updateOrientation() {
        SensorManager.getRotationMatrix(rotationMatrix, null, lastAccelerometer, lastMagnetometer);
        SensorManager.getOrientation(rotationMatrix, orientationValues);

        float azimuth = (float) Math.toDegrees(orientationValues[0]);
        textViewDirection.setText("Kierunek: " + azimuth);
        compass.setText(formatAzimuth(azimuth));
        float pitch = (float) Math.toDegrees(orientationValues[1]);
        textViewTilt.setText("Nachylenie: " + pitch + " stopni");

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(
                this,
                accelerometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
        );
        sensorManager.registerListener(
                this,
                magnetometer,
                SensorManager.SENSOR_DELAY_NORMAL,
                SensorManager.SENSOR_DELAY_UI
        );
    }


    private String formatAzimuth(float azimuth) {
        if (azimuth < 0) {
            azimuth += 360; // Ensure azimuth is positive
        }

        String[] directions = {"Północny", "Północno-Wschodni", "Wschodni", "Południowo-Wschodni",
                "Południowy", "Południowo-Zachodni", "Zachodni", "Północno-Zachodni"};

        int index = Math.round(azimuth / 45) % 8;
        return directions[index];
    }

}
