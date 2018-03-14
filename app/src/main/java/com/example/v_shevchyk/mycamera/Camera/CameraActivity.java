package com.example.v_shevchyk.mycamera.Camera;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import android.widget.Toast;

import com.example.v_shevchyk.mycamera.CameraPreview;
import com.example.v_shevchyk.mycamera.R;
import com.example.v_shevchyk.mycamera.ResizeModule;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

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
    private ImageButton flashLight, timer, colorEfects, whitelvl, sceneMode;

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

        View.OnClickListener handler = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()){
                    case R.id.color_efects:
                        presenter.clickColor();
                        break;
                    case R.id.flash_light:
                        presenter.clickFlashLight();
                        break;
                    case R.id.white_level:
                        presenter.clickWhiteLvl();
                        break;
                    case R.id.scene:
                        presenter.clickSceneMode();
                        break;
                    case R.id.gal:
                        presenter.galeryClick();
                        break;
                    case R.id.settings:
                        presenter.settingsClick(settingsLayout.getVisibility());
                        break;
                    case R.id.picture_btn:
                        presenter.savePicture();
                        break;
                    case R.id.timer:
                        presenter.timerClick();
                        break;
                }
            }
        };

        pictureBtn.setOnClickListener(handler);
        galery.setOnClickListener(handler);
        settings.setOnClickListener(handler);
        flashLight.setOnClickListener(handler);
        timer.setOnClickListener(handler);
        colorEfects.setOnClickListener(handler);
        whitelvl.setOnClickListener(handler);
        sceneMode.setOnClickListener(handler);

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
        whitelvl = settingsLayout.findViewById(R.id.white_level);
        sceneMode = settingsLayout.findViewById(R.id.scene);
        timer = settingsLayout.findViewById(R.id.timer);
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
        sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, // update gallery after take a photo
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

    @Override
    public void aplyColor() {
        optionsDialog(parameters.getSupportedColorEffects(), colorEfects.getId());
    }

    @Override
    public void aplyFlashLight() {
        optionsDialog(parameters.getSupportedFlashModes(), flashLight.getId());
    }

    @Override
    public void applyWhiteLvl() {
        optionsDialog(parameters.getSupportedWhiteBalance(), whitelvl.getId());
    }

    @Override
    public void applySceneMode() {
        optionsDialog(parameters.getSupportedSceneModes(), sceneMode.getId());
    }

    @Override
    public void applyTimer() {
        String[] sec = {"3", "5", "7"};
        optionsDialog(Arrays.asList(sec), timer.getId());
    }

    private void optionsDialog(List<String> posibleOptions, final int id){
        final String [] options = posibleOptions.toArray(new String[posibleOptions.size()]);
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(CameraActivity.this);
        mBuilder.setTitle(R.string.option);
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
                    case R.id.white_level:
                        parameters.setWhiteBalance(options[i]);
                        mCamera.setParameters(parameters);
                        break;
                    case R.id.scene:
                        parameters.setSceneMode(options[i]);
                        mCamera.setParameters(parameters);
                        break;
                    case R.id.timer:
                        final Handler handler = new Handler();
                        Timer timer = new Timer(false);
                        TimerTask timerTask = new TimerTask() {
                            @Override
                            public void run() {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "BOO", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        };
                        timer.schedule(timerTask, Integer.parseInt(options[i])*1000);
                        break;
                }
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();

    }
}
