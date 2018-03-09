package com.example.v_shevchyk.mycamera.Camera;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.example.v_shevchyk.mycamera.CameraPreview;
import com.example.v_shevchyk.mycamera.R;
import com.example.v_shevchyk.mycamera.ResizeModule;

public class CameraActivity extends AppCompatActivity implements CameraContract.ICameraView{
    private Camera mCamera;
    private FrameLayout preview;
    private CameraPreview mPreview;
    private FloatingActionButton pictureBtn;
    private CameraPresenter presenter;
    private ResizeModule resizeModule;

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
        presenter.startCamera();

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
                presenter.savePicture();
            }
        });
    }

    private void initView() {
        mCamera = getCameraInstance();
        Display display = getWindowManager().getDefaultDisplay();
        preview = findViewById(R.id.preview);
        pictureBtn = findViewById(R.id.picture_btn);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        checkOrientation(parameters);
        resizeModule = new ResizeModule(display, mCamera);
        mPreview = new CameraPreview(this, mCamera);
    }

    private void checkOrientation(Camera.Parameters p) {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {
            p.set("orientation", "portrait");
            p.set("rotation",90);
            mCamera.setParameters(p);
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {
            p.set("orientation", "landscape");
            p.set("rotation", 90);
            mCamera.setParameters(p);
        }
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
    public void takePicture(Camera.PictureCallback callback) {
        mCamera.takePicture(null, null, callback);
    }

    @Override
    public void startPreview() {
        preview.addView(mPreview);
    }

    @Override
    public void fitPreview() {
        preview.getLayoutParams().height = (int)(resizeModule.calculate(true).bottom);
        preview.getLayoutParams().width = (int)(resizeModule.calculate(true).right);
    }

    private void setDisplayOrientation(){
        presenter.setDisplayOrientation(this.getResources().getConfiguration().orientation);
    }
}
