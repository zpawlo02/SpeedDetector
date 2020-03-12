package com.example.speeddetector;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Camera camera;
    private FrameLayout frameLayout;
    private CameraPreview showCamera;
    private Spinner spinnerObjects;
    private Objects objects = new Objects();

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerObjects = (Spinner) findViewById(R.id.spinnerChoseObject);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, objects.getObj());
        spinnerObjects.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);

        }


        try {
            frameLayout = (FrameLayout) findViewById(R.id.flVideo);
            camera = Camera.open();
            showCamera = new CameraPreview(this, camera);
            frameLayout.addView(showCamera);
        }catch (Exception e){
            e.printStackTrace();
        }





    }
}
