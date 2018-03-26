package com.example.v_shevchyk.mycamera;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.example.v_shevchyk.mycamera.camera.CameraPresenter;
import com.example.v_shevchyk.mycamera.camera.CameraViews;
import com.example.v_shevchyk.mycamera.cameralogic.MyCamera;
import com.example.v_shevchyk.mycamera.modules.DrawModule;

public class CameraActivity extends AppCompatActivity {

    private MyCamera myCamera;
    private CameraViews views;
    private CameraPresenter presenter;
    private DrawModule module;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
        updateGaleryBroadcast();
    }

    @Override
    protected void onPause() {
        super.onPause();
        myCamera.releaseMediaRecorder();
        myCamera.releaseCamera();
        updateGaleryBroadcast();
        Log.e("Pause", "reached");
    }

    private void init() {
        myCamera = new MyCamera(this, getWindowManager().getDefaultDisplay());
        presenter = new CameraPresenter(myCamera, getResources().getConfiguration().orientation);
        views = new CameraViews(this, presenter);
        module = new DrawModule(this, myCamera.getmPreview().getCallback());

    }

    private void updateGaleryBroadcast() {
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                Uri.parse("file://"
                        + Environment.getExternalStorageDirectory())));
    }

}