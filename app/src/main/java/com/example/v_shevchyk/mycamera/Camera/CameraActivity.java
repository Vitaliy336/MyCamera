package com.example.v_shevchyk.mycamera.Camera;

import android.content.Intent;
import android.content.pm.ActivityInfo;
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

//        zoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
//            @Override
//            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
//                if(mCamera.getParameters().isZoomSupported()){
//                    parameters.setZoom(i);
//                    mCamera.setParameters(parameters);
//                }
//            }
//
//            @Override
//            public void onStartTrackingTouch(SeekBar seekBar) {
//
//            }
//
//            @Override
//            public void onStopTrackingTouch(SeekBar seekBar) {
//
//            }
//        });

    private void init() {
        myCamera = new MyCamera(this);
        Log.e("camera", myCamera.toString());
        presenter = new CameraPresenter(myCamera);
        Log.e("presenter", presenter.toString());
        views = new CameraViews(this, presenter);
        Log.e("presenter", presenter.toString());

        Display display = getWindowManager().getDefaultDisplay();

    }


//    public void showGalery() {
//        Intent intent = new Intent();
//        intent.setAction(android.content.Intent.ACTION_VIEW);
//        intent.setType("image/*");
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }

    public void updateGaleryBroadcast() {
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, // update gallery after take a photo
                Uri.parse("file://"
                        + Environment.getExternalStorageDirectory())));
    }

}
