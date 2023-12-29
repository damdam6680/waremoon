package com.example.waremoon.activity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.waremoon.R;
import com.example.waremoon.handler.CameraPreviewHandler;
import com.example.waremoon.handler.SessionManagerHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class CameraFragment extends Fragment {
    private MediaPlayer mediaPlayer = null;

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
                play();

                cameraPreview.takePicture(userId,400,400);
            }
        });

        FloatingActionButton cameraButton = view.findViewById(R.id.gallery);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();

                SessionManagerHandler sessionManager = new SessionManagerHandler(requireContext());
                int userId = sessionManager.getUserId();

                GalleryFragment galleryFragment = new GalleryFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("USER_ID", userId);
                galleryFragment.setArguments(bundle);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, galleryFragment);

                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        return view;
    }

    private void play() {
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.click2);
        } else {
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(requireContext(), R.raw.click2);
        }
        mediaPlayer.start();
    }
}
