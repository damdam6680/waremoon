package com.example.waremoon.handler;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.waremoon.R;
import com.example.waremoon.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyFirebaseMessagingService", "in onCreate, try to get informations from backend server, ex..send http request here");

        ApiHandler apiHandler = new ApiHandler();

        apiHandler.fetchNeoFeed(new ApiHandler.NeoInfoCallback() {
            @Override
            public void onNeoInfoLoaded(List<String> neoInfoList) {
                if (neoInfoList != null && !neoInfoList.isEmpty()) {
                    // Data is available, show the notification

                    showNotification("Uwaga", neoInfoList.get(0));


                } else {
                    // Handle the case where the neoInfoList is empty
                    Log.e("MyFirebaseMessagingService", "NeoInfoList is empty");
                }
            }

            @Override
            public void onNeoInfoError(String errorMessage) {
                // Handle error loading NEO feed
                Log.e("MyFirebaseMessagingService", errorMessage);
            }
        });

    }




    public void showNotification(String title,
                                 String message) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "default_channel";
            NotificationChannel channel = new NotificationChannel(channelId, "Default Channel", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Pass the intent to switch to the MainActivity
        Intent intent = new Intent(this, MainActivity.class);
        String channel_id = "notification_channel";
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent
                = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder builder
                = new NotificationCompat
                .Builder(getApplicationContext(), "default_channel")
                .setSmallIcon(R.drawable.baseline_timer_24)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000,
                        1000, 1000})
                .setOnlyAlertOnce(true)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager
                = (NotificationManager) getSystemService(
                Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT
                >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel
                    = new NotificationChannel(
                    channel_id, "notification_channel",
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(
                    notificationChannel);
        }

        notificationManager.notify(0, builder.build());
    }

}


