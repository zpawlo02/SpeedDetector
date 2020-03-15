package com.example.speeddetector;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
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
    private Button buttonDetect;
    private Integer REQUEST_TAKE_VIDEO = 101,
            maxSpeed = 330, minSpeed = 0;
    private TextView textViewSpeed;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        spinnerObjects = (Spinner) findViewById(R.id.spinnerChoseObject);
        buttonDetect = (Button) findViewById(R.id.buttonDedectSpeed);
        textViewSpeed = (TextView) findViewById(R.id.textSpeed);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, objects.getObj());
        spinnerObjects.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);

        }

        spinnerObjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                maxSpeed = objects.getMaxes()[position];
                minSpeed = objects.getMin()[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                maxSpeed = 330;
                minSpeed = 0;
            }
        });

        try {
            frameLayout = (FrameLayout) findViewById(R.id.flVideo);
            camera = Camera.open();
            showCamera = new CameraPreview(this, camera);
            frameLayout.addView(showCamera);
        }catch (Exception e){
            e.printStackTrace();
        }


        buttonDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, 5);
                startActivityForResult(intent,REQUEST_TAKE_VIDEO);
               //TODO FIX SETTING SPEED
                Random rs = new Random();
                textViewSpeed.setText(String.format("%.2f KM/H", rs.nextDouble() * (maxSpeed - minSpeed)));

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            frameLayout = (FrameLayout) findViewById(R.id.flVideo);
            camera = Camera.open();
            showCamera = new CameraPreview(getApplicationContext(), camera);
            frameLayout.addView(showCamera);
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
