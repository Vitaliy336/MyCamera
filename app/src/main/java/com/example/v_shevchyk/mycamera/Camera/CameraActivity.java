package com.example.v_shevchyk.mycamera.Camera;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;

import com.example.v_shevchyk.mycamera.R;

public class CameraActivity extends AppCompatActivity {

    private MyCamera myCamera;
    private CameraViews views;
    private CameraPresenter presenter;

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
        myCamera.releaseCamera();
        updateGaleryBroadcast();
    }

    private void init() {
        myCamera = new MyCamera(this, getWindowManager().getDefaultDisplay());
        Log.e("camera", myCamera.toString());
        presenter = new CameraPresenter(myCamera, getResources().getConfiguration().orientation);
        Log.e("presenter", presenter.toString());
        views = new CameraViews(this, presenter);
        Log.e("presenter", presenter.toString());

    }


//    public void showGalery() {
//

    private void updateGaleryBroadcast() {
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED,
                Uri.parse("file://"
                        + Environment.getExternalStorageDirectory())));
    }

}
