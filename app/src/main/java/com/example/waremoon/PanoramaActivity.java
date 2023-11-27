package com.example.waremoon;

import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;

import com.panoramagl.PLImage;
import com.panoramagl.PLManager;
import com.panoramagl.PLSphericalPanorama;
import com.panoramagl.utils.PLUtils;

public class PanoramaActivity extends AppCompatActivity {
    private PLManager plManager;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        plManager = new PLManager(this);
////        plManager.setContentView(findViewById(android.R.id.content));
//        plManager.setContentView(findViewById(R.id.content_view));
//        plManager.onCreate();
//
//        PLSphericalPanorama panorama = new PLSphericalPanorama();
//        panorama.getCamera().lookAt(30.0f, 90.0f);
//
//        panorama.setImage(new PLImage(PLUtils.getBitmap(this, R.raw.sighisoara_sphere), false));
//
//        plManager.setPanorama(panorama);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.panorama);

        GLSurfaceView glSurfaceView = findViewById(R.id.panorama_view);
        plManager = new PLManager(this);

        // Zmieniona linia - używamy rodzica GLSurfaceView jako argumentu
        ViewGroup glSurfaceParent = (ViewGroup) glSurfaceView.getParent();
        plManager.setContentView(glSurfaceParent);

        plManager.onCreate();

        PLSphericalPanorama panorama = new PLSphericalPanorama();
        panorama.getCamera().lookAt(30.0f, 90.0f);
        panorama.setImage(new PLImage(PLUtils.getBitmap(this, R.raw.sighisoara_sphere), false));

        plManager.setPanorama(panorama);
    }


    @Override
    protected void onResume() {
        super.onResume();
        plManager.onResume();
    }

    @Override
    protected void onPause() {
        plManager.onPause();
        super.onPause();
    }
    @Override
    protected void onDestroy() {
        plManager.onDestroy();
        super.onDestroy();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return plManager.onTouchEvent(event);
    }
}
