package com.example.v_shevchyk.mycamera.Camera;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.v_shevchyk.mycamera.CameraPreview;
import com.example.v_shevchyk.mycamera.R;

import java.util.List;

/**
 * Created by v_shevchyk on 15.03.18.
 */

public class CameraViews implements View.OnClickListener, CameraContract.ICameraView, SeekBar.OnSeekBarChangeListener {

    private Activity activity;
    private FrameLayout preview;
    private SeekBar zoom;
    private LinearLayout settingsLayout;
    private FloatingActionButton pictureBtn, galery, settings;
    private ImageButton flashLight, timer, colorEfects, whitelvl, sceneMode;
    private CameraPresenter presenter;


    public CameraViews(Activity activity, CameraPresenter presenter) {
        this.activity = activity;
        this.presenter = presenter;
        initViews();
        initListeners();
        presenter.attachView(this);
        presenter.createPreview();
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
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.flash_light:
                presenter.clickFlashlight();
                break;
            case R.id.timer:
                break;
            case R.id.white_level:
                break;
            case R.id.scene:
                break;
            case R.id.settings:
                presenter.settingsClick(settingsLayout.getVisibility());
                break;
            case R.id.picture_btn:
                break;
            case R.id.color_efects:
                break;
            case R.id.gal:
                break;
        }
    }


    @Override
    public void startPreview(CameraPreview cp) {
        preview.addView(cp);
    }

    @Override
    public void openGalery() {

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
    public void takePicture() {

    }

    @Override
    public void flasLight(String[] modes) {
        buildDialog(modes);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
        presenter.setZoomLvl(i);
        presenter.uplyZoom();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
    }

    private void buildDialog(String[] options){
        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(activity);
        mBuilder.setTitle(R.string.option);
        mBuilder.setSingleChoiceItems(options, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        AlertDialog mDialog = mBuilder.create();
        mDialog.show();
    }
}
