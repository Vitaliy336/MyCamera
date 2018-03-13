package com.example.v_shevchyk.mycamera.Camera;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;

import com.example.v_shevchyk.mycamera.CameraPreview;
import com.example.v_shevchyk.mycamera.R;
import com.example.v_shevchyk.mycamera.ResizeModule;

import java.util.List;

public class CameraActivity extends AppCompatActivity implements CameraContract.ICameraView{
    private Camera mCamera;
    private SeekBar zoom;
    private LinearLayout settingsLayout;
    private Camera.Parameters parameters;
    private FrameLayout preview;
    private CameraPreview mPreview;
    private FloatingActionButton pictureBtn, galery, settings;;
    private CameraPresenter presenter;
    private ResizeModule resizeModule;
    private ImageButton flashLight, timer, colorEfects;

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
        presenter.updateGalery();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null){
            mPreview.getHolder().removeCallback(mPreview);
            mCamera.release();
            mCamera = null;

        }
        presenter.updateGalery();
    }

    private void initPresenter() {
        presenter = new CameraPresenter();
        presenter.attachView(this);
    }

    private void initListener() {
        flashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionsDialog(parameters.getSupportedFlashModes(), flashLight.getId());
            }
        });

        colorEfects.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                optionsDialog(parameters.getSupportedColorEffects(), colorEfects.getId());
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.settingsClick(settingsLayout.getVisibility());
            }
        });

        zoom.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(mCamera.getParameters().isZoomSupported()){
                    parameters.setZoom(i);
                    mCamera.setParameters(parameters);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        pictureBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.savePicture();
            }
        });

        galery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.galeryClick();
            }
        });
    }

    private void initView() {
        mCamera = getCameraInstance();
        settingsLayout = findViewById(R.id.settingslayout);
        parameters = mCamera.getParameters();
        preview = findViewById(R.id.preview);
        galery = findViewById(R.id.gal);
        settings = findViewById(R.id.settings);
        zoom = findViewById(R.id.zoom);
        pictureBtn = findViewById(R.id.picture_btn);

        Display display = getWindowManager().getDefaultDisplay();

        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        checkOrientation(parameters);
        resizeModule = new ResizeModule(display, mCamera);
        mPreview = new CameraPreview(this, mCamera);

        zoom.setMax(parameters.getMaxZoom());

        colorEfects = settingsLayout.findViewById(R.id.color_efects);
        flashLight = settingsLayout.findViewById(R.id.flash_light);
    }

    private void checkOrientation(Camera.Parameters p) { //magic
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
        presenter.updateGalery();

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

    @Override
    public void showGalery() {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void updateGaleryBroadcast() {
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, // update galery after take a photo
                Uri.parse("file://"
                        + Environment.getExternalStorageDirectory())));
    }

    @Override
    public void showSettings() {
        settingsLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSettings() {
        settingsLayout.setVisibility(View.INVISIBLE);
    }

    private void setDisplayOrientation(){
        presenter.setDisplayOrientation(this.getResources().getConfiguration().orientation);
    }


    private void optionsDialog(List<String> posibleOptions, final int id){
        final String [] options = posibleOptions.toArray(new String[posibleOptions.size()]);
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(CameraActivity.this);
        mBuilder.setTitle("Choose an item");
        mBuilder.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (id){
                    case R.id.flash_light:
                        parameters.setFlashMode(options[i]);
                        mCamera.setParameters(parameters);
                        break;
                    case R.id.color_efects:
                        parameters.setColorEffect(options[i]);
                        mCamera.setParameters(parameters);
                        break;
                }


                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
}
