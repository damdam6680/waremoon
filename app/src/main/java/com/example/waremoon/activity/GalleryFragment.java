package com.example.waremoon.activity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.waremoon.R;
import com.example.waremoon.adapter.ImageAdapter;
import com.example.waremoon.handler.SessionManagerHandler;
import com.example.waremoon.interfaces.OnItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GalleryFragment extends Fragment {
    private MediaPlayer mediaPlayer = null;
    private static final int IMAGE_DETAIL_REQUEST_CODE = 1;

    private ImageAdapter imageAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.activity_gallery, container, false);

        GridView gridView = view.findViewById(R.id.gridView);
        imageAdapter = new ImageAdapter(requireContext());
        gridView.setAdapter(imageAdapter);

        imageAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position, int imageId) {
                Intent intent = new Intent(requireContext(), ImageDetailActivity.class);
                intent.putExtra("position", position);
                intent.putExtra("imageId", imageId);
                startActivityForResult(intent, IMAGE_DETAIL_REQUEST_CODE);
            }
        });

        FloatingActionButton cameraButton = view.findViewById(R.id.camera);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                play();

                SessionManagerHandler sessionManager = new SessionManagerHandler(requireContext());
                int userId = sessionManager.getUserId();

                CameraFragment cameraFragment = new CameraFragment();

                Bundle bundle = new Bundle();
                bundle.putInt("USER_ID", userId);
                cameraFragment.setArguments(bundle);

                FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.fragment_container, cameraFragment);

                transaction.addToBackStack(null);

                transaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_DETAIL_REQUEST_CODE) {
            if (resultCode == AppCompatActivity.RESULT_OK) {
                updateGallery();
            }
        }
    }

    private void updateGallery() {
        imageAdapter.updateUserPhotos();
        imageAdapter.notifyDataSetChanged();
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
