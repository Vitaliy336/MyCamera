package com.example.v_shevchyk.mycamera.Camera;

import android.app.Activity;
import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;

import com.example.v_shevchyk.mycamera.CameraPreview;
import com.example.v_shevchyk.mycamera.R;

/**
 * Created by v_shevchyk on 15.03.18.
 */

public class CameraViews implements View.OnClickListener, CameraContract.ICameraView {

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
    }

    public FrameLayout getPreview() {
        return preview;
    }

    public void getSettings() {
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("sdasd", "dasdasd");
            }
        });
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void startPreview(CameraPreview cp) {
        preview.addView(cp);
    }
}
