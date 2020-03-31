package com.example.speeddetector;

import com.example.speeddetector.R;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private Camera camera;
    private FrameLayout frameLayout;
    private CameraPreview showCamera;
    private Spinner spinnerObjects;
    private Objects objects;
    private Button buttonDetect;
    private int REQUEST_TAKE_VIDEO = 1,
            maxSpeed = 330, minSpeed = 0;
    private TextView textViewSpeed;
    private AdView mAdView;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        objects = new Objects(getApplicationContext());
        SharedPreferences preferences = getSharedPreferences("PREFS",0);
        boolean ifShowDialog = preferences.getBoolean("showDialog",true);
        if(ifShowDialog){
            showWarning();
        }

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {

            }
        });
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        spinnerObjects = (Spinner) findViewById(R.id.spinnerChoseObject);
        buttonDetect = (Button) findViewById(R.id.buttonDedectSpeed);
        textViewSpeed = (TextView) findViewById(R.id.textSpeed);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinner_item, objects.getObj());
        spinnerObjects.setAdapter(adapter);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);

        }

        spinnerObjects.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view;
                ((TextView) view).setGravity(Gravity.CENTER);
                ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.textColor));

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
               // clicked = true;

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TAKE_VIDEO && resultCode == -1) {
            Random rs = new Random();
            textViewSpeed.setText(String.format("%.2f KM/H", rs.nextDouble() * (maxSpeed - minSpeed) + minSpeed));

        }
        ;
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onPause() {
        super.onPause();
        camera.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        camera.release();
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

    private void showWarning(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(R.string.warning)
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setNeutralButton("NEVER SHOW AGAIN", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        SharedPreferences preferences = getSharedPreferences("PREFS",0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("showDialog", false);
                        editor.apply();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }
}
