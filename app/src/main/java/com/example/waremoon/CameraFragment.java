package com.example.waremoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.waremoon.activity.GalleryActivity;
import com.example.waremoon.handler.CameraPreviewHandler;
import com.example.waremoon.handler.SessionManagerHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CameraFragment extends Fragment {

    private CameraPreviewHandler cameraPreview;
    private SessionManagerHandler sessionManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_camera, container, false);

        cameraPreview = view.findViewById(R.id.camera_preview);

        sessionManager = new SessionManagerHandler(requireContext());

        int userId = sessionManager.getUserId();

        Button takePhotoButton = view.findViewById(R.id.takePhoto);
        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cameraPreview.takePicture(userId);
            }
        });

        FloatingActionButton cameraButton = view.findViewById(R.id.gallery);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManagerHandler sessionManager = new SessionManagerHandler(requireContext());
                int userId = sessionManager.getUserId();

                Intent cameraIntent = new Intent(requireContext(), GalleryActivity.class);
                cameraIntent.putExtra("USER_ID", userId);
                startActivity(cameraIntent);
            }
        });

        return view;
    }
}
