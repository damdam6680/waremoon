package com.example.waremoon.handler;

import android.util.Log;

import com.example.waremoon.interfaces.NeoWsApi;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiHandler {

    public interface NeoInfoCallback {
        void onNeoInfoLoaded(List<String> neoInfoList);
        void onNeoInfoError(String errorMessage);
    }

    private List<String> neoInfoList = new ArrayList<>();

    public void fetchNeoFeed(final NeoInfoCallback callback) {


        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.nasa.gov/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NeoWsApi neoWsApi = retrofit.create(NeoWsApi.class);

        String apiKey = "xjVMVwgMxzzYJzTuOmhaHvshPLELEgAwFZSE5iDz";
        String startDate = "2023-01-01"; // Set the start date as needed

        Call<JsonObject> call = neoWsApi.getNeoFeed(startDate, apiKey);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    handleNeoFeedResponse(response.body());
                    callback.onNeoInfoLoaded(neoInfoList);
                } else {
                    // Handle error
                    callback.onNeoInfoError("Error loading NEO feed");
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                // Handle failure
                callback.onNeoInfoError("Network request failed");
            }
        });
    }

    private void handleNeoFeedResponse(JsonObject response) {
        if (response != null) {
            JsonObject nearEarthObjects = response.getAsJsonObject("near_earth_objects");

            if (nearEarthObjects != null) {
                for (String date : nearEarthObjects.keySet()) {
                    JsonArray asteroidList = nearEarthObjects.getAsJsonArray(date);

                    if (asteroidList != null && asteroidList.size() > 0) {
                        for (int i = 0; i < asteroidList.size(); i++) {
                            JsonObject asteroid = asteroidList.get(i).getAsJsonObject();
                            String asteroidName = asteroid.get("name").getAsString();
                            String asteroidCloseApproachDate = asteroid.getAsJsonArray("close_approach_data")
                                    .get(0).getAsJsonObject().get("close_approach_date_full").getAsString();

                            String neoInfo = "Asteroid Name: " + asteroidName + "\nClose Approach Date: " + asteroidCloseApproachDate;
                            neoInfoList.add(neoInfo);
                            Log.d("neo", neoInfo);
                        }
                    }
                }
            }
        }
    }

    public List<String> getNeoInfoList() {
        return neoInfoList;
    }


}
