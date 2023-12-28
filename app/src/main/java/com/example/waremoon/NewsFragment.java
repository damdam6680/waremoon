package com.example.waremoon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.waremoon.handler.ApiHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NewsFragment extends Fragment {

    private LinearLayout linearLayout;
    private Date date = new Date();
    private ApiHandler apiHandler;

    Button btnNext;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        ImageSlider imageSlider = view.findViewById(R.id.imageSlider);

        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.image1, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image2, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image3, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image4, ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.image5, ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.FIT);

        btnNext = view.findViewById(R.id.btnNext);
        linearLayout = view.findViewById(R.id.linear_layout);
        apiHandler = new ApiHandler(); // Create an instance of ApiHandler

        Api(date);

        btnNext.setVisibility(isCurrentDate() ? View.GONE : View.VISIBLE);

        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackButtonClick(v);
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNextButtonClick(v);
            }
        });
        return view;
    }

    public void onNextButtonClick(View view) {
        long sevenDaysInMillis = 24 * 60 * 60 * 1000;
        date.setTime(date.getTime() + sevenDaysInMillis);

        btnNext.setVisibility(isCurrentDate() ? View.GONE : View.VISIBLE);

        clearTextViews();
        Api(date);
    }


    private void Api(Date date) {
        apiHandler.fetchNeoFeed(new ApiHandler.NeoInfoCallback() {
            @Override
            public void onNeoInfoLoaded(List<String> neoInfoList) {
                displayNeoInfo(neoInfoList);
            }

            @Override
            public void onNeoInfoError(String errorMessage) {
                // Handle error, for example, display an error message
            }
        }, date);
    }

    private void clearTextViews() {
        linearLayout.removeAllViews(); // Remove all child views from the LinearLayout
    }

    private void displayNeoInfo(List<String> neoInfoList) {
        if (neoInfoList != null && !neoInfoList.isEmpty()) {
            for (String neoInfo : neoInfoList) {
                TextView textView = new TextView(requireContext()); // Update with requireContext()
                textView.setText(neoInfo);
                textView.setTextSize(18);
                textView.setPadding(0, 10, 0, 0);
                linearLayout.addView(textView);
            }
        } else {
            // Handle case where the list is empty
        }
    }

    private boolean isCurrentDate() {
        Calendar currentDate = Calendar.getInstance();
        Calendar selectedDate = Calendar.getInstance();
        selectedDate.setTime(date);

        return currentDate.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR) &&
                currentDate.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH) &&
                currentDate.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH);
    }

    public void onBackButtonClick(View view) {
        long sevenDaysInMillis = 24 * 60 * 60 * 1000;
        date.setTime(date.getTime() - sevenDaysInMillis);

        // Clear existing TextViews and fetch data with the updated date
        clearTextViews();
        Api(date);

        btnNext.setVisibility(isCurrentDate() ? View.GONE : View.VISIBLE);
    }


}
