package com.example.v_shevchyk.mycamera.Camera;

import android.content.pm.ActivityInfo;
import android.hardware.Camera;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.v_shevchyk.mycamera.CameraPreview;
import com.example.v_shevchyk.mycamera.R;
import com.example.v_shevchyk.mycamera.RisizeModule;

public class CameraActivity extends AppCompatActivity implements CameraContract.ICameraView{
    private Camera mCamera;
    private FrameLayout preview;
    private CameraPreview mPreview;
    private FloatingActionButton pictureBtn;
    private CameraPresenter presenter;
    private RisizeModule risizeModule;

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
        initView();
        initListener();
        initPresenter();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null){
            mCamera.release();
            mCamera = null;
        }
    }

    private void initPresenter() {
        presenter = new CameraPresenter();
        presenter.attachView(this);
    }

    private void initListener() {
        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.takePictureClick();
            }
        });
    }

    private void initView() {
        Display display = getWindowManager().getDefaultDisplay();
        int rotation = getWindowManager().getDefaultDisplay().getRotation();
        preview = findViewById(R.id.preview);
        pictureBtn = findViewById(R.id.picture_btn);
        mCamera = getCameraInstance();
        risizeModule = new RisizeModule(display, mCamera);
        mPreview = new CameraPreview(this, mCamera);
        preview.addView(mPreview);
        preview.getLayoutParams().height = (int)(risizeModule.calculate(true).bottom);
        preview.getLayoutParams().width = (int)(risizeModule.calculate(true).right);
    }


    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open();
        }
        catch (Exception e){
        }
        return c;
    }

    @Override
    public void takePicture() {
        Toast.makeText(this, "sss", Toast.LENGTH_SHORT).show();
    }
}
