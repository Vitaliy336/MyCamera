package com.example.v_shevchyk.mycamera.camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.v_shevchyk.mycamera.CameraPreview;
import com.example.v_shevchyk.mycamera.R;
import com.example.v_shevchyk.mycamera.ResizeModule;

import java.util.Timer;
import java.util.TimerTask;

public class CameraViews implements View.OnClickListener, CameraContract.ICameraView, SeekBar.OnSeekBarChangeListener {

    private Activity activity;
    private FrameLayout preview;
    private SeekBar zoom;
    private LinearLayout settingsLayout;
    private FloatingActionButton pictureBtn, galery, settings, videoBtn, stopBtn;
    private ImageButton flashLight, timer, colorEfects, whitelvl, sceneMode;
    private CameraPresenter presenter;
    private SwitchCompat changeMode;
    private static boolean isChecked = false;

    public CameraViews(Activity activity, CameraPresenter presenter) {
        this.activity = activity;
        this.presenter = presenter;
        initViews();
        initListeners();
        initPresenter();
    }

    private void initPresenter() {
        presenter.attachView(this);
        presenter.createPreview();
        presenter.resizePreview();
    }

    private void initViews() {
        zoom = activity.findViewById(R.id.zoom);
        settingsLayout = activity.findViewById(R.id.settingslayout);
        pictureBtn = activity.findViewById(R.id.picture_btn);
        galery = activity.findViewById(R.id.gal);
        settings = activity.findViewById(R.id.settings);
        flashLight = activity.findViewById(R.id.flash_light);
        timer = activity.findViewById(R.id.timer);
        whitelvl = activity.findViewById(R.id.white_level);
        sceneMode = activity.findViewById(R.id.scene);
        colorEfects = activity.findViewById(R.id.color_efects);
        preview = activity.findViewById(R.id.preview);
        zoom.setMax(presenter.getMaxZomm());
        changeMode = activity.findViewById(R.id.switch_mode);
        videoBtn = activity.findViewById(R.id.start_r);
        stopBtn = activity.findViewById(R.id.stop_r);

    }

    private void initListeners() {
        pictureBtn.setOnClickListener(this);
        galery.setOnClickListener(this);
        flashLight.setOnClickListener(this);
        timer.setOnClickListener(this);
        whitelvl.setOnClickListener(this);
        sceneMode.setOnClickListener(this);
        colorEfects.setOnClickListener(this);
        settings.setOnClickListener(this);
        zoom.setOnSeekBarChangeListener(this);
        stopBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);

        changeMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (changeMode.isChecked() == true) {
                    Toast.makeText(activity, "CHECKED", Toast.LENGTH_SHORT).show();
                    presenter.videoMode();
                } else {
                    Toast.makeText(activity, "UNCHECKED", Toast.LENGTH_SHORT).show();
                    presenter.pictureMode();
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.flash_light:
                presenter.clickFlashlight();
                break;
            case R.id.timer:
                presenter.timerClick();
                break;
            case R.id.white_level:
                presenter.clikWhitelvl();
                break;
            case R.id.scene:
                presenter.clickScene();
                break;
            case R.id.settings:
                presenter.settingsClick(settingsLayout.getVisibility());
                break;
            case R.id.picture_btn:
                presenter.savePicture();
                break;
            case R.id.color_efects:
                presenter.clickColor();
                break;
            case R.id.gal:
                presenter.galeryClick();
                break;
            case R.id.start_r:
                presenter.startVideoClick();
                break;
            case R.id.stop_r:
                presenter.stopVideoClick();
                break;
        }
    }

    @Override
    public void startPreview(CameraPreview cp) {
        preview.addView(cp);
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
    public void flasLight(String[] modes) {
        buildDialog(modes, flashLight.getId());
    }

    @Override
    public void fitPreview(ResizeModule resizeModule) {
        preview.getLayoutParams().height = (int) (resizeModule.calculate(true).bottom);
        preview.getLayoutParams().width = (int) (resizeModule.calculate(true).right);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        presenter.uplyZoom(i);
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void buildDialog(final String[] options, final int id) {
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        mBuilder.setTitle(R.string.option);
        mBuilder.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch (id) {
                    case R.id.flash_light:
                        presenter.flash(options[i]);
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
                                        presenter.savePicture();
                                    }
                                });
                            }
                        };
                        timer.schedule(timerTask, Integer.parseInt(options[i]) * 1000);
                        break;
                    case R.id.white_level:
                        presenter.whiteLvl(options[i]);
                        break;
                    case R.id.scene:
                        presenter.sceneMode(options[i]);
                        break;
                    case R.id.color_efects:
                        presenter.colorsOptions(options[i]);
                        break;
                }
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }

    @Override
    public void goToGallery() {
        Intent intent = new Intent();
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        activity.startActivity(intent);
    }

    @Override
    public void colorEfects(String[] efects) {
        buildDialog(efects, colorEfects.getId());
    }

    @Override
    public void whiteLvl(String[] lvl) {
        buildDialog(lvl, whitelvl.getId());
    }

    @Override
    public void scenes(String[] scenes) {
        buildDialog(scenes, sceneMode.getId());
    }

    @Override
    public void startTimer() {
        buildDialog(activity.getResources().getStringArray(R.array.seconds), timer.getId());
    }

    @Override
    public void showPictureBtn() {
        pictureBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideidePictureBtn() {
        pictureBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showVideoBtn() {
        videoBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideVideoBtn() {
        videoBtn.setVisibility(View.INVISIBLE);
    }

    @Override
    public void showStopBtn() {
        stopBtn.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideStopBtn() {
        stopBtn.setVisibility(View.INVISIBLE);
    }
}