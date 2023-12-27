package com.example.waremoon;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.waremoon.activity.ImageDetailActivity;
import com.example.waremoon.activity.PhotosActivity;
import com.example.waremoon.adapter.ImageAdapter;
import com.example.waremoon.handler.SessionManagerHandler;
import com.example.waremoon.interfaces.OnItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GalleryFragment extends Fragment {
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
                SessionManagerHandler sessionManager = new SessionManagerHandler(requireContext());
                int userId = sessionManager.getUserId();

                Intent cameraIntent = new Intent(requireContext(), PhotosActivity.class);
                cameraIntent.putExtra("USER_ID", userId);
                startActivity(cameraIntent);
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
}
